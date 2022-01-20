package com.stream.gkrpc.example;

import com.stream.gkrpc.server.RPCServerConfig;
import com.stream.gkrpc.server.RpcServer;

/**
 * @author : codingchao
 * @date : 2022-01-20 11:01
 * @Description:
 **/
public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer(new RPCServerConfig());
        server.register(CalcService.class,new CalcServiceImpl());
        server.start();
    }
}
