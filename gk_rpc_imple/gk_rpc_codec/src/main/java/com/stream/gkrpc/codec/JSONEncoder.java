package com.stream.gkrpc.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author : codingchao
 * @date : 2022-01-20 09:39
 * @Description:
 **/
public class JSONEncoder implements Encoder {
    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}
