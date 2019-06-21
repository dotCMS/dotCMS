package com.dotcms.content.elasticsearch.business;

import static com.dotcms.util.DotPreconditions.checkArgument;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.dotcms.cluster.ClusterUtils;
import com.dotcms.cluster.business.ClusterAPI;
import com.dotcms.cluster.business.ReplicasMode;
import com.dotcms.cluster.business.ServerAPI;
import com.dotcms.content.elasticsearch.util.DotRestHighLevelClient;
import com.dotcms.content.elasticsearch.util.ESClient;
import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotcms.repackage.org.dts.spell.utils.FileUtils;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.sitesearch.business.SiteSearchAPI;
import com.dotmarketing.util.AdminLogger;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.ConfigUtils;
import com.dotmarketing.util.DateUtil;
import com.dotmarketing.util.FileUtil;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.ZipUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.rainerhahnekamp.sneakythrow.Sneaky;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse;
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.verify.VerifyRepositoryResponse;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsResponse;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsRequest;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotResponse;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.forcemerge.ForceMergeRequest;
import org.elasticsearch.action.admin.indices.forcemerge.ForceMergeResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.IndexMetaData.State;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.metadata.RepositoryMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.snapshots.SnapshotInfo;

public class ESIndexAPI {

    private  final String MAPPING_MARKER = "mapping=";
    private  final String JSON_RECORD_DELIMITER = "---+||+-+-";
    private static final ESMappingAPIImpl mappingAPI = new ESMappingAPIImpl();

    public static final String BACKUP_REPOSITORY = "backup";
    private final String REPOSITORY_PATH = "path.repo";

	public static final int INDEX_OPERATIONS_TIMEOUT_IN_MS =
			Config.getIntProperty("ES_INDEX_OPERATIONS_TIMEOUT", 15000);

	final private RestHighLevelClient esclient;
	final private ContentletIndexAPI iapi;
	final private ESIndexHelper esIndexHelper;
	private final ServerAPI serverAPI;
	private final ClusterAPI clusterAPI;

	public enum Status { ACTIVE("active"), INACTIVE("inactive"), PROCESSING("processing");
		private final String status;

		Status(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}

	public ESIndexAPI(){
		this.esclient = DotRestHighLevelClient.getClient();
		this.iapi = new ContentletIndexAPIImpl();
		this.esIndexHelper = ESIndexHelper.INSTANCE;
		this.serverAPI = APILocator.getServerAPI();
		this.clusterAPI = APILocator.getClusterAPI();
	}

	@VisibleForTesting
	protected ESIndexAPI(final RestHighLevelClient esclient, final ContentletIndexAPIImpl iapi, final ESIndexHelper esIndexHelper,
						 final ServerAPI serverAPI, final ClusterAPI clusterAPI){
		this.esclient = esclient;
		this.iapi = iapi;
		this.esIndexHelper = esIndexHelper;
		this.serverAPI = serverAPI;
		this.clusterAPI = clusterAPI;
	}

	/**
	 * returns all indicies and status
	 * @return
	 */
	public Map<String, IndexStats> getIndicesAndStatus() {
		// TODO adapt
//		final Client client = transportClient.getClient();
//		final IndicesStatsResponse
//				indicesStatsResponse =
//				client.admin().indices().prepareStats().setStore(true).execute().actionGet(INDEX_OPERATIONS_TIMEOUT_IN_MS);
//
//		return indicesStatsResponse.getIndices();

		return Collections.emptyMap();
	}

	public void getIndicesStats() {

		GetIndexRequest request = new GetIndexRequest("*");
		GetIndexResponse response = Sneaky.sneak(()->esclient.indices()
				.get(request, RequestOptions.DEFAULT));
		response.getSettings();

	}

    /**
     * This method will flush ElasticSearches field and filter 
     * caches.  The operation can take up to a minute to complete
     * 
     * @param indexNames
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
	public Map<String, Integer> flushCaches(final List<String> indexNames)  {
		Logger.warn(this.getClass(), "Flushing Elasticsearch index caches:" + indexNames);
		if(indexNames==null || indexNames.isEmpty()) {
			return ImmutableMap.of("failedShards",0, "successfulShards", 0);
		}

		ClearIndicesCacheRequest request = new ClearIndicesCacheRequest(
				indexNames.toArray(new String[0]));

		ClearIndicesCacheResponse clearCacheResponse = Sneaky.sneak(()->(esclient.indices()
				.clearCache(request, RequestOptions.DEFAULT)));


		Map<String, Integer> map= ImmutableMap.of("failedShards",
				clearCacheResponse.getFailedShards(), "successfulShards",
				clearCacheResponse.getSuccessfulShards());

		Logger.warn(this.getClass(), "Flushed Elasticsearch index caches:" + map);
		return map;
	}
    
    
    
    
	/**
	 * Writes an index to a backup file
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public  File backupIndex(String index) throws IOException {
		return backupIndex(index, null);
	}

	/**
	 * writes an index to a backup file
	 * @param index
	 * @param toFile
	 * @return
	 * @throws IOException
	 */
	public  File backupIndex(String index, File toFile) throws IOException {

		AdminLogger.log(this.getClass(), "backupIndex", "Trying to backup index: " + index);

	    boolean indexExists = indexExists(index);
        if (!indexExists) {
            throw new IOException("Index :" + index + " does not exist");
        }

		String date = new java.text.SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date());
		if (toFile == null) {
			toFile = new File(ConfigUtils.getBackupPath());
			if(!toFile.exists()){
				toFile.mkdirs();
			}
			toFile = new File(ConfigUtils.getBackupPath() + File.separator + index + "_" + date + ".json");
		}

