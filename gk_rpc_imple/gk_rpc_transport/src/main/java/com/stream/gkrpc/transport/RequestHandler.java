package com.stream.gkrpc.transport;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author : codingchao
 * @date : 2022-01-20 10:36
 * @Description: 处理网络请求的handler
 **/
public interface RequestHandler {
    void onRequest(InputStream receive, OutputStream toResp);
}
