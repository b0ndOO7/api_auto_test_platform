package com.main.manage.modules.dao;

import com.main.manage.modules.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface TestCaseDao {

    List<TestCaseEntity> findTestCaseByProjectId(@Param("uid")String uid, @Param("projectId")String projectId);

    List<ProjectHostEntity> getProjectHostListByUid(@Param("uid")String uid);

    String checkProjectOwner(@Param("projectId")String projectId);

    String isExstsVersion(@Param("uid")String uid, @Param("projectId")String projectId, @Param("version")String version);

    boolean updateHostIp(@Param("uid")String uid, @Param("hostId")String hostId, @Param("projectId")String projectId, @Param("hostIp")String hostIp, @Param("domain")String domain);

    boolean insertHostIp(@Param("uid")String uid, @Param("projectId")String projectId, @Param("version")String version, @Param("hostIp")String hostIp, @Param("domain")String domain);

    //api 接口相关
    List<TestApiEntity> getAllApiByUid(@Param("uid")String uid, @Param("key_word")String key_word, @Param("startRow")int startRow, @Param("pageSize")int pageSize, @Param("moduleOrProjectList")List<String> moduleOrProjectList );
    Integer getTotalApiByUid(@Param("uid")String uid, @Param("key_word")String key_word, @Param("moduleOrProjectList")List<String> moduleOrProjectList);
    List<TestApiEntity> getApiListByUid(String uid, String key_word, String modulesId, int startRow, int pageSize);
    String isExstsTestApi(@Param("uid")String uid, @Param("projectId")String projectId, @Param("apiId")String apiId);
    boolean insertTestApi(@Param("projectId")String projectId, @Param("moduleId")String moduleId, @Param("apiName")String apiName,
                          @Param("apiProtoType")String apiProtoType,
                          @Param("apiMethodType")String apiMethodType,
                          @Param("domain")String domain, @Param("uri")String uri);
    boolean updateTestApi(@Param("id")String apiId, @Param("projectId")String projectId, @Param("moduleId")String moduleId, @Param("apiName")String apiName,
                          @Param("apiProtoType")String apiProtoType,
                          @Param("apiMethodType")String apiMethodType,
                          @Param("domain")String domain, @Param("uri")String uri);
    boolean updateTestApiStatusByIds(@Param("uid")String uid, @Param("ids")List<Integer> apiIds, @Param("status")int value) throws DataAccessException;

    // 获取单条api记录
    TestApiEntity getApiByUid(@Param("uid")String uid, @Param("apiId")String apiId);
    // 通过apiid获取api参数信息
    List<ApiInfoEntity> getApiInfoByAid(@Param("apiId")String apiId);

    boolean deleteApiInfoByAid(@Param("apiId")String apiId);
    boolean insertApiInfoBatch(List<ApiInfoEntity> apiInfoEntityList);

}
