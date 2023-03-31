package zcc.es.Bean;

import java.io.Serializable;
import java.util.*;

public class EsQueryBean implements Serializable {
    private String indexNames;
    private int searchType;
    private float minScore;
    private int from;
    private Integer currentPage;
    private Integer pageSize;
    private String resultType;
    private Boolean isPage;
    private Map<String, List<FcQueryBean>> mapAnalyzersCdtn;
    private List<FcQueryBean> analyzersCdtn;
    private List<EsQueryInfoBean> multiFieldCdtn;
    private List<EsQueryInfoBean<String>> regxCdtn;
    private List<RangQueryBean> rangCdtn;
    private List<EsQueryInfoBean<String>> likeEqCdtn;
    private List<EsQueryInfoBean<String>> noLikeEqCdtn;
    private List<EsQueryInfoBean<String>> noEqCdtn;
    private List<EsQueryInfoBean<String>> eqCdtn;
    private List<EsQueryInfoBean> orCdtn;
    private List<List<EsQueryInfoBean>> andOrCdtn;
    private List<String> isNullCdtn;
    private List<String> isNotNullCdtn;
    private Map<String, String> sortFileds;
    private List<String> hightFields;
    private String collapseField;
    private String[] includeFields;
    private String[] excludeFields;
    private Set<Integer> queryIf;

    public String getSortFileds(String key) {
        return (String)this.sortFileds.get(key);
    }

    public void setAnalyzersCdtno(FcQueryBean analyzer) {
        if (null == this.analyzersCdtn) {
            this.analyzersCdtn = new ArrayList();
        }

        this.analyzersCdtn.add(analyzer);
        this.queryIf.add(EsFunctionDescEnum.PARTICIPLE.getCode());
    }

