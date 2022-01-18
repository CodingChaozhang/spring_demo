package com.lcz.netty;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author : codingchao
 * @date : 2022-01-18 16:13
 * @Description: 存储整个工程的全局配置
 **/
public class NettyConfig {
    /**
     * 存储接入的客户端的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
