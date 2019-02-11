package com.main.manage.modules.model;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class ApiInfoModel {

    @NotBlank(message = "apiid不能为空")
    private int apiid;
    private String apiname;
    @NotBlank(message = "请求地址不能为空")
    private String apiurl;
    private String bodytype;
    private String jsonstr;
    private String methodtype;
    private String project_id;
    private String protocol_type;
    private String uid;
    private List<Map> form;
    private List<Map> headers;

    public int getApiid() {
        return apiid;
    }

    public void setApiid(int apiid) {
        this.apiid = apiid;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    public String getApiurl() {
        return apiurl;
    }

    public void setApiurl(String apiurl) {
        this.apiurl = apiurl;
    }

    public String getBodytype() {
        return bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }

    public String getJsonstr() {
        return jsonstr;
    }

    public void setJsonstr(String jsonstr) {
        this.jsonstr = jsonstr;
    }

    public String getMethodtype() {
        return methodtype;
    }

    public void setMethodtype(String methodtype) {
        this.methodtype = methodtype;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProtocol_type() {
        return protocol_type;
    }

    public void setProtocol_type(String protocol_type) {
        this.protocol_type = protocol_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Map> getForm() {
        return form;
    }

    public void setForm(List<Map> form) {
        this.form = form;
    }

    public List<Map> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map> headers) {
        this.headers = headers;
    }
}
