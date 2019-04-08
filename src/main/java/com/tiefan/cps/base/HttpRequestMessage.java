package com.tiefan.cps.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * 类名称: HttpRequestMessage
 * 类描述:
 * 创建人: dingchao
 * 修改人: dingchao
 * 修改时间: 2015-10-15 下午3:33:18
 * 修改备注:
 * @version  V1.0.0	 
 */
public class HttpRequestMessage {
	
	// Request Line ----- 请求行
	private String url;
	
	
	
	private HttpMethod httpMethod;
	
	// Message Headers ----- 消息头
	private HttpHeaders httpHeaders = new HttpHeaders();
	
	// Entity Body ----- 消息体
	private Object entity;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}
//	public void addHttpHeader(HttpHeader httpHeader) {
//		this.httpHeaders = httpHeaders;
//	}
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
}