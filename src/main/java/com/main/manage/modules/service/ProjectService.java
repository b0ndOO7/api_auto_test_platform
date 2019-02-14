package com.main.manage.modules.service;

import com.main.manage.modules.dao.ProjectDao;
import com.main.manage.modules.entity.ModuleEntity;
import com.main.manage.modules.entity.ProjectEntity;
import com.main.manage.modules.entity.ProjectHostEntity;
import com.main.manage.modules.model.ProjectModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    public HashMap getProjectListByUid(String uid, String key_word, int curPage, int pageSize) {
        curPage = curPage<1 ? 1:curPage;
        pageSize = pageSize<1 ? 1:pageSize;
        int startRow = (curPage - 1) * pageSize;

        HashMap returnMap = new HashMap<>();
        returnMap.put("projects",projectDao.getUserProjectByUid(uid, startRow, pageSize, key_word));
        returnMap.put("totalrow",projectDao.getTotalProjectByUid(uid, key_word));
        log.info("getProjectListByUid: projects:{}", returnMap.get("projects"));
        log.info("getProjectListByUid: totalrow:{}", returnMap.get("totalrow"));

        return returnMap;
    }

    public HashMap getAllProjectByUid(String uid) {
        HashMap returnMap = new HashMap<>();
        returnMap.put("projects",projectDao.getAllUserProjectByUid(uid));

        return returnMap;
    }

    /**
     * 根据用户id查询项目及项目模块
     * @param uid
     * @return
     */
    public List<ModuleEntity> getUserProjectAndModuleByUid(String uid) {
        List<ModuleEntity> moduleEntityList = projectDao.getUserProjectAndModuleByUid(uid);

        return moduleEntityList;
    }
    /**
     * 根据项目id查询项目及项目模块
     * @param uid
     * @return
     */
    public List<ModuleEntity> getModuleByPid(String uid, String projectId) {
        List<ModuleEntity> moduleEntityList = projectDao.getModuleByProjectId(uid, projectId);

        return moduleEntityList;
    }

    /**
     * 根据项目id获取项目版本
     * @param uid
     * @param projectId
     * @return
     */
    public List<HashMap> getVersionsByProjectId(String uid, String projectId) {

        return projectDao.getVersionsByProjectId(uid, projectId);
    }

    public boolean addProject(ProjectModel projectModel) {
        //int id, String project, String type, String version, String desc, String uid
        return projectDao.addProject(new ProjectEntity(projectModel.getProject(),projectModel.getType(),projectModel.getVersion(),projectModel.getRemark(),projectModel.getUid()));
    }

    public String addVersion(ProjectModel projectModel) {
        //int id, String project, String type, String version, String desc, String uid
        ProjectHostEntity projectHostEntity = new ProjectHostEntity();
        projectHostEntity.setProjectId(String.valueOf(projectModel.getId()));
        projectHostEntity.setVersion(projectModel.getVersion());
        projectHostEntity.setRemark(projectModel.getRemark());

        String returnString = "";
        try {
            projectDao.addVersion(projectHostEntity);
            returnString = "SUCCESS";
        } catch (DataAccessException e) {
            System.out.println("新增项目版本异常，唯一索引重复");
            returnString = "FAIL,该版本号已存在";
        }
        return returnString;
    }

    public boolean editProjectById(ProjectModel projectModel) {
        //int id, String project, String type, String version, String desc, String uid
        if (StringUtils.isEmpty(projectModel.getUid()) || projectModel.getId() < 0 ) {
            return false;
        }
        ProjectEntity projectEntity = new ProjectEntity(projectModel.getId(),projectModel.getProject(),projectModel.getType(),projectModel.getVersion(),projectModel.getRemark(),projectModel.getUid(), projectModel.getVersionId());

        return projectDao.editProjectById(projectEntity) && projectDao.editVersionById(projectModel.getVersionId(),projectModel.getVersion(), projectModel.getId(), projectModel.getRemark());
    }

    public boolean deleteProjectById(ProjectModel[] projectModels) {
        //int id, String project, String type, String version, String desc, String uid
        String uid = "";
        List<Integer> deleteProjectIdList = new ArrayList<>();
        for (int i=0; i<projectModels.length; ++i) {
            if (projectModels[i].getUid().isEmpty() || projectModels[i].getId() < 0) {
                return false;
            }
            uid = projectModels[i].getUid();
            deleteProjectIdList.add(Integer.valueOf(projectModels[i].getId()));
        }
        return projectDao.deleteProjectByIds(uid, deleteProjectIdList);
    }

    public boolean deleteVersionById(ProjectModel projectModel) {
        //int id, String project, String type, String version, String desc, String uid

        return projectDao.deleteVersionById(projectModel.getVersionId());
    }

}
