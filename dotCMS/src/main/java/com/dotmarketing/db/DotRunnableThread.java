package com.dotmarketing.db;

import com.dotcms.concurrent.DotConcurrentFactory;
import com.dotcms.concurrent.DotSubmitter;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DotRunnableThread extends Thread {

    private static final int INDEX_ONLY                          = 0;
    private static final int RUN_ONLY_LISTENERS                  = 1;
    private static final int INDEX_AND_RUN_LISTENERS             = 2;

    private final static String LISTENER_SUBMITTER               = DotConcurrentFactory.DOT_SYSTEM_THREAD_POOL;
    public static final String  NETWORK_CACHE_FLUSH_DELAY        = "NETWORK_CACHE_FLUSH_DELAY";
    public static final String  INDEX_COMMIT_LISTENER_BATCH_SIZE = "INDEX_COMMIT_LISTENER_BATCH_SIZE";

    private final List<DotRunnable> listeners;
    private final List<DotRunnable> flushers;
    private final boolean isSync;


    public DotRunnableThread(final List<DotRunnable> allListeners) {
    this(allListeners, false);
  }

    public DotRunnableThread(final List<DotRunnable> allListeners, final boolean isSync) {
        this.isSync    = isSync;
        this.listeners = getListeners(allListeners);
        this.flushers  = getFlushers(allListeners);
    }

    private void runNetworkflowCacheFlushThread() {

        flushers.forEach(runner -> runner.run());
    }

    @Override
    public void run() {

        try {

            if (UtilMethods.isSet(this.flushers)) {

                final DotSubmitter submitter = DotConcurrentFactory.getInstance().getSubmitter(LISTENER_SUBMITTER);
                submitter.submit(this::runNetworkflowCacheFlushThread,
                        Config.getLongProperty(NETWORK_CACHE_FLUSH_DELAY, 3000), TimeUnit.MILLISECONDS);
            }

            LocalTransaction.wrap(this::internalRunner);
        } catch (Exception dde) {
              throw new DotStateException(dde);
        }
    }

  private void internalRunner() {

      final Set<String> reindexInodes                = new HashSet<>();
      final List<List<Contentlet>> reindexList       = new ArrayList<>();
      final List<DotRunnable>      otherListenerList = new ArrayList<>();
      List<Contentlet> contentToIndex                = new ArrayList<>();
      final int batchSize = Config.getIntProperty(INDEX_COMMIT_LISTENER_BATCH_SIZE, 50);

      for (final DotRunnable runner : listeners) {

          if (runner instanceof ReindexRunnable) {

              final ReindexRunnable reindexRunnable = (ReindexRunnable) runner;

              if (ReindexRunnable.Action.REMOVING.equals(reindexRunnable.getAction())) {

                  reindexRunnable.run();
                  continue;
              }

              for (final Contentlet contentlet : reindexRunnable.getReindexIds()) {

                if (reindexInodes.add(contentlet.getInode())) {

                  contentToIndex.add(contentlet);

                  if (contentToIndex.size() == batchSize) {

                    reindexList.add(contentToIndex);
                    contentToIndex = new ArrayList<>();
                  }
                }
              }
          } else {

            if (this.isOrdered (runner)) {
                otherListenerList.add(runner);
            } else {
                runner.run();
            }
          }
      }

      // If there is some contentlet left
      if (UtilMethods.isSet(contentToIndex)) {

          reindexList.add(contentToIndex);
      }

      if (reindexList.isEmpty()) {

          otherListenerList.stream().forEach(DotRunnable::run);
      } else {

          this.indexContentList(reindexList, otherListenerList);
      }
  }

  private void indexContentList(final List<List<Contentlet>> reindexList,
                                  final List<DotRunnable> otherListenerList) {

        for (int i = 0; i < reindexList.size(); ++i) {

            try {

                int action                       = INDEX_ONLY;
                final List<Contentlet> batchList = reindexList.get(i);

                if (i == reindexList.size() -1) { // if it is the last one batch

                    action = (UtilMethods.isSet(batchList))?
                                UtilMethods.isSet(otherListenerList)?
                                        INDEX_AND_RUN_LISTENERS: INDEX_ONLY
                                :RUN_ONLY_LISTENERS;
                }

                switch (action) {

                    case RUN_ONLY_LISTENERS:
                        otherListenerList.stream().forEach(DotRunnable::run);
                    break;

                    case INDEX_AND_RUN_LISTENERS:
                        APILocator.getContentletIndexAPI().indexContentList(batchList, null, false, new ReindexActionListeners(otherListenerList));
                    break;

                    default:
                        APILocator.getContentletIndexAPI().indexContentList(batchList, null, false);
                }
            } catch (DotDataException e) {
                Logger.error(this, e.getMessage(), e);
            }
        }
    }

    private static class ReindexActionListeners implements ActionListener<BulkResponse> {

        private final List<DotRunnable> listeners;

        public ReindexActionListeners(final List<DotRunnable> listeners) {
            this.listeners = listeners;
        }

        @Override
        public void onResponse(BulkResponse bulkItemResponses) {
            listeners.stream().forEach(DotRunnable::run);
        }

        @Override
        public void onFailure(final Exception e) {
            Logger.error(this, e.getMessage(), e);
        }
    }

    private boolean isOrdered(final DotRunnable runner) {

    return this.getOrder(runner) > 0;
  }

  private List<DotRunnable> getFlushers(final List<DotRunnable> allListeners) {
    return allListeners.stream().filter(this::isFlushCacheRunnable).collect(Collectors.toList());
  }

  private List<DotRunnable> getListeners(final List<DotRunnable> allListeners) {
    return allListeners.stream().filter(this::isNotFlushCacheRunnable).sorted(this::compare).collect(Collectors.toList());
  }

  private int compare(final DotRunnable runnable, final DotRunnable runnable1) {
    return this.getOrder(runnable).compareTo(this.getOrder(runnable1));
  }

  private Integer  getOrder(final DotRunnable runnable) {

    final int order = (runnable instanceof HibernateUtil.DotSyncRunnable)?
            HibernateUtil.DotSyncRunnable.class.cast(runnable).getOrder():0;

    return (runnable instanceof HibernateUtil.DotAsyncRunnable)?
            HibernateUtil.DotAsyncRunnable.class.cast(runnable).getOrder(): order;
  }

  private boolean isNotFlushCacheRunnable (final DotRunnable listener) {

      return !this.isFlushCacheRunnable(listener);
  }

  private boolean isFlushCacheRunnable (final DotRunnable listener) {

      return  (
                listener instanceof FlushCacheRunnable ||
                (listener instanceof HibernateUtil.DotAsyncRunnable
                        && HibernateUtil.DotAsyncRunnable.class.cast(listener).getRunnable() instanceof FlushCacheRunnable) ||
                (listener instanceof HibernateUtil.DotSyncRunnable
                        && HibernateUtil.DotSyncRunnable.class.cast(listener).getRunnable() instanceof FlushCacheRunnable)
              );
  }
}
