package com.main.manage.utils;

public class HttpResp {

    private int statusCode;
    private String respStr;

    public HttpResp() { }

    public HttpResp(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpResp(int statusCode, String respStr) {
        this.statusCode = statusCode;
        this.respStr = respStr;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRespStr() {
        return respStr;
    }

    public void setRespStr(String respStr) {
        this.respStr = respStr;
    }
}
