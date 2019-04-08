package com.tiefan.cps.base.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author chenyuejin
 *         create 2017-07-03 15:47
 */

public class LoggerSensitiveInfoConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        // TODO 处理敏感信息
        return event.getMessage();
    }
}
