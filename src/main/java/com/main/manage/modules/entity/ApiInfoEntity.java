package com.main.manage.modules.entity;

public class ApiInfoEntity {

    private int id;
    private int api_id;
    private String param_type;
    private String request_key;
    private String request_value;
    private String is_correlation;  //是否关联
    private String status;
    private String domain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApi_id() {
        return api_id;
    }

    public void setApi_id(int api_id) {
        this.api_id = api_id;
    }

    public String getParam_type() {
        return param_type;
    }

    public void setParam_type(String param_type) {
        this.param_type = param_type;
    }

    public String getRequest_key() {
        return request_key;
    }

    public void setRequest_key(String request_key) {
        this.request_key = request_key;
    }

    public String getRequest_value() {
        return request_value;
    }

    public void setRequest_value(String request_value) {
        this.request_value = request_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIs_correlation() {
        return is_correlation;
    }

    public void setIs_correlation(String is_correlation) {
        this.is_correlation = is_correlation;
    }
}