    public void setAnalyzersCdtn(List<FcQueryBean> analyzersCdtn) {
        if (null == this.analyzersCdtn) {
            this.analyzersCdtn = analyzersCdtn;
        } else {
            this.analyzersCdtn.addAll(analyzersCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.PARTICIPLE.getCode());
    }

    public void setMapAnalyzersCdtno(String key, List<FcQueryBean> analyzersCdtn) {
        if (null == this.mapAnalyzersCdtn) {
            this.mapAnalyzersCdtn = new HashMap();
        } else {
            this.mapAnalyzersCdtn.put(key, analyzersCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.PARTICIPLE.getCode());
    }

    public void setMapAnalyzersCdtn(Map<String, List<FcQueryBean>> mapAnalyzersCdtn) {
        if (null == this.mapAnalyzersCdtn) {
            this.mapAnalyzersCdtn = mapAnalyzersCdtn;
        } else {
            this.mapAnalyzersCdtn.putAll(mapAnalyzersCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.PARTICIPLE.getCode());
    }

    public void setMultiFieldCdtno(EsQueryInfoBean<String> multiFieldCdtn) {
        if (null == this.multiFieldCdtn) {
            this.multiFieldCdtn = new ArrayList();
        }

        this.multiFieldCdtn.add(multiFieldCdtn);
        this.queryIf.add(EsFunctionDescEnum.MULTIFIELD.getCode());
    }

    public void setMultiFieldCdtn(List<EsQueryInfoBean> multiFieldCdtn) {
        if (null == this.multiFieldCdtn) {
            this.multiFieldCdtn = multiFieldCdtn;
        } else {
            this.multiFieldCdtn.addAll(multiFieldCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.MULTIFIELD.getCode());
    }

    public void setRegxCdtno(EsQueryInfoBean<String> regxCdtn) {
        if (null == this.regxCdtn) {
            this.regxCdtn = new ArrayList();
        }

        this.regxCdtn.add(regxCdtn);
        this.queryIf.add(EsFunctionDescEnum.REGX.getCode());
    }

    public void setRegxCdtn(List<EsQueryInfoBean<String>> regxCdtn) {
        if (null == this.regxCdtn) {
            this.regxCdtn = regxCdtn;
        } else {
            this.regxCdtn.addAll(regxCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.REGX.getCode());
    }

    public void setRangCdtno(RangQueryBean rangCdtn) {
        if (null == this.rangCdtn) {
            this.rangCdtn = new ArrayList();
        }

        this.rangCdtn.add(rangCdtn);
        this.queryIf.add(EsFunctionDescEnum.RANGE.getCode());
    }

    public void setRangCdtn(List<RangQueryBean> rangCdtn) {
        if (null == this.rangCdtn) {
            this.rangCdtn = rangCdtn;
        } else {
            this.rangCdtn.addAll(rangCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.RANGE.getCode());
    }

    public void setLikeEqCdtno(EsQueryInfoBean<String> likeEqCdtn) {
        if (null == this.likeEqCdtn) {
            this.likeEqCdtn = new ArrayList();
        }

        this.likeEqCdtn.add(likeEqCdtn);
        this.queryIf.add(EsFunctionDescEnum.LIKE.getCode());
    }

    public void setLikeEqCdtn(List<EsQueryInfoBean<String>> likeEqCdtn) {
        if (null == this.likeEqCdtn) {
            this.likeEqCdtn = likeEqCdtn;
        } else {
            this.likeEqCdtn.addAll(likeEqCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.LIKE.getCode());
    }

    public void setNoLikeEqCdtno(EsQueryInfoBean<String> noLikeEqCdtn) {
        if (null == this.noLikeEqCdtn) {
            this.noLikeEqCdtn = new ArrayList();
        }

        this.noLikeEqCdtn.add(noLikeEqCdtn);
        this.queryIf.add(EsFunctionDescEnum.NO_LIKE.getCode());
    }

    public void setNoLikeEqCdtn(List<EsQueryInfoBean<String>> noLikeEqCdtn) {
        if (null == this.noLikeEqCdtn) {
            this.noLikeEqCdtn = noLikeEqCdtn;
        } else {
            this.noLikeEqCdtn.addAll(noLikeEqCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.NO_LIKE.getCode());
    }

    public void setNoEqCdtno(EsQueryInfoBean<String> noEqCdtn) {
        if (null == this.noEqCdtn) {
            this.noEqCdtn = new ArrayList();
        }

        this.noEqCdtn.add(noEqCdtn);
        this.queryIf.add(EsFunctionDescEnum.NO_EQ.getCode());
    }

    public void setNoEqCdtn(List<EsQueryInfoBean<String>> noEqCdtn) {
        if (null == this.noEqCdtn) {
            this.noEqCdtn = noEqCdtn;
        } else {
            this.noEqCdtn.addAll(noEqCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.NO_EQ.getCode());
    }

    public void setEqCdtno(EsQueryInfoBean<String> eqCdtn) {
        if (null == this.eqCdtn) {
            this.eqCdtn = new ArrayList();
        }

        this.eqCdtn.add(eqCdtn);
        this.queryIf.add(EsFunctionDescEnum.EQ.getCode());
    }

    public void setEqCdtn(List<EsQueryInfoBean<String>> eqCdtn) {
        if (null == this.eqCdtn) {
            this.eqCdtn = eqCdtn;
        } else {
            this.eqCdtn.addAll(eqCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.EQ.getCode());
    }

    public void setOrCdtno(EsQueryInfoBean orCdtn) {
        if (null == this.orCdtn) {
            this.orCdtn = new ArrayList();
        }

        this.orCdtn.add(orCdtn);
        this.queryIf.add(EsFunctionDescEnum.OR.getCode());
    }

    public void setOrCdtn(List<EsQueryInfoBean> orCdtn) {
        if (null == this.orCdtn) {
            this.orCdtn = orCdtn;
        } else {
            this.orCdtn.addAll(orCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.OR.getCode());
    }

    public void setAndOrCdtno(List<EsQueryInfoBean> andOrCdtn) {
        if (null == this.andOrCdtn) {
            this.andOrCdtn = new ArrayList();
        }

        this.andOrCdtn.add(andOrCdtn);
        this.queryIf.add(EsFunctionDescEnum.AND_OR.getCode());
    }

    public void setIsNullCdtno(String isNullCdtn) {
        if (null == this.isNullCdtn) {
            this.isNullCdtn = new ArrayList();
        }

        this.isNullCdtn.add(isNullCdtn);
        this.queryIf.add(EsFunctionDescEnum.IS_NULL.getCode());
    }

    public void setIsNullCdtn(List<String> isNullCdtn) {
        if (null == this.isNullCdtn) {
            this.isNullCdtn = isNullCdtn;
        } else {
            this.isNullCdtn.addAll(isNullCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.IS_NULL.getCode());
    }

    public void setIsNotNullCdtno(String isNotNullCdtn) {
        if (null == this.isNotNullCdtn) {
            this.isNotNullCdtn = new ArrayList();
        }

        this.isNotNullCdtn.add(isNotNullCdtn);
        this.queryIf.add(EsFunctionDescEnum.NOT_NULL.getCode());
    }

    public void setIsNotNullCdtn(List<String> isNotNullCdtn) {
        if (null == this.isNotNullCdtn) {
            this.isNotNullCdtn = isNotNullCdtn;
        } else {
            this.isNotNullCdtn.addAll(isNotNullCdtn);
        }

        this.queryIf.add(EsFunctionDescEnum.NOT_NULL.getCode());
    }

    public String getIndexNames() {
        return this.indexNames;
    }

    public int getSearchType() {
        return this.searchType;
    }

    public float getMinScore() {
        return this.minScore;
    }

    public int getFrom() {
        return this.from;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public String getResultType() {
        return this.resultType;
    }

    public Boolean getIsPage() {
        return this.isPage;
    }

    public Map<String, List<FcQueryBean>> getMapAnalyzersCdtn() {
        return this.mapAnalyzersCdtn;
    }

    public List<FcQueryBean> getAnalyzersCdtn() {
        return this.analyzersCdtn;
    }

    public List<EsQueryInfoBean> getMultiFieldCdtn() {
        return this.multiFieldCdtn;
    }

    public List<EsQueryInfoBean<String>> getRegxCdtn() {
        return this.regxCdtn;
    }

    public List<RangQueryBean> getRangCdtn() {
        return this.rangCdtn;
    }

    public List<EsQueryInfoBean<String>> getLikeEqCdtn() {
        return this.likeEqCdtn;
    }

    public List<EsQueryInfoBean<String>> getNoLikeEqCdtn() {
        return this.noLikeEqCdtn;
    }

    public List<EsQueryInfoBean<String>> getNoEqCdtn() {
        return this.noEqCdtn;
    }

    public List<EsQueryInfoBean<String>> getEqCdtn() {
        return this.eqCdtn;
    }

    public List<EsQueryInfoBean> getOrCdtn() {
        return this.orCdtn;
    }

    public List<List<EsQueryInfoBean>> getAndOrCdtn() {
        return this.andOrCdtn;
    }

    public List<String> getIsNullCdtn() {
        return this.isNullCdtn;
    }

    public List<String> getIsNotNullCdtn() {
        return this.isNotNullCdtn;
    }

    public Map<String, String> getSortFileds() {
        return this.sortFileds;
    }

    public List<String> getHightFields() {
        return this.hightFields;
    }

    public String getCollapseField() {
        return this.collapseField;
    }

    public String[] getIncludeFields() {
        return this.includeFields;
    }

    public String[] getExcludeFields() {
        return this.excludeFields;
    }

    public Set<Integer> getQueryIf() {
        return this.queryIf;
    }

    public void setIndexNames(String indexNames) {
        this.indexNames = indexNames;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public void setMinScore(float minScore) {
        this.minScore = minScore;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void setIsPage(Boolean isPage) {
        this.isPage = isPage;
    }

    public void setAndOrCdtn(List<List<EsQueryInfoBean>> andOrCdtn) {
        this.andOrCdtn = andOrCdtn;
    }

    public void setSortFileds(Map<String, String> sortFileds) {
        this.sortFileds = sortFileds;
    }

    public void setHightFields(List<String> hightFields) {
        this.hightFields = hightFields;
    }

    public void setCollapseField(String collapseField) {
        this.collapseField = collapseField;
    }

    public void setIncludeFields(String[] includeFields) {
        this.includeFields = includeFields;
    }

    public void setExcludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }

    public void setQueryIf(Set<Integer> queryIf) {
        this.queryIf = queryIf;
    }

    public String toString() {
        return "EsQueryBean(indexNames=" + this.getIndexNames() + ", searchType=" + this.getSearchType() + ", minScore=" + this.getMinScore() + ", from=" + this.getFrom() + ", currentPage=" + this.getCurrentPage() + ", pageSize=" + this.getPageSize() + ", resultType=" + this.getResultType() + ", isPage=" + this.getIsPage() + ", mapAnalyzersCdtn=" + this.getMapAnalyzersCdtn() + ", analyzersCdtn=" + this.getAnalyzersCdtn() + ", multiFieldCdtn=" + this.getMultiFieldCdtn() + ", regxCdtn=" + this.getRegxCdtn() + ", rangCdtn=" + this.getRangCdtn() + ", likeEqCdtn=" + this.getLikeEqCdtn() + ", noLikeEqCdtn=" + this.getNoLikeEqCdtn() + ", noEqCdtn=" + this.getNoEqCdtn() + ", eqCdtn=" + this.getEqCdtn() + ", orCdtn=" + this.getOrCdtn() + ", andOrCdtn=" + this.getAndOrCdtn() + ", isNullCdtn=" + this.getIsNullCdtn() + ", isNotNullCdtn=" + this.getIsNotNullCdtn() + ", sortFileds=" + this.getSortFileds() + ", hightFields=" + this.getHightFields() + ", collapseField=" + this.getCollapseField() + ", includeFields=" + Arrays.deepToString(this.getIncludeFields()) + ", excludeFields=" + Arrays.deepToString(this.getExcludeFields()) + ", queryIf=" + this.getQueryIf() + ")";
    }

    public EsQueryBean() {
        this.searchType = SearchTypeEnum.QUERY_THEN_FETCH.getCode();
        this.currentPage = 1;
        this.pageSize = 10;
        this.resultType = "map";
        this.isPage = true;
        this.sortFileds = new LinkedHashMap();
        this.queryIf = new HashSet();
    }
}
