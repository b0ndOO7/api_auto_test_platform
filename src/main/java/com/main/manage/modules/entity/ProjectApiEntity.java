package com.main.manage.modules.entity;

public class ProjectApiEntity {

    private int id;
    private String projectId;
    private String version;
    private String apiName;
    private String apiProtocolType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiProtocolType() {
        return apiProtocolType;
    }

    public void setApiProtocolType(String apiProtocolType) {
        this.apiProtocolType = apiProtocolType;
    }
}
