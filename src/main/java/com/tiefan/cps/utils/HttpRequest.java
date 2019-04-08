package com.tiefan.cps.utils;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP、HTTPS 请求工具类
 * 
 * @author ydq
 *
 */
public final class HttpRequest implements AutoCloseable{
	
	//模拟Log，请自己实现自己的Logger
	private class Logger{
		public void error(String msg){
			System.err.println(msg);
		}
	}
	private Logger logger = new Logger();

	/**
	 * 创建一个Get请求
	 * 
	 * @param url
	 * @return
	 */
	public static HttpRequest get(String url) {
		return new HttpRequest(url, Method.GET);
	}

	/**
	 * 创建一个Post请求
	 * 
	 * @param url
	 * @return
	 */
	public static HttpRequest post(String url) {
		return new HttpRequest(url, Method.POST);
	}
	// 请求方法
	private enum Method{GET, POST}

	// 请求协议
	private enum Protocol{HTTP, HTTPS}

	private String url;// 请求地址
	//默认模拟IE11的UserAgent
	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
	private String referer;
	private Method method;// 请求方法
	private Map<String, Object> params;// 请求参数
	private Charset reqCharset = Charset.forName("UTF-8");// 请求编码
	private Charset respCharset = Charset.forName("UTF-8");// 返回的编码
	
	private Protocol protocol;//请求协议

	private String sslKey;// 微信退款API请求证书Key
	private String certPath;// 微信退款API证书文件路径
	private String postData;//Post 请求方式发送的数据
	
	private String savePath;//文件下载保存路径
	
	
	private Integer reqTimeout=5000;//请求超时时间设置
	private Integer readTimeout;//读取超时时间设置
	
	//私有的构造方法
	private HttpRequest(String url, Method method) {
		super();
		this.url = url;
		this.method = method;
		this.params = new HashMap<>();
		if (url != null && url.toUpperCase().indexOf(Protocol.HTTP.toString()) == 0) {
			protocol = Protocol.HTTP;
			if (url.toUpperCase().indexOf(Protocol.HTTPS.toString()) == 0)
				protocol = Protocol.HTTPS;
			referer = protocol.toString().toLowerCase()+"://"+url.split("/")[2];
		}
	}

	/**
	 * 设置请求编码
	 * @param charset
	 * @return
	 */
	public HttpRequest reqCharset(Charset charset) {
		reqCharset = charset;
		return this;
	}

	/**
	 * 设置返回值编码
	 * @param charset
	 * @return
	 */
	public HttpRequest respCharset(Charset charset) {
		respCharset = charset;
		return this;
	}
	/**
	 * 设置请求编码
	 * @param charset
	 * @return
	 */
	public HttpRequest reqCharset(String charset) {
		reqCharset = Charset.forName(charset);
		return this;
	}
	
	/**
	 * 设置返回值编码
	 * @param charset
	 * @return
	 */
	public HttpRequest respCharset(String charset) {
		respCharset = Charset.forName(charset);
		return this;
	}

