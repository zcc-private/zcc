package zcc.es.Bean;

public enum  SearchTypeEnum {
    QUERY_THEN_FETCH(1, "query_then_fetch", "默认的搜索方式"),
    QUERY_AND_FETCH(2, "query_and_fetch", "最快的搜索方式，但结果是N倍!"),
    DFS_QUERY_THEN_FETCH(3, "dfs_query_then_fetch", "比2方式多了初始化散发步骤，可以更精确控制搜索打分和排名"),
    DFS_QUERY_AND_FETCH(4, "dfs_query_and_fetch", "比1方式多了初始化散发步骤!"),
    COUNT(5, "count", "根据,查询");

    private String msg;
    private String searchCode;
    private int code;

    private SearchTypeEnum(int code, String searchCode, String msg) {
        this.msg = msg;
        this.code = code;
        this.searchCode = searchCode;
    }

    public static String getName(int index) {
        SearchTypeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            SearchTypeEnum c = var1[var3];
            if (c.getCode() == index) {
                return c.searchCode;
            }
        }

        return null;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getSearchCode() {
        return this.searchCode;
    }
}
