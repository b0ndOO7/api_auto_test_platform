package com.main.manage.modules.service;

import com.main.manage.modules.dao.TestCaseDao;
import com.main.manage.modules.entity.ProjectHostEntity;
import com.main.manage.modules.entity.TestApiEntity;
import com.main.manage.modules.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public boolean saveTestApi(String uid, String apiId, String projectId, String modulesId, String apiName, String apiProtoType, String domain, String uri) {

        boolean isSucc = false;
        if (StringUtils.isEmpty(apiId)) {
            isSucc = testCaseDao.insertTestApi(projectId, modulesId, apiName, apiProtoType, domain, uri);
        } else {
            if (apiId.equals(testCaseDao.isExstsTestApi(uid, projectId, apiId))) {
                isSucc = testCaseDao.updateTestApi(apiId, projectId, modulesId, apiName, apiProtoType, domain, uri);
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

}
