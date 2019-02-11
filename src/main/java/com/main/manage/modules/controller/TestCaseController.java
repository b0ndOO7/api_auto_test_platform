package com.main.manage.modules.controller;

import com.main.manage.RestTemplate.Result;
import com.main.manage.modules.entity.ProjectHostEntity;
import com.main.manage.modules.model.ApiInfoModel;
import com.main.manage.modules.model.UserModel;
import com.main.manage.modules.service.TestCaseService;
import com.main.manage.modules.service.TestExecService;
import com.main.manage.utils.FastJsonUtil;
import com.main.manage.utils.HttpResp;
import com.main.manage.utils.ResultCode;
import com.main.manage.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.ibatis.annotations.Case;
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
    @Autowired
    private TestExecService testExecService;

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
        try {
            String uid = requestMap.get("uid");
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

        try {
            String uid = requestMap.get("uid");
            String apiId = requestMap.get("id");
            String projectId = requestMap.get("projectId");
            String moduleId = requestMap.get("moduleId");
            String apiName = requestMap.get("apiName");
            String apiProtoType = requestMap.get("apiProtoType");
            String apiMethodType = requestMap.get("apiMethodType");
            String domain = requestMap.get("domain");
            String uri = requestMap.get("uri");

            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(projectId) || StringUtils.isEmpty(apiName) || StringUtils.isEmpty(apiMethodType) || StringUtils.isEmpty(uri)) {
                log.error("savetestapi: 参数为空" + requestMap);
                return ResultUtils.warn(ResultCode.UID_NULL,"UID、项目、接口名、请求方法类型、请求地址不能为空");
            }
            boolean isOK = testCaseService.saveTestApi(uid, apiId, projectId, moduleId, apiName, apiProtoType, apiMethodType, domain, uri);

            return ResultUtils.success(isOK);
        }catch (Exception e) {
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR,"参数错误");
        }
