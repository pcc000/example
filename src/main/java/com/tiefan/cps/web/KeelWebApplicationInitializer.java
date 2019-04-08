package com.tiefan.cps.web;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tiefan.cps.base.log.trace.TNSeqFilter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Properties;

public class KeelWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeelWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext container) {
        customInit();

        container.setInitParameter("contextConfigLocation", "classpath*:spring/spring-*.xml");
        container.setInitParameter("contextInitializerClasses", "com.tiefan.keel.fsp4server.ContextProfileInitializer, "
                + "com.tiefan.cmc.client.boot.ConfigPrepareApplicationContextInitializer");

        // config.spring dispatcher
        ServletRegistration.Dynamic springDispatcher = container.addServlet("dispatcher", new DispatcherServlet());
        springDispatcher.setInitParameter("contextConfigLocation", "classpath:dispatcher-servlet.xml");
        springDispatcher.setLoadOnStartup(1);
        springDispatcher.addMapping("/");

        // listener
        container.addListener(ContextLoaderListener.class);

        // thread data share filter
//        container.addFilter("DataSharedFilter", DataSharedFilter.class)
//                .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "dispatcher");


        // cors support
        CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        CorsFilter corsFilter = new CorsFilter(source);

        container.addFilter("CorsFilter", corsFilter)
                .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "dispatcher");


        // character encoding filter
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        container.addFilter("CharacterEncodingFilter", encodingFilter)
                .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "dispatcher");

//        container.addFilter("actionLogFilter", ActionLogFilter.class)
//                .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "dispatcher");

        //TNSeqFilter
        container.addFilter("tnSeqFilter", TNSeqFilter.class)
        .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "dispatcher");
    }

    private void customInit() {
        // 载入logback配置
        initLogger();

        // convertUtils register
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);

        // fast json default setting
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteDateUseDateFormat.getMask();
    }

    private void initLogger() {
        String logbackConfigLocation;
        try {
            Resource resourceSetting = new ClassPathResource("main-setting.properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resourceSetting);
            logbackConfigLocation = "log/logback_" + properties.getProperty("application.envName") + ".xml";
        } catch (Exception ex) {
            throw new RuntimeException("can't loading main-setting.properties file from classpath.", ex);
        }

        try {
            Resource resource = new ClassPathResource(logbackConfigLocation);
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.reset();
            JoranConfigurator joranConfigurator = new JoranConfigurator();
            joranConfigurator.setContext(loggerContext);
            joranConfigurator.doConfigure(resource.getInputStream());
            LOGGER.info("logback configure file loaded. filePath={}", resource.getURI());
        } catch (Exception e) {
            LOGGER.error("can't loading logback configure file. classpath:" + logbackConfigLocation, e);
        }
    }
}