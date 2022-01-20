package com.stream.gkrpc.codec;

/**
 * @author : codingchao
 * @date : 2022-01-20 09:32
 * @Description:
 **/
public interface Decoder {
    <T> T decode(byte[] bytes,Class<T> clazz);
}
