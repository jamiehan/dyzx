/*
 * Copyright 2018-2020 Pnoker. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.company.project.service.netty.udp;

import com.company.project.common.config.WsHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @author Jamie
 */
@Component
public class NettyUdpServer {

    @Value("${server.udp.additionalPorts}")
    private String ports;

    private String[] portArray;

    @PostConstruct
    public void init(){

        portArray = ports.split(",");
        this.start();
    }

    @Resource
    private WsHandler wsHandler;

    @SneakyThrows
    public void start() {
//        EventLoopGroup bossGroup  = new NioEventLoopGroup();  // 用来接收进来的连接
//        EventLoopGroup workerGroup  = new NioEventLoopGroup();// 用来处理已经被接收的连接
        EventLoopGroup group = new NioEventLoopGroup();
        try {
//            ServerBootstrap bootstrap = new ServerBootstrap();//是一个启动NIO服务的辅助启动类
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .localAddress(new InetSocketAddress(Integer.parseInt(portArray[0])))
                    .localAddress(new InetSocketAddress(Integer.parseInt(portArray[1])))
                    .localAddress(new InetSocketAddress(Integer.parseInt(portArray[2])))
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(2205))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline().addLast(
                                    new WriteTimeoutHandler(30),
                                    new NettyUdpServerHandler(wsHandler)
                            );
                        }
                    });
            ChannelFuture future1 = bootstrap.bind(Integer.parseInt(portArray[0])).sync();
            ChannelFuture future2 = bootstrap.bind(Integer.parseInt(portArray[1])).sync();
            ChannelFuture future3 = bootstrap.bind(Integer.parseInt(portArray[2])).sync();

            future1.channel().closeFuture().addListener(new ChannelFutureListener()
            {
                @Override public void operationComplete(ChannelFuture future) throws Exception
                {       //通过回调只关闭自己监听的channel
                    future.channel().close();
                }
            });
            future2.channel().closeFuture().addListener(new ChannelFutureListener()
            {
                @Override public void operationComplete(ChannelFuture future) throws Exception
                {
                    future.channel().close();
                }
            });
            future3.channel().closeFuture().addListener(new ChannelFutureListener()
            {
                @Override public void operationComplete(ChannelFuture future) throws Exception
                {
                    future.channel().close();
                }
            });

//            future1.channel().closeFuture().sync();
//            future2.channel().closeFuture().sync();

        } finally {
//            group.shutdownGracefully().sync();
        }
    }
}