package zcc.es.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zcc.es.Bean.EsPage;
import zcc.es.Bean.EsQueryBean;
import zcc.es.Bean.IndexInfo;
import zcc.es.Bean.SearchTypeEnum;
import zcc.es.common.ResultType;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
public class EsUtils {
    private static final Logger log = LoggerFactory.getLogger(EsUtils.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    private static RestHighLevelClient client;

    public EsUtils() {
    }

    @PostConstruct
    public void initClient() {
        client = this.restHighLevelClient;
    }

    public static RestHighLevelClient getClient() {
        return client;
    }

    private static String getIndexName(Object obj) {
        return StringUtil.toLowerCase(obj.getClass().getSimpleName(), "_");
    }

    public static List<String> getAnalyze(String text, String analyzer, int limitkeywordNum) throws Exception {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        } else {
            if (StringUtils.isEmpty(analyzer)) {
                analyzer = "not_analyzed";
            }

            if (limitkeywordNum == 0) {
                limitkeywordNum = 100;
            }

            List<String> list = new ArrayList();
            text = text.length() > limitkeywordNum ? text.substring(0, limitkeywordNum) : text;
            Request request = new Request("GET", "_analyze");
            JSONObject entity = new JSONObject();
            entity.put("analyzer", analyzer);
            entity.put("text", text);
            request.setJsonEntity(entity.toJSONString());
            Response response = client.getLowLevelClient().performRequest(request);
            JSONObject tokens = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            JSONArray arrays = tokens.getJSONArray("tokens");

            for(int i = 0; i < arrays.size(); ++i) {
                JSONObject obj = JSON.parseObject(arrays.getString(i));
                list.add(obj.getString("token"));
            }

            return list;
        }
    }

