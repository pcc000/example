package com.tiefan.cps.base.sensutive;

import java.lang.reflect.Field;

import javax.annotation.Resource;

import com.tiefan.cps.utils.AesECBUtil;
import com.tiefan.cps.utils.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 日志加密工具类
 * 
 * @author tonyjia
 *
 */
@Component
public class SensitiveInfoUtils {

	@Resource
    SystemConfig systemConfig;

	public String toJsonString(Object object) {
		return JSON.toJSONString(object, getValueFilter());
	}

	/**
	 * 身份证加密
	 * 
	 * @param identity
	 * @param aesPrivateKey
	 * @return
	 */
	private String desensitizeIdentity(String identity, String aesPrivateKey) {

		if (StringUtils.isEmpty(identity)) {
			return StringUtils.EMPTY;
		}
		return AesECBUtil.encrypt(identity, aesPrivateKey);
	}

	/**
	 * 银行卡号加密
	 * 
	 * @param card
	 * @param aesPrivateKey
	 * @return
	 */
	private String desensitizeCard(String card, String aesPrivateKey) {
		if (StringUtils.isEmpty(card)) {
			return StringUtils.EMPTY;
		}
		return AesECBUtil.encrypt(card, aesPrivateKey);
	}

	private final ValueFilter getValueFilter() {

		String aesPrivateKey = systemConfig.getAesPrivateKey();

		return new ValueFilter() {
			@Override
			public Object process(Object object, String name, Object value) {

				try {
					Field field = getField(object, name);
					SensitiveInfo annotation = field.getAnnotation(SensitiveInfo.class);
					if (null != annotation) {

						String strVal = null;

						if (value instanceof String) {
							strVal = (String) value;
						}
						if (value instanceof JSONObject) {
							strVal = value.toString();
						}

						if (StringUtils.isNotEmpty(strVal)) {
							switch (annotation.type()) {
							case IDETITY: // 身份证
								return desensitizeIdentity(strVal, aesPrivateKey);
							case CARD_NUM: // 银行卡号
								return desensitizeCard(strVal, aesPrivateKey);
							default:
								break;
							}
						}
					}

				} catch (Exception e) {
					// 发生异常，正常返回，不做处理
				}
				return value;
			}

		};
	}

	private Field getField(Object object, String fieldName) {

		Class<?> clazz = object.getClass();

		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (Exception e) {
				// 不打印日志
			}
		}
		return null;
	}
}
