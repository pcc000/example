package com.tiefan.cps.utils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

public class BaseHttpSSLSocketFactory extends SSLSocketFactory
{
	
	private static final Logger logger = LoggerFactory.getLogger(BaseHttpSSLSocketFactory.class);
	
    public static class MyX509TrustManager implements X509TrustManager
    {
        public X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }
        
        public void checkClientTrusted(X509Certificate ax509certificate[], String s)
        {
        }

        public void checkServerTrusted(X509Certificate ax509certificate[], String s)
        {
        }

        static MyX509TrustManager manger = new MyX509TrustManager();

        public MyX509TrustManager()
        {
        }
    }

    public static class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }

        public TrustAnyHostnameVerifier()
        {
        }
    }

    public BaseHttpSSLSocketFactory()
    {
    }

    private SSLContext getSSLContext()
    {
        return createEasySSLContext();
    }

    public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3)
        throws IOException
    {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
        throws IOException, UnknownHostException
    {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    public Socket createSocket(InetAddress arg0, int arg1)
        throws IOException
    {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
    }

    public Socket createSocket(String arg0, int arg1)
        throws IOException, UnknownHostException
    {
        return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
    }

    public String[] getSupportedCipherSuites()
    {
        return null;
    }

    public String[] getDefaultCipherSuites()
    {
        return null;
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
    {
        return getSSLContext().getSocketFactory().createSocket(s, host, port, autoClose);
    }

    private SSLContext createEasySSLContext()
    {
        try
        {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] {MyX509TrustManager.manger}, null);
            return context;
        } catch(Exception e) {
        	logger.error(e.getMessage(), e);
        }
        return null;
    }
}