package zcc.es.Bean;

import java.io.Serializable;

public class EsQueryInfoBean<T> implements Serializable {
    private String field;
    private T value;
    private String format;
    private float boost = 1.0F;

    public void setField(String field) {
        this.field = field;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }

    public String getField() {
        return this.field;
    }

    public T getValue() {
        return this.value;
    }

    public String getFormat() {
        return this.format;
    }

    public float getBoost() {
        return this.boost;
    }

    public String toString() {
        return "EsQueryInfoBean(field=" + this.getField() + ", value=" + this.getValue() + ", format=" + this.getFormat() + ", boost=" + this.getBoost() + ")";
    }

    public EsQueryInfoBean() {
    }

    public EsQueryInfoBean(String field, T value, String format, float boost) {
        this.field = field;
        this.value = value;
        this.format = format;
        this.boost = boost;
    }
}
