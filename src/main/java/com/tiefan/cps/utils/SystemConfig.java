package com.tiefan.cps.utils;

import com.tiefan.keel.conf.BaseSystemConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

/**
 * config.spring 配置服务
 *
 * @author chengyao
 */
@ManagedResource(objectName = "com.tiefan:type=StaticAndConfig,name=SystemConfig", description = "配置项目")
@Service
public class SystemConfig extends BaseSystemConfig {

	@Value("${aes.private.key}")
	private String aesPrivateKey;

	public String getAesPrivateKey() {
		return aesPrivateKey;
	}

	public void setAesPrivateKey(String aesPrivateKey) {
		this.aesPrivateKey = aesPrivateKey;
	}
}
