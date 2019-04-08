package com.tiefan.cps.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Https工具类，仅使用https协议访问
 *
 * @author kunma
 * @date 2017/11/9
 */
public class HttpsClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsClientUtils.class);

    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 0;
    public static final int TIMEOUT = 15000;
    public static final String ENCODING = "UTF-8";

    public static String executePostUTF(Map<String, String> submitFromData, String requestUrl) {
        try {
            HttpsURLConnection connection = createConnection(requestUrl, "POST", ENCODING);
            if (connection == null) {
                return null;
            } else {
                requestServer(connection, getRequestParamString(submitFromData, ENCODING), ENCODING);
                return response(connection, ENCODING);
            }
        } catch (Exception e) {
            LOGGER.error("https请求失败...:", e);
            return null;
        }
    }

    public static String executePostUTF2(String submitFromData, String requestUrl) {
        try {
            HttpsURLConnection connection = createConnection2(requestUrl, "POST", ENCODING);
            if (connection == null) {
                return null;
            } else {
                requestServer(connection, submitFromData, ENCODING);
                return response(connection, ENCODING);
            }
        } catch (Exception e) {
            LOGGER.error("https请求失败...:", e);
            return null;
        }
    }

    public static String executeGetUTF(String requestUrl) {
        try {
            HttpsURLConnection connection = createConnection(requestUrl, "GET", ENCODING);
            if (connection == null) {
                return null;
            } else {
                return response(connection, ENCODING);
            }
        } catch (Exception e) {
            LOGGER.error("https请求失败...:", e);
            return null;
        }
    }

    private static void requestServer(HttpsURLConnection connection, String message, String encoding) {
        PrintStream out = null;
        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoding);
            out.print(message);
            out.flush();
        } catch (Exception e) {
            LOGGER.error("打开https连接失败...:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static String response(HttpsURLConnection connection, String encoding) {
        InputStream in = null;
        StringBuilder sb = null;
        BufferedReader br = null;
        String temp = null;
        try {
            //如果返回状态码为200,读取返回消息
            if (connection.getResponseCode() == HttpStatus.OK.value()) {
                sb = new StringBuilder();
                in = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(in, encoding));
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                return sb.toString();
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("https读取返回数据失败...:", e);
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (in != null) {
                    in.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
                LOGGER.error("https连接释放资源失败...:", e2);
            }

        }
    }

    private static HttpsURLConnection createConnection(String requestUrl, String method, String encoding) {
        HttpsURLConnection httpsURLConnection = null;
        URL url = null;
        try {
            url = new URL(requestUrl);
//	        	仅允许https协议使用
            if (!url.getProtocol().equalsIgnoreCase("https")) {
                //非https协议请求返回空
                return null;
            }
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setConnectTimeout(TIMEOUT);
            httpsURLConnection.setReadTimeout(TIMEOUT);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setRequestProperty("Content-type", (new StringBuilder("application/x-www-form-urlencoded;charset=")).append(encoding).toString());
            httpsURLConnection.setRequestMethod(method);
            httpsURLConnection.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            httpsURLConnection.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());

            return httpsURLConnection;
        } catch (IOException e) {
            LOGGER.error("创建https连接失败...:", e);
            return null;
        }
    }

    private static HttpsURLConnection createConnection2(String requestUrl, String method, String encoding) {
        HttpsURLConnection httpsURLConnection = null;
        URL url = null;
        try {
            url = new URL(requestUrl);
//	        	仅允许https协议使用
            if (!url.getProtocol().equalsIgnoreCase("https")) {
                //非https协议请求返回空
                return null;
            }
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setConnectTimeout(TIMEOUT);
            httpsURLConnection.setReadTimeout(TIMEOUT);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setRequestProperty("Content-type", "application/json");
            httpsURLConnection.setRequestMethod(method);
            httpsURLConnection.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            httpsURLConnection.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());

            return httpsURLConnection;
        } catch (IOException e) {
            LOGGER.error("创建https连接失败...:", e);
            return null;
        }
    }

    private static String getRequestParamString(Map requestParam, String encoding) {
        if (StringUtils.isBlank(encoding)) {
            encoding = "UTF-8";
        }
        StringBuffer sf = new StringBuffer("");
        String reqstr = "";
        if (requestParam != null && requestParam.size() != 0) {
            for (Iterator iterator = requestParam.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry en = (Map.Entry) iterator.next();
                try {
                    sf.append((new StringBuilder(String.valueOf((String) en.getKey()))).append("=").append(en.getValue() != null && !"".equals(en.getValue()) ? URLEncoder.encode((String) en.getValue(), encoding) : "").append("&").toString());
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e.getMessage(), e);
                    return "";
                }
            }

            reqstr = sf.substring(0, sf.length() - 1);
        }

        System.out.println(reqstr);
        return reqstr;
    }
}
