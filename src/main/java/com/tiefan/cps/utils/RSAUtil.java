package com.tiefan.cps.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.StringUtils;

/**
 * Created by jiaowu on 2017/12/19.
 */
public class RSAUtil {
   // private static Logger LOGGER = LoggerFactory.getLogger(RSAUtil.class);
    private static String KEY_ALGORITHM = "RSA";

    public static final int KEY_SIZE = 2048; // 密钥长度, 一般2048
    public static final int RESERVE_BYTES = 11;

    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; // 加密block需要预留11字节
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";// sign值生成方式
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private String algorithm;// 密钥生成模式
    private String signature;// 签名sign生成模式
    private Charset charset;// 编码格式

    private int keySize;// RSA密钥长度必须是64的倍数，在512~65536之间
    private int decryptBlock; // 默认keySize=2048的情况下, 256 bytes
    private int encryptBlock; // 默认keySize=2048的情况下, 245 bytes

    private static KeyFactory keyFactory;
    static {
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            //LOGGER.info("生成RSA密钥对异常，{}", e);
        }
    }

    public RSAUtil() {
        this(CIPHER_ALGORITHM);
    }

    public RSAUtil(String algorithm) {
        this(algorithm, CHARSET_UTF8);
    }

    public RSAUtil(int keySize) {
        this(CIPHER_ALGORITHM, keySize, CHARSET_UTF8, SIGNATURE_ALGORITHM);
    }

    public RSAUtil(String algorithm, Charset charset) {
        this(algorithm, KEY_SIZE, charset, SIGNATURE_ALGORITHM);
    }

    public RSAUtil(String algorithm, int keySize, Charset charset, String signature) {
        this.algorithm = algorithm;
        this.signature = signature;
        this.charset = charset;
        this.keySize = keySize;

        this.decryptBlock = this.keySize / 8;
        this.encryptBlock = decryptBlock - RESERVE_BYTES;
    }

    /**
     * 公钥解密
     * @param publicKeyStr 公钥字符串
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decryptByPublicKey(String publicKeyStr, String data) throws Exception {
        PublicKey publicKey = restorePublicKey(publicKeyStr);
        return decryptByPublicKey(publicKey, data);
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decryptByPublicKey(PublicKey publicKey, String data) throws Exception {
        byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(data.getBytes(charset));
        return decryptByPublicKey(publicKey, bytes);
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param data 需要解密的数据
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public String decryptByPublicKey(PublicKey publicKey, byte[] data) throws Exception {
        byte[] decrypt = null;

        int dataLen = data.length;
        // 计算分段解密的block数 (理论上应该能整除)
        int nBlock = (dataLen / encryptBlock);
        if ((dataLen % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个encryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * encryptBlock);

        Cipher cipher = getCipher();
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        // 分段解密
        for (int offset = 0; offset < data.length; offset += decryptBlock) {
            // block大小: decryptBlock 或 剩余字节数
            int inputLen = (data.length - offset);
            if (inputLen > decryptBlock) {
                inputLen = decryptBlock;
            }

            // 得到分段解密结果
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            // 追加结果到输出buffer中
            outbuf.write(decryptedBlock);
        }
        outbuf.flush();
        decrypt = outbuf.toByteArray();
        outbuf.close();

        return StringUtils.newString(decrypt, charset.name());
    }

    /**
     * 私钥加密
     * @param privateKeyStr 私钥字符串
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encryptByPrivateKey(String privateKeyStr, String data) {
        PrivateKey privateKey = restorePrivateKey(privateKeyStr);
        return encryptByPrivateKey(privateKey, data);
    }

    /**
     * 私钥加密
     * @param privateKey 私钥
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encryptByPrivateKey(PrivateKey privateKey, String data) {
        byte[] bytes = data.getBytes(charset);
        return encryptByPrivateKey(privateKey, bytes);
    }

    /**
     * 私钥加密
     * @param privateKey 私钥
     * @param data 需要加密的数据
     * @return 加密后字符串
     */
    public String encryptByPrivateKey(PrivateKey privateKey, byte[] data) {
        byte[] encrypt = null;

        int dataLen = data.length;
        // 计算分段加密的block数 (向上取整)
        int nBlock = (dataLen / encryptBlock);
        if ((dataLen % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个decryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlock);

        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            // 分段加密
            for (int offset = 0; offset < dataLen; offset += encryptBlock) {
                // block大小: encryptBlock 或 剩余字节数
                int inputLen = (dataLen - offset);
                if (inputLen > encryptBlock) {
                    inputLen = encryptBlock;
                }

                // 得到分段加密结果
                byte[] encryptedBlock = cipher.doFinal(data, offset, inputLen);
                // 追加结果到输出buffer中
                outbuf.write(encryptedBlock);
            }

            outbuf.flush();
            encrypt = outbuf.toByteArray();
            outbuf.close();
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
            //LOGGER.info("使用RSA，加密数据异常", e);
        }

        byte[] enData = org.apache.commons.codec.binary.Base64.encodeBase64(encrypt);
        return StringUtils.newString(enData, charset.name());
    }

    /**
     * 根据keyFactory生成Cipher
     * @return cipher
     */
    private Cipher getCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
           // LOGGER.info("生成RSA Cipher异常", e);
        }
        return cipher;
    }

    /**
     * 公钥还原，将公钥转化为PublicKey对象
     * @param publicKeyStr 公钥字符串
     * @return PublicKey对象
     */
    public PublicKey restorePublicKey(String publicKeyStr) {
        PublicKey publicKey = null;

        byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(publicKeyStr.getBytes(charset));
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            publicKey = keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
           // LOGGER.info("公钥还原，将公钥转化为PublicKey对象异常", e);
        }

        return publicKey;
    }

    /**
     * 私钥还原，将私钥转化为privateKey对象
     * @param privateKeyStr 私钥字符串
     * @return PrivateKey对象
     */
    public PrivateKey restorePrivateKey(String privateKeyStr) {
        PrivateKey privateKey = null;

        byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyStr.getBytes(charset));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
           // LOGGER.info("私钥还原，将私钥转化为privateKey对象异常", e);
        }

        return privateKey;
    }

    /**
     * RSA生成sign值
     * @param privateKeyStr 私钥字符串
     * @param data 数据
     * @return sign值
     * @throws Exception 异常
     */
    public String generateSign(String privateKeyStr, String data) throws Exception {
        return generateSign(restorePrivateKey(privateKeyStr), data);
    }

    /**
     * RSA生成sign值
     * @param privateKey 私钥
     * @param data 数据
     * @return sign值
     * @throws Exception 异常
     */
    public String generateSign(PrivateKey privateKey, String data) throws Exception {
        Signature signature = Signature.getInstance(this.signature);
        signature.initSign(privateKey);
        signature.update(data.getBytes(charset));
        return org.apache.commons.codec.binary.Base64.encodeBase64String(signature.sign());
    }

    /**
     * RSA验签
     * @param publicKeyStr 公钥字符串
     * @param data 数据
     * @param sign sign值
     * @return 验签是否成功
     * @throws Exception 异常
     */
    public boolean verifyRSA(String publicKeyStr, String data, String sign) throws Exception {
        return verifyRSA(restorePublicKey(publicKeyStr), data, sign);
    }

    /**
     * RSA验签
     * @param publicKey 公钥
     * @param data 数据
     * @param sign sign值
     * @return 验签是否成功
     * @throws Exception 异常
     */
    public boolean verifyRSA(PublicKey publicKey, String data, String sign) throws Exception {
        Signature signature = Signature.getInstance(this.signature);
        signature.initVerify(publicKey);
        signature.update(data.getBytes(charset));

        return signature.verify(org.apache.commons.codec.binary.Base64.decodeBase64(sign));
    }

    public static void main(String[] args) {
        RSAUtil rSAUtil= new RSAUtil();
        String data ="123456789";
        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCXIyAYK+NVoJ7DJEgTs3GyXIUu0+HtYm2mArKh8yUIbuiSN4aksNb4xAh0cS1YNg0G7prR+CKV7iUo1f8sA0VtN32XHxd6Fr3Q4QsC/+FH6HHS6GGI9i363j4YGtOwGEOyb9qt9YjFHlVqDyZiNYDazK3f+EZzfputFwVfZSp0M2fQhgBCZM2lycKvq/OJm79NDKMeld/ejpvNAqi7xehKy6eqNjdRiRmLcZvg6dW/A7QwlkQ/vCos5Mtd/kLu6oHgjXnBFiGsKGDScJYBvVFHcupmnE0eqJtnwr8OBIn9naeVaAHE46i6uxZtTe4K6QUM0JtdODL70EEjy7qG0WjpAgMBAAECggEBAIhFmIOS1th3CY8j4IU679IFT+SIERZsADeGCTCyvfpbngFwZUuLU1lbz8/F2D/IBHjynM+jLvQGlKS8RuaVUH0IYonm89EWPjHfJ4Gd269ta2viMUc/yPeAeXZcgfAuAKQb7I2bbKVnE1acsFwup68gi8n83vD2AEHSFvsLiXrZFZr9MmNIIyRfYZJCDPunZOv7XCgsDhdlYAx+Z092MwpEeLPEIpgueMHycU7AsBgq667m4/LP0l9CnCxk/cRUz9w5iZRmlyLLvL8Tox8VCcCtoOb7cxZpm1TYCQSDcVfhtIf4BKWjKBiHiHM17z0t111dEUc+XkbxacnPxDgn/80CgYEA3VgYY/1rPC5CPqmMXXl6iGdjsbSkJWaGigx3QkaUMwYv6xCxCL6zReNqHchbp+mvqNgWD6HPDDZk5dJnJeHvAdNCA6AQhodoPDFpYc70WgTciNv84cVTUSj9RGlRD9jvYN3k+lm8yxvykOc0xBsqE93lyNYEBuPE4cAi4LNBfgsCgYEArsz/94gXyvi/I2yAapkq3l+ieE+L731tc7FqB4QYVxyxUOgEla03++bRAikzOjT9YbGNl85khweNNle5J++3GL+Ak4hXmwHw6e1ZP4dP1d9OiBio96jn5okMD8AIFU/Y/JIoBr1o+k9jBkbOWOyVO5KD1z6ijGDCVMhovuWssVsCgYEApXjQcx/m5QyoFXRnLRI92m+Ahj9HX3ZwKg/7sB5XeHWtqQvHbYQzPZIvqKg6bSM0YQN6KqGKydR4RZ+v4RAwv6qRdWhaMlhUQnumDqrK3ek4fVAIkzgTe18rR9N7+F7zRfVc0xP3Idh41H8kYV71a/i9ahEk3Ym1jBc5e8ZGtdUCgYBv7erVqPp7SM6zsy2DlLKDlD9nxJ/5aZplY6xeRbKETWYpRXhyE2nuzjz1okYgNoAtR1FAbLOoVyiQLJnuPaxDl5SQY9Sc+CA42ne0m0N+0q/pq8i+VRSxZP4pM7C5XNi32irxLeYDqkPhaAOHo25nqAjuEjhppSeqvG1+F3l+UwKBgQC6puG4Xr/w4FSyKgRsDAE2tBKBllqLUh2S/68ptHpB54uO+bN1MI+I4B6CFArHfJlmPO/Vc4dC4IC2byrDd0uLjWT+D+7EzFgygpSxyYW5be/qjY/AZo7LfUbHQa77/uhhvCPZQZgYk+FEIIPIcruWejJmkbHpYE+WBtcFrsfjtw==";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlyMgGCvjVaCewyRIE7NxslyFLtPh7WJtpgKyofMlCG7okjeGpLDW+MQIdHEtWDYNBu6a0fgile4lKNX/LANFbTd9lx8Xeha90OELAv/hR+hx0uhhiPYt+t4+GBrTsBhDsm/arfWIxR5Vag8mYjWA2syt3/hGc36brRcFX2UqdDNn0IYAQmTNpcnCr6vziZu/TQyjHpXf3o6bzQKou8XoSsunqjY3UYkZi3Gb4OnVvwO0MJZEP7wqLOTLXf5C7uqB4I15wRYhrChg0nCWAb1RR3LqZpxNHqibZ8K/DgSJ/Z2nlWgBxOOoursWbU3uCukFDNCbXTgy+9BBI8u6htFo6QIDAQAB";
        String cusPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtxUeho47bzGLK+IrxCtysogzeLehTZT3xbD8W9P+Na63eA06BnrDWaXpLG5NiTdis/hRHwnIgwiOwcn3OdPNx7OjlzrtvwutUWC6nMOSdDHvZYFaWHIyvyFhljUbTTgW2QSJpX+IR1sopVha0G1SBxNazbs/9TGihjl4tc4Jg2e8D6ScOSJGxo1DzysyLxchw3A+1VVwiBTLEy4xS4nL14z2XSMcRrj5Pl9dO7r1O7G3+d5ubFNoq1/Ql+SzKRaxaMaP87CUJUxCrPffhcTvE4vihPOb7okcRgl3REvXdp9VSCmeTCZ9Gm/mBScazqQoGBbzcTDuCvc1zRG05qsvlwIDAQAB";

        String pub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh+mOu6wdFTBftNwBpLsc5uw8UsSA0q3o6TCzCdFQXMvGkm27vSLjrZnMiPV+L13D6S2FK53N/Fc/ClqoaAtWTxCVZaoPCV28g296FJbVyIqGKZ6gh6GT2YRiDDtmrZkbRMpqtZzmOPgSuKb1SRpY9JmtLWnN3NSxUhWsi+bcAdzA1mC8Y2WgcMFQOHEDISY3/YDufVj4pXutOs/El0+kIY35so7lO1qnWLgZ9PxSs+Hb+zU2Oe/YMjDHPfGj+OQ7xT0L0s6/VRe7nDalm0vWqZeEk+Gky9oKMqGvlA6MovNo3kS5pWJvFxhaqC88yVdmnJvgKO6FwoxUMh4CQtKreQIDAQAB";
        System.out.println(privateKey.length());
        System.out.println(publicKey.length());
        System.out.println(cusPubKey.length());


        data = rSAUtil.encryptByPrivateKey(privateKey,data);
        try {
            String datas = rSAUtil.decryptByPublicKey(publicKey,data);
            System.out.println(datas.equals("123456789"));
            System.out.println(datas);
            System.out.println(pub.length());

        } catch (Exception e) {
            
        }
    }

}
