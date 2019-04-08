package com.tiefan.cps.utils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangmingan
 * @time 2017/4/28 9:15
 */
public class StringUtils {

    /**
     * 此方法描述的是：根据string取得相应的md5加密值
     *
     * @param plainText 明码文本
     * @return String
     */
    public static String getMd5String(String plainText) {
        if (plainText == null) {
            return "";
        }

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return plainText;
        }

        char[] charArray = plainText.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString()
                .substring(8, 24);
    }

    /**
     * 此方法描述的是：根据md5码取得相应的加密值，加密一次，解密两次
     *
     * @param inStr MD5
     * @return String
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    /**
     * 将string 转换 map
     *
     * @param paramStr 输入字符串
     * @return 转换map
     */
    public static Map<String, String> getMapFromParamString(String paramStr) {
        Map<String, String> paramMap = new HashMap<String, String>();
        String[] args = paramStr == null ? new String[]{""} : paramStr.split(",");
        for (String param : args) {
            if (null == param || param.trim().length() <= 0) {
                continue;
            }
            param = param.trim();
            String[] keyValue = param.split(":");
            if (keyValue.length < 2) {
                continue;
            }
            paramMap.put(keyValue[0].replace("\"", ""), keyValue[1].replace("\"", ""));
        }
        return paramMap;
    }


    public static void main(String[] args) {
        String s = new String("tuniu520");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + getMd5String(s));
        System.out.println("加密的：" + convertMD5(s));
        System.out.println("解密的：" + convertMD5(convertMD5(s)));
    }
}
