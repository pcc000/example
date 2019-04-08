package com.tiefan.cps.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressStringUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CompressStringUtil.class);
	
	/**
     * 解压缩
     * @param compressed
     * @return
     */
    public static final String decompress(byte[] compressed) {
        if (compressed == null)
            return null;
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed;
        try {
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            ZipEntry entry = zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString("GBK");
        } catch (IOException e) {
            decompressed = null;
            throw new RuntimeException("解压缩字符串数据出错", e);
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                	logger.error(e.getMessage(), e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                	logger.error(e.getMessage(), e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                	logger.error(e.getMessage(), e);
                }
            }
        }
        return decompressed;
    }
}