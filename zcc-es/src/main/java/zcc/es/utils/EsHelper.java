package zcc.es.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import zcc.es.Bean.*;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EsHelper {
    private static final Logger log = LoggerFactory.getLogger(EsHelper.class);

    public EsHelper() {
    }

    public static BoolQueryBuilder queryBuilder(EsQueryBean pageModel) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        if (pageModel.getQueryIf().size() == 0) {
            return boolBuilder;
        } else {
            Set<Integer> set = pageModel.getQueryIf();
            if (Tools.isEmpty(pageModel.getMapAnalyzersCdtn()) && Tools.isEmpty(pageModel.getAnalyzersCdtn())) {
                MatchAllQueryBuilder m = QueryBuilders.matchAllQuery();
                boolBuilder.must(m);
            } else {
                mapAnalyzersSearch(pageModel, boolBuilder);
                analyzersSearch(pageModel, boolBuilder);
            }

            multiMatchSearch(pageModel, boolBuilder);
            rangeSearch(pageModel.getRangCdtn(), boolBuilder);
            likeEqSearch(pageModel, boolBuilder);
            noLikeEqSearch(pageModel, boolBuilder);
            eqSearch(pageModel, boolBuilder);
            noEqSearch(pageModel, boolBuilder);
            orSearch(pageModel, boolBuilder);
            andOrSearch(pageModel, boolBuilder);
            nullSearch(pageModel, boolBuilder);
            notNullSearch(pageModel, boolBuilder);
            regxSearch(pageModel, boolBuilder);
            return boolBuilder;
        }
    }

    public static SearchSourceBuilder searchBuilder(EsQueryBean pageModel, QueryBuilder boolBuilder) {
        int pageStart = 0;
        int pageSize = 10000;
        if (pageModel.getIsPage()) {
            pageSize = pageModel.getPageSize();
            pageStart = (pageModel.getCurrentPage() - 1) * pageSize;
        }

        SearchSourceBuilder ssb = new SearchSourceBuilder();
        ssb.query(boolBuilder).from(pageStart).size(pageSize).explain(false);
        if (Tools.isNotEmpty(pageModel.getHightFields())) {
            ssb.highlighter(getHighlightBuilder(pageModel.getHightFields()));
        }

        if (ArrayUtils.isNotEmpty(pageModel.getIncludeFields()) || ArrayUtils.isNotEmpty(pageModel.getExcludeFields())) {
            ssb.fetchSource(pageModel.getIncludeFields(), pageModel.getExcludeFields());
        }

        Iterator var5 = pageModel.getSortFileds().keySet().iterator();

        while(var5.hasNext()) {
            String sortKey = (String)var5.next();
            if ("asc".equalsIgnoreCase(pageModel.getSortFileds(sortKey))) {
                ssb.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.ASC));
            } else if ("desc".equalsIgnoreCase(pageModel.getSortFileds(sortKey))) {
                ssb.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.DESC));
            }
        }

        if (StringUtils.isNotBlank(pageModel.getCollapseField())) {
            ssb.collapse(new CollapseBuilder(pageModel.getCollapseField()));
        }

        if (pageModel.getFrom() != 0) {
            ssb.from(pageModel.getFrom());
        }

        if (pageModel.getPageSize() != 0) {
            ssb.size(pageModel.getPageSize());
        }

        if (pageModel.getMinScore() != 0.0F) {
            ssb.minScore(pageModel.getMinScore());
        }

        ssb.timeout(new TimeValue(60L, TimeUnit.SECONDS));
        ssb.explain(true);
        return ssb;
    }

    public static HighlightBuilder getHighlightBuilder(List<String> fields) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags(new String[]{"<span style='color:#3480FF;'>"});
        highlightBuilder.postTags(new String[]{"</span>"});
        fields.forEach((f) -> {
            highlightBuilder.field(f);
        });
        highlightBuilder.highlighterType("unified");
        highlightBuilder.fragmentSize(150);
        highlightBuilder.numOfFragments(0);
        return highlightBuilder;
    }

    public static void analyzersSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getAnalyzersCdtn())) {
            analyzersSearchc(pageModel.getAnalyzersCdtn(), boolBuilder);
        }
    }

    private static void analyzersSearchc(List<FcQueryBean> analyzersCdtn, BoolQueryBuilder boolBuilder) {
        DisMaxQueryBuilder d = QueryBuilders.disMaxQuery();
        float sum = 0.0F;
        Iterator var4 = analyzersCdtn.iterator();

        while(var4.hasNext()) {
            FcQueryBean x = (FcQueryBean)var4.next();
            String value;
            if (x.isMatchPhrase()) {
                MatchPhraseQueryBuilder mp;
                if (x.isDefaults()) {
                    mp = (MatchPhraseQueryBuilder)QueryBuilders.matchPhraseQuery(x.getField(), x.getValue()).slop(x.getSlot()).boost(x.getBoost());
                    d.add(mp);
                }

                if (x.isIfPinyin()) {
                    value = x.getValue();
                    MatchPhraseQueryBuilder mf = (MatchPhraseQueryBuilder)QueryBuilders.matchPhraseQuery(x.getField().concat(".full_pinyin"), value).slop(x.getSlot()).boost(0.4F * x.getBoost());
                    d.add(mf);
                    mp = (MatchPhraseQueryBuilder)QueryBuilders.matchPhraseQuery(x.getField().concat(".prefix_pinyin"), value).slop(x.getSlot()).boost(0.1F * x.getBoost());
                    d.add(mp);
                }

                if (x.isIfSign()) {
                    mp = (MatchPhraseQueryBuilder)QueryBuilders.matchPhraseQuery(x.getField().concat(".if_sign"), x.getValue()).slop(x.getSlot()).boost(x.getBoost());
                    d.add(mp);
                }

                sum += x.getBoost();
            } else {
                MatchQueryBuilder mp;
                if (x.isDefaults()) {
                    mp = (MatchQueryBuilder)QueryBuilders.matchQuery(x.getField(), x.getValue()).boost(x.getBoost());
                    d.add(mp);
                }

                if (x.isIfPinyin()) {
                    value = x.getValue();
                    MatchQueryBuilder mf = (MatchQueryBuilder)QueryBuilders.matchQuery(x.getField().concat(".full_pinyin"), value).boost(0.4F * x.getBoost());
                    d.add(mf);
                    mp = (MatchQueryBuilder)QueryBuilders.matchQuery(x.getField().concat(".prefix_pinyin"), value).boost(0.1F * x.getBoost());
                    d.add(mp);
                }

                if (x.isIfSign()) {
                    mp = (MatchQueryBuilder)QueryBuilders.matchQuery(x.getField().concat(".if_sign"), x.getValue()).boost(x.getBoost());
                    d.add(mp);
                }

                sum += x.getBoost();
            }
        }

        d.boost(sum / (float)analyzersCdtn.size());
        boolBuilder.must(d);
    }

    public static void mapAnalyzersSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getMapAnalyzersCdtn())) {
            Iterator var2 = pageModel.getMapAnalyzersCdtn().entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, List<FcQueryBean>> x = (Map.Entry)var2.next();
                analyzersSearchc((List)x.getValue(), boolBuilder);
            }

        }
    }

    public static void multiMatchSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getMultiFieldCdtn())) {
            pageModel.getMultiFieldCdtn().forEach((x) -> {
                MultiMatchQueryBuilder q = QueryBuilders.multiMatchQuery(x.getValue(), x.getField().split(","));
                boolBuilder.must(q).boost(x.getBoost());
            });
        }
    }

    public static void regxSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getRegxCdtn())) {
            pageModel.getRegxCdtn().forEach((x) -> {
                RegexpQueryBuilder rqb = QueryBuilders.regexpQuery(x.getField(), (String)x.getValue());
                boolBuilder.must(rqb).boost(x.getBoost());
            });
        }
    }

    public static void notNullSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getIsNotNullCdtn())) {
            pageModel.getIsNotNullCdtn().forEach((x) -> {
                ExistsQueryBuilder eqb = QueryBuilders.existsQuery(x);
                boolBuilder.mustNot(eqb);
            });
        }
    }

    public static void nullSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getIsNullCdtn())) {
            pageModel.getIsNullCdtn().forEach((x) -> {
                ExistsQueryBuilder eqb = QueryBuilders.existsQuery(x);
                boolBuilder.mustNot(eqb);
            });
        }
    }

    public static void andOrSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getAndOrCdtn())) {
            Iterator var2 = pageModel.getAndOrCdtn().iterator();

            while(var2.hasNext()) {
                List<EsQueryInfoBean> orList = (List)var2.next();
                BoolQueryBuilder tbq = QueryBuilders.boolQuery();
                filterQuery(orList, tbq);
                boolBuilder.must(tbq);
            }

        }
    }

    public static void orSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getOrCdtn())) {
            BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
            filterQuery(pageModel.getOrCdtn(), orBoolQueryBuilder);
            boolBuilder.must(orBoolQueryBuilder);
        }
    }

    public static void eqSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getEqCdtn())) {
            pageModel.getEqCdtn().forEach((x) -> {
                BoolQueryBuilder pbqb = QueryBuilders.boolQuery();
                filterQuery(x, pbqb);
                boolBuilder.must(pbqb);
            });
        }
    }

    public static void noEqSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getNoEqCdtn())) {
            pageModel.getNoEqCdtn().forEach((x) -> {
                BoolQueryBuilder pbqb = QueryBuilders.boolQuery();
                filterQuery(x, pbqb);
                boolBuilder.mustNot(pbqb);
            });
        }
    }

    public static void likeEqSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getLikeEqCdtn())) {
            pageModel.getLikeEqCdtn().forEach((x) -> {
                if (StringUtils.isNotEmpty((CharSequence)x.getValue())) {
                    BoolQueryBuilder q = wildCardQuery(x);
                    boolBuilder.must(q).boost(x.getBoost());
                }

            });
        }
    }

    public static void noLikeEqSearch(EsQueryBean pageModel, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(pageModel.getNoLikeEqCdtn())) {
            pageModel.getNoLikeEqCdtn().forEach((x) -> {
                if (StringUtils.isNotEmpty((CharSequence)x.getValue())) {
                    BoolQueryBuilder q = wildCardQuery(x);
                    boolBuilder.mustNot(q).boost(x.getBoost());
                }

            });
        }
    }

    public static void rangeSearch(List<RangQueryBean> ranges, BoolQueryBuilder boolBuilder) {
        if (!Tools.isEmpty(ranges)) {
            ranges.forEach((rcb) -> {
                RangeQueryBuilder range = QueryBuilders.rangeQuery(rcb.getField());
                if (null != rcb.getBeginValue()) {
                    range.gte(rcb.getBeginValue());
                }

                if (null != rcb.getEndValue()) {
                    range.lte(rcb.getEndValue());
                }

                range.includeLower(true);
                range.includeUpper(false);
                if (StringUtils.isEmpty(rcb.getFormat())) {
                    range.format(rcb.getFormat());
                }

                boolBuilder.must(range).boost(rcb.getBoost());
                log.debug("rangeQueryBuilder:" + range);
            });
        }
    }

    private static BoolQueryBuilder wildCardQuery(EsQueryInfoBean<String> info) {
        BoolQueryBuilder q = QueryBuilders.boolQuery();
        String[] var2 = ((String)info.getValue()).split(",");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String t = var2[var4];
            if (!StringUtils.isEmpty(t)) {
                WildcardQueryBuilder wq = QueryBuilders.wildcardQuery(info.getField(), "*" + t + "*");
                q.should(wq);
            }
        }

        return q;
    }

    private static void filterQuery(EsQueryInfoBean<String> info, BoolQueryBuilder bqb) {
        String[] vs = ((String)info.getValue()).split(",");
        BoolQueryBuilder b;
        if (vs.length > 1) {
            b = QueryBuilders.boolQuery();
            TermsQueryBuilder tq = QueryBuilders.termsQuery(info.getField() + ".keyword", vs);
            b.should(tq);
            TermsQueryBuilder lg = QueryBuilders.termsQuery(info.getField(), vs);
            b.should(lg);
            bqb.should(b).boost(info.getBoost());
        } else {
            b = QueryBuilders.boolQuery();
            TermQueryBuilder tq = QueryBuilders.termQuery(info.getField() + ".keyword", vs[0]);
            b.should(tq);
            TermQueryBuilder lg = QueryBuilders.termQuery(info.getField(), vs[0]);
            b.should(lg);
            bqb.should(b).boost(info.getBoost());
        }

    }

    private static void filterQuery(List<EsQueryInfoBean> info, BoolQueryBuilder bqb) {
        info.forEach((x) -> {
            BoolQueryBuilder b = QueryBuilders.boolQuery();
            TermQueryBuilder lg = QueryBuilders.termQuery(x.getField(), x.getValue());
            b.should(lg);
            bqb.should(b).boost(x.getBoost());
        });
    }

    public static XContentBuilder packXContentBuilder(IndexInfo indexInfo) throws IOException {
        XContentBuilder mapping = packESMapping(FieldMappingUtils.getFieldInfo(indexInfo.getClazz()), (XContentBuilder)null);
        mapping.endObject().startObject("settings");
        setShardsAndReplicas(mapping, indexInfo.getNumberOfshards(), indexInfo.getNumberOfReplicas());
        mapping.endObject().endObject();
        return mapping;
    }

    protected static XContentBuilder packESMapping(List<FieldMapping> fieldMappingList, XContentBuilder mapping) throws IOException {
        if (mapping == null) {
            mapping = XContentFactory.jsonBuilder().startObject().startObject("properties");
        }

        Iterator var2 = fieldMappingList.iterator();

        while(true) {
            while(true) {
                FieldMapping info;
                do {
                    if (!var2.hasNext()) {
                        return mapping;
                    }

                    info = (FieldMapping)var2.next();
                } while(!info.isStore());

                String field = info.getField();
                String dateType = info.getType();
                if (StringUtils.isBlank(dateType)) {
                    dateType = "string";
                }

                dateType = dateType.toLowerCase();
                String participle = info.getAnalyzer();
                if ("string".equals(dateType)) {
                    if (participle.equals("not_analyzed")) {
                        mapping.startObject(field).field("type", "keyword").endObject();
                    } else {
                        mapping.startObject(field).field("type", "text").field("analyzer", participle);
                        if (StringUtils.isNotEmpty(info.getSearchAnalyze())) {
                            mapping.field("search_analyzer", info.getSearchAnalyze());
                        }

                        mapping.endObject();
                    }
                } else if ("date".equals(dateType)) {
                    mapping.startObject(field).field("type", dateType).field("format", info.getFormat()).endObject();
                } else if (!"float".equals(dateType) && !"double".equals(dateType)) {
                    if ("nested".equals(dateType)) {
                        mapping.startObject(field).field("type", dateType).startObject("properties");
                        mapping = packESMapping(info.getFieldMappingList(), mapping);
                        mapping.endObject().endObject();
                    } else if (info.getFieldMappingList() != null) {
                        mapping.startObject(field).startObject("properties");
                        mapping = packESMapping(info.getFieldMappingList(), mapping);
                        mapping.endObject().endObject();
                    } else {
                        mapping.startObject(field).field("type", dateType).field("index", true).endObject();
                    }
                } else {
                    mapping.startObject(field).field("type", "scaled_float").field("scaling_factor", 100).endObject();
                }
            }
        }
    }

    private static void setShardsAndReplicas(XContentBuilder mapping, int numberOfshards, int numberOfReplicas) throws IOException {
        if (numberOfshards == 0) {
            mapping.field("number_of_shards", numberOfshards);
        }

        if (numberOfReplicas == 0) {
            mapping.field("number_of_replicas", numberOfReplicas);
        }

    }

    protected static BulkRequest packBatchBulkAdd(String indexName, String primaryKeyName, List<Map<String, Object>> list) {
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach((obj) -> {
            IndexRequest indexRequest = new IndexRequest(indexName);
            if (StringUtils.isNotEmpty(primaryKeyName)) {
                if (null != obj.get(primaryKeyName) && !obj.get(primaryKeyName).equals("")) {
                    indexRequest.id(String.valueOf(obj.get(primaryKeyName)));
                } else {
                    indexRequest.id(String.valueOf(SnowIdUtils.generateId()));
                }
            }

            indexRequest.source(indexValue(obj), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        return bulkRequest;
    }

    protected static BulkRequest packBatchBulkAdd(String indexName, String primaryKeyName, String jsonArray) {
        List<Map<String, Object>> maps = JacksonUtil.INSTANCE.jsonToListMap(jsonArray);
        return packBatchBulkAdd(indexName, primaryKeyName, maps);
    }

    protected static BulkRequest packBatchBulkUpdate(String indexName, String primaryKeyName, String jsonArray, boolean isAdd) {
        List<Map<String, Object>> maps = JacksonUtil.INSTANCE.jsonToListMap(jsonArray);
        return packBatchBulkUpdate(indexName, primaryKeyName, maps, isAdd);
    }

    protected static BulkRequest packBatchBulkUpdate(String indexName, String primaryKeyName, List<Map<String, Object>> list, boolean isAdd) {
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach((obj) -> {
            UpdateRequest updateRequest = new UpdateRequest(indexName, String.valueOf(obj.get(primaryKeyName)));
            updateRequest.docAsUpsert(isAdd);
            updateRequest.doc(obj, XContentType.JSON);
            bulkRequest.add(updateRequest);
        });
        return bulkRequest;
    }

    public static BulkRequest packIdsBatchBulk(String indexName, List<Long> ids) {
        BulkRequest bulkRequest = new BulkRequest();
        ids.forEach((id) -> {
            DeleteRequest deleteRequest = new DeleteRequest(indexName);
            deleteRequest.id(String.valueOf(id));
            bulkRequest.add(deleteRequest);
        });
        return bulkRequest;
    }

    public static BulkRequest packIdsBatchBulk(String indexName, String[] ids) {
        BulkRequest bulkRequest = new BulkRequest();
        String[] var3 = ids;
        int var4 = ids.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String id = var3[var5];
            DeleteRequest deleteRequest = new DeleteRequest(indexName);
            deleteRequest.id(id);
            bulkRequest.add(deleteRequest);
        }

        return bulkRequest;
    }

    public static String indexValue(Object obj) {
        return obj instanceof String ? String.valueOf(obj) : JacksonUtil.INSTANCE.toJson(obj);
    }

    public static void setBatchCount(BulkResponse bulk, Map<String, Object> response) {
        Integer createdCount = 0;
        Integer updatedCount = 0;
        BulkItemResponse[] var4 = bulk.getItems();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            BulkItemResponse item = var4[var6];
            if (DocWriteResponse.Result.CREATED.equals(item.getResponse().getResult())) {
                createdCount = createdCount + 1;
            } else if (DocWriteResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                updatedCount = updatedCount + 1;
            }
        }

        response.put("createdCount", createdCount);
        response.put("updatedCount", updatedCount);
    }

    public static boolean ifBulkFailur(BulkResponse bulk, Map<String, Object> response) {
        if (!bulk.hasFailures()) {
            return false;
        } else {
            StringBuffer sb = new StringBuffer();
            BulkItemResponse[] var3 = bulk.getItems();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                BulkItemResponse item = var3[var5];
                sb.append("索引[" + item.getIndex() + "],主键[" + item.getId() + "]更新操作失败,状态为:[" + item.status() + "],错误信息:{" + item.getFailureMessage() + "}").append("\n");
            }

            response.put("fail", sb.toString());
            return true;
        }
    }

    protected static void valBulkParam(String indexName, String primaryKeyName, List jsonArray) {
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(primaryKeyName) || Tools.isEmpty(jsonArray) || jsonArray.equals("{}")) {
            throw new RuntimeException("操作ES请求参数不能为空");
        }
    }

    protected static void valBulkParam(String jsonArray) {
        if (StringUtils.isEmpty(jsonArray) || jsonArray.equals("{}")) {
            throw new RuntimeException("操作ES请求参数不能为空");
        }
    }

    protected static void valBulkParam(String indexName, String primaryKeyName, String jsonArray) {
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(primaryKeyName) || StringUtils.isEmpty(jsonArray) || jsonArray.equals("{}")) {
            throw new RuntimeException("操作ES请求参数不能为空");
        }
    }

    protected static void valBulkParam(String indexName, String primaryKeyName, Object jsonArray) {
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(primaryKeyName) || ObjectUtils.isEmpty(jsonArray)) {
            throw new RuntimeException("操作ES请求参数不能为空");
        }
    }

    protected static void valBulkParam(String indexName, Object jsonArray) {
        if (StringUtils.isEmpty(indexName) || ObjectUtils.isEmpty(jsonArray)) {
            throw new RuntimeException("操作ES请求参数不能为空");
        }
    }

    public static boolean setUpdate(UpdateResponse updateResponse, String indexName, String oid) {
        log.info("索引[{}],主键:[{}]操作结果:[{}]", new Object[]{indexName, oid, updateResponse.getResult()});
        if (DocWriteResponse.Result.CREATED.equals(updateResponse.getResult())) {
            log.info("索引:[{}],主键:[{}]新增成功", indexName, oid);
            return true;
        } else if (DocWriteResponse.Result.UPDATED.equals(updateResponse.getResult())) {
            log.info("索引:[{}],主键:[{}]修改成功", indexName, oid);
            return true;
        } else if (DocWriteResponse.Result.NOOP.equals(updateResponse.getResult())) {
            log.info("索引:[{}] 主键:[{}] 无变化", indexName, oid);
            return true;
        } else {
            return true;
        }
    }
}
