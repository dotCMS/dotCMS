package com.dotmarketing.quartz.job;

import com.dotcms.business.WrapInTransaction;
import com.dotcms.cluster.bean.Server;
import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.cluster.ClusterFactory;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.util.Logger;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * This job will execute the below tasks after a server in the cluster reach the HEARTBEAT_TIMEOUT.
 *
 * - Frees the license: It will use LicenseUtil.freeLicenseOnRepo().
 * - Clean up the DB tables(cluster_server_uptime, cluster_server).
 * - Updates the replica Count: Only if the license was free successfully.
 * - Rewires the other nodes: Only if the license was free successfully.
 *
 * By default this job will run every minute: HEARTBEAT_CRON_EXPRESSION=0 0/1 * * * ?
 *
 * Created by Oscar Arrieta on 5/18/16.
 */
public class FreeServerFromClusterJob implements StatefulJob {

    @Override
    @WrapInTransaction
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<Server> inactiveServers = APILocator.getServerAPI().getInactiveServers();

            if (!inactiveServers.isEmpty()) {
                inactiveServers.forEach(this::removeServerFromClusterTables);
                ClusterFactory.rewireClusterIfNeeded();
            }
        } catch (DotDataException dotDataException) {
            Logger.error(FreeServerFromClusterJob.class,
                    "Error trying to Free License or Clean Cluster Tables", dotDataException);
        } catch (Exception exception) {
            Logger.error(FreeServerFromClusterJob.class,
                    "Error trying to Free Server from Cluster", exception);
        }
    }

    /**
     * Clean up the DB tables(cluster_server_uptime, cluster_server).
     *
     */
    private void removeServerFromClusterTables(Server server) {

        try {
            Logger.info(this, String
                .format("Server %s with license %s was Removed", server.getServerId(), server.getLicenseSerial()));
            APILocator.getServerAPI().removeServerFromClusterTable(server.getServerId());

            LicenseUtil.freeLicenseOnRepo(server.getLicenseSerial(), server.getServerId());
        } catch(DotDataException e) {
            throw new DotRuntimeException(e.getMessage(), e);
        }
    }
}