//        HashMap<String, Object> returnMap = testCaseService.getApiListByUid(uid, keyWord, modulesId, curPage, pageSize);
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
        log.info("changeapistatus: {} ", requestMap);

        try {
            String uid = String.valueOf(requestMap.get("uid"));
            String apiId = String.valueOf(requestMap.get("apiId"));
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

    //
    /**
     * 更新接口状态
     * @param requestMap
     * @return
     */
    @RequestMapping("getapiinfo")
    public Result getTestApiInfo(@RequestBody Map<String, Object> requestMap) {
        log.info("getTestApiInfo: {} ", requestMap);

        try {
            String uid = String.valueOf(requestMap.get("uid"));
            String apiId = String.valueOf(requestMap.get("apiId"));

            if (StringUtils.isEmpty(apiId) || StringUtils.isEmpty(uid)) {
                log.warn("接口ID为空: apiId:{}, uid:{}", apiId, uid);
                return ResultUtils.warn(ResultCode.PARAMETER_NULL, "接口ID为空");
            }
            List<Integer> list = new ArrayList<>();
            list.add(Integer.valueOf(apiId));

            Map<String, Object>  map = testCaseService.getTestApiInfoById(uid, apiId);

            return ResultUtils.success(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }
    }

    //
    @RequestMapping("saveapiinfo")
    public Result saveTestApiInfo(@RequestBody Map<String, Object> requestMap) {
        log.info("saveTestApiInfo: {} ", requestMap);
        try {
            String uid = String.valueOf(requestMap.get("uid"));
            String apiId = String.valueOf(requestMap.get("apiid"));
            String projectId = String.valueOf(requestMap.get("project_id"));
            String protocolType = String.valueOf(requestMap.get("protocol_type"));
            String methodtype = String.valueOf(requestMap.get("methodtype"));
            List<Map> headerList = (List) requestMap.get("headers");
            String bodyType = String.valueOf(requestMap.get("bodytype"));
            List<Map> formList = (List) requestMap.get("form");
            String jsonStr = String.valueOf(requestMap.get("jsonstr"));

            List<Map> toSaveHeaderList = new ArrayList<>();
            List<Map> toSaveFormList = new ArrayList<>();
            for (int i = 0, length=headerList.size(); i < length; i++) {
                Map<String, String> tmpMap = headerList.get(i);
                if ( StringUtils.isEmpty(tmpMap.get("request_key")) ) {
                    continue;
                } else {
                    if (StringUtils.isEmpty(tmpMap.get("request_value"))) {
                        return ResultUtils.warn(ResultCode.PARAMETER_ERROR, "请输入正确的请求头");
                    }
                    toSaveHeaderList.add(tmpMap);
                }
            }
            for (int i = 0, length=formList.size(); i < length; i++) {
                Map<String, String> tmpMap = formList.get(i);
                if ( StringUtils.isEmpty(tmpMap.get("request_key"))) {
                    continue;
                } else {
                    if (StringUtils.isEmpty(tmpMap.get("request_value"))){
                        return ResultUtils.warn(ResultCode.PARAMETER_ERROR, "请输入正确的表单项");
                    }
                    toSaveFormList.add(tmpMap);
                }
            }

            boolean updateTestApi = testCaseService.saveTestApi(uid, apiId, projectId, "", "", protocolType, methodtype, "", "");
            boolean updateApiInfo = testCaseService.saveApiInfo(apiId, toSaveHeaderList, bodyType, toSaveFormList, jsonStr);
            log.info("updateTestApi: {}" ,updateTestApi);
            log.info("updateApiInfo: {}" ,updateApiInfo);
            return updateTestApi && updateApiInfo ? ResultUtils.success(ResultCode.SUCCESS) : ResultUtils.warn(ResultCode.FAIL);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }
    }


    @RequestMapping("debugtestapi")
    public Result debugTestApi(@RequestBody ApiInfoModel apiInfoModel) {
        log.info("debugTestApi: {} ", FastJsonUtil.parseToJSON(apiInfoModel));

        String apiId = String.valueOf(apiInfoModel.getApiid());
        String projectId = apiInfoModel.getProject_id();
        String protoType = apiInfoModel.getProtocol_type();
        String url = apiInfoModel.getApiurl();
        if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")) {
            // nothing
        } else {
            url = protoType.toLowerCase() + "://" + apiInfoModel.getApiurl();
        }
        String methodType = apiInfoModel.getMethodtype();

        List<Map> requestHeader = apiInfoModel.getHeaders();
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 0, length=requestHeader.size(); i < length; i++) {
            Map<String, String> tmpMap = requestHeader.get(i);
            if ( StringUtils.isEmpty(tmpMap.get("request_key")) ) {
                continue;
            } else {
                if (StringUtils.isEmpty(tmpMap.get("request_value"))) {
                    return ResultUtils.warn(ResultCode.PARAMETER_ERROR, "请输入正确的请求头");
                }
                headerMap.put(tmpMap.get("request_key"), tmpMap.get("request_value"));
            }
        }

        String formStr = "";
        List<Map> requestForm = apiInfoModel.getForm();
        for (int i = 0, length=requestForm.size(); i < length; i++) {
            Map<String, String> tmpMap = requestForm.get(i);
            if ( StringUtils.isEmpty(tmpMap.get("request_key"))) {
                continue;
            } else {
                if (StringUtils.isEmpty(tmpMap.get("request_value"))){
                    return ResultUtils.warn(ResultCode.PARAMETER_ERROR, "请输入正确的表单项");
                }
                formStr += "&" + tmpMap.get("request_key") + "=" + tmpMap.get("request_value");
            }
        }

        HttpResp httpResp = new HttpResp();
        switch (methodType) {
            case "GET":
                httpResp = testExecService.doGetRequest(url, headerMap, formStr);
                break;
            case "POST":
                if (apiInfoModel.getBodytype().equals("json")) {
                    String jsonStr = apiInfoModel.getJsonstr();
                    httpResp = testExecService.doPostJsonRequest( url, headerMap, jsonStr);
                }
                if (apiInfoModel.getBodytype().equals("form")) {
                    httpResp = testExecService.doPostFormRequest(url, headerMap, formStr);
                }
                break;
            case "PUT":
                // to do
                break;
            default:
                break;

        }

        return ResultUtils.success(httpResp);
    }


}
