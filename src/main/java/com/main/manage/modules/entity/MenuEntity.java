package com.main.manage.modules.entity;

import java.util.ArrayList;
import java.util.List;

public class MenuEntity {

    private int id;
    private String icon;
    private int index;
    private String title;
    private String path;
    private int parent_id;
    private String roler_id;
    private List<MenuEntity> childmenus = new ArrayList<>();

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getRoler_id() {
        return roler_id;
    }

    public void setRoler_id(String roler_id) {
        this.roler_id = roler_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MenuEntity> getChildmenus() {
        return childmenus;
    }

    public void setChildmenus(List<MenuEntity> childmenus) {
        this.childmenus = childmenus;
    }
}
