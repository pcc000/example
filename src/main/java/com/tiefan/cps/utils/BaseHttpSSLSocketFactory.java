package com.tiefan.cps.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

/**
 * @author kunma
 * @date 2017/11/9
 */
public class BaseHttpSSLSocketFactory extends SSLSocketFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseHttpSSLSocketFactory.class);

    public static class MyX509TrustManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate ax509certificate[], String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate ax509certificate[], String s) {
        }

        static MyX509TrustManager manger = new MyX509TrustManager();

        public MyX509TrustManager() {
        }
    }

    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

        public TrustAnyHostnameVerifier() {
        }
    }

    public BaseHttpSSLSocketFactory() {
    }

    private SSLContext getSSLContext() {
        return createEasySSLContext();
    }

    @Override
    public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3)
            throws IOException {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public Socket createSocket(InetAddress arg0, int arg1)
            throws IOException {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
    }

    @Override
    public Socket createSocket(String arg0, int arg1)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return null;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return null;
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(s, host, port, autoClose);
    }

    private SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{MyX509TrustManager.manger}, null);
            return context;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}

