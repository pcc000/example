package com.tiefan.cps.intf.constants;

/**
 * <Description> 异常码<br>
 *    第一位数字 标识 大交通，暂定使用 7<br>
 *    第二位数字 标识 业务线，机票（0）/用车（1）/汽车票（2）/火车票（3）<br>
 *    第三到四位数字 标识 系统/模块 <br>
 *    第五到八位数字 标识 异常码本身<br>
 * Created by chengyao on 2016/2/20.
 */
public class BizzEx {

    /**
     * 成功
     */
    public static final int SUCCESS = 7081000;

    /** 入参校验 */
    public static final int VERIFY_INPUT_ERROR = 7081001;

    /**
     * 通用异常
     */
    public static final int COMM_EX_WRAP = 7081002;

    /** 反馈异常 */
    public static final int FEED_BACK_ERROR = 7081003;

    /**
     * 特殊返回封装异常
     */
    public static final int API_RESP_WRAP = 7081004;

    public static final int INTERRUPTED = 7081005;

    // 序列化失败
    public static final int PROTOBUF_EX = 7081007;

    public static final int HYSTRIX_TIMEOUT = 7081801;
    public static final int HYSTRIX_REJECTED = 7081802;
    public static final int HYSTRIX_SHORTCIRCUIT = 7081803;

    // 接口请求，返回失败信息
    public static final int CTRIP_INTF_RESULT_ERROR = 7081101;
    // 接口请求，result = null
    public static final int CTRIP_INTF_RESULT_NONE = 7081102;
    // 接口请求，http status != 200
    public static final int CTRIP_INTF_HTTP_CODE_EX = 7081103;
    // 接口请求，异常
    public static final int CTRIP_INTF_EX = 7081104;
    // 接口请求，检查返回数据出错
    public static final int CTRIP_INTF_RESULT_CHECK = 7081105;
    // 转换接口对象
    public static final int CTRIP_TO_REQ = 7081106;
    

    // 接口请求，返回失败信息
    public static final int IBE_INTF_RESULT_ERROR = 7081201;
    // 接口请求，result = null
    public static final int IBE_INTF_RESULT_NONE = 7081202;
    // 接口请求，http status != 200
    public static final int IBE_INTF_HTTP_CODE_EX = 7081203;
    // 接口请求，异常
    public static final int IBE_INTF_EX = 7081204;
    // 转换接口对象
    public static final int IBE_TO_REQ = 7081205;

    public static final int IBE_HYSTRIX = 7081206;
    // 接口请求参数错误
    public static final int IBE_INTF_REQ_PARAM_EX = 7081207;


    // 接口请求，返回失败信息
    public static final int IBE_PLUS_INTF_RESULT_ERROR = 7081301;
    // 接口请求，result = null
    public static final int IBE_PLUS_INTF_RESULT_NONE = 7081302;
    // 接口请求，http status != 200
    public static final int IBE_PLUS_INTF_HTTP_CODE_EX = 7081303;
    // 接口请求，异常
    public static final int IBE_PLUS_INTF_EX = 7081304;
    // 转换接口对象
    public static final int IBE_PLUS_TO_REQ = 7081305;

    public static final int IBE_PLUS_HYSTRIX = 7081306;
    
    public static final int IBE_PLUS_FACADE = 7081307;
    // 接口请求参数错误
    public static final int IBE_PLUS_INTF_REQ_PARAM_EX = 7081308;

    // 接口请求，返回失败信息
    public static final int REST_INTF_RESULT_ERROR = 7081501;
    // 接口请求，result = null
    public static final int REST_INTF_RESULT_NONE = 7081502;
    // 接口请求，http status != 200
    public static final int REST_INTF_HTTP_CODE_EX = 7081503;
    // 接口请求，异常
    public static final int REST_INTF_EX = 7081504;
    // 转换接口对象
    public static final int REST_TO_REQ = 7081505;

    public static final int REST_HYSTRIX = 7081506;

    public static final int TSP_RESOURCE_INTF_EX = 7081601;

    public static final int TSP_RESOURCE_INTF_ERROR = 7081602;

    public static final int TSP_RESOURCE_INTF_REQ_ERROR = 7081603;

    public static final int TSP_ADT_INTF_EX = 7081604;

    public static final int TSP_ADT_INTF_ERROR = 7081605;

    public static final int TSP_ADT_INTF_REQ_ERROR = 7081606;

    public static final int TSP_AOP_INTF_RESULT_NONE = 7081701;

    public static final int TSP_AOP_INTF_INVALID_POLICY = 7081701;

//
//    // AV 查询 没有线路
//    public static final int AV_NO_ROUTING = 7081901;
//
//    // AV 查询 城市对错误
//    public static final int AV_CITY_PAIR = 7081902;
//
//    // 航班动态接收处理错误
//    public static final int FLT_AWARE_ERROR = 7081903;
//
//    // 更新经停信息错误
//    public static final int FLT_BRIEF_UPDATE_ERROR = 7081904;
//
//    // 经停查询超并发
//    public static final int FLT_BRIEF_TPS_LIMIT = 7081905;
//
//    // AV缓存MISS
//    public static final int AV_CACHE_MISS = 7081906;
}
