package com.tiefan.cps.utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Title:AesECBUtil.java
 * @Package:com.tuniu.finance.xff.vca.utils
 * @author: dengweiwei
 * @date:2016年11月12日 下午3:45:15
 * 算法模式：ECB 密钥
 * 长度：128bits 16位长
 * 偏移量： 默认
 * 补码方式：PKCS5Padding
 * 解密串编码方式：base64
 */
public class AesECBUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AesECBUtil.class);
    
    

    /**
     * gz
     */
    private AesECBUtil() {

    }

    /**
     * 加密
     * @param sSrc sSrc
     * @param sKey sKey
     * @return String
     */
    public static String encrypt(String sSrc, String sKey) {
        if (StringUtils.isBlank(sSrc)) {
            return sSrc;
        }
        try {
            if (sKey.length() != 16) {
                LOGGER.info("the key should be 16 位长");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            return new Base64().encodeToString(encrypted); //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            LOGGER.error("AesECB Encrypt exception", e);
            return null;
        }
    }

    /**
     * 使用参数中的密钥解密
     * @param sSrc 密文
     * @param sKey 密钥
     * @return 明文
     */
    public static String decrypt(String sSrc, String sKey) {
        if (StringUtils.isBlank(sSrc)) {
            return sSrc;
        }

        try {
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                LOGGER.info("the key should be 16 位长");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc); //先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                LOGGER.info("AesECB Decrypt exception", e);
                return null;
            }
        } catch (Exception ex) {
            LOGGER.info("AesECB Decrypt exception", ex);
            return null;
        }
    }
    
    

}

