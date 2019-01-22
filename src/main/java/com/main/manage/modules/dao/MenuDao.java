package com.main.manage.modules.dao;

import com.main.manage.modules.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuDao {

    List<MenuEntity> getUserMenuById(String uid);

}
