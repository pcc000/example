package com.tiefan.cps.base.sensutive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密字段注解
 * 
 * @author tonyjia
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveInfo {

	SensitiveType type();
}
