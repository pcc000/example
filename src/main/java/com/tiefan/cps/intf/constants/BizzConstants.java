package com.tiefan.cps.intf.constants;

/**
 * @author tonyjia
 * 征信系统CSS 7打头
 * 1:系统错误码
 * 2:SQL异常
 * 3:编码异常
 * 4:渠道
 */
public class BizzConstants {


    /**
     * 系统异常
     */
    public static int SYS_ERROR = 71000001;

    /**
     * 入参校验错误
     */
    public static int PARAM_ERROR = 71000002;


    /**
     * 系统SQL异常
     */
    public static int SYS_SQL_ERROR = 72000001;

    /**
     * 编码异常
     */
    public static int ENCRY_ERROR = 73000001;


    /**
     * 渠道超时
     */
    public static int CHANNEL_TIME_OUT = 74000001;


    /**
     * 渠道返回值错误
     */
    public static int CHANNEL_RETURN_ERROR = 74000002;


    /**
     * 渠道转换xml异常
     */
    public static int CHANNEL_TRANS_XML = 74000003;

    /**
     * 第三方查询错误
     */
    public static int CHANNEL_QUERY_ERROR = 74000004;

    /**
     * 第三方登陆失败
     */
    public static int CHANNEL_LOGIN_FAILED = 74000005;

    /**
     * 第三方查询返回为空
     */
    public static int CHANNEL_QUERY_NONE = 74000006;
    
    
    /**
     * 芝麻信用中用户未授权
     */
    public static int ZMXY_PEOPLE_NO_AUTH = 75000001;
}
