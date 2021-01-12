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

import cn.hutool.core.util.CharsetUtil;
import com.company.project.common.config.WsHandler;
import com.company.project.common.utils.DriverUtils;
import com.company.project.entity.AttributeInfo;
//import com.dc3.common.bean.driver.PointValue;
//import com.dc3.common.model.Point;
//import com.dc3.common.sdk.bean.DriverContext;
//import com.dc3.common.sdk.service.DriverService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报文处理，需要视具体情况开发
 * 本驱动中使用报文（设备名称[22]+关键字[1]+海拔[4]+速度[8]+液位[8]+方向[4]+锁定[1]+经纬[21]）进行测试使用
 * 4C 69 73 74 65 6E 69 6E 67 56 69 72 74 75 61 6C 44 65 76 69 63 65
 * 62
 * ‭44 C3 E7 5C‬
 * ‭40 46 D5 C2 8F 5C 28 F6‬
 * 00 00 00 00 00 00 00 0C
 * 00 00 00 2D
 * 01
 * 31 33 31 2E 32 33 31 34 35 36 2C 30 32 31 2E 35 36 38 32 31 31
 * <p>
 * 使用 sokit 发送以下报文
 * lg:[4C 69 73 74 65 6E 69 6E 67 56 69 72 74 75 61 6C 44 65 76 69 63 65 62 44 C3 E7 5C 40 46 D5 C2 8F 5C 28 F6 00 00 00 00 00 00 00 0C 00 00 00 2D 01 31 33 31 2E 32 33 31 34 35 36 2C 30 32 31 2E 35 36 38 32 31 31]
 *
 * @author pnoker
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyUdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static NettyUdpServerHandler nettyUdpServerHandler;

    private static String robot_audio_device_host = "172.16.0.21";

    private static int ROBOT_AUDIO_DEVICE_PORT = 1233;

    private static int LOCAL_PORT = 10001;

    private WsHandler wsHandler;

    public NettyUdpServerHandler(WsHandler wsHandler) {
        this.wsHandler = wsHandler;
    }

    //保留所有与服务器建立连接的channel对象
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @PostConstruct
    public void init() {
        nettyUdpServerHandler = this;
    }

