package com.main.manage.modules.controller;

import com.main.manage.RestTemplate.Result;
import com.main.manage.modules.entity.ProjectHostEntity;
import com.main.manage.modules.model.*;
import com.main.manage.modules.service.TestCaseService;
import com.main.manage.modules.service.TestExecService;
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
@RequestMapping("/testcase")
public class TestCaseController extends BaseController {

    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private TestExecService testExecService;


    @RequestMapping("getlist")
    public Result getTestCase(@RequestHeader String uid, @RequestBody TestCaseSeachModel testCaseSeachModel) {
        log.info("getTestCase: reuestheader uid: {}, requestbody: {}", uid, FastJsonUtil.parseToJSON(testCaseSeachModel));

        if (StringUtils.isEmpty(uid)) {
            return ResultUtils.warn(ResultCode.UID_NULL);
        }
        if (StringUtils.isEmpty(testCaseSeachModel.getProjectId())) {
            return ResultUtils.warn(ResultCode.PARAMETER_NULL, "请选择项目");
        }

        return ResultUtils.success(testCaseService.getTestCaseListByPid(uid, testCaseSeachModel.getProjectId(), testCaseSeachModel.getModuleId()));
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
    public Result saveProjectHost(@RequestHeader String uid, @RequestBody Map<String,String> requestMap) {
        log.info("savehost:" + requestMap);
        try {
            String hostId = requestMap.get("id");
            String hostIp = requestMap.get("ip");
            String projectId = requestMap.get("projectId");
            String version = requestMap.get("version");
            String domain = requestMap.get("domain");

            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(projectId)|| StringUtils.isEmpty(version)) {
                log.error("savehost UID、项目或版本号为空" + requestMap);
                return ResultUtils.warn(ResultCode.PARAMETER_NULL,"UID、项目、版本号为空");
            }
            boolean isOk = testCaseService.saveHostIp(uid, hostId, projectId, version, hostIp, domain);

            return isOk ? ResultUtils.success(ResultCode.SUCCESS):ResultUtils.warn(ResultCode.FAIL, "项目版本号不存在或项目所属错误");
        }catch (Exception e) {
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }
    }


    /**
     * 用例新增、修改接口
     * @param uid
     * @param testCaseModel
     * @return
     */
    @RequestMapping("savecase")
    public Result saveTestCase(@RequestHeader String uid, @RequestBody TestCaseModel testCaseModel) {
        log.info("saveTestCase: {}", FastJsonUtil.parseToJSON(testCaseModel));

        if (StringUtils.isEmpty(testCaseModel.getProjectId())) {
            return ResultUtils.warn(ResultCode.PARAMETER_NULL);
        }
        boolean isOK = testCaseService.saveTestCase(testCaseModel.getCaseId(), testCaseModel.getProjectId(), testCaseModel.getModuleId(), testCaseModel.getCaseName(), testCaseModel.getCaseDesc(), testCaseModel.getStatus());
        return isOK ? ResultUtils.success("保存成功"):ResultUtils.warn(ResultCode.SAVE_FAIL);
    }

    /**
     * 获取测试用例步骤
     * @param uid
     * @param testCaseModel
     * @return
     */
    @RequestMapping("getsteps")
    public Result getTestCaseSteps(@RequestHeader String uid, @RequestBody TestCaseModel testCaseModel) {
        log.info("getTestCaseSteps: {}", FastJsonUtil.parseToJSON(testCaseModel));

        if (StringUtils.isEmpty(testCaseModel.getCaseId()) || testCaseModel.getCaseId() < 1) {
            return ResultUtils.warn(ResultCode.PARAMETER_NULL, "TestCaseId为空");
        }
        List list = testCaseService.getTestCaseStepsByCaseId(uid, testCaseModel.getCaseId());
        log.info("CaseSteps: {}", FastJsonUtil.parseToJSON(list));

        return ResultUtils.success(list);
    }

    /**
     * 保存测试用例步骤
     * @param uid
     * @param testCaseStepSaveModel
     * @return
     */
    @RequestMapping("savesteps")
    public Result saveTestCaseSteps(@RequestHeader String uid, @RequestBody TestCaseStepSaveModel testCaseStepSaveModel) {
        log.info("saveTestCaseSteps: {}", FastJsonUtil.parseToJSON(testCaseStepSaveModel));

        if (StringUtils.isEmpty(testCaseStepSaveModel.getCaseId()) || testCaseStepSaveModel.getCaseId() < 1) {
            return ResultUtils.warn(ResultCode.PARAMETER_NULL, "TestCaseId为空");
        }
        boolean isOK = testCaseService.saveTestCaseStepsByCaseId(uid, testCaseStepSaveModel.getCaseId(), testCaseStepSaveModel.getTestCaseSteps());

        return ResultUtils.success(isOK);
    }

    /**
     * 获取步骤 测试数据
     * @param uid
     * @param testCaseStepDataModel
     * @return
     */
    @RequestMapping("getstepdata")
    public Result getCaseStepData(@RequestHeader String uid, @RequestBody TestCaseStepDataModel testCaseStepDataModel) {
        log.info("getCaseStepData: {}", FastJsonUtil.parseToJSON(testCaseStepDataModel));

        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(testCaseStepDataModel.getApiId())) {
            return ResultUtils.warn(ResultCode.PARAMETER_NULL, "UID及ApiId不能为空");
        }

        return ResultUtils.success(testCaseService.getCaseStepDataByRelationId(testCaseStepDataModel.getApiId(), testCaseStepDataModel.getRelationId()));
    }

}
