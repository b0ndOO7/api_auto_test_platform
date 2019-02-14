package com.main.manage.modules.controller;

import com.main.manage.RestTemplate.Result;
import com.main.manage.modules.entity.ModuleEntity;
import com.main.manage.modules.entity.ModuleTree;
import com.main.manage.modules.model.ProjectModel;
import com.main.manage.modules.service.ProjectService;
import com.main.manage.utils.FastJsonUtil;
import com.main.manage.utils.ResultCode;
import com.main.manage.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    /**
     * 获取用户项目列表-分页
     * @param map
     * @return
     */
    @RequestMapping("getuserprojectlist")
    public Result getProjectList(@RequestBody Map<String,String> map) {
        log.info("getProjectList: {}", map);
        int curPage = 0, pageSize = 0;
        try {
            String uid = map.get("uid");
            String key_word = map.get("keyword");
            curPage = Integer.valueOf(map.get("cur_page"));
            pageSize = Integer.valueOf(map.get("page_size"));
            if (StringUtils.isEmpty(uid)) {
                ResultUtils.warn(ResultCode.PARAMETER_NULL, "UID不能为空");
            }
            return ResultUtils.success(projectService.getProjectListByUid(uid, key_word, curPage, pageSize));
        } catch (Exception e) {
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }


    }

    /**
     * 获取用户所有项目列表
     * @param map
     * @return
     */
    @RequestMapping("getuserallproject")
    public Result getAllProjectList(@RequestBody Map<String,String> map) {
        String uid = map.get("uid");
        if ( StringUtils.isEmpty(uid)) {
            ResultUtils.warn(ResultCode.PARAMETER_NULL, "UID不能为空");
        }
        return ResultUtils.success(projectService.getAllProjectByUid(uid));
    }

    /**
     * 获取项目树型结构
     * @return
     */
    @RequestMapping("getprojectandmodule")
    public Result getUserProjectAndModule(@RequestHeader String uid) {
        if ( StringUtils.isEmpty(uid)) {
            ResultUtils.warn(ResultCode.PARAMETER_NULL, "UID不能为空");
        }
        List<ModuleEntity> moduleEntityList = projectService.getUserProjectAndModuleByUid(uid);
        return ResultUtils.success(new ModuleTree().getMenuList(moduleEntityList));
    }

    /**
     * 根据项目id获取模块信息
     * @param uid
     * @param map
     * @return
     */
    @RequestMapping("getmodule")
    public Result getModuleByProjectId(@RequestHeader String uid, @RequestBody Map<String, String> map) {
        if ( StringUtils.isEmpty(uid)) {
            ResultUtils.warn(ResultCode.PARAMETER_NULL, "UID不能为空");
        }
        List<ModuleEntity> moduleEntityList = projectService.getModuleByPid(uid, map.get("id"));
        log.info("moduleEntityList:{}", FastJsonUtil.parseToJSON(moduleEntityList));

        return ResultUtils.success(new ModuleTree().getMenuListNoProject(moduleEntityList));
    }

    /**
     * 获取项目所有版本
     * @param map
     * @return
     */
    @RequestMapping("getprojectversions")
    public Result getProjectVersions(@RequestBody Map<String,String> map) {
        String uid = map.get("uid");
        String projectId = map.get("projectId");
        if ( StringUtils.isEmpty(uid) || StringUtils.isEmpty(projectId) ) {
            ResultUtils.warn(ResultCode.PARAMETER_NULL, "UID、projectId不能为空");
        }
        return ResultUtils.success(projectService.getVersionsByProjectId(uid, projectId));
    }

    @RequestMapping("addproject")
    public Result addProject(@RequestBody ProjectModel projectModel) {
        boolean b = projectService.addProject(projectModel);
        return b==true ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.EDIT_FAIL);
    }

    @RequestMapping("addversion")
    public Result addProjectVersion(@RequestBody ProjectModel projectModel) {
        String res = projectService.addVersion(projectModel);
        return res.equals("SUCCESS") ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.ADD_FAIL, res);
    }

    @RequestMapping("edit")
    public Result editProject(@RequestBody ProjectModel projectModel) {
        if (StringUtils.isEmpty(projectModel.getUid()) || StringUtils.isEmpty(projectModel.getType()) || StringUtils.isEmpty(projectModel.getVersion()) || StringUtils.isEmpty(projectModel.getId())) {
            return ResultUtils.warn(ResultCode.PARAMETER_NULL);
        }
        boolean b = projectService.editProjectById(projectModel);
        return b==true ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.EDIT_FAIL);
    }


    @RequestMapping("deleteversion")
    public Result deleteProjectVersion(@RequestBody ProjectModel projectModel) {
        if (StringUtils.isEmpty(projectModel.getUid()) || projectModel.getId() < 0) {
            return ResultUtils.warn(ResultCode.PARAMETER_NULL);
        }
        boolean b = projectService.deleteVersionById(projectModel);

        return b ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.EDIT_FAIL);
    }

    @RequestMapping("deleteall")
    public Result deleteProjects(@RequestBody ProjectModel[] projectModels) {
        for (int i=0; i<projectModels.length;++i) {
            if (projectModels[i].getUid().isEmpty() || projectModels[i].getId() < 0) {
                return ResultUtils.warn(ResultCode.PARAMETER_NULL);
            }
        }
//        boolean b = projectService.deleteProjectById(projectModels);

        return projectService.deleteProjectById(projectModels) ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.EDIT_FAIL);
    }

}
