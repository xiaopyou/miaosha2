package com.qi.miaosha2.util;

public class RespEntity {//extends RuntimeException
    private Integer code;
    private String msg;

    public RespEntity(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    private Integer httpCode;
    private Object data;

    public RespEntity(Integer code, String msg, Integer httpCode, Object data) {
        this.code = code;
        this.msg = msg;
        this.httpCode = httpCode;
        this.data = data;
    }
    public RespEntity(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.httpCode = httpCode;
        this.data = data;
    }
    public RespEntity(Integer code, String msg) {

        this.code = code;
        this.msg = msg;
    }
    public RespEntity(Object data) {
        this.code = 20000;
        this.msg = "执行成功";
        this.httpCode = 200;
        this.data = data;
    }
    public RespEntity(String  msg, Object data) {
        this.code = 20000;
        this.msg = msg;
        this.httpCode = 200;
        this.data = data;
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

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RespEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", httpCode=" + httpCode +
                ", data=" + data +
                '}';
    }
}
