package com.main.manage.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public class HttpClientUtil {

    private static int TIMEOUT_TIME = 10000;

    /**
     * Get请求不带head
     * @param url
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static HttpResp doGetRequest(String url) throws URISyntaxException {
        log.info("doGetRequest: {}", url);
        HttpClient httpClient = wrapClient(url);

        //创建一个uri对象
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_TIME).setConnectionRequestTimeout(TIMEOUT_TIME)
                .setSocketTimeout(TIMEOUT_TIME).build();
        httpGet.setConfig(requestConfig);
        //执行请求
        int statusCode = -1;
        String respStr = "";
        try {
            HttpResponse response =httpClient.execute(httpGet);
            //取响应的结果
            statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity =response.getEntity();
            respStr = EntityUtils.toString(entity,"utf-8");
        } catch (Exception e) {
            return new HttpResp(statusCode, e.toString());
        }
        return new HttpResp(statusCode, respStr);
    }

    /**
     * Get请求带head
     * @param url
     * @param header
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static HttpResp doGetRequest(String url, Map<String, String> header) throws URISyntaxException {
        log.info("doGetRequest: url:{}, header:{}", url, header);
        HttpClient httpClient = wrapClient(url);
        //创建一个uri对象
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_TIME).setConnectionRequestTimeout(TIMEOUT_TIME)
                .setSocketTimeout(TIMEOUT_TIME).build();
        httpGet.setConfig(requestConfig);

        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpGet.addHeader(entry.getKey(),entry.getValue());
        }
        int statusCode = -1;
        String respStr = "";
        try {
            //执行请求
            HttpResponse response = httpClient.execute(httpGet);
            //取响应的结果
            statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity =response.getEntity();
            respStr = EntityUtils.toString(entity,"utf-8");
            return new HttpResp(statusCode, respStr);
        } catch (Exception e) {
            return new HttpResp(statusCode, e.toString());
        }
    }

    /**
     * Post请求不带head
     * @param url
     * @param requestData
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static HttpResp doPostRequst(String url, String requestData) {
        log.info("doPostRequst: url:{}, requestData:{}", url, requestData);
        HttpClient httpClient= wrapClient(url);
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_TIME).setConnectionRequestTimeout(TIMEOUT_TIME)
                .setSocketTimeout(TIMEOUT_TIME).build();
        httpPost.setConfig(requestConfig);

        //包装成一个Entity对象
        StringEntity entity = new StringEntity(requestData,"utf-8");
        //设置请求的内容
        httpPost.setEntity(entity);

        //执行post请求
        int statusCode = 0;
        String respStr = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            respStr = EntityUtils.toString(response.getEntity(),"utf-8");
            return new HttpResp(statusCode, respStr);
        } catch (Exception e) {
            return new HttpResp(statusCode, e.toString());
        }
    }

    /**
     * Post请求带head
     * @param url
     * @param requestData
     * @param header
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static HttpResp doPostRequst(String url, String requestData, Map<String, String> header){
        log.info("doPostRequst: url:{}, requestData:{}", url, requestData);

        HttpClient httpClient= wrapClient(url);
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_TIME).setConnectionRequestTimeout(TIMEOUT_TIME)
                .setSocketTimeout(TIMEOUT_TIME).build();
        httpPost.setConfig(requestConfig);
        //Userid: 7f0pRahZRLc 添加消息头
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpPost.addHeader(entry.getKey(),entry.getValue());
        }

        //包装成一个Entity对象
        StringEntity entity = new StringEntity(requestData,"utf-8");
        httpPost.setEntity(entity);

        //执行post请求
        int statusCode = 0;
        String respStr = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            respStr = EntityUtils.toString(response.getEntity(),"utf-8");
        } catch (Exception e) {
            return new HttpResp(statusCode, e.toString());
        }
        return new HttpResp(statusCode, respStr);
    }

    /**
     * 获取 HttpClient
     * @param url
     * @return
     */
    private static HttpClient wrapClient(String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (url != null && url.startsWith("https://")) {
            return sslClient();
        }
        return httpClient;
    }

    /**
     *
     * @return HttpClient 支持https
     */
    private static HttpClient sslClient() {
        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                }
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[]{trustManager}, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            // 创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig).build();
            return closeableHttpClient;
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
