package com.shsxt.exception;

/**
 * 未登录异常
 */
public class NoLoginException extends RuntimeException {
    private Integer code=300;
    private String msg="用户未登录";


    public NoLoginException() {
    }

    public NoLoginException(String msg) {
        this.msg = msg;
    }

    public NoLoginException(Integer code) {
        this.code = code;
    }

    public NoLoginException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
