package com.tiefan.cps.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tiefan.cps.intf.constants.BizzConstants;
import com.tiefan.cps.intf.exception.BizzException;

/**
 *
 * @author Administrator
 *
 */
public class DESEncryptUtils {

	private static final Logger logger = LoggerFactory.getLogger(DESEncryptUtils.class);

	/**
	 * 加密
	 *
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encode(String key, String data) {

		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(key.getBytes());// 向量
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data.getBytes("utf-8"));

			return Base64.encode(bytes);
		} catch (Exception e) {
			logger.error("base64编码异常，错误信息：{}", e);
			throw new BizzException(BizzConstants.ENCRY_ERROR, "base64编码异常");
		}
	}

	/**
	 * 解密
	 *
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(String key, byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(key.getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			byte[] bs = cipher.doFinal(data);
			return bs;
		} catch (Exception e) {
			logger.error("base64解密异常，错误信息：{}", e);
			throw new BizzException(BizzConstants.ENCRY_ERROR, "base64解密异常");
		}
	}

}
