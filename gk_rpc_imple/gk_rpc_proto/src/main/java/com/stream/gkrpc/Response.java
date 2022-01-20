package com.stream.gkrpc;

import lombok.Data;

/**
 * @author : codingchao
 * @date : 2022-01-20 09:44
 * @Description: RPC的返回
 **/
@Data
public class Response {
    //服务返回编码 0 成功
    private  int code;

    //具体信息描述
    private String message = "ok";
    //返回的数据
    private Object data;
}
