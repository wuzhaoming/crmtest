package com.shsxt.crm.model;

public class ResultInfo {
    private Integer code=200;
    private String msg="success";
    private Object result;

    public ResultInfo(String msg, Object result) {
        this.msg = msg;
        this.result = result;
    }

    public ResultInfo(Integer code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public ResultInfo() {
        this.msg=msg;
    }
    public ResultInfo(String msg) {
        this.msg=msg;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
