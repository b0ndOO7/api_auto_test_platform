package com.main.manage.modules.dao;

import com.main.manage.modules.entity.ProjectHostEntity;
import com.main.manage.modules.entity.TestApiEntity;
import com.main.manage.modules.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface TestCaseDao {

    UserEntity findByUsername(String username);

    UserEntity findByUid(int uid);

    UserEntity checkPassword(@Param("username") String username, @Param("password") String password);

    List<ProjectHostEntity> getProjectHostListByUid(@Param("uid")String uid);

    String checkProjectOwner(@Param("projectId")String projectId);

    String isExstsVersion(@Param("uid")String uid, @Param("projectId")String projectId, @Param("version")String version);

    boolean updateHostIp(@Param("uid")String uid, @Param("hostId")String hostId, @Param("projectId")String projectId, @Param("hostIp")String hostIp, @Param("domain")String domain);

    boolean insertHostIp(@Param("uid")String uid, @Param("projectId")String projectId, @Param("version")String version, @Param("hostIp")String hostIp, @Param("domain")String domain);

    //api 接口相关
    List<TestApiEntity> getAllApiByUid(@Param("uid")String uid, @Param("key_word")String key_word, @Param("startRow")int startRow, @Param("pageSize")int pageSize, @Param("moduleList")List<String> moduleList );
    Integer getTotalApiByUid(@Param("uid")String uid, @Param("key_word")String key_word, @Param("moduleList")List<String> moduleList);
    List<TestApiEntity> getApiListByUid(String uid, String key_word, String modulesId, int startRow, int pageSize);
    String isExstsTestApi(@Param("uid")String uid, @Param("projectId")String projectId, @Param("apiId")String apiId);
    boolean insertTestApi(@Param("projectId")String projectId, @Param("moduleId")String moduleId, @Param("apiName")String apiName, @Param("apiProtoType")String apiProtoType, @Param("domain")String domain, @Param("uri")String uri);
    boolean updateTestApi(@Param("id")String apiId, @Param("projectId")String projectId, @Param("moduleId")String moduleId, @Param("apiName")String apiName, @Param("apiProtoType")String apiProtoType, @Param("domain")String domain, @Param("uri")String uri);
    boolean updateTestApiStatusByIds(@Param("uid")String uid, @Param("ids")List<Integer> apiIds, @Param("status")int value) throws DataAccessException;
}