	/**
	 * 添加一个请求参数
	 * @param key
	 * @param value
	 * @return
	 */
	public HttpRequest put(String key, Object value) {
		this.params.put(key, value);
		return this;
	}
	/**
	 * 一次性添加多个请求参数
	 * @param params
	 * @return
	 */
	public HttpRequest put(Map<String,Object> params) {
		if (params != null) {
			//java8+
//			params.forEach((k, v) -> {
//				if(v!=null)
//					this.params.put(k, v);
//			});
			for (Entry<String, ? extends Object> entry : params.entrySet()) {
				this.params.put(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	/**
	 * 设置UserAgent
	 * @param userAgent
	 * @return
	 */
	public HttpRequest userAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}
	
	/**
	 * 设置Referer 有些接口需要验证Referer如网易云音乐，有些接口有防盗链只能为特定Referer或者为空，如微信图片
	 * 不设置则默认为请求地址的根路径，如果不想设置这请设置null
	 * @param referer
	 */
	public HttpRequest referer(String referer) {
		this.referer = referer;
		return this;
	}

	public HttpRequest sslConfig(String sslKey,String certPath){
		this.sslKey = sslKey;
		this.certPath = certPath;
		return this;
	}
	
	
	
	/**
	 * 设置读取超时
	 * @return
	 */
	public HttpRequest reqTimeout(Integer reqTimeout){
		this.reqTimeout = reqTimeout;
		return this;
	}
	
	/**
	 * 设置读取超时
	 * @param readTimeout
	 * @return
	 */
	public HttpRequest readTimeout(Integer readTimeout){
		this.readTimeout = readTimeout;
		return this;
	}
	
	/**
	 * 文件下载
	 * @param path 文件下载存储的文字
	 * @return
	 */
	public boolean download(String path){
		if(path==null) return false;
		savePath = path;
		return "success".equals(send());
	}

	/**
	 * 发送请求 并返回结果
	 * @return
	 */
	public String send() {
		if (protocol!=null) {
			boolean hasMap = !params.keySet().isEmpty();
			//如果是Get方式 或 Post方式但是有需要直接Post输出的参数 则将Map参数设置在URL字符串上
			if (this.method == Method.GET||this.postData!=null)
				url =  hasMap? (url + (url.indexOf("?") >= 0 ? "&" : "?") + mapToParamStr(this.params)):url;
			//如果是Post方式 并且没有设置需要直接Post输出的参数（非键值对）则将Map参数转化为Post参数
			if(this.method == Method.POST&&hasMap&&this.postData==null)
				this.postData = mapToParamStr(this.params);
			try {
				return request();
			} catch (KeyManagementException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException
					| KeyStoreException | CertificateException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}
	/**
	 * 发送带有Post数据的请求（非键值对的Post参数，如腾讯微信创建自定义菜单的接口）
	 * @param postData
	 * @return
	 */
	public String send(String postData) {
		this.postData = postData;
		return send();
	}
	
	/**
	 * 发送带有post数据请求（将Map数据转为键值对形式）
	 * @param param
	 * @return
	 */
	public String send(Map<String,Object> param){
		return send(mapToParamStr(param));
	}

	/**
	 * 执行请求的方法
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
	@SuppressWarnings("static-access")
	private String request() throws IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException {
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		URL url = new URL(this.url);
		URLConnection conn = url.openConnection();
		String result = null,redirect=null;
		if (Protocol.HTTPS == protocol) {
			SSLContext sslContext = SSLContext.getInstance("TLSV1.2");
			X509TrustManager[] tm = { new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {return null;}
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			} };
			if (sslKey == null || certPath == null) {
				sslContext.init(null, tm, new SecureRandom());
			} else {
				// 需要用到证书文件和证书key的时候，专为微信退款请求接口设计
				final char[] kp = sslKey.toCharArray();
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				KeyStore ks = KeyStore.getInstance("PKCS12");
				FileInputStream fileInputStream = new FileInputStream(certPath);
				ks.load(fileInputStream, kp);
				kmf.init(ks, kp);
				sslContext.init(kmf.getKeyManagers(), tm, new SecureRandom());
			}
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			HttpsURLConnection httpsConn = ((HttpsURLConnection) conn);
			httpsConn.setSSLSocketFactory(ssf);
			httpsConn.setRequestMethod(method.toString());
			result = URLConnectionRequest(httpsConn);
			redirect = httpsConn.getHeaderField("Location");
			httpsConn.disconnect();
		} else {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod(method.toString());
			result = URLConnectionRequest(httpConn);
			redirect = httpConn.getHeaderField("Location");
			httpConn.disconnect();
		}
		return redirect==null?result:this.get(redirect).send();
	}

	/**
	 * 通用的请求方法
	 * @param conn
	 * @return
	 */
	private String URLConnectionRequest(final URLConnection conn) {
		conn.setConnectTimeout(reqTimeout);
		conn.setRequestProperty("User-Agent", userAgent);
		conn.setRequestProperty("Referer", referer);
		conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml,application/json;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty("Accept-Charset", reqCharset.name());
		conn.setUseCaches(false);
		if(readTimeout!=null) conn.setReadTimeout(readTimeout);
		if (Method.POST == method) {
			//默认认为是表单提交
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + reqCharset.name());
			conn.setDoOutput(true);
			conn.setDoInput(true);
			if (postData!=null&&postData.length()>0) {
				postData = postData.trim();
				if(postData.indexOf("{")==0)//当以"{"开头的post数据要提交则认为是提交json数据
					conn.setRequestProperty("Content-Type", "application/json;charset=" + reqCharset.name());
				else if(postData.indexOf("<")==0)//当以"<"开头的post数据要提交则认为是提交xml数据
					conn.setRequestProperty("Content-Type", "application/xml;charset=" + reqCharset.name());
				try(OutputStream outputStream = conn.getOutputStream()){
					outputStream.write(postData.getBytes(reqCharset));
				}catch(IOException e){
					logger.error(e.getMessage());
				}
			}
		} else {
			conn.setRequestProperty("Content-Type","text/html,application/xml,application/json,text/json,image/png,image/jpeg,*/*;charset="+ reqCharset.name());
		}
		StringBuilder sb = new StringBuilder();
		if(savePath==null){
			try (InputStream inStream = conn.getInputStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inStream, respCharset);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
				String str = null;
				while ((str = bufferedReader.readLine()) != null)
					sb.append(str);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}else{
			try(InputStream inStream = conn.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			FileOutputStream fos = new FileOutputStream(savePath)){
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1)
				bos.write(buffer, 0, len);
			fos.write(bos.toByteArray());
			sb.append("success");
			}catch(IOException e){
				logger.error(e.getMessage());
			}
		}
		return sb.toString();
	}

	/**
	 * 创建类似于超链接的字符串，并将值urlEncode一下，key如果是中文那我无话可说
	 * @return 
	 */
	private String mapToParamStr(Map<String,Object> map){
		StringBuilder sb = new StringBuilder();
		String charset = reqCharset.name();
		//java8+
//		map.forEach((k, v) -> {
//			if (v != null) {
//				sb.append("&").append(k).append("=");
//				try {
//					sb.append(URLEncoder.encode(v.toString(), charset));
//				} catch (UnsupportedEncodingException e) {
//					sb.append(v);
//				}
//			}
//		});

		for (Entry<String, Object> entry : map.entrySet()) {
			if(entry.getValue()!=null){
				sb.append("&").append(entry.getKey()).append("=");
				try {
					sb.append(URLEncoder.encode(entry.getValue().toString(), charset));
				} catch (UnsupportedEncodingException e) {
					sb.append(entry.getValue());
				}
			}
		}
		return sb.length() > 0 ? sb.substring(1) : sb.toString();
	}
	
	@Override
	public void close() throws Exception {
		
	}
}