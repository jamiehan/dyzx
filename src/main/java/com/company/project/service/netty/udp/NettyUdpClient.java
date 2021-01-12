package com.company.project.service.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@Component
public class NettyUdpClient {

//    private final Bootstrap bootstrap2;
//    public final NioEventLoopGroup workerGroup2;
//    public static Channel channel2;
//    private static final Charset ASCII = Charset.forName("ASCII");
//
//    public void start() throws Exception{
//        try {
//            channel2 = bootstrap2.bind().sync().channel();
//            channel2.closeFuture().await(1000);
//        } finally {
//        	workerGroup2.shutdownGracefully();
//        }
//    }
//
//    public Channel getChannel(){
//        return channel2;
//    }
//
//    public static NettyUdpClient getInstance(){
//        return nettyUdpClient.INSTANCE;
//    }
//
//    private static final class nettyUdpClient{
//        static final NettyUdpClient INSTANCE = new NettyUdpClient();
//    }
//    @SneakyThrows
//    private NettyUdpClient(){
//        bootstrap2 = new Bootstrap();
//        workerGroup2 = new NioEventLoopGroup();
//        bootstrap2.group(workerGroup2)
//                .channel(NioDatagramChannel.class)
////                .localAddress(new InetSocketAddress(1234))
//                .option(ChannelOption.SO_BROADCAST, true)
////                .handler(new ChannelInitializer<Channel>() {
////                    @Override
////                    protected void initChannel(Channel channel) {
////                        channel.pipeline().addLast(
////                                new WriteTimeoutHandler(30),
////                                new NettyUdpClientHandler()
////                        );
////                    }
////                });
//                .handler(new ChannelInitializer<NioDatagramChannel>() {
//                    @Override
//                    protected void initChannel(NioDatagramChannel ch)throws Exception {
//                        ChannelPipeline pipeline = ch.pipeline();
////				pipeline.addLast(new StringDecoder(ASCII))
////                .addLast(new StringEncoder(ASCII))
//                        pipeline.addLast(new NettyUdpClientHandler());
//                    }
//                });
////        ChannelFuture future = bootstrap.bind().sync();
////        future.channel().closeFuture().sync();
//    }

}
