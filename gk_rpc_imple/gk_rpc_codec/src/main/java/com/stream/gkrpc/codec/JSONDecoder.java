package com.stream.gkrpc.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author : codingchao
 * @date : 2022-01-20 09:40
 * @Description:
 **/
public class JSONDecoder implements Decoder {
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
