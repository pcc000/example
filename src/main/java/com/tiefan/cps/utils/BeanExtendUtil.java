package com.tiefan.cps.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * 类名称: BeanExtendUtil
 * 类描述:
 * 创建人: dingchao
 * 修改人: dingchao
 * 修改时间: 2015-10-15 下午4:37:02
 * 修改备注:
 * @version  V1.0.0	 
 */
public class BeanExtendUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(BeanExtendUtil.class);
	
	public static boolean isNull(Object object)
	{
		return null == object;
	}
	
	public static boolean isNotNull(Object object)
	{
		return !isNull(object);
	}

	public static Map<String,Object> toMap(Object bean)
	{
		if(bean instanceof Map) {
			return (Map<String,Object>)bean;
		}
		Map<String,Object> result = new HashMap<String,Object>();
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors)
		{
			Method readMethod = propertyDescriptor.getReadMethod();
			if(!(null == readMethod || null == propertyDescriptor.getWriteMethod())) {
				makeAccessible(readMethod);
				try {
					Object value = readMethod.invoke(bean, new Object[0]);
					result.put(propertyDescriptor.getName(), value);
				} catch (Exception e) {
				} 
			}
		}
		return result;
	}
	
	public static Object newInstance(Class type,Object argument)
	{
		return newInstance(type, new Object[]{argument});
	}
	
	public static Object newInstance(Class type,Object[] arguments)
	{
		Class[] clazz = new Class[arguments.length];
		try {
			for(int i = 0; i < arguments.length; i++) {
				clazz[i] = arguments[i].getClass();
			}
			Constructor constructor = type.getConstructor(clazz);
			return constructor.newInstance(arguments);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		return null;
	}
	
	public static Object getField(Object object,String fieldName)
	{
		try {
			Field field = object.getClass().getField(fieldName);
			makeAccessible(field);
			return field.get(object);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static void makeAccessible(AccessibleObject accessibleObject)
	{
		accessibleObject.setAccessible(true);
	}
	
	public static void invoke(AccessibleObject accessibleObject,Object owner,Object[] values)
	{
	}
}