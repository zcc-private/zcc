package zcc.es.Bean;

import java.io.Serializable;

public class RangQueryBean<T> implements Serializable {
    private static final long serialVersionUID = 4872669865514539066L;
    private String field;
    private T beginValue;
    private T endValue;
    private String format;
    private float boost;

    public void setField(String field) {
        this.field = field;
    }

    public void setBeginValue(T beginValue) {
        this.beginValue = beginValue;
    }

    public void setEndValue(T endValue) {
        this.endValue = endValue;
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

    public T getBeginValue() {
        return this.beginValue;
    }

    public T getEndValue() {
        return this.endValue;
    }

    public String getFormat() {
        return this.format;
    }

    public float getBoost() {
        return this.boost;
    }

    public String toString() {
        return "RangQueryBean(field=" + this.getField() + ", beginValue=" + this.getBeginValue() + ", endValue=" + this.getEndValue() + ", format=" + this.getFormat() + ", boost=" + this.getBoost() + ")";
    }

    public RangQueryBean() {
    }

    public RangQueryBean(String field, T beginValue, T endValue, String format, float boost) {
        this.field = field;
        this.beginValue = beginValue;
        this.endValue = endValue;
        this.format = format;
        this.boost = boost;
    }
}
