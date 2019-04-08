package com.tiefan.cps.base.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpClientTemplate {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);

    private HttpClient httpClient;

    private static final int TIMEOUT = 15000;

    private static final String DEFAULT_CHARSET = "utf-8";

    public HttpClientTemplate() {
        logger.info("--------------------------初始化HttpClientTemplate-----------------------");
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setMaxTotal(200);
        httpClientConnectionManager.setDefaultMaxPerRoute(20);
        this.httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionManager).build();
    }

    /**
     * 执行POST请求并将响应实体以字符串返回
     *
     * @param url          url
     * @param stringEntity 请求内容体，默认为json格式
     * @throws IOException
     */
    public String executePost(String url, String stringEntity)
            throws IOException {
        HttpPost postRequest = makePostRequest(url, stringEntity);
        String result = requestAndParse(postRequest);
        logger.debug("HttpClientTemplate response from url={}, params={}, response={}", url, stringEntity, result);
        return result;
    }

    /**
     * 执行GET请求并将响应实体以字符串返回
     *
     * @param url url
     * @throws IOException
     */
    public String executeGET(String url)
            throws IOException {
        HttpGet getRequest = getHttpGet(url);

        String result = requestAndParse(getRequest);
        logger.debug("HttpClientTemplate response from url={} data={}", url, result);
        return result;
    }


    /**
     * 根据给定的url、参数和编码方式构建一个POST请求
     *
     * @param url          url
     * @param stringEntity 请求内容体，默认为json格式
     * @return
     */
    private HttpPost makePostRequest(String url, String stringEntity) throws IOException {
        HttpPost post = getHttpPost(url);
        if (stringEntity != null) {
            StringEntity se = new StringEntity(stringEntity, DEFAULT_CHARSET);
            post.setEntity(se);
            logger.debug("HttpClientTemplate making post request url={}, entity={}", url, stringEntity);
        }
        return post;
    }

    /**
     * 生成POST请求，使用配置的参数
     *
     * @param url
     * @return
     */
    private HttpPost getHttpPost(String url) {
        HttpPost postMethod = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).setRedirectsEnabled(false).build();
        postMethod.setConfig(requestConfig);
        postMethod.addHeader("Content-Type", "application/json; charset=utf-8");
        return postMethod;
    }


    private HttpGet getHttpGet(String url) {
        HttpGet getMethod = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).setRedirectsEnabled(false).build();
        getMethod.setConfig(requestConfig);
        return getMethod;
    }

    /**
     * 执行请求并获取响应
     *
     * @param httpRequest HTTP请求
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private String requestAndParse(HttpUriRequest httpRequest)
            throws IOException {
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        return getResponseContentStr(httpResponse);
    }

    /**
     * 使用指定编码将响应实体转为字符串
     *
     * @param httpResponse 响应
     * @throws IOException
     */
    private String getResponseContentStr(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = getResponseContentEntity(httpResponse);
        if (null == entity) {
            return null;
        }
        return EntityUtils.toString(entity, DEFAULT_CHARSET);
    }

    /**
     * 获取响应实体
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private HttpEntity getResponseContentEntity(HttpResponse httpResponse)
            throws IOException {
        StatusLine statusLine = httpResponse.getStatusLine();
        if (null == statusLine) {
            throw new IOException("status not specified");
        }
        int statusCode = statusLine.getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            EntityUtils.consumeQuietly(httpResponse.getEntity());
            throw new IOException("status code: " + statusCode);
        }
        return httpResponse.getEntity();
    }

}