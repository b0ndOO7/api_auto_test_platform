package com.main.manage.modules.entity;

import java.util.ArrayList;
import java.util.List;

public class ModuleEntity {

    private String id;
    private int project_id;
    private String project;
    private String module;
    private String parent_id;
    private int status;
    private List<ModuleEntity> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ModuleEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ModuleEntity> childs) {
        this.children = childs;
    }
}
