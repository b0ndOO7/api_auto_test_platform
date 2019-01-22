package com.main.manage.modules.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuTree {

    public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
    public List<MenuEntity> menuEntityList;
    public List<Object> list = new ArrayList<Object>();

    public List<Object> getMenuList(List<MenuEntity> menuEntities){
        this.menuEntityList = menuEntities;
        for (MenuEntity menuEntity : menuEntities) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if( menuEntity.getParent_id() == 0 ){
//                mapArr.put("id", menuEntity.getId());
                mapArr.put("title", menuEntity.getTitle());
//                mapArr.put("index", menuEntity.getIndex());
                mapArr.put("icon", menuEntity.getIcon());
                mapArr.put("path", menuEntity.getPath());
//                mapArr.put("parentid", menuEntity.getParent_id());
//                mapArr.put("rolerid", menuEntity.getRoler_id());
                mapArr.put("children", menuChild(menuEntity.getId()));
                list.add(mapArr);
            }
        }
        return list;
    }


    public List<?> menuChild(int id){
        List<Object> lists = new ArrayList<Object>();
        for(MenuEntity menuEntity : menuEntityList){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if( menuEntity.getParent_id() == id ){
//                childArray.put("id", menuEntity.getId());
                childArray.put("title", menuEntity.getTitle());
//                childArray.put("index", menuEntity.getIndex());
                childArray.put("icon", menuEntity.getIcon());
                childArray.put("path", menuEntity.getPath());
//                childArray.put("parent_id", menuEntity.getParent_id());
//                childArray.put("roler_id", menuEntity.getRoler_id());
                childArray.put("children", menuChild(menuEntity.getId()));
                lists.add(childArray);
            }
        }
        return lists;
    }


}
