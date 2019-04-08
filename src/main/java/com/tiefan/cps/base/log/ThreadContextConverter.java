package com.tiefan.cps.base.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;

public class ThreadContextConverter extends ClassicConverter {
    private static final String LOG_SEPARATE = "一";

    @Override
    public String convert(ILoggingEvent event) {
        try {
            StringBuilder sb = new StringBuilder();
            String uuid = ThreadContext.getUUID();

            if (StringUtils.isNotEmpty(uuid)) {
                sb.append(LOG_SEPARATE).append(uuid);
            }

            if (sb.length() > 1) {
                sb.append(LOG_SEPARATE).append(" ");
            }

            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}