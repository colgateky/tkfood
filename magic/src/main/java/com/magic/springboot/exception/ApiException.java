package com.magic.springboot.exception;

/**
 * Created by tt on 2017/3/23.
 */
public class ApiException extends RuntimeException {
    private int code;
    private String message;
    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiException build(String message) {
        ApiException ret = new ApiException(code, message);
        return ret;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void throwSelf() {
        throw this;
    }
}
