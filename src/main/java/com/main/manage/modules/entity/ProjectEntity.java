package com.main.manage.modules.entity;

public class ProjectEntity {

    Integer id;
    String project;
    String type;
    String version;
    Integer versionId;
    String remark;
    String uid;

    public ProjectEntity(Integer id, String project, String type, String version, String remark, String uid, Integer versionId) {
        this.id = id;
        this.project = project;
        this.type = type;
        this.version = version;
        this.remark = remark;
        this.uid = uid;
        this.versionId = versionId;
    }

    public ProjectEntity(String project, String type, String version, String remark, String uid) {
        this.project = project;
        this.type = type;
        this.version = version;
        this.remark = remark;
        this.uid = uid;
    }

    public ProjectEntity(Integer id, String project, String type, String uid) {
        this.id = id;
        this.project = project;
        this.type = type;
        this.uid = uid;
    }

    public ProjectEntity(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }
}
