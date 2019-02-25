package com.main.manage.modules.entity;

public class TestCaseStepDataEntity {

    private int id;
    private int relation_id;
    private int case_id;
    private int api_id;
    private String param_type;
    private String request_key;
    private String request_value;
    private String is_correlation;  //是否关联
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(int relation_id) {
        this.relation_id = relation_id;
    }

    public int getCase_id() {
        return case_id;
    }

    public void setCase_id(int case_id) {
        this.case_id = case_id;
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

    public String getIs_correlation() {
        return is_correlation;
    }

    public void setIs_correlation(String is_correlation) {
        this.is_correlation = is_correlation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