		BufferedWriter bw;
		try (final ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(toFile.toPath()))){
		    zipOut.setLevel(9);
		    zipOut.putNextEntry(new ZipEntry(toFile.getName()));

			bw = new BufferedWriter(
			        new OutputStreamWriter(zipOut), 500000); // 500K buffer

			final String type=index.startsWith("sitesearch_") ? SiteSearchAPI.ES_SITE_SEARCH_MAPPING
					: "content";
	        final String mapping = mappingAPI.getMapping(index);
	        bw.write(MAPPING_MARKER);
	        bw.write(mapping);
	        bw.newLine();

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.matchAllQuery());
			searchSourceBuilder.size(100);
			searchSourceBuilder.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
			//_doc has no real use-case besides being the most efficient sort order.
			searchSourceBuilder.sort("_doc", SortOrder.DESC);


			SearchRequest searchRequest = new SearchRequest();
			searchRequest.scroll(TimeValue.timeValueMinutes(2));
			searchRequest.source(searchSourceBuilder);

			final SearchResponse searchResponse = Sneaky.sneak(()->
					esclient.search(searchRequest, RequestOptions.DEFAULT));

			String scrollId = searchResponse.getScrollId();


			while (true) {
				// new way
				SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
				scrollRequest.scroll(TimeValue.timeValueMinutes(2));
				SearchResponse searchScrollResponse = esclient
						.scroll(scrollRequest, RequestOptions.DEFAULT);

				scrollId = searchResponse.getScrollId();

				boolean hitsRead = false;
				for (SearchHit hit : searchResponse.getHits()) {
					bw.write(hit.getId());
					bw.write(JSON_RECORD_DELIMITER);
					bw.write(hit.getSourceAsString());
					bw.newLine();
					hitsRead = true;
				}
				if (!hitsRead) {
					break;
				}
			}
			return toFile;
		} catch (Exception e) {
		    Logger.error(this.getClass(), "Can't export index",e);
			throw new IOException(e.getMessage(),e);
		} finally {
			AdminLogger.log(this.getClass(), "backupIndex", "Back up for index: " + index + " done.");
		}
	}

	public boolean optimize(List<String> indexNames) {
		try {
            final ForceMergeRequest forceMergeRequest = new ForceMergeRequest(indexNames.toArray(
					new String[0]));
            final ForceMergeResponse forceMergeResponse =
                esclient.indices().forcemerge(forceMergeRequest, RequestOptions.DEFAULT);

            Logger.info(this.getClass(),
                "Optimizing " + indexNames + " :" + forceMergeResponse.getSuccessfulShards()
                    + "/" + forceMergeResponse.getTotalShards() + " shards optimized");

			return true;
		} catch (Exception e) {
			throw new ElasticsearchException(e.getMessage(),e);
		}
	}

	public boolean delete(String indexName) {
		if(indexName==null) {
			Logger.error(this.getClass(), "Failed to delete a null ES index");
			return true;
		}

		try {
            AdminLogger.log(this.getClass(), "delete", "Trying to delete index: " + indexName);
			DeleteIndexRequest request = new DeleteIndexRequest(indexName);
			request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
			AcknowledgedResponse deleteIndexResponse = esclient.indices()
					.delete(request, RequestOptions.DEFAULT);

            AdminLogger.log(this.getClass(), "delete", "Index: " + indexName + " deleted.");

            return deleteIndexResponse.isAcknowledged();
		} catch (Exception e) {
			throw new ElasticsearchException(e.getMessage(),e);
		}
	}

	/**
	 * Restores an index from a backup file
	 * @param backupFile
	 * @param index
	 * @throws IOException
	 */
	public  void restoreIndex(File backupFile, String index) throws IOException {

        AdminLogger.log(this.getClass(), "restoreIndex", "Trying to restore index: " + index);

        BufferedReader br = null;

        boolean indexExists = indexExists(index);

        try {
            if (!indexExists) {

                createIndex(index);
            }

            final ZipInputStream zipIn = new ZipInputStream(
                    Files.newInputStream(backupFile.toPath()));
            zipIn.getNextEntry();
            br = new BufferedReader(new InputStreamReader(zipIn));

            // setting number_of_replicas=0 to improve the indexing while restoring
            // also we restrict the index to the current server
            moveIndexToLocalNode(index);

            // wait a bit for the changes be made
            Thread.sleep(1000L);

            // setting up mapping
            String mapping = br.readLine();
            boolean mappingExists = mapping.startsWith(MAPPING_MARKER);
            String type = "content";
            ArrayList<String> jsons = new ArrayList<>();
            if (mappingExists) {

                String patternStr = "^" + MAPPING_MARKER + "\\s*\\{\\s*\"(\\w+)\"";
                Pattern pattern = Pattern.compile(patternStr);
                Matcher matcher = pattern.matcher(mapping);
                boolean matchFound = matcher.find();
                if (matchFound) {
                    type = matcher.group(1);

                    // we recover the line that wasn't a mapping so it should be content

                    ObjectMapper mapper = new ObjectMapper();
                    while (br.ready()) {
                        //read in 100 lines
                        for (int i = 0; i < 100; i++) {
                            if (!br.ready()) {
                                break;
                            }
                            jsons.add(br.readLine());
                        }

                        if (jsons.size() > 0) {
                            try {
								BulkRequest request = new BulkRequest();
								request.timeout(TimeValue.
										timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
                                for (String raw : jsons) {
                                    int delimidx = raw.indexOf(JSON_RECORD_DELIMITER);
                                    if (delimidx > 0) {
                                        String id = raw.substring(0, delimidx);
                                        String json = raw.substring(
                                                delimidx + JSON_RECORD_DELIMITER.length());
                                        if (id != null) {
                                            @SuppressWarnings("unchecked")
                                            Map<String, Object> oldMap = mapper
                                                    .readValue(json, HashMap.class);
                                            Map<String, Object> newMap = new HashMap<String, Object>();

                                            for (String key : oldMap.keySet()) {
                                                Object val = oldMap.get(key);
                                                if (val != null && UtilMethods
                                                        .isSet(val.toString())) {
                                                    newMap.put(key, oldMap.get(key));
                                                }
                                            }
											request.add(new IndexRequest(index, type, id)
                                                    .source(mapper.writeValueAsString(newMap)));
                                        }
                                    }
                                }
                                if (request.numberOfActions() > 0) {
                                	esclient.bulk(request, RequestOptions.DEFAULT);
                                }
                            } finally {
                                jsons = new ArrayList<>();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            if (br != null) {
                br.close();
            }

            // back to the original configuration for number_of_replicas
            // also let it go other servers
            moveIndexBackToCluster(index);

            final List<String> list = new ArrayList<>();
            list.add(index);
            iapi.optimize(list);

            AdminLogger.log(this.getClass(), "restoreIndex", "Index restored: " + index);
        }
    }

	/**
	 * List of all indicies
	 * @return
	 */
	public  Set<String> listIndices() {
		final GetIndexRequest request = new GetIndexRequest("*");
		final GetIndexResponse response = Sneaky.sneak(()->(
				esclient.indices().get(request, RequestOptions.DEFAULT)));

		return new HashSet<>(Arrays.asList(response.getIndices()));
	}

	/**
	 * Returns close status of an index
	 * @return
	 */
	// TODO replace with high level client
//	public boolean isIndexClosed(String index) {
//
//		Client client = esclient.getClient();
//		ImmutableOpenMap<String,IndexMetaData> indices = client.admin().cluster()
//			    .prepareState().get().getState()
//			    .getMetaData().getIndices();
//		IndexMetaData indexMetaData = indices.get(index);
//		if(indexMetaData != null)
//			return indexMetaData.getState() == State.CLOSE;
//		return true;
//	}

	/**
	 *
	 * @param indexName
	 * @return
	 */
	public  boolean indexExists(String indexName) {
		return listIndices().contains(indexName.toLowerCase());
	}

	/**
	 * Creates an index with default settings
	 * @param indexName
	 * @throws DotStateException
	 * @throws IOException
	 */
	public  void  createIndex(String indexName) throws DotStateException, IOException{

		createIndex(indexName, null, 0);
	}

	/**
	 * Creates an index with default settings. If shards<1 then shards will be default
	 * @param indexName
	 * @param shards
	 * @return
	 * @throws DotStateException
	 * @throws IOException
	 */
	public  CreateIndexResponse  createIndex(String indexName, int shards) throws DotStateException, IOException{

		return createIndex(indexName, null, shards);
	}

	/**
	 * deletes and recreates an index
	 * @param indexName
	 * @throws DotStateException
	 * @throws IOException
	 * @throws DotDataException
	 */
	public  void clearIndex(final String indexName) throws DotStateException, IOException, DotDataException{
		if(indexName == null || !indexExists(indexName)){
			throw new DotStateException("Index" + indexName + " does not exist");
		}

        AdminLogger.log(this.getClass(), "clearIndex", "Trying to clear index: " + indexName);

		final ClusterIndexHealth cih = getClusterHealth().get(indexName);
		final int shards = cih.getNumberOfShards();
		final String alias=getIndexAlias(indexName);

		iapi.delete(indexName);

		if(UtilMethods.isSet(indexName) && indexName.indexOf("sitesearch") > -1) {
			APILocator.getSiteSearchAPI().createSiteSearchIndex(indexName, alias, shards);
		} else {
			final CreateIndexResponse res=createIndex(indexName, shards);

			try {
				int w=0;
				while(!res.isAcknowledged() && ++w<100)
					Thread.sleep(100);
			}
			catch(InterruptedException ex) {
				Logger.warn(this, ex.getMessage(), ex);
			}
		}

		if(UtilMethods.isSet(alias)) {
		    createAlias(indexName, alias);
		}

        AdminLogger.log(this.getClass(), "clearIndex", "Index: " + indexName + " cleared");
	}

	/**
	 * unclusters an index, including changing the routing to all local
	 * @param index
	 * @throws IOException
	 */
	private void moveIndexToLocalNode(final String index) throws IOException {
		UpdateSettingsRequest request = new UpdateSettingsRequest(index);
		request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));

		final String nodeName="dotCMS_" + APILocator.getServerAPI().readServerId();

		request.settings(jsonBuilder().startObject()
				.startObject("index")
				.field("number_of_replicas",0)
				.field("routing.allocation.include._name",nodeName)
				.endObject()
				.endObject().toString(), XContentType.JSON);

		esclient.indices().putSettings(request, RequestOptions.DEFAULT);
    }

	/**
	 * clusters an index, including changing the routing
	 * @param index
	 * @throws IOException
	 */
    public void moveIndexBackToCluster(final String index) throws IOException {
		final XContentBuilder builder = jsonBuilder().startObject().startObject("index");

		setReplicasFields(builder);

		UpdateSettingsRequest request = new UpdateSettingsRequest(index);
		request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
		request.settings(builder.endObject().endObject().toString(), XContentType.JSON);
		esclient.indices().putSettings(request, RequestOptions.DEFAULT);
    }

	private void setReplicasFields(XContentBuilder builder) throws IOException {
		final ReplicasMode replicasMode = clusterAPI.getReplicasMode();
		if(replicasMode.getNumberOfReplicas()>-1) {
			builder.field("number_of_replicas", replicasMode.getNumberOfReplicas());
		}
		builder.field("auto_expand_replicas",replicasMode.getAutoExpandReplicas());
		builder.field("routing.allocation.include._name","*");
	}

	/**
     * Creates a new index.  If settings is null, the getDefaultIndexSettings() will be applied,
     * if shards <1, then the default # of shards will be set
     * @param indexName
     * @param settings
     * @param shards
     * @return
     * @throws ElasticsearchException
     * @throws IOException
     */
	public synchronized CreateIndexResponse createIndex(final String indexName, String settings,
			int shards) throws ElasticsearchException, IOException {

		final ReplicasMode replicasMode = clusterAPI.getReplicasMode();

		AdminLogger.log(this.getClass(), "createIndex",
			"Trying to create index: " + indexName + " with shards: " + shards);

		shards = getShardsFromConfigIfNeeded(shards);

		//default settings, if null
		if(settings ==null){
			settings = getDefaultIndexSettings(shards);
		}
		Map map = new ObjectMapper().readValue(settings, LinkedHashMap.class);
		map.put("number_of_shards", shards);
		map.put("index.mapping.total_fields.limit",
			Config.getIntProperty("ES_INDEX_MAPPING_TOTAL_FIELD_LIMITS", 5000));

		if(replicasMode.getNumberOfReplicas()>-1) {
			map.put("number_of_replicas", replicasMode.getNumberOfReplicas());
		}
		map.put("auto_expand_replicas",replicasMode.getAutoExpandReplicas());

		final CreateIndexRequest request = new CreateIndexRequest(indexName);
		request.settings(map);
		request.setTimeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
		final CreateIndexResponse createIndexResponse = esclient.indices().
				create(request, RequestOptions.DEFAULT);

		AdminLogger.log(this.getClass(), "createIndex",
			"Index created: " + indexName + " with shards: " + shards);

		return createIndexResponse;
	}

	private int getShardsFromConfigIfNeeded(int shards) {
		if(shards <1){
			try{
				shards = Integer.parseInt(System.getProperty("es.index.number_of_shards"));
			}catch(Exception e){
				Logger.warnAndDebug(ESIndexAPI.class, "Unable to parse shards from config", e);
			}
		}
		if(shards <1){
			try{
				shards = Config.getIntProperty("es.index.number_of_shards", 2);
			}catch(Exception e){
				Logger.warnAndDebug(ESIndexAPI.class, "Unable to parse shards from config", e);
			}
		}

		if(shards <0){
			shards=1;
		}
		return shards;
	}

	/**
	 * Returns the json (String) for
	 * the default ES index settings
	 * @param shards
	 * @return
	 * @throws IOException
	 */
	public String getDefaultIndexSettings(int shards) throws IOException{
		return jsonBuilder().startObject()
            .startObject("index")
           	.field("number_of_shards",shards)
            .startObject("analysis")
             .startObject("analyzer")
              .startObject("default")
               .field("type", "whitespace")
              .endObject()
             .endObject()
            .endObject()
           .endObject()
          .endObject().toString();
	}

    /**
     * returns cluster health
     * @return
     */
    public Map<String,ClusterIndexHealth> getClusterHealth() {
		ClusterHealthRequest request = new ClusterHealthRequest();
		request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
		ClusterHealthResponse response = Sneaky.sneak(()->
				esclient.cluster().health(request,RequestOptions.DEFAULT));

		return response.getIndices();
    }

	/**
	 * This method will update the number of
	 * replicas on a given index
	 * @param indexName
	 * @param replicas
	 * @throws DotDataException
	 */
    public synchronized void updateReplicas (final String indexName, final int replicas) throws DotDataException {

		if (!ClusterUtils.isReplicasSet() || !StringUtils
				.isNumeric(Config.getStringProperty("ES_INDEX_REPLICAS", null))) {
			AdminLogger.log(this.getClass(), "updateReplicas",
					"Replicas can only be updated when an Enterprise License is used and ES_INDEX_REPLICAS is set to a specific value.");
			throw new DotDataException(
					"Replicas can only be updated when an Enterprise License is used and ES_INDEX_REPLICAS is set to a specific value.");
		}

    	AdminLogger.log(this.getClass(), "updateReplicas", "Trying to update replicas to index: " + indexName);

		final ClusterIndexHealth health = getClusterHealth().get( indexName);
		if(health ==null){
			return;
		}

		final int curReplicas = health.getNumberOfReplicas();

		if(curReplicas != replicas){
			final Map<String,Integer> newSettings = new HashMap<>();
	        newSettings.put("number_of_replicas", replicas);

			UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);
			request.settings(newSettings);
			request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
			Sneaky.sneak(()->esclient.indices().putSettings(request, RequestOptions.DEFAULT));
		}

		AdminLogger.log(this.getClass(), "updateReplicas", "Replicas updated to index: " + indexName);
    }

//    public void putToIndex(String idx, String json, String id){
//	   try{
//		   Client client=new ESClient().getClient();
//
//		   IndexResponse response = client.prepareIndex(idx, SiteSearchAPI.ES_SITE_SEARCH_MAPPING, id)
//			        .setSource(json)
//			        .execute()
//			        .actionGet(INDEX_OPERATIONS_TIMEOUT_IN_MS);
//
//		} catch (Exception e) {
//		    Logger.error(ESIndexAPI.class, e.getMessage(), e);
//
//
//		}
//
//    }

    public void createAlias(String indexName, String alias) {
        try{
            // checking for existing alias
            if(getAliasToIndexMap(APILocator.getSiteSearchAPI().listIndices()).get(alias)==null) {
                IndicesAliasesRequest request = new IndicesAliasesRequest();
				request.addAliasAction(IndicesAliasesRequest.AliasActions.add().alias(alias)
						.index(indexName));
				request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
				esclient.indices().updateAliases(request, RequestOptions.DEFAULT);
            }
         } catch (Exception e) {
             Logger.error(ESIndexAPI.class, e.getMessage(), e);
             throw new RuntimeException(e);
         }
    }

    public Map<String,String> getIndexAlias(List<String> indexNames) {
        return getIndexAlias(indexNames.toArray(new String[indexNames.size()]));
    }

    public Map<String,String> getIndexAlias(String[] indexNames) {

    	GetAliasesRequest request = new GetAliasesRequest();
		request.indices(indexNames);
		GetAliasesResponse response = Sneaky.sneak(()->esclient.indices()
				.getAlias(request, RequestOptions.DEFAULT));

		Map<String,String> alias=new HashMap<>();

		response.getAliases().forEach((indexName, value) -> {
			final String aliasName = value.iterator().next().alias();
			alias.put(indexName, aliasName);
		});

		return alias;
    }

    public String getIndexAlias(String indexName) {
        return getIndexAlias(new String[]{indexName}).get(indexName);
    }

    public Map<String,String> getAliasToIndexMap(List<String> indices) {
        Map<String,String> map=getIndexAlias(indices);
        Map<String,String> mapReverse=new HashMap<>();
        for (String idx : map.keySet())
            mapReverse.put(map.get(idx), idx);
        return mapReverse;
    }

    public void closeIndex(String indexName) {
    	AdminLogger.log(this.getClass(), "closeIndex", "Trying to close index: " + indexName);

		final CloseIndexRequest request = new CloseIndexRequest(indexName);
		request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
        Sneaky.sneak(()->esclient.indices().close(request, RequestOptions.DEFAULT));

        AdminLogger.log(this.getClass(), "closeIndex", "Index: " + indexName + " closed");
    }

    public void openIndex(String indexName) {
    	AdminLogger.log(this.getClass(), "openIndex", "Trying to open index: " + indexName);

        final OpenIndexRequest request = new OpenIndexRequest(indexName);
		request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
		Sneaky.sneak(()->esclient.indices().open(new OpenIndexRequest(indexName)));

        AdminLogger.log(this.getClass(), "openIndex", "Index: " + indexName + " opened");
    }

    public List<String> getClosedIndexes() {

    	// new way TODO

		// old way
//        Client client = new ESClient().getClient();
//        ImmutableOpenMap<String, IndexMetaData>
//            indexState =
//            client.admin().cluster().prepareState().execute().actionGet(INDEX_OPERATIONS_TIMEOUT_IN_MS)
//                .getState().getMetaData().indices();
//
//        List<String> closeIdx = new ArrayList<>();
//
//        for (ObjectCursor<String> idx : indexState.keys()) {
//            IndexMetaData idxM = indexState.get(idx.value);
//            if (idxM.getState().equals(State.CLOSE)) {
//                closeIdx.add(idx.value);
//            }
//        }
//        return closeIdx;

		return Collections.emptyList();
    }

    public Status getIndexStatus(String indexName) throws DotDataException {
    	List<String> currentIdx = iapi.getCurrentIndex();
		List<String> newIdx =iapi.getNewIndex();

		boolean active =currentIdx.contains(indexName);
		boolean building =newIdx.contains(indexName);

		if(active) return Status.ACTIVE;
		else if(building) return Status.PROCESSING;
		else return Status.INACTIVE;

    }

	/**
	 * Creates a snapshot zip file using the index and creating a repository on
	 * the path.repo location. This file structure will remain on the file system.
	 * The snapshot name is usually the same as the index name.
	 *
	 * @param repositoryName
	 *            repository name
	 * @param snapshotName
	 *            snapshot name
	 * @param indexName
	 *            index name
	 * @return
	 *            zip file with the repository and snapshot
	 * @throws IllegalArgumentException
	 *            for invalid repository and snapshot names
	 * @throws IOException
	 *            for problems writing the files to the repository path
	 */
	public File createSnapshot(String repositoryName, String snapshotName, String indexName)
			throws IOException, IllegalArgumentException, DotStateException, ElasticsearchException {
		checkArgument(snapshotName!=null,"There is no valid snapshot name.");
		checkArgument(indexName!=null,"There is no valid index name.");

		String fileName = indexName + "_" + DateUtil.format(new Date(), "yyyy-MM-dd_hh-mm-ss");
		File toFile = null;
		// creates specific backup path (if it shouldn't exist)


		ClusterGetSettingsRequest clusterGetSettingsRequest = new ClusterGetSettingsRequest();
		ClusterGetSettingsResponse clusterGetSettingsResponse = esclient.cluster().
				getSettings(clusterGetSettingsRequest, RequestOptions.DEFAULT);

		// TODO this might bring no value, please check
		final String REPO_PATH = clusterGetSettingsResponse.getSetting(REPOSITORY_PATH);

		toFile = new File(REPO_PATH);
		if (!toFile.exists()) {
			toFile.mkdirs();
		}
		// initial repository under the complete path
		createRepository(toFile.getPath(), repositoryName, true);
		// if the snapshot exists on the repository
		if (isSnapshotExist(repositoryName, snapshotName)) {
			Logger.warn(this.getClass(), snapshotName + " snapshot already exists");
		} else {
			CreateSnapshotRequest request = new CreateSnapshotRequest(repositoryName,snapshotName);
			request.waitForCompletion(true);
			request.indices(indexName);

			CreateSnapshotResponse response = esclient.snapshot()
					.create(request, RequestOptions.DEFAULT);
			if (response.status().equals(RestStatus.OK)) {
				Logger.debug(this.getClass(), "Snapshot was created:" + snapshotName);
			} else {
				Logger.error(this.getClass(), response.status().toString());
				throw new ElasticsearchException("Could not create snapshot");
			}
		}
		// this will be the zip file using the same name of the directory path

		File toZipFile = new File(toFile.getParent() + File.separator + fileName + ".zip");
		try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(toZipFile.toPath()))) {
			ZipUtil.zipDirectory(toFile.getAbsolutePath(), zipOut);
			return toZipFile;
		}
	}

	/**
	 * Restores snapshot validating that such snapshot name exists on the
	 * repository
	 *
	 * @param repositoryName
	 *            Repository name
	 * @param snapshotName
	 *            Snapshot name, most exists on the repository
	 * @return Is true if the snapshot was restored
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 */
	private boolean restoreSnapshot(String repositoryName, String snapshotName)
			throws InterruptedException, ExecutionException {
		if (!isSnapshotExist(repositoryName, snapshotName) && ESIndexAPI.BACKUP_REPOSITORY.equals(repositoryName)) {
			snapshotName = BACKUP_REPOSITORY; //When restoring a snapshot created straight from a live index, the snapshotName is also: backup
		}
		if (isRepositoryExist(repositoryName) && isSnapshotExist(repositoryName, snapshotName)) {
			GetSnapshotsRequest getSnapshotsRequest = new GetSnapshotsRequest(repositoryName);
			GetSnapshotsResponse getSnapshotsResponse = Sneaky.sneak(()->esclient.snapshot().
					get(getSnapshotsRequest, RequestOptions.DEFAULT));

			final List<SnapshotInfo> snapshots = getSnapshotsResponse.getSnapshots();
			for(SnapshotInfo snapshot: snapshots){
				List<String> indices = snapshot.indices();
				for(String index: indices){
					// TODO verify if index is closed
//					if(!isIndexClosed(index)){
//						throw new DotStateException("Index \"" + index + "\" is not closed and can not be restored");
//					}
				}
			}
			RestoreSnapshotRequest restoreSnapshotRequest = new RestoreSnapshotRequest(repositoryName, snapshotName).waitForCompletion(true);
			RestoreSnapshotResponse response = Sneaky.sneak(()->esclient.snapshot().restore(restoreSnapshotRequest,
					RequestOptions.DEFAULT));

			if (response.status() != RestStatus.OK) {
				Logger.error(this.getClass(),
						"Problems restoring snapshot " + snapshotName + " with status: " + response.status().name());
			} else {
				Logger.debug(this.getClass(), "Snapshot was restored.");
				return true;
			}
		}
		return false;
	}


	/**
	 * Uploads and restore a snapshot by using a zipped repository from a input
	 * stream as source. The file name most comply to the format
	 * <index_name>.zip as the <index_name> will be used to identify the index
	 * name to be restored. The zip file contains the repository information,
	 * this includes the snapshot name. The index name is used to restore the
	 * snapshot, a repository might contain several snapshot thus the need to
	 * identify a snapshot by index name.  The repository is deleted after the
	 * restore is done.
	 *
	 * @param inputFile
	 *            stream with the zipped repository file
     *
	 * @return true if the snapshot was restored
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @throws ZipException
	 * 			   for problems during the zip extraction process
	 * @throws IOException
	 *             for problems writing the temporal zip file or the temporal zip contents
	 */
	public boolean uploadSnapshot(InputStream inputFile)
			throws InterruptedException, ExecutionException, ZipException, IOException {
		return uploadSnapshot(inputFile, true);
	}

	/**
	 * Uploads and restore a snapshot by using a zipped repository from a input
	 * stream as source. The file name most comply to the format
	 * <index_name>.zip as the <index_name> will be used to identify the index
	 * name to be restored. The zip file contains the repository information,
	 * this includes the snapshot name. The index name is used to restore the
	 * snapshot, a repository might contain several snapshot thus the need to
	 * identify a snapshot by index name.
	 *
	 * @param inputFile
	 *            stream with the zipped repository file
	 * @param cleanRepository
	 * 	          defines if the respository should be deleted after the restore.
	 *
	 * @return true if the snapshot was restored
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @throws ZipException
	 * 			   for problems during the zip extraction process
	 * @throws IOException
	 *             for problems writing the temporal zip file or the temporal zip contents
	 */
	public boolean uploadSnapshot(InputStream inputFile, boolean cleanRepository)
			throws InterruptedException, ExecutionException, ZipException, IOException {
		File outFile = null;
		AdminLogger.log(this.getClass(), "uploadSnapshot", "Trying to restore snapshot index");
		// creates specific backup path (if it shouldn't exist)
		ClusterGetSettingsRequest clusterGetSettingsRequest = new ClusterGetSettingsRequest();
		ClusterGetSettingsResponse clusterGetSettingsResponse = esclient.cluster().
				getSettings(clusterGetSettingsRequest, RequestOptions.DEFAULT);

		// TODO this might bring no value, please check
		final String REPO_PATH = clusterGetSettingsResponse.getSetting(REPOSITORY_PATH);

		File toDirectory = new File(REPO_PATH);
		if (!toDirectory.exists()) {
			toDirectory.mkdirs();
		}
		// zip file extraction
		outFile = File.createTempFile("snapshot", null, toDirectory.getParentFile());
		//File outFile = new File(toDirectory.getParent() + File.separator + snapshotName);
		FileUtils.copyStreamToFile(outFile, inputFile, null);
		ZipFile zipIn = new ZipFile(outFile);
		return uploadSnapshot(zipIn, toDirectory.getAbsolutePath(), cleanRepository);
	}

	/**
	 * Uploads and restore a snapshot using a zipped repository file and the
	 * index name to restore from the repository.
	 *
	 * @param zip
	 *            zip file containing the repository file structure
	 * @param toDirectory
	 *            place to extract the zip file
     *
	 * @return true if the snapshot was restored
     *
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @throws ZipException
	 * 			   for problems during the zip extraction process
	 * @throws IOException
	 *             for problems writing the temporal zip file or the temporal zip contents
	 */
	public boolean uploadSnapshot(ZipFile zip, String toDirectory, boolean cleanRepository)
			throws InterruptedException, ExecutionException, ZipException, IOException {
		ZipUtil.extract(zip, new File(toDirectory));
		File zipDirectory = null;
		try{
			zipDirectory = new File(toDirectory);
			String snapshotName = esIndexHelper.findSnapshotName(zipDirectory);
			if (snapshotName == null) {
				Logger.error(this.getClass(), "No snapshot file on the zip.");
				throw new ElasticsearchException("No snapshot file on the zip.");
			}
			if (!isRepositoryExist(BACKUP_REPOSITORY)) {
				// initial repository under the complete path
				createRepository(toDirectory, BACKUP_REPOSITORY, true);
			}
			return restoreSnapshot(BACKUP_REPOSITORY, snapshotName);
		}finally{
			File tempZip = new File(zip.getName());
			if(zip!=null && tempZip.exists()){
				tempZip.delete();
			}
			if(cleanRepository){
				deleteRepository(BACKUP_REPOSITORY);
			}
		}
	}

	/**
	 * Validates if a repository name exists on the ES client, using the data
	 * directory and the path.repo
	 *
	 * @param repositoryName
	 *            valid not null repository name
	 * @return true if the repository exists
	 */
	private boolean isRepositoryExist(String repositoryName) {
		boolean result = false;

		GetRepositoriesRequest request = new GetRepositoriesRequest();
		GetRepositoriesResponse response = Sneaky.sneak(()->esclient.snapshot()
				.getRepository(request, RequestOptions.DEFAULT));

		List<RepositoryMetaData> repositories = response.repositories();

		if (repositories.size() > 0) {
			for (RepositoryMetaData repo : repositories) {
				result = repo.name().equals(repositoryName);
				if (result){
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Creates a new repository for snapshots.  The snapshot name is usually the index name.
	 *
	 * @param path
	 *            path to repository, this should be within the repo.path
	 *            location
	 * @param repositoryName
	 *            repository name, if empty "catchall" in used by ES
	 * @param compress
	 *            if the repository should be compressed
	 * @throws IllegalArgumentException
	 *            if the path to the repository doesn't exists
	 * @return
	 *            true if the repository was created
	 */
	private boolean createRepository(String path, String repositoryName, boolean compress)
			throws IllegalArgumentException, DotStateException {
		boolean result = false;
		Path directory = Paths.get(path);
		if (!Files.exists(directory)) {
			throw new IllegalArgumentException("Invalid path to repository while creating the repository.");
		}
		if (!isRepositoryExist(repositoryName)) {
			Settings settings = Settings.builder().put("location", path).put("compress", compress)
					.build();

			PutRepositoryRequest request = new PutRepositoryRequest();
			request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));
			request.settings(settings);

			AcknowledgedResponse response = Sneaky.sneak(()->esclient.snapshot()
					.createRepository(request, RequestOptions.DEFAULT));

			if(result = response.isAcknowledged()){
				Logger.debug(this.getClass(), "Repository was created.");
			}else{
				//throw new DotStateException("Error creating respository on [" + path + "] named " + repositoryName);
				throw new DotIndexRepositoryException("Error creating respository on [" + path + "] named " + repositoryName,"error.creating.index.repository",path,repositoryName);
			}
		} else {
			Logger.warn(this.getClass(), repositoryName + " repository already exists");
		}
		return result;
	}

	/**
	 * Validates if a snapshot exist in a given repository usually the index name
	 *
	 * @param repositoryName
	 *            this repository should exists
	 * @param snapshotName
	 *            snapshot name
	 * @return true is the snapshot exists
	 */
	private boolean isSnapshotExist(String repositoryName, String snapshotName) {
		boolean result = false;
		GetSnapshotsRequest request = new GetSnapshotsRequest();
		request.repository(repositoryName);
		request.masterNodeTimeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));

		GetSnapshotsResponse response = Sneaky.sneak(()->esclient.snapshot()
				.get(request, RequestOptions.DEFAULT));

		List<SnapshotInfo> snapshotInfo = response.getSnapshots();

		if (snapshotInfo.size() > 0) {
			for (SnapshotInfo snapshot : snapshotInfo){
				result = snapshot.snapshotId().getName().equals(snapshotName);
				if(result){
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Deletes repository deleting the file system structure as well, beware
	 * various snapshot might be stored on the repository, this can not be
	 * undone.
	 *
	 * @param repositoryName repository name
	 * @return true if the repository is deleted
	 */
	public boolean deleteRepository(String repositoryName) {
		return deleteRepository(repositoryName, true);
	}

	/**
	 * Deletes repository, by setting cleanUp to true the repository will be
	 * removed from file system, beware various snapshot might be stored on the
	 * repository, this can not be undone.
	 *
	 * @param repositoryName repository name
	 * @param cleanUp true to remove files from file system after deleting the repository
	 *        reference.
	 * @return true if the repository is deleted
	 */
	public boolean deleteRepository(String repositoryName, boolean cleanUp) {

		boolean result = false;
		if (isRepositoryExist(repositoryName)) {
			try {
				DeleteRepositoryRequest request = new DeleteRepositoryRequest(repositoryName);
				request.timeout(TimeValue.timeValueMillis(INDEX_OPERATIONS_TIMEOUT_IN_MS));

				AcknowledgedResponse response = esclient.snapshot()
						.deleteRepository(request, RequestOptions.DEFAULT);

				if (response.isAcknowledged()) {
					Logger.info(this.getClass(), repositoryName + " repository has been deleted.");
					result = true;
				}
			} catch (Exception e) {
				Logger.error(this.getClass(), e.getMessage());
			}
			if (cleanUp) {
				ClusterGetSettingsRequest clusterGetSettingsRequest = new ClusterGetSettingsRequest();
				ClusterGetSettingsResponse clusterGetSettingsResponse = Sneaky.sneak(()-> esclient
						.cluster().getSettings(clusterGetSettingsRequest, RequestOptions.DEFAULT));

				// TODO this might bring no value, please check
				final String REPO_PATH = clusterGetSettingsResponse.getSetting(REPOSITORY_PATH);

				File toDelete = new File(REPO_PATH);
				try {
					FileUtil.deleteDir(toDelete.getAbsolutePath());
				} catch (IOException e) {
					Logger.error(this.getClass(), "The files on " + toDelete.getAbsolutePath() + " were not deleted.");
				}
			} else {
				Logger.warn(this.getClass(), "No files were deleted");
			}
		}
		return result;
	}

	public String getRepositoryPath(){
		ClusterGetSettingsRequest clusterGetSettingsRequest = new ClusterGetSettingsRequest();
		ClusterGetSettingsResponse clusterGetSettingsResponse = Sneaky.sneak(()-> esclient
				.cluster().getSettings(clusterGetSettingsRequest, RequestOptions.DEFAULT));

		// TODO this might bring no value, please check
		return clusterGetSettingsResponse.getSetting(REPOSITORY_PATH);
	}
}
