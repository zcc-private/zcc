package zcc.es.Bean;

import java.util.List;

public class FieldMapping {
    private String field;
    private String type;
    private String analyzer;
    private String searchAnalyze;
    private boolean store;
    private String format;
    private List<FieldMapping> fieldMappingList;

    public FieldMapping(String name, String type, String analyzer, String searchAnalyze, boolean store, String format) {
        this.field = name;
        this.type = type;
        this.analyzer = analyzer;
        this.searchAnalyze = searchAnalyze;
        this.store = store;
        this.format = format;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public void setSearchAnalyze(String searchAnalyze) {
        this.searchAnalyze = searchAnalyze;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setFieldMappingList(List<FieldMapping> fieldMappingList) {
        this.fieldMappingList = fieldMappingList;
    }

    public String getField() {
        return this.field;
    }

    public String getType() {
        return this.type;
    }

    public String getAnalyzer() {
        return this.analyzer;
    }

    public String getSearchAnalyze() {
        return this.searchAnalyze;
    }

    public boolean isStore() {
        return this.store;
    }

    public String getFormat() {
        return this.format;
    }

    public List<FieldMapping> getFieldMappingList() {
        return this.fieldMappingList;
    }

    public String toString() {
        return "FieldMapping(field=" + this.getField() + ", type=" + this.getType() + ", analyzer=" + this.getAnalyzer() + ", searchAnalyze=" + this.getSearchAnalyze() + ", store=" + this.isStore() + ", format=" + this.getFormat() + ", fieldMappingList=" + this.getFieldMappingList() + ")";
    }

    public FieldMapping() {
    }
}
