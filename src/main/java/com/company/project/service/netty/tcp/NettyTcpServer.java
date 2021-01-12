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

package com.company.project.service.netty.tcp;

import com.company.project.common.config.WsHandler;
import com.company.project.service.BlacklistService;
import com.company.project.service.netty.udp.NettyUdpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author pnoker
 */
@Component
public class NettyTcpServer {


    /**
     * DeviceId:Channel
     * 用于存放设备的 Netty Context Channel
     */
    public static final Map<String, Channel> deviceChannelMap = new ConcurrentHashMap<>(16);

    @Value("${server.tcp.additionalPorts}")
    private String ports;

    private String[] portArray;

    static final int DEFAULT_MINIMUM = 64;
    static final int DEFAULT_INITIAL = 1024;
    static final int DEFAULT_MAXIMUM = 2048;

    // 帧头
    byte[] header= new byte[]{0x5B,0x5B};

    @PostConstruct
    public void init(){

        portArray = ports.split(",");
        this.start();
    }

//    @Resource
//    private WsHandler wsHandler;

    @Resource
    private NettyTcpServerHandler nettyTcpServerHandler;


    @SneakyThrows
    public void start() {
        //NioEventLoopGroup是用来处理IO操作的多线程事件循环器
        EventLoopGroup bossGroup  = new NioEventLoopGroup();  // 用来接收进来的连接
        EventLoopGroup workerGroup  = new NioEventLoopGroup();// 用来处理已经被接收的连接
//        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();//是一个启动NIO服务的辅助启动类
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 这里告诉Channel如何接收新的连接
//                    .localAddress(new InetSocketAddress(Integer.parseInt(portArray[0])))
//                    .localAddress(new InetSocketAddress(Integer.parseInt(portArray[1])))
//                    .localAddress(new InetSocketAddress(Integer.parseInt(portArray[2])))
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(10240))
//                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(DEFAULT_MAXIMUM))
//                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(DEFAULT_MINIMUM, DEFAULT_INITIAL, DEFAULT_MAXIMUM))
                    .option(ChannelOption.SO_RCVBUF,1024*10)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            channel.pipeline().addLast(new CommonDecoder(Integer.parseInt(portArray[0]),Integer.parseInt(portArray[1])));
                            channel.pipeline().addLast("ping",new IdleStateHandler(2,2,2*2, TimeUnit.SECONDS));
//                            channel.pipeline().addLast(" framer",new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,Unpooled.wrappedBuffer(new byte [] {'E','O','F','\n'})));
////                            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Unpooled.copiedBuffer(header)));
//                            channel.pipeline().addLast(new StringDecoder());
                            channel.pipeline().addLast(new WriteTimeoutHandler(30), nettyTcpServerHandler);
                        }
                    });
                 /*   .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
//                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Unpooled.copiedBuffer("$_".getBytes())));
                            socketChannel.pipeline().addLast("ping",new IdleStateHandler(2,2,2*2, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new WriteTimeoutHandler(30), nettyTcpServerHandler);
                        }
                    });*/
//            ChannelFuture future = bootstrap.bind().sync();
//            future.channel().closeFuture().sync();
//            bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535));
            // 绑定端口，开始接收进来的连接
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
        } finally {
//            group.shutdownGracefully().sync();
//            bossGroup.shutdownGracefully().sync();
//            workerGroup.shutdownGracefully().sync();
        }
    }
}