package zcc.es.Bean;

public enum QuerySign {
    AND("must", ""),
    OR("should", ""),
    NOTIN("notmust", "");

    private String code;
    private String msg;

    private QuerySign(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
