package com.main.manage.modules.dao;

import com.main.manage.modules.entity.ModuleEntity;
import com.main.manage.modules.entity.ProjectEntity;
import com.main.manage.modules.entity.ProjectHostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ProjectDao {

    List<ProjectEntity> getUserProjectByUid(@Param("uid")String uid, @Param("startRow")int startRow, @Param("pageSize")int pageSize, @Param("key_word")String key_word);

    List<ProjectEntity> getAllUserProjectByUid(@Param("uid")String uid);

    List<ModuleEntity> getUserProjectAndModuleByUid(@Param("uid")String uid);
    List<ModuleEntity> getModuleByProjectId(@Param("uid")String uid, @Param("projectId")String projectId);

    List<HashMap> getVersionsByProjectId(@Param("uid")String uid, @Param("projectId")String projectId);

    int getTotalProjectByUid(@Param("uid")String uid, @Param("key_word")String key_word);

    boolean editProjectById(ProjectEntity project);
    boolean editVersionById(@Param("versionId")int versionId, @Param("version")String version, @Param("projectId")int projectId, @Param("desc")String desc);

    boolean deleteProjectByIds(@Param("uid")String uid, @Param("projectList")List<Integer> list);
    boolean deleteVersionById(@Param("id")Integer id);

    boolean addProject(ProjectEntity project);

    boolean addVersion(ProjectHostEntity hostEntity) throws DataAccessException;
}