    public static List<String> getAnalyze(String text, String analyzer, String index, int limitkeywordNum) throws Exception {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        } else {
            if (StringUtils.isEmpty(analyzer)) {
                analyzer = "not_analyzed";
            }

            if (limitkeywordNum == 0) {
                limitkeywordNum = 100;
            }

            List<String> list = new ArrayList();
            text = text.length() > limitkeywordNum ? text.substring(0, limitkeywordNum) : text;
            Request request = new Request("GET", index.concat("/_analyze"));
            JSONObject entity = new JSONObject();
            entity.put("analyzer", analyzer);
            entity.put("text", text);
            request.setJsonEntity(entity.toJSONString());
            Response response = client.getLowLevelClient().performRequest(request);
            JSONObject tokens = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            JSONArray arrays = tokens.getJSONArray("tokens");

            for(int i = 0; i < arrays.size(); ++i) {
                JSONObject obj = JSON.parseObject(arrays.getString(i));
                list.add(obj.getString("token"));
            }

            return list;
        }
    }

    public static boolean createIndex(IndexInfo indexInfo) {
        if (!StringUtils.isEmpty(indexInfo.getIndexName()) && null != indexInfo.getClazz()) {
            if (isIndexExists(indexInfo.getIndexName())) {
                throw new RuntimeException(ResultType.ALREADY_EXIST.getDesc());
            } else {
                try {
                    XContentBuilder mapping = EsHelper.packXContentBuilder(indexInfo);
                    log.info(mapping.toString());
                    CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexInfo.getIndexName());
                    createIndexRequest.source(mapping);
                    createIndexRequest.setTimeout(TimeValue.timeValueMinutes(2L));
                    CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                    return createIndexResponse.isAcknowledged();
                } catch (IOException var4) {
                    log.info("索引[{}]创建失败", indexInfo.getIndexName());
                    throw new RuntimeException(ResultType.CREATE_FAILURE.getDesc(), var4);
                }
            }
        } else {
            throw new RuntimeException(ResultType.BAD_REQUEST.getDesc());
        }
    }

    public static boolean createIndexInfoJson(String indexName, String indexJson) {
        if (!StringUtils.isEmpty(indexName) && !StringUtils.isEmpty(indexJson)) {
            if (isIndexExists(indexName)) {
                throw new RuntimeException(ResultType.ALREADY_EXIST.getDesc());
            } else {
                try {
                    CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                    createIndexRequest.source(indexJson, XContentType.JSON);
                    createIndexRequest.setTimeout(TimeValue.timeValueMinutes(2L));
                    CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                    return createIndexResponse.isAcknowledged();
                } catch (IOException var4) {
                    log.info("索引[{}]创建失败", indexName);
                    throw new RuntimeException(ResultType.CREATE_FAILURE.getDesc(), var4);
                }
            }
        } else {
            throw new RuntimeException(ResultType.BAD_REQUEST.getDesc());
        }
    }

    public static String save(String indexName, String oid, String paramJson) {
        EsHelper.valBulkParam(indexName, paramJson);
        IndexRequest indexRequest = packSaveSingle(indexName, oid);
        indexRequest.source(paramJson, XContentType.JSON);
        IndexResponse response = saveData(indexRequest, indexName, oid);
        return response.getId();
    }

    public static String save(String indexName, String oid, Map<String, Object> map) {
        EsHelper.valBulkParam(indexName, map);
        IndexRequest indexRequest = packSaveSingle(indexName, oid);
        indexRequest.source(map, XContentType.JSON);
        IndexResponse response = saveData(indexRequest, indexName, oid);
        return response.getId();
    }

    public static Map<String, Object> saveBatch(String indexName, String primaryKeyName, String datas) {
        EsHelper.valBulkParam(indexName, primaryKeyName, datas);
        BulkRequest bulkRequest = EsHelper.packBatchBulkAdd(indexName, primaryKeyName, datas);
        return packSaveBatch(bulkRequest, indexName);
    }

    public static Map<String, Object> saveBatch(String indexName, String primaryKeyName, List<Map<String, Object>> datas) {
        EsHelper.valBulkParam(indexName, primaryKeyName, datas);
        BulkRequest bulkRequest = EsHelper.packBatchBulkAdd(indexName, primaryKeyName, datas);
        return packSaveBatch(bulkRequest, indexName);
    }

    public static boolean saveBatchAsny(final String indexName, String primaryKeyName, List<Map<String, Object>> datas) {
        EsHelper.valBulkParam(indexName, primaryKeyName, datas);
        BulkRequest bulkRequest = EsHelper.packBatchBulkAdd(indexName, primaryKeyName, datas);

        try {
            ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkResponse) {
                    if (bulkResponse.hasFailures()) {
                        BulkItemResponse[] var2 = bulkResponse.getItems();
                        int var3 = var2.length;

                        for(int var4 = 0; var4 < var3; ++var4) {
                            BulkItemResponse item = var2[var4];
                            EsUtils.log.error("索引[{}],主键[{}]更新操作失败,状态为:[{}],错误信息:{}", new Object[]{indexName, item.getId(), item.status(), item.getFailureMessage()});
                        }
                    }

                }

                @Override
                public void onFailure(Exception e) {
                    EsUtils.log.error("索引[{}]批量异步更新出现异常:{}", indexName, e);
                }
            };
            client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, listener);
            log.info("异步批量更新索引[{}]中", indexName);
            return true;
        } catch (Exception var5) {
            log.info("异步批量更新索引[{}]出现异常:{}", indexName, var5);
            throw new RuntimeException("索引[" + indexName + "]异步批量更新出现异常");
        }
    }

    public static boolean update(String indexName, String oid, String paramJson, boolean isAdd) {
        EsHelper.valBulkParam(indexName, oid, paramJson);
        UpdateRequest updateRequest = new UpdateRequest(indexName, oid);
        updateRequest.docAsUpsert(isAdd);
        updateRequest.doc(paramJson, XContentType.JSON);
        return setUpdate(indexName, updateRequest, oid);
    }

    public static boolean update(String indexName, String oid, Map<String, Object> map, boolean isAdd) {
        EsHelper.valBulkParam(indexName, oid, map);
        UpdateRequest updateRequest = new UpdateRequest(indexName, oid);
        updateRequest.docAsUpsert(isAdd);
        updateRequest.doc(map, XContentType.JSON);
        return setUpdate(indexName, updateRequest, oid);
    }

    public static Map<String, Object> updateBatch(String indexName, String primaryKeyName, List<Map<String, Object>> datas, boolean isAdd) {
        EsHelper.valBulkParam(indexName, primaryKeyName, datas);
        Map<String, Object> response = new HashMap();
        BulkRequest bulkRequest = EsHelper.packBatchBulkUpdate(indexName, primaryKeyName, datas, isAdd);

        try {
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (EsHelper.ifBulkFailur(bulk, response)) {
                return response;
            } else {
                EsHelper.setBatchCount(bulk, response);
                return response;
            }
        } catch (IOException var7) {
            log.error("索引[{}]批量修改更新出现异常", indexName);
            throw new RuntimeException("索引[" + indexName + "]批量修改出现异常");
        }
    }

    public static boolean delete(String indexName, String oid) {
        DeleteRequest deleteRequest = new DeleteRequest(indexName);
        deleteRequest.id(oid);

        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.error("索引[{}]主键[{}]删除失败", indexName, oid);
                return false;
            } else {
                log.info("索引[{}]主键[{}]删除成功", indexName, oid);
                return true;
            }
        } catch (IOException var4) {
            log.error("删除索引[{}]出现异常[{}]", indexName, var4);
            throw new RuntimeException("索引[" + indexName + "]删除出现异常");
        }
    }

    public static void clear(String index) {
        DeleteRequest deleteRequest = new DeleteRequest(index);

        try {
            DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("delete: {}", JSON.toJSONString(response));
        } catch (IOException var3) {
            throw new RuntimeException(ResultType.DEL_FAILURE.getDesc(), var3);
        }
    }

    public static boolean delete(String indexName) {
        if (!isIndexExists(indexName)) {
            log.error("索引[{}]不存在", indexName);
            return true;
        } else {
            try {
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
                deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
                AcknowledgedResponse acknowledgedResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
                return acknowledgedResponse.isAcknowledged();
            } catch (IOException var3) {
                log.error("索引[{}]删除异常:{}", indexName, var3);
                return false;
            }
        }
    }

    public static Map<String, Object> deleteBatch(String indexName, List<Long> ids) {
        Map<String, Object> response = new HashMap();
        BulkRequest bulkRequest = EsHelper.packIdsBatchBulk(indexName, ids);
        return setDelBatch(bulkRequest, response, indexName);
    }

    public static Map<String, Object> deleteBatch(String indexName, String[] ids) {
        Map<String, Object> response = new HashMap();
        BulkRequest bulkRequest = EsHelper.packIdsBatchBulk(indexName, ids);
        return setDelBatch(bulkRequest, response, indexName);
    }

    public static String queryDataById(String indexName, String oid) {
        GetRequest getRequest = new GetRequest(indexName, oid);

        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            String resultJson = getResponse.getSourceAsString();
            log.debug("索引[{}]主键[{}]查询结果[{}]", new Object[]{indexName, oid, resultJson});
            return resultJson;
        } catch (IOException var5) {
            log.debug("索引[{}]主键[{}]查询异常:{}", new Object[]{indexName, oid, var5});
            throw new RuntimeException("根据主键【" + oid + "】查询索引【" + indexName + "】异常", var5);
        }
    }

    public static EsPage queryData(EsQueryBean pageModel) {
        if (StringUtils.isEmpty(pageModel.getIndexNames())) {
            throw new RuntimeException("索引名称不能为空！");
        } else {
            QueryBuilder boolBuilder = EsHelper.queryBuilder(pageModel);
            SearchSourceBuilder ssb = EsHelper.searchBuilder(pageModel, boolBuilder);
            SearchRequest searchRequest = new SearchRequest(pageModel.getIndexNames().split(","));
            searchRequest.source(ssb);
            searchRequest.searchType(SearchTypeEnum.getName(pageModel.getSearchType()));
            return getSearchResponse(searchRequest, pageModel);
        }
    }

    protected static EsPage getSearchResponse(SearchRequest request, EsQueryBean pageModel) {
        try {
            SearchResponse sr = client.search(request, RequestOptions.DEFAULT);
            if (sr.status().getStatus() != 200) {
                log.error("检索出现错误", JacksonUtil.INSTANCE.toJson(sr));
                throw new RuntimeException("检索出现错误:" + JacksonUtil.INSTANCE.toJson(sr));
            } else {
                EsPage page = new EsPage();
                page.setPageSize(pageModel.getPageSize());
                page.setCurrentPage(pageModel.getCurrentPage());
                SearchHits hits = sr.getHits();
                long totalHits = hits.getTotalHits().value;
                page.setRecordCount((int)totalHits);
                long length = (long)hits.getHits().length;
                log.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
                List<Map<String, Object>> list = page.getResultData();
                List<String> hf = pageModel.getHightFields();
                Iterator var11 = hits.iterator();

                while(true) {
                    label45:
                    while(var11.hasNext()) {
                        SearchHit searchHit = (SearchHit)var11.next();
                        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                        if (null == hf) {
                            list.add(sourceAsMap);
                        } else {
                            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                            Iterator var15 = hf.iterator();

                            while(true) {
                                String f;
                                HighlightField titleField;
                                do {
                                    if (!var15.hasNext()) {
                                        list.add(sourceAsMap);
                                        continue label45;
                                    }

                                    f = (String)var15.next();
                                    titleField = (HighlightField)highlightFields.get(f);
                                } while(titleField == null);

                                Text[] fragments = titleField.fragments();
                                String title = "";
                                Text[] var20 = fragments;
                                int var21 = fragments.length;

                                for(int var22 = 0; var22 < var21; ++var22) {
                                    Text text = var20[var22];
                                    title = title + text;
                                }

                                sourceAsMap.put(f, title);
                            }
                        }
                    }

                    return page;
                }
            }
        } catch (IOException var24) {
            throw new RuntimeException("搜索出错", var24);
        }
    }

    public static void deleteIndex(String index) {
        try {
            client.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static boolean isIndexExists(String indexName) {
        boolean exists = false;

        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(new String[]{indexName});
            getIndexRequest.humanReadable(true);
            exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            return exists;
        } catch (IOException var3) {
            log.error("判断索引:{} 是否存在异常，异常内容:{}", indexName, var3);
            throw new RuntimeException("判断索引是否存在失败", var3);
        }
    }

    private static Map<String, Object> packSaveBatch(BulkRequest bulkRequest, String indexName) {
        HashMap response = new HashMap();

        try {
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (EsHelper.ifBulkFailur(bulk, response)) {
                return response;
            } else {
                EsHelper.setBatchCount(bulk, response);
                return response;
            }
        } catch (IOException var4) {
            log.error("索引[{}]批量同步更新出现异常", indexName);
            throw new RuntimeException("索引[" + indexName + "]批量同步更新出现异常");
        }
    }

    private static IndexRequest packSaveSingle(String indexName, String oid) {
        IndexRequest indexRequest = new IndexRequest(indexName);
        if (StringUtil.isNotEmpty(oid)) {
            indexRequest.id(oid);
        }

        return indexRequest;
    }

    private static IndexResponse saveData(IndexRequest indexRequest, String indexName, String oid) {
        IndexResponse response = null;

        try {
            response = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException var5) {
            log.error("索引[{}],主键[{}]保存异常:{}", new Object[]{indexName, oid, var5});
            throw new RuntimeException(ResultType.INTERNAL_ERROR.getDesc());
        }

        if (DocWriteResponse.Result.CREATED.equals(response.getResult())) {
            log.info("索引[{}],主键[{}]保存成功", indexName, oid);
        } else if (DocWriteResponse.Result.UPDATED.equals(response.getResult())) {
            log.info("索引[{}],主键[{}]修改成功", indexName, oid);
        }

        return response;
    }

    private static boolean setUpdate(String indexName, UpdateRequest updateRequest, String oid) {
        try {
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
            return EsHelper.setUpdate(updateResponse, indexName, oid);
        } catch (IOException var4) {
            log.info("索引:[{}],主键:[{}] 更新异常:{}", new Object[]{indexName, oid, var4});
            throw new RuntimeException("索引[" + indexName + "]更新出现异常");
        }
    }

    private static Map<String, Object> setDelBatch(BulkRequest bulkRequest, Map<String, Object> response, String indexName) {
        try {
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (EsHelper.ifBulkFailur(bulk, response)) {
                return response;
            } else {
                EsHelper.setBatchCount(bulk, response);
                Integer deleteCount = 0;
                BulkItemResponse[] var5 = bulk.getItems();
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    BulkItemResponse item = var5[var7];
                    if (DocWriteResponse.Result.DELETED.equals(item.getResponse().getResult())) {
                        deleteCount = deleteCount + 1;
                    }
                }

                response.put("deleteCount", deleteCount);
                log.info("批量删除索引[{}]成功,共删除[{}]个", indexName, deleteCount);
                return response;
            }
        } catch (IOException var11) {
            log.error("删除索引：{}批量保存数据出现异常:{}", indexName, var11);
            throw new RuntimeException("索引[" + indexName + "]删除出现异常");
        }
    }
}
