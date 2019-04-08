package com.tiefan.cps.base.rest;

/**
 * codec 接口
 * Created by chengyao on 2016/8/15.
 */
public interface RestCodec {

    byte[] encode(Object input);

    Object decode(byte[] input);

    String mediaType();

}
