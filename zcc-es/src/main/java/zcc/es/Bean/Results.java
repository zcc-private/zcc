package zcc.es.Bean;

import zcc.es.common.ResultType;

public class Results<T> {
    private int code;
    private String message;
    private T data;

    public Results(T data) {
        this.code = ResultType.OK.getCode();
        this.message = ResultType.OK.getDesc();
        this.data = data;
    }

    public Results() {
        this.code = ResultType.OK.getCode();
        this.message = ResultType.OK.getDesc();
    }

    public static Results error(int code, String message) {
        Results r = new Results();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static Results error(ResultType type) {
        Results r = new Results();
        r.setCode(type.getCode());
        r.setMessage(type.getDesc());
        return r;
    }

    public static Results error(int code) {
        Results r = new Results();
        r.setCode(code);
        return r;
    }

    public static Results error(String message) {
        Results r = new Results();
        r.setCode(ResultType.FAILURE.getCode());
        r.setMessage(message);
        return r;
    }

    public static Results success(Object data) {
        Results r = new Results();
        r.setData(data);
        return r;
    }

    public static Results success() {
        Results r = new Results();
        return r;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
