package com.tiefan.cps.base.log.trace;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tiefan.keel.fsp.client.util.FspSeqUtils;

public class TNSeqFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String traceId = request.getHeader("tn-sequence-id");
		if(StringUtils.isEmpty(traceId)){
			FspSeqUtils.setTnSeq(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		
		filterChain.doFilter(request, response);
	}
}
