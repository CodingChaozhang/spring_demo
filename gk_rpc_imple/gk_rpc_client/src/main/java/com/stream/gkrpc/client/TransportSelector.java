package com.stream.gkrpc.client;

import com.stream.gkrpc.Peer;
import com.stream.gkrpc.transport.TransportClient;

import java.util.List;

/**
 * @author : codingchao
 * @date : 2022-01-20 10:50
 * @Description:
 **/
public interface TransportSelector {
    /**
     * 初始化selector
     * @param peers 可以连接的server端点信息
     * @param count client与server建立多少个连接
     * @param clazz client实现class
     */
    void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz);

    /**
     * 表示选择哪个transport与selector做交互
     * @return
     */
    TransportClient select();

    /**
     * 释放用完的client
     * @param client
     */
    void release(TransportClient client);

    void close();
}