//    @Resource
//    private DriverService driverService;
//    @Resource
//    private DriverContext driverContext;

    @Override
    @SneakyThrows
    public void channelRead0(ChannelHandlerContext context, DatagramPacket msg) {
        ByteBuf byteBuf = msg.content();
        InetSocketAddress sender = msg.sender();
        InetAddress address = sender.getAddress();
        String hostAddress = address.getHostAddress();
        int port = sender.getPort();
        InetSocketAddress recipient = msg.recipient();
        int recipientPort = recipient.getPort();
        int dataLength = byteBuf.readableBytes();
        byte[] data = new byte[dataLength];
        switch (recipientPort) {
            //接收机器人发送过来的音频，往浏览器客户端送
            case 6271:
//                NettyUdpClient.getInstance();
//                NettyUdpClientHandler.sendAudioData(byteBuf, new InetSocketAddress(robot_audio_device_host, robot_audio_device_port));
                byteBuf.readBytes(data);
                wsHandler.sendAudio(data, hostAddress);

                break;
            //监听从浏览器客户端过来的音频数据，给机器人端发送
            case 6272:

                Map<String, WebSocketSession> clients = wsHandler.getClients();
                for (Map.Entry<String, WebSocketSession> sessionEntry : clients.entrySet()) {
                    try {
                        WebSocketSession session = sessionEntry.getValue();
                        if (session.isOpen() && session.getUri().getPath().endsWith("/onekeyalarm")) {

                            String audioAddress = wsHandler.audioAddressMap.get(session.getId());
                            DatagramSocket socket = new DatagramSocket();
//                            InetAddress inetAddress = session.getRemoteAddress().getAddress();
                            byteBuf.readBytes(data);
                            java.net.DatagramPacket dp = new java.net.DatagramPacket(data, data.length, InetAddress.getByName(audioAddress),
                                    ROBOT_AUDIO_DEVICE_PORT);

                            // 3，通过socket服务，将已有的数据包发送出去。通过send方法。
                            socket.send(dp);
                            // 4，关闭资源。
                            socket.close();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                DatagramSocket socket = new DatagramSocket();
//
//                byteBuf.readBytes(data);
//                java.net.DatagramPacket dp = new java.net.DatagramPacket(data, dataLength, InetAddress.getByName(robot_audio_device_host),
//                        robot_audio_device_port);
//
//                // 3，通过socket服务，将已有的数据包发送出去。通过send方法。
//                socket.send(dp);
//                // 4，关闭资源。
//                socket.close();
//                NettyUdpClient.getInstance();
//                NettyUdpClientHandler.sendAudioData(byteBuf, new InetSocketAddress(robot_audio_device_host, robot_audio_device_port));
                break;
            //监听机器人发送状态的接口
            case 6273:
                Channel channel = context.channel();

                channel.writeAndFlush("");
                break;

            default:
        }


        log.info("{}->{}", context.channel().remoteAddress(), ByteBufUtil.hexDump(byteBuf));
        String deviceName = byteBuf.toString(0, 1000, CharsetUtil.CHARSET_ISO_8859_1);
//        Long deviceId = nettyUdpServerHandler.driverContext.getDeviceIdByName(deviceName);
        String hexKey = ByteBufUtil.hexDump(byteBuf, 22, 1);

        AudioFormat.Encoding encoding =  new AudioFormat.Encoding("PCM_UNSIGNED");
        //编码格式，采样率，每个样本的位数，声道，帧长（字节），帧数，是否按big-endian字节顺序存储
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 11025, 8, 1, 1, 11025 ,false);
//        AudioFormat format = new AudioFormat(11025F, 8, 1, false ,false);

        SourceDataLine  auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        auline.start();
//        byte[] b = new byte[256];
        try {
//            while(fis.read(b)>0) {
//                auline.write(b, 0, b.length);
//                System.out.println(1);
//            }

//            while(byteBuf.hasArray()) {
//                int off = byteBuf.arrayOffset() + byteBuf.readerIndex();
//            AudioSystem.write(new AudioInputStream(auline), AudioFileFormat.Type.WAVE, new File("D://ss.wav"));
//            int dataLength = byteBuf.readInt();
            int totalLength = byteBuf.readableBytes();

            if ( totalLength > 0 ) {
                byte[] array = new byte[totalLength];
                byteBuf.readBytes(array);
//                auline.write(array, 0, totalLength);

//                byte[] data = byteBuf.array();
//                auline.write(data, 0, data.length);

//            if (!byteBuf.hasArray()) {
//                byte[] array = new byte[length];
//                byteBuf.readBytes(array);
//                byteBuf.getBytes(byteBuf.readerIndex(), array);
    //            handleArray(array, 0, length);
//                auline.write(array, 0, length);
    //                System.out.println(1);
            }

            auline.drain();
            auline.close();

//            context.channel().writeAndFlush(byteBuf);

        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        finally {

        }

//        List<PointValue> pointValues = new ArrayList<>();
//        Map<Long, Map<String, AttributeInfo>> pointInfoMap = nettyUdpServerHandler.driverContext.getDevicePointInfoMap().get(deviceId);
//        for (Long pointId : pointInfoMap.keySet()) {
//            Point point = nettyUdpServerHandler.driverContext.getDevicePoint(deviceId, pointId);
//            Map<String, AttributeInfo> infoMap = pointInfoMap.get(pointId);
//            int start = DriverUtils.value(infoMap.get("start").getType(), infoMap.get("start").getValue());
//            int end = DriverUtils.value(infoMap.get("end").getType(), infoMap.get("end").getValue());
//
//            if (infoMap.get("key").getValue().equals(hexKey)) {
//                PointValue pointValue = null;
//                switch (point.getName()) {
//                    case "海拔":
//                        float altitude = byteBuf.getFloat(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(altitude),
//                                nettyUdpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(altitude)));
//                        break;
//                    case "速度":
//                        double speed = byteBuf.getDouble(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(speed),
//                                nettyUdpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(speed)));
//                        break;
//                    case "液位":
//                        long level = byteBuf.getLong(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(level),
//                                nettyUdpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(level)));
//                        break;
//                    case "方向":
//                        int direction = byteBuf.getInt(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(direction),
//                                nettyUdpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(direction)));
//                        break;
//                    case "锁定":
//                        boolean lock = byteBuf.getBoolean(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(lock),
//                                nettyUdpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(lock)));
//                        break;
//                    case "经纬":
//                        String lalo = byteBuf.toString(start, end, CharsetUtil.CHARSET_ISO_8859_1).trim();
//                        pointValue = new PointValue(deviceId, pointId, lalo,
//                                nettyUdpServerHandler.driverService.convertValue(deviceId, pointId, lalo));
//                        break;
//                    default:
//                        break;
//                }
//                if (null != pointValue) {
//                    pointValues.add(pointValue);
//                }
//            }
//        }
//        nettyUdpServerHandler.driverService.pointValueSender(pointValues);
    }

    @Override
    @SneakyThrows
    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        log.debug(throwable.getMessage());
        context.close();
    }

    //表示服务端与客户端连接建立
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();  //其实相当于一个connection

        /**
         * 调用channelGroup的writeAndFlush其实就相当于channelGroup中的每个channel都writeAndFlush
         *
         * 先去广播，再将自己加入到channelGroup中
         */
        channelGroup.writeAndFlush(" 【服务器】 -" +channel.remoteAddress() +" 加入\n");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(" 【服务器】 -" +channel.remoteAddress() +" 离开\n");

        //验证一下每次客户端断开连接，连接自动地从channelGroup中删除调。
        System.out.println(channelGroup.size());
        //当客户端和服务端断开连接的时候，下面的那段代码netty会自动调用，所以不需要人为的去调用它
        //channelGroup.remove(channel);
    }

    //连接处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() +" 上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() +" 下线了");
    }



}