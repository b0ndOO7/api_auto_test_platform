package com.main.manage.modules.controller;

import com.main.manage.RestTemplate.Result;
import com.main.manage.modules.entity.ProjectHostEntity;
import com.main.manage.modules.model.UserModel;
import com.main.manage.modules.service.TestCaseService;
import com.main.manage.utils.FastJsonUtil;
import com.main.manage.utils.ResultCode;
import com.main.manage.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/testcase")
public class TestCaseController extends BaseController {

    @Autowired
    private TestCaseService testCaseService;

    @RequestMapping("list")
    public Result getTestCaseByUid(@RequestBody UserModel userModel) {
        log.info("list: " + userModel);
        if (StringUtils.isEmpty(userModel.getUid())) {
            return ResultUtils.warn(ResultCode.UID_NULL);
        }
       testCaseService.getTestCaseListByUid(userModel.getUid());

        return ResultUtils.success("");
    }

    @RequestMapping("hosts")
    public Result getProjectHostListByUid(@RequestBody UserModel userModel) {
        log.info("hosts: " + userModel);
        if (StringUtils.isEmpty(userModel.getUid())) {
            return ResultUtils.warn(ResultCode.UID_NULL);
        }
        List<ProjectHostEntity> projectHosts = testCaseService.getProjectHostsByUid(userModel.getUid());
        log.info("hosts:" + projectHosts);
        return ResultUtils.success(projectHosts);
    }

    @RequestMapping("savehost")
    public Result saveProjectHost(@RequestBody Map<String,String> requestMap) {
        log.info("savehost:" + requestMap);
        String uid = "";
        String hostId = "";
        String hostIp = "";
        String domain = "";
        String projectId = "";
        String version = "";
        try {
            uid = requestMap.get("uid");
            hostId = requestMap.get("id");
            hostIp = requestMap.get("ip");
            projectId = requestMap.get("projectId");
            version = requestMap.get("version");
            domain = requestMap.get("domain");

            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(projectId)|| StringUtils.isEmpty(version)) {
                log.error("savehost UID、项目或版本号为空" + requestMap);
                return ResultUtils.warn(ResultCode.PARAMETER_NULL,"UID、项目、版本号为空");
            }
        }catch (Exception e) {
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }
        boolean isOk = testCaseService.saveHostIp(uid, hostId, projectId, version, hostIp, domain);

        return isOk ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.FAIL, "项目版本号不存在或项目所属错误");
    }

    /**
     * 查询用户接口列表
     * @param requestMap
     * @return
     */
    @RequestMapping("getapilist")
    public Result getApiListByUid(@RequestBody Map<String,Object> requestMap) {
        log.info("getapilist: {}", FastJsonUtil.parseToJSON(requestMap));

//        try {
            String uid = (String) requestMap.get("uid");
            String keyWord = (String) requestMap.get("keyword");
            int curPage = (Integer) requestMap.get("cur_page");
            int pageSize = (Integer) requestMap.get("page_size");
            List<String> searchScope = (List) requestMap.get("seach_module");

            if (StringUtils.isEmpty(uid)) {
                return ResultUtils.warn(ResultCode.UID_NULL,"UID不能为空");
            }

            HashMap<String, Object> returnMap = testCaseService.getApiListByUid(uid, keyWord, curPage, pageSize, searchScope);

            return ResultUtils.success(returnMap);
//        }catch (Exception e) {
//            log.error(e.getMessage(),e.toString());
//            return ResultUtils.warn(ResultCode.PARAMETER_ERROR,"参数错误");
//        }

    }

    /**
     * 编辑、保存接口
     * @param requestMap
     * @return
     */
    @RequestMapping("savetestapi")
    public Result saveTestApiByUid(@RequestBody Map<String,String> requestMap) {
        log.info("savetestapi: " + requestMap);
        String uid = "";
        String apiId = "";
        String projectId = "";
        String moduleId = "";
        String apiName = "";
        String apiProtoType = "";
        String domain = "";
        String uri = "";

        try {
            uid = requestMap.get("uid");
            apiId = requestMap.get("id");
            projectId = requestMap.get("projectId");
            moduleId = requestMap.get("moduleId");
            apiName = requestMap.get("apiName");
            apiProtoType = requestMap.get("apiProtoType");
            domain = requestMap.get("domain");
            uri = requestMap.get("uri");

            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(projectId) || StringUtils.isEmpty(apiName) || StringUtils.isEmpty(apiProtoType) || StringUtils.isEmpty(uri)) {
                log.error("savetestapi: 参数为空" + requestMap);
                return ResultUtils.warn(ResultCode.UID_NULL,"UID、项目、接口名、请求类型、请求地址不能为空");
            }
        }catch (Exception e) {
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR,"参数错误");
        }
//        HashMap<String, Object> returnMap = testCaseService.getApiListByUid(uid, keyWord, modulesId, curPage, pageSize);
        boolean isOK = testCaseService.saveTestApi(uid, apiId, projectId, moduleId, apiName, apiProtoType, domain, uri);

        return ResultUtils.success(isOK);
    }

    /**
     * 根据接口id删除用户接口
     * @param requestMap
     * @return
     */
    @RequestMapping("deletetestapi")
    public Result deleteTestApi(@RequestBody Map<String, Object> requestMap) {
        log.info("deletetestapi: {} ", requestMap);

        try {
            String uid = (String) requestMap.get("uid");
            List<Integer> apiIds = (List) requestMap.get("apiIds");
            if (apiIds.size() < 1) {
                log.warn("接口ID为空: apiIds:{}, uid:{}", apiIds, uid);
                return ResultUtils.warn(ResultCode.PARAMETER_NULL, "接口ID为空");
            }
             boolean isOK = testCaseService.updateTestApiStatusById(uid, apiIds, "-1");

            return isOK ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.FAIL);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultUtils.success(ResultCode.PARAMETER_ERROR);
        }
    }

    /**
     * 更新接口状态
     * @param requestMap
     * @return
     */
    @RequestMapping("changeapistatus")
    public Result changeTestApiStatus(@RequestBody Map<String, Object> requestMap) {
        log.info("changeTestApiStatus: {} ", requestMap);

        try {
            String uid = (String) requestMap.get("uid");
            String apiId = (String) requestMap.get("apiId");
            boolean status = (boolean) requestMap.get("status");

            if (StringUtils.isEmpty(apiId) ) {
                log.warn("接口ID为空: apiId:{}, uid:{}", apiId, uid);
                return ResultUtils.warn(ResultCode.PARAMETER_NULL, "接口ID为空");
            }
            List<Integer> list = new ArrayList<>();
            list.add(Integer.valueOf(apiId));

            boolean isOK = testCaseService.updateTestApiStatusById(uid, list, status ? "0":"1");

            return isOK ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.FAIL);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }
    }

}
