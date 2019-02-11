package com.main.manage.modules.service;

import com.main.manage.modules.dao.TestCaseDao;
import com.main.manage.modules.entity.ApiInfoEntity;
import com.main.manage.modules.entity.ProjectHostEntity;
import com.main.manage.modules.entity.TestApiEntity;
import com.main.manage.modules.entity.UserEntity;
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
     * 获取用户测试用例
     * @param uid
     * @return
     */
    public UserEntity getTestCaseListByUid(String uid) {
        return testCaseDao.findByUid(Integer.valueOf(uid));
    }

    /**
     * 获取项目host
     * @param uid
     * @return
     */
    public List<ProjectHostEntity> getProjectHostsByUid(String uid) {
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
    public HashMap<String, Object> getApiListByUid(String uid, String key_word, int curPage, int pageSize, List<String> searchScope) {

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
}
