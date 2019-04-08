package com.tiefan.cps.base.log.trace;

import com.tiefan.keel.fsp.client.util.FspSeqUtils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TNSeqConverter extends ClassicConverter{

	@Override
	public String convert(ILoggingEvent event) {
		String tnSeq = FspSeqUtils.getTnSeq();
		return tnSeq;
	}

}
