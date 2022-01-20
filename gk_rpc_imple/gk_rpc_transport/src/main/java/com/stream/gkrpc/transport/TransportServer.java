package com.stream.gkrpc.transport;

/**
 * @author : codingchao
 * @date : 2022-01-20 10:36
 * @Description:
 * 1.启动监听端口
 * 2.接收请求
 * 3.关闭监听
 **/
public interface TransportServer {
    void init(int port,RequestHandler handler);

    void start();

    void stop();
}
