package com.stream.gkrpc.transport;

import com.stream.gkrpc.Peer;

import java.io.InputStream;

/**
 * @author : codingchao
 * @date : 2022-01-20 10:31
 * @Description:
 * 1.创建连接
 * 2.发送数据，并且等待响应
 * 3.关闭连接
 **/
public interface TransportClient {
    void connect(Peer peer);

    InputStream write(InputStream data);

    void close();
}
