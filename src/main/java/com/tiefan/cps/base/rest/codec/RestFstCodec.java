package com.tiefan.cps.base.rest.codec;

import com.tiefan.cps.base.rest.RestCodec;
import com.tiefan.keel.utils.serialize.FstWrapper;


/**
 * fst codec
 * Created by chengyao on 2016/8/16.
 */
public class RestFstCodec implements RestCodec {

    private final static String MEDIA_TYPE = "application/fst; charset=utf-8";

    @Override
    public byte[] encode(Object input) {
        return FstWrapper.writeObject(input);
    }

    @Override
    public Object decode(byte[] input) {
        return FstWrapper.readObject(input);
    }

    @Override
    public String mediaType() {
        return MEDIA_TYPE;
    }

}
