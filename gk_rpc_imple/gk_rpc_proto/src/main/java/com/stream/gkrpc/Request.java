package com.stream.gkrpc;

import lombok.Data;

/**
 * @author : codingchao
 * @date : 2022-01-20 09:44
 * @Description: RPC请求 【服务描述+请求参数数组】
 **/
@Data
public class Request {
    private ServiceDescriptor serviceDescriptor;
    private Object[] parameters;
}
