package com.tot.team4.util;

import java.io.Serializable;

/**
 * @author guohb-a
 * @time 2023.08.30
 */
public class ResultObject<T> implements Serializable {
    private boolean success;
    private int code;
    private String message;
    private transient T data;
    public static final int NO_PRIVILEGE = -1;
    public static final int REPEAT_SUBMIT = -2;
    public static final int REPEAT_EXISTENT = -3;
    public static final int PARAMETER_ERROR = 1;
    public static final int SERVER_ERROR = 2;
    public static final int SESSION_INVALID = 3;
    public static final int SUCCESS = 0;
    private static final long serialVersionUID = -4534389991664735893L;

    public ResultObject() {
    }

    public ResultObject(boolean success) {
        this(success, (String)null);
    }

    public ResultObject(boolean success, String message) {
        this(success, message, null);
    }

    public ResultObject(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResultObject(boolean success, String message, int code, T data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static <T> ResultObject<T> success(T data) {
        return new ResultObject(true, "", 0, data);
    }

    public static <T> ResultObject<T> error(String message, int code) {
        return new ResultObject(false, message, code, (Object)null);
    }

    public static <T> ResultObject<T> empty() {
        return new ResultObject();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
