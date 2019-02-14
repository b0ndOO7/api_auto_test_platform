package com.main.manage.modules.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModuleTree {

    public List<ModuleEntity> moduleEntityList;
    public List<Object> list = new ArrayList<Object>();

    public List<Object> getMenuList(List<ModuleEntity> moduleEntities){
        this.moduleEntityList = moduleEntities;
        for (ModuleEntity moduleEntity : moduleEntities) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if( String.valueOf(-1).equals(moduleEntity.getParent_id()) ){
                mapArr.put("id", moduleEntity.getId());
                mapArr.put("label", moduleEntity.getProject());
                mapArr.put("projectid", moduleEntity.getProject_id());
                mapArr.put("project", moduleEntity.getProject());
                mapArr.put("module", moduleEntity.getModule());
                mapArr.put("parentid", moduleEntity.getParent_id());
                mapArr.put("status", moduleEntity.getStatus());
                List<?> tmpList = moduleChild(moduleEntity.getProject_id());
                if (tmpList.size()>0) {
                    mapArr.put("children", tmpList);
                }
                list.add(mapArr);
            }
        }
        return list;
    }

    /**
     * 没时间解释
     * @param projectId
     * @return
     */
    public List<?> moduleChild(int projectId){
        List<Object> lists = new ArrayList<Object>();
        for(ModuleEntity moduleEntity : moduleEntityList){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if ( String.valueOf(0).equals(moduleEntity.getParent_id()) && moduleEntity.getProject_id() == projectId) {
                childArray.put("id", moduleEntity.getId());
                childArray.put("label", moduleEntity.getModule());
                childArray.put("projectid", moduleEntity.getProject_id());
                childArray.put("project", moduleEntity.getProject());
                childArray.put("module", moduleEntity.getModule());
                childArray.put("parentid", moduleEntity.getParent_id());
                childArray.put("status", moduleEntity.getStatus());
                List<?> list = moduleChild(moduleEntity.getId(), projectId);
                if (list.size()>0) {
                    childArray.put("children", list);
                }
                lists.add(childArray);
            }
        }
        return lists;
    }

    /**
     * 没时间解释
     * @param id
     * @param projectId
     * @return
     */
    public List<?> moduleChild(String id, int projectId){
        List<Object> lists = new ArrayList<Object>();
        for(ModuleEntity moduleEntity : moduleEntityList){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if (moduleEntity.getParent_id().equals(id) && moduleEntity.getProject_id() == projectId) {
                childArray.put("id", moduleEntity.getId());
                childArray.put("label", moduleEntity.getModule());
                childArray.put("projectid", moduleEntity.getProject_id());
                childArray.put("project", moduleEntity.getProject());
                childArray.put("module", moduleEntity.getModule());
                childArray.put("parentid", moduleEntity.getParent_id());
                childArray.put("status", moduleEntity.getStatus());
                List<?> list = moduleChild(moduleEntity.getId(), projectId);
                if (list.size()>0) {
                    childArray.put("children", list);
                }
                lists.add(childArray);
            }
        }
        return lists;
    }


    public List<Object> getMenuListNoProject(List<ModuleEntity> moduleEntities){
        this.moduleEntityList = moduleEntities;
        for (ModuleEntity moduleEntity : moduleEntities) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if( String.valueOf(0).equals(moduleEntity.getParent_id()) ){
                mapArr.put("id", moduleEntity.getId());
                mapArr.put("label", moduleEntity.getModule());
                mapArr.put("projectid", moduleEntity.getProject_id());
                mapArr.put("project", moduleEntity.getProject());
                mapArr.put("module", moduleEntity.getModule());
                mapArr.put("parentid", moduleEntity.getParent_id());
                mapArr.put("status", moduleEntity.getStatus());
                List<?> tmpList = moduleChildNoProject(moduleEntity.getId());
                if (tmpList.size()>0) {
                    mapArr.put("children", tmpList);
                }
                list.add(mapArr);
            }
        }
        return list;
    }

    public List<?> moduleChildNoProject(String moduleId){
        List<Object> lists = new ArrayList<Object>();
        for(ModuleEntity moduleEntity : moduleEntityList){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if ( moduleId.equals(moduleEntity.getParent_id())) {
                childArray.put("id", moduleEntity.getId());
                childArray.put("label", moduleEntity.getModule());
//                childArray.put("projectid", moduleEntity.getProject_id());
//                childArray.put("project", moduleEntity.getProject());
                childArray.put("module", moduleEntity.getModule());
                childArray.put("parentid", moduleEntity.getParent_id());
                childArray.put("status", moduleEntity.getStatus());
                List<?> list = moduleChildNoProject(moduleEntity.getId());
                if (list.size()>0) {
                    childArray.put("children", list);
                }
                lists.add(childArray);
            }
        }
        return lists;
    }

}
