package com.main.manage.modules.service;

import com.main.manage.modules.dao.TestCaseDao;
import com.main.manage.utils.FastJsonUtil;
import com.main.manage.utils.HttpClientUtil;
import com.main.manage.utils.HttpResp;
import com.main.manage.utils.HttpRespCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TestExecService {

    @Autowired
    private TestCaseDao testCaseDao;

    public HttpResp doGetRequest(String url, Map<String, String> headerMap, String formStr) {
        log.info("doGetRequest: url:{}, headers:{}, form:{}", url, headerMap, formStr);

        try {
            HttpResp httpResp = HttpClientUtil.doGetRequest(url + "?" + formStr, headerMap);
            log.info("doGetRequest response: {}", FastJsonUtil.parseUnicodeJSON(httpResp));
            return httpResp;
        } catch (Exception e) {
            log.error("doGetRequest error: {}", e.getMessage());
            return new HttpResp(HttpRespCode.NOT_ALLOW, e.toString());
        }
    }

    public HttpResp doPostFormRequest(String url, Map<String, String> headerMap, String formStr) {
        log.info("doPostFormRequest: url:{}, headers:{}, form:{}", url, headerMap, formStr);

        try {
            HttpResp httpResp = HttpClientUtil.doPostRequst(url, formStr, headerMap);
            log.info("doPostFormRequest response: {}", FastJsonUtil.parseUnicodeJSON(httpResp));
            return httpResp;
        } catch (Exception e) {
            log.error("doPostFormRequest error: {}", e.getMessage());
            return new HttpResp(HttpRespCode.NOT_ALLOW, e.toString());
        }
    }
    public HttpResp doPostJsonRequest(String url, Map<String, String> headerMap, String jsonStr) {
        log.info("doPostJsonRequest: url:{}, headers:{}, json:{}", url, headerMap, jsonStr);

        try {
            HttpResp httpResp = HttpClientUtil.doPostRequst(url, jsonStr, headerMap);
            log.info("doPostJsonRequest response: {}", FastJsonUtil.parseUnicodeJSON(httpResp));
            return httpResp;
        } catch (Exception e) {
            log.error("doPostJsonRequest error: {}", e.getMessage());
            return new HttpResp(HttpRespCode.NOT_ALLOW, e.toString());
        }
    }

}
