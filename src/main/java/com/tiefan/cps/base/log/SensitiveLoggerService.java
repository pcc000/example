package com.tiefan.cps.base.log;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.tiefan.cps.utils.AesECBUtil;
import com.tiefan.cps.utils.SystemConfig;
import com.tiefan.fbs.fsp.base.core.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * 敏感service打印log
 *
 * @author tonyjia
 */
@Service
public class SensitiveLoggerService {

    private static Logger logger = LoggerFactory.getLogger(SensitiveLoggerService.class);

    private static int PoolSize = 50;

    private static final ListeningExecutorService service = MoreExecutors
            .listeningDecorator(Executors.newFixedThreadPool(PoolSize));

    @Resource
    SystemConfig systemConfig;

    /**
     * 涉及到身份证，银行卡打印日志加密
     *
     * @param flag
     * @param data
     */
    public void println(String flag, Object data) {

        String aesPrivateKey = systemConfig.getAesPrivateKey();

        service.submit(new Runnable() {
            @Override
            public void run() {
                String json = JsonUtil.toString(data);

                JSONObject jsonObject = JSONObject.parseObject(json);
                // 银行卡，脱敏
                if (jsonObject.containsKey("card")) {
                    jsonObject.put("card", AesECBUtil.encrypt(jsonObject.getString("card"), aesPrivateKey));
                }
                // 身份证
                if (jsonObject.containsKey("identity")) {
                    jsonObject.put("identity", AesECBUtil.encrypt(jsonObject.getString("identity"), aesPrivateKey));
                }
                // 身份证
                if (jsonObject.containsKey("identitycard")) {
                    jsonObject.put("identitycard", AesECBUtil.encrypt(jsonObject.getString("identitycard"), aesPrivateKey));
                }
                // 银行卡
                if (jsonObject.containsKey("bankCard")) {
                    jsonObject.put("bankCard", AesECBUtil.encrypt(jsonObject.getString("bankCard"), aesPrivateKey));
                }
                //身份证
                if (jsonObject.containsKey("docNo")) {
                    jsonObject.put("docNo", AesECBUtil.encrypt(jsonObject.getString("docNo"), aesPrivateKey));
                }
                //银行账号
                if (jsonObject.containsKey("accountNo")) {
                    jsonObject.put("accountNo", AesECBUtil.encrypt(jsonObject.getString("accountNo"), aesPrivateKey));
                }
                //身份证
                if (jsonObject.containsKey("idCardNum")) {
                    jsonObject.put("idCardNum", AesECBUtil.encrypt(jsonObject.getString("idCardNum"), aesPrivateKey));
                }
                //银行卡
                if (jsonObject.containsKey("bankCardNum")) {
                    jsonObject.put("bankCardNum", AesECBUtil.encrypt(jsonObject.getString("bankCardNum"), aesPrivateKey));
                }
                //jsonObject
                if (jsonObject.containsKey("id")) {
                    jsonObject.put("id", AesECBUtil.encrypt(jsonObject.getString("id"), aesPrivateKey));
                }
                //同盾身份证
                if (jsonObject.containsKey("id_number")) {
                    jsonObject.put("id_number", AesECBUtil.encrypt(jsonObject.getString("id_number"), aesPrivateKey));
                }
                if (jsonObject.containsKey("idNumber")) {
                    jsonObject.put("idNumber", AesECBUtil.encrypt(jsonObject.getString("idNumber"), aesPrivateKey));
                }
                //同盾 银行卡
                if (jsonObject.containsKey("card_number")) {
                    jsonObject.put("card_number", AesECBUtil.encrypt(jsonObject.getString("card_number"), aesPrivateKey));
                }
                if (jsonObject.containsKey("cardNumber")) {
                    jsonObject.put("cardNumber", AesECBUtil.encrypt(jsonObject.getString("cardNumber"), aesPrivateKey));
                }
                if (jsonObject.containsKey("id_card_num")) {
                    jsonObject.put("id_card_num", AesECBUtil.encrypt(jsonObject.getString("id_card_num"), aesPrivateKey));
                }
                if (jsonObject.containsKey("bank_card_num")) {
                    jsonObject.put("bank_card_num", AesECBUtil.encrypt(jsonObject.getString("bank_card_num"), aesPrivateKey));
                }
                // 闪银身份证
                if (jsonObject.containsKey("idCard")) {
                    jsonObject.put("idCard", AesECBUtil.encrypt(jsonObject.getString("idCard"), aesPrivateKey));
                }
                // 探知 银行卡
                if (jsonObject.containsKey("bankCardNo")) {
                    jsonObject.put("bankCardNo", AesECBUtil.encrypt(jsonObject.getString("bankCardNo"), aesPrivateKey));
                }
                // 探知 身份证
                if (jsonObject.containsKey("identityNo")) {
                    jsonObject.put("identityNo", AesECBUtil.encrypt(jsonObject.getString("identityNo"), aesPrivateKey));
                }
                // 敬众乘机人信息 身份证
                if (jsonObject.containsKey("pid")) {
                    jsonObject.put("pid", AesECBUtil.encrypt(jsonObject.getString("pid"), aesPrivateKey));
                }
                // 敬众乘机人信息 护照
                if (jsonObject.containsKey("gid")) {
                    jsonObject.put("gid", AesECBUtil.encrypt(jsonObject.getString("gid"), aesPrivateKey));
                }
                for (String key : jsonObject.keySet()) {
                    try {
                        Object child = jsonObject.get(key);
                        if (child != null && child instanceof JSONObject) {
                            // 敬众乘机人信息
                            JSONObject childJsonObj = (JSONObject) child;
                            if (childJsonObj.containsKey("idCard")) {
                                childJsonObj.put("idCard", AesECBUtil.encrypt(childJsonObj.getString("idCard"), aesPrivateKey));
                            }
                            //北冥二要素校验
                            if (childJsonObj.containsKey("IDNumber")) {
                                childJsonObj.put("IDNumber", AesECBUtil.encrypt(childJsonObj.getString("IDNumber"), aesPrivateKey));
                            }
                            //冰鉴 身份证
                            if (childJsonObj.containsKey("id")) {
                                childJsonObj.put("id", AesECBUtil.encrypt(childJsonObj.getString("id"), aesPrivateKey));
                            }
                            //第三级遍历
                            if(CollectionUtils.isEmpty(childJsonObj.keySet())){
                                continue;
                            }
                            for (String key2 : childJsonObj.keySet()) {
                                Object child2 = childJsonObj.get(key2);
                                if (child != null && child2 instanceof JSONObject) {
                                    // 敬众乘机人信息
                                    JSONObject child2Obj = (JSONObject) child2;
                                    if (child2Obj.containsKey("id")) {
                                        child2Obj.put("id", AesECBUtil.encrypt(child2Obj.getString("id"), aesPrivateKey));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                logger.info(flag, jsonObject.toJSONString());
            }
        });

    }

    /**
     * list中是要加密的属性
     *
     * @param flag
     * @param data
     * @param list
     */
    public void println(String flag, Object data, List<String> list) {

        String aesPrivateKey = systemConfig.getAesPrivateKey();

        service.submit(new Runnable() {
            @Override
            public void run() {
                String json = JsonUtil.toString(data);

                JSONObject jsonObject = JSONObject.parseObject(json);
                if (!CollectionUtils.isEmpty(list)) {
                    for (String key : list) {
                        if (jsonObject.containsKey(key)) {
                            jsonObject.put(key, AesECBUtil.encrypt(jsonObject.getString(key), aesPrivateKey));
                        }
                    }

                }
                // 银行卡，脱敏
                logger.info(flag, jsonObject.toJSONString());
            }
        });

    }

    /**
     * arg中是要加密的属性
     *
     * @param flag
     * @param data
     */
    public void println(String flag, Object data, String... args) {

        String aesPrivateKey = systemConfig.getAesPrivateKey();

        service.submit(new Runnable() {
            @Override
            public void run() {
                String json = JsonUtil.toString(data);

                JSONObject jsonObject = JSONObject.parseObject(json);

                for (String key : args) {
                    if (jsonObject.containsKey(key)) {
                        jsonObject.put(key, AesECBUtil.encrypt(jsonObject.getString(key), aesPrivateKey));
                    }
                }
                // 银行卡，脱敏
                logger.info(flag, jsonObject.toJSONString());
            }
        });

    }
}