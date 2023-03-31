package zcc.es.Bean;

import java.io.Serializable;

public class FcQueryBean implements Serializable {
    private String field;
    private String value;
    private String format;
    private float boost = 1.0F;
    private boolean ifPinyin = false;
    private boolean defaults = true;
    private boolean ifSign = false;
    private int slot = 1;
    private boolean isMatchPhrase = false;

    public void setField(String field) {
        this.field = field;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }

    public void setIfPinyin(boolean ifPinyin) {
        this.ifPinyin = ifPinyin;
    }

    public void setDefaults(boolean defaults) {
        this.defaults = defaults;
    }

    public void setIfSign(boolean ifSign) {
        this.ifSign = ifSign;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setMatchPhrase(boolean isMatchPhrase) {
        this.isMatchPhrase = isMatchPhrase;
    }

    public String getField() {
        return this.field;
    }

    public String getValue() {
        return this.value;
    }

    public String getFormat() {
        return this.format;
    }

    public float getBoost() {
        return this.boost;
    }

    public boolean isIfPinyin() {
        return this.ifPinyin;
    }

    public boolean isDefaults() {
        return this.defaults;
    }

    public boolean isIfSign() {
        return this.ifSign;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isMatchPhrase() {
        return this.isMatchPhrase;
    }

    public String toString() {
        return "FcQueryBean(field=" + this.getField() + ", value=" + this.getValue() + ", format=" + this.getFormat() + ", boost=" + this.getBoost() + ", ifPinyin=" + this.isIfPinyin() + ", defaults=" + this.isDefaults() + ", ifSign=" + this.isIfSign() + ", slot=" + this.getSlot() + ", isMatchPhrase=" + this.isMatchPhrase() + ")";
    }

    public FcQueryBean() {
    }

    public FcQueryBean(String field, String value, String format, float boost, boolean ifPinyin, boolean defaults, boolean ifSign, int slot, boolean isMatchPhrase) {
        this.field = field;
        this.value = value;
        this.format = format;
        this.boost = boost;
        this.ifPinyin = ifPinyin;
        this.defaults = defaults;
        this.ifSign = ifSign;
        this.slot = slot;
        this.isMatchPhrase = isMatchPhrase;
    }
}
