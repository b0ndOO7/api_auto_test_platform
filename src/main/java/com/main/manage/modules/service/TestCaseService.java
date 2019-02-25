package com.main.manage.modules.service;

import com.main.manage.modules.dao.TestCaseDao;
import com.main.manage.modules.entity.*;
import com.main.manage.utils.FastJsonUtil;
import com.main.manage.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TestCaseService {

    @Autowired
    private TestCaseDao testCaseDao;

    /**
     * 根据项目id获取测试用例
     * @param uid
     * @param projectId
     * @return
     */
    public List getTestCaseListByPid(String uid, String projectId, String moduleId) {
        return testCaseDao.findTestCaseByPId(uid, projectId, moduleId);
    }

    /**
     * 获取项目host
     * @param uid
     * @return
     */
    public List<ProjectHostEntity> getProjectHostsByUid(String uid) {

        Map<String, Object> returnMap = new HashMap<>();

        return testCaseDao.getProjectHostListByUid(uid);
    }

    /**
     *  保存项目配置的host
     * @param uid
     * @param hostId
     * @param projectId
     * @param version
     * @param hostIp
     * @return
     */
    public boolean saveHostIp(String uid, String hostId, String projectId, String version, String hostIp, String domain) {

        boolean isSucc = false;
        if (StringUtils.isEmpty(hostId)) {
            if (uid.equals(testCaseDao.checkProjectOwner(projectId))) {
                isSucc = testCaseDao.insertHostIp(uid, projectId, version, hostIp, domain);
            }else {
                log.error("项目所属错误:uid:{},projectId:{}", uid, projectId);
                isSucc = false;
            }
        } else {
            if (version.equals(testCaseDao.isExstsVersion(uid, projectId, version))) {
                isSucc = testCaseDao.updateHostIp(uid, hostId, projectId, hostIp, domain);
            } else {
                log.error("版本不匹配: uid:{}, projectId:{}, version:{}", uid, projectId, version);
                isSucc = false;
            }
        }
        return isSucc;
    }


    /**
     * 获取用户接口
     * @param uid
     * @return
     */
    public HashMap<String, Object> getApiList(String uid, String key_word, int curPage, int pageSize, List<String> searchScope) {

        List<String> moduleList = new ArrayList<>();
        log.info("searchScope:{}" ,searchScope);
        for (String str : searchScope) {
            if (! StringUtils.isEmpty(str)) {
                moduleList.add(str);
            }
        }
        log.info("moduleList:{}" ,moduleList);
        curPage = curPage<1 ? 1:curPage;
        pageSize = pageSize<1 ? 1:pageSize;
        int startRow = (curPage - 1) * pageSize;

        List<TestApiEntity> testApiEntityList = testCaseDao.getAllApiByUid(uid, key_word, startRow, pageSize, moduleList);
        int total = testCaseDao.getTotalApiByUid(uid, key_word, moduleList);

        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("apis", testApiEntityList);
        returnMap.put("totalRow", total);

        return returnMap;
    }

    /**
     * 获取用户接口
     * @param uid
     * @return
     */
    public List getApiListByPid(String uid, int projectId) {

        List<TestApiEntity> testApiEntityList = testCaseDao.getAllApiByPid(uid, projectId);

        return testApiEntityList;
    }

    /**
     * 保存接口
     * @param uid
     * @param apiId
     * @param projectId
     * @param modulesId
     * @param apiName
     * @param apiProtoType
     * @param domain
     * @param uri
     * @return
     */
    public boolean saveTestApi(String uid, String apiId, String projectId, String modulesId, String apiName, String apiProtoType, String apiMethodType, String domain, String uri) {
        log.info("saveTestApi: uid:{}, apiId:{}, projectId:{}", uid, apiId, projectId);
        boolean isSucc = false;
        if (StringUtils.isEmpty(apiId)) {
            isSucc = testCaseDao.insertTestApi(projectId, modulesId, apiName, apiProtoType, apiMethodType, domain, uri);
            log.info("saveTestApi: insertTestApi.isSucc:{}", isSucc);
        } else {
            if (apiId.equals(testCaseDao.isExstsTestApi(uid, projectId, apiId))) {
                isSucc = testCaseDao.updateTestApi(apiId, projectId, modulesId, apiName, apiProtoType, apiMethodType, domain, uri);
                log.info("saveTestApi: updateTestApi.isSucc:{}", isSucc);
            } else {
                isSucc = false;
            }
        }
        return isSucc;
    }

    /**
     * 按apiId更新接口状态
     * @param uid 用户ID
     * @param ids apiID
     * @param value  状态值
     * @return
     */
    public boolean updateTestApiStatusById(String uid, List<Integer> ids, String value) {
        return testCaseDao.updateTestApiStatusByIds(uid, ids, Integer.valueOf(value));
    }

    /**
     * 获取api参数数据
     * @param uid
     * @param apiId
     * @return
     */
    public Map getTestApiInfoById(String uid, String apiId) {
        Map<String,Object> returnMap = new HashMap<>();

        TestApiEntity testApiEntity = testCaseDao.getApiByUid(uid,apiId);
        log.info("testApiEntity:" + testApiEntity);
        try {
            returnMap = ObjectUtil.objectToMap(testApiEntity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (testApiEntity == null) {
            return null;
        }
        List<ApiInfoEntity> apiInfoEntityList = testCaseDao.getApiInfoByAid(apiId);
        List<ApiInfoEntity> headerApiInfoList = new ArrayList<>();
        List<ApiInfoEntity> paramApiInfoList = new ArrayList<>();

        for (int i = 0; i < apiInfoEntityList.size(); i++) {
            ApiInfoEntity apiInfoEntity = apiInfoEntityList.get(i);
            String paramType = apiInfoEntity.getParam_type();

            switch (paramType){
                case "header":
                    headerApiInfoList.add(apiInfoEntity);
                    break;
                case "form":
                    paramApiInfoList.add(apiInfoEntity);
                    returnMap.put("bodytype", "form");
                    break;
                case "json":
                    returnMap.put("jsonstr", apiInfoEntity.getRequest_value());
                    returnMap.put("bodytype", "json");
                    break;
                default:
                    break;
            }
        }
        returnMap.put("headers", headerApiInfoList);
        returnMap.put("form", paramApiInfoList);

        log.info("TestApi:" + returnMap);
        return returnMap;
    }

    /**
     *  保存api接口信息
     * @param apiId
     * @param headerList
     * @param bodyType
     * @param formList
     * @param jsonStr
     * @return
     */
    public boolean saveApiInfo(String apiId, List<Map> headerList, String bodyType, List<Map> formList, String jsonStr) {
        log.info("saveApiInfo: apiId:{}, headers:{}, bodyType:{}, form:{}, jsonstr:{}",apiId, headerList, bodyType, formList, jsonStr);
        //删除全部 api接口 信息
        boolean isDelete = testCaseDao.deleteApiInfoByAid(apiId);

        List<ApiInfoEntity> apiInfoEntityList = new ArrayList<>();
        for (int i = 0, length = headerList.size(); i < length; i++) {
            ApiInfoEntity apiInfoEntity = new ApiInfoEntity();
            apiInfoEntity.setApi_id(Integer.valueOf(apiId));
            apiInfoEntity.setParam_type("header");
            apiInfoEntity.setRequest_key((String) headerList.get(i).get("request_key"));
            apiInfoEntity.setRequest_value((String) headerList.get(i).get("request_value"));

            String isCorrelation = (String) headerList.get(i).get("iscorrelation");
            apiInfoEntity.setIs_correlation(StringUtils.isEmpty(isCorrelation) ? "1" : isCorrelation);

            apiInfoEntityList.add(apiInfoEntity);
        }

        if (bodyType.equals("form")) {
            for (int i = 0, length = formList.size(); i < length; i++) {
                ApiInfoEntity apiInfoEntity = new ApiInfoEntity();
                apiInfoEntity.setApi_id(Integer.valueOf(apiId));
                apiInfoEntity.setParam_type(bodyType);
                apiInfoEntity.setRequest_key((String) headerList.get(i).get("request_key"));
                apiInfoEntity.setRequest_value((String) headerList.get(i).get("request_value"));

                String isCorrelation = (String) headerList.get(i).get("iscorrelation");
                apiInfoEntity.setIs_correlation(StringUtils.isEmpty(isCorrelation) ? "1" : isCorrelation);

                apiInfoEntityList.add(apiInfoEntity);
            }
        }
        if (bodyType.equals("json")) {
            ApiInfoEntity apiInfoEntity = new ApiInfoEntity();
            apiInfoEntity.setApi_id(Integer.valueOf(apiId));
            apiInfoEntity.setParam_type(bodyType);
            apiInfoEntity.setRequest_key("");
            apiInfoEntity.setRequest_value(jsonStr);

            apiInfoEntityList.add(apiInfoEntity);
        }
        log.info("begin to insert apiInfo: {}", apiInfoEntityList);
        boolean isInsert = testCaseDao.insertApiInfoBatch(apiInfoEntityList);

        return isDelete && isInsert;
    }


    /**
     * 保存测试用例
     * @param caseId
     * @param projectId
     * @param moduleId
     * @param caseName
     * @param caseDesc
     * @param status
     * @return
     */
    public boolean saveTestCase(int caseId, int projectId, String moduleId, String caseName, String caseDesc, String status){
        if (StringUtils.isEmpty(caseId) || caseId == 0) {
            return testCaseDao.insertTestCase(projectId, moduleId, caseName, caseDesc);
        } else {
            return testCaseDao.updateTestCase(caseId, projectId, moduleId, caseName, caseDesc, status);
        }
    }

    /**
     * 查询用例步骤
     * @param uid
     * @param caseId
     * @return
     */
    public List getTestCaseStepsByCaseId(String uid, int caseId) {

        return testCaseDao.getTestCaseStep(caseId);
    }

    /**
     *  保存测试步骤
     * @param uid
     * @param caseId
     * @param testCaseStepEntities
     * @return
     */
    public boolean saveTestCaseStepsByCaseId(String uid, int caseId, TestCaseStepEntity[] testCaseStepEntities) {

        boolean isOK = true;
//        for (int i = 0, length = testCaseStepEntities.length; i < length; i++) {
//            int apiId = testCaseStepEntities[i].getApi_id();
//            // 查询是否存在测试步骤，若存在，则更新之；若不存在，插入操作；
//            List<TestCaseStepEntity> testCaseSteps = testCaseDao.getTestCaseStep(caseId, apiId);
//            if (testCaseSteps.size() > 0) {
//                isOK = testCaseDao.updateTestCaseStep(testCaseSteps.get(0).getRelationId(), i, "0");
//            } else {
//                isOK =testCaseDao.insertTestCaseStep(caseId, apiId, i);
//            }
//        }

        //筛选出db中需要删除的步骤，并删除
        List<TestCaseStepEntity> testCaseStepEntitylist = testCaseDao.getTestCaseStep(caseId);
        for (int i = 0, size = testCaseStepEntitylist.size(); i < size; i++) {
            TestCaseStepEntity dbTestCaseStep = testCaseStepEntitylist.get(i);
            boolean isNeedDel = true;
            for (int j = 0, length = testCaseStepEntities.length; j < length; j++) {
                TestCaseStepEntity requestTestCaseStep = testCaseStepEntities[j];
                //判断是否需要删除测试步骤
                if (requestTestCaseStep.getApi_id() == dbTestCaseStep.getApi_id() && requestTestCaseStep.getCase_id() == dbTestCaseStep.getCase_id()) {
                    isNeedDel = false;
                    // 判断 排序是否需要更新
                    if (dbTestCaseStep.getIndex() != requestTestCaseStep.getIndex()) {
                        if (! testCaseDao.updateTestCaseStep(dbTestCaseStep.getRelationId(), requestTestCaseStep.getIndex(), "0") ) {
                            return false;
                        }
                    }
                }
            }
            if (isNeedDel) {
                if (! testCaseDao.deleteTestCaseStepById(dbTestCaseStep.getRelationId()))
                    return false;
            }
        }

        // 筛选出需要插入的步骤，并做插入操作
        testCaseStepEntitylist = testCaseDao.getTestCaseStep(caseId);
        for (int i = 0, length = testCaseStepEntities.length; i < length; i++) {
            TestCaseStepEntity requestTestCaseStep = testCaseStepEntities[i];
            boolean isExist = false;
            for (int j = 0, size = testCaseStepEntitylist.size(); j < size; j++) {
                TestCaseStepEntity dbTestCaseStep = testCaseStepEntitylist.get(j);
                if ( requestTestCaseStep.getApi_id() == dbTestCaseStep.getApi_id() && requestTestCaseStep.getCase_id() == dbTestCaseStep.getCase_id() && dbTestCaseStep.getIndex() == requestTestCaseStep.getIndex()) {
                    isExist = true;
                }
            }
            if (! isExist) {
                if (! testCaseDao.insertTestCaseStep(requestTestCaseStep.getCase_id(), requestTestCaseStep.getApi_id(), requestTestCaseStep.getIndex()))
                    return false;
            }
        }

        return isOK;
    }


    /**
     * 获取测试数据
     * @param relationId
     * @return
     */
    public Map<String, Object> getCaseStepDataByRelationId(int apiId, int relationId) {

        List<TestCaseStepDataEntity> stepsData = testCaseDao.getTestCaseStepDataByRelationId(relationId);

        Map<String, Object> returnMap = new HashMap<>();
        //若测试步骤数据为空，刚返回api数据模板
        if (stepsData.size() == 0) {
            List<ApiInfoEntity> apiData = testCaseDao.getApiInfoByAid(String.valueOf(apiId));
            List<ApiInfoEntity> headers = new ArrayList<>();
            List<ApiInfoEntity> bodys = new ArrayList<>();

            for (int i = 0, size = apiData.size(); i < size; i++) {
                ApiInfoEntity apiInfoEntity = apiData.get(i);
                String paramType = apiInfoEntity.getParam_type();
                apiInfoEntity.setId(0); //id置为0
                switch (paramType){
                    case "header":
                        headers.add(apiInfoEntity);
                        break;
                    case "form":
                        bodys.add(apiInfoEntity);
                        returnMap.put("bodytype", "form");
                        break;
                    case "json":
                        returnMap.put("jsonstr", apiInfoEntity.getRequest_value());
                        returnMap.put("bodytype", "json");
                        break;
                    default:
                        break;
                }
            }
            returnMap.put("headers", headers);
            returnMap.put("form", bodys);
        } else if (stepsData.size() > 0) {
            List<TestCaseStepDataEntity> headers = new ArrayList<>();
            List<TestCaseStepDataEntity> bodys = new ArrayList<>();
            for (int i = 0, size = stepsData.size(); i < size; i++) {
                TestCaseStepDataEntity caseStepData = stepsData.get(i);
                String paramType = caseStepData.getParam_type();

                switch (paramType){
                    case "header":
                        headers.add(caseStepData);
                        break;
                    case "form":
                        bodys.add(caseStepData);
                        returnMap.put("bodytype", "form");
                        break;
                    case "json":
                        returnMap.put("jsonstr", caseStepData.getRequest_value());
                        returnMap.put("bodytype", "json");
                        break;
                    default:
                        break;
                }
            }
            returnMap.put("headers", headers);
            returnMap.put("form", bodys);
        }

        log.info("getCaseStepDataByRelationId:" + FastJsonUtil.parseToJSON(returnMap));

        return returnMap;
    }

}
