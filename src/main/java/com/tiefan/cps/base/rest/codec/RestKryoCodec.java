package com.tiefan.cps.base.rest.codec;

import com.tiefan.cps.base.rest.RestCodec;
import com.tiefan.keel.utils.serialize.KryoWrapper;

/**
 * kryo codec
 * Created by chengyao on 2016/8/15.
 */
public class RestKryoCodec implements RestCodec {

    private final static String MEDIA_TYPE = "application/kryo; charset=utf-8";

    @Override
    public byte[] encode(Object input) {
        return KryoWrapper.writeClassAndObject(input);
    }

    @Override
    public Object decode(byte[] input) {
        return KryoWrapper.readClassAndObject(input);
    }

    @Override
    public String mediaType() {
        return MEDIA_TYPE;
    }
}
