package com.main.manage.modules.dao;

import com.main.manage.modules.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

    UserEntity findByUsername(String username);

    UserEntity findByUid(int uid);

    UserEntity checkPassword(@Param("username") String username, @Param("password") String password);

    List<UserEntity> findAll();
}
