package com.stream.gkrpc;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : codingchao
 * @date : 2022-01-20 09:44
 * @Description: 网络传输中的一个端点
 **/
@Data
@AllArgsConstructor
public class Peer {
    private String host;
    private int port;
}
