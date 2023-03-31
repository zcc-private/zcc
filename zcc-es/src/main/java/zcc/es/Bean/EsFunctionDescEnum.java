package zcc.es.Bean;

public enum EsFunctionDescEnum {
    PARTICIPLE(1, "analyzersCdtn", "分词查询"),
    MULTIFIELD(2, "multiFieldCdtn", "多字段一个值查询，多个字段使用,隔开"),
    REGX(3, "regxCdtn", "正则匹配查询，value中为正则表达式"),
    RANGE(4, "rangCdtn", "区间查询，时间区间需增加format格式"),
    LIKE(5, "likeEqCdtn", "模糊查询"),
    NO_LIKE(6, "noLikeEqCdtn", "过滤模糊匹配的信息"),
    NO_EQ(7, "noEqCdtn", "精确查询，过滤不相等的信息"),
    EQ(8, "eqCdtn", "精确查询，可单个字段多个值，即IN查询，多值通过,分隔"),
    OR(9, "orCdtn", "OR条件查询"),
    AND_OR(10, "andOrCdtn", "or条件查询集合类操作,即(x =1 or y=2)and(x1=2 or z=3)"),
    IS_NULL(11, "isNullCdtn", "为空过滤"),
    NOT_NULL(12, "isNotNullCdtn", "不为空过滤");

    private Integer code;
    private String field;
    private String desc;

    private EsFunctionDescEnum(Integer code, String field, String desc) {
        this.code = code;
        this.desc = desc;
        this.field = field;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getField() {
        return this.field;
    }
}
