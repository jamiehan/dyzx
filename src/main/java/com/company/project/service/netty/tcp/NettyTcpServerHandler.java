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

import cn.hutool.core.util.CharsetUtil;
//import com.dc3.common.bean.driver.PointValue;
//import com.dc3.common.model.Point;
//import com.dc3.common.sdk.bean.AttributeInfo;
//import com.dc3.common.sdk.bean.DriverContext;
//import com.dc3.common.sdk.service.DriverService;
//import com.dc3.common.sdk.util.DriverUtils;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.common.config.WsHandler;
import com.company.project.common.constant.ActionEnum;
import com.company.project.common.constant.Common;
import com.company.project.common.utils.ByteUtils;
import com.company.project.entity.BlacklistEntity;
import com.company.project.service.BlacklistService;
import com.company.project.vo.resp.FaceRecognitionResultVO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class NettyTcpServerHandler extends ChannelInboundHandlerAdapter {
    private static NettyTcpServerHandler nettyTcpServerHandler;

    @PostConstruct
    public void init() {
        nettyTcpServerHandler = this;
    }

//    @Resource
//    private DriverService driverService;
//    @Resource
//    private DriverContext driverContext;
    @Resource
    private BlacklistService blacklistService;

    @Resource
    private WsHandler wsHandler;

    // 帧头
    byte[] header= new byte[]{0x5B,0x5B};

    // 帧尾
    byte[] tail= new byte[]{(byte) 0xB5,(byte) 0xB5};
//    // 一次完整数据长度
//    private int onetimeDataLen = 0;

//    public NettyTcpServerHandler(WsHandler wsHandler) {
//        this.wsHandler = wsHandler;
//    }

    //保留所有与服务器建立连接的channel对象
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    @SneakyThrows
    public void channelActive(ChannelHandlerContext context) {

        NettyTcpServer.deviceChannelMap.put(this.getIPString(context), context.channel());
        log.debug("Virtual Listening Driver Listener({}) accept clint({})", context.channel().localAddress(), context.channel().remoteAddress());
    }

    @Override
    @SneakyThrows
    public void channelRead(ChannelHandlerContext context, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        byte[] allByteArr = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, allByteArr);
        String allStr = new String(allByteArr,"UTF-8");
        System.out.println("收到的数据（字符串表示）：" + allStr);
        //TODO数据校验
/*        //判断数据帧头是否为0x5B,0x5B
        byte[] headerByte = new byte[2];
        byteBuf.getBytes(0, headerByte, 0, 2);
        if(!Arrays.equals(header,headerByte)) {
            return;
        }*/
//        if(byteBuf.readableBytes() == 1024
//                && byteBuf.readBytes.indexOf(0,1024, tail)) {
//            onetimeDataLen = onetimeDataLen + byteBuf.readableBytes();
//            return;
//        } else {
//            onetimeDataLen = onetimeDataLen + byteBuf.readableBytes();
//        }


//        byte[] onetimeData = new byte[onetimeDataLen];

/*        //获取数据长度
        byte[] dataLenByte = new byte[4];
        byteBuf.getBytes(4, dataLenByte, 0, 4);
        int dataLen = ByteUtils.bytesToInt(dataLenByte);*/
        //获取头部8个字节数据
        ByteBuf headByteBuf = byteBuf.readBytes(8);
        //读取内容和尾部的长度
        int bodyAndTailLen = byteBuf.readableBytes();
        //获取内容数据（校验位和尾部占用4个字节）
        ByteBuf bodyByteBuf = byteBuf.readBytes(bodyAndTailLen - 4);
        //获取尾部数据
        ByteBuf tailByteBuf = byteBuf.readBytes(4);
        //读取内容字节缓存到字节数组中
        byte[] dataByteArray = new byte[bodyByteBuf.readableBytes()];
        bodyByteBuf.readBytes(dataByteArray);
//        byteBuf.getBytes(8, dataByteArray, 0, dataLen);
//        ByteBuf dataByteBuf = new ByteBuf();


//        ByteBuf echo = Unpooled.copiedBuffer(byteBuf);
//        log.info("{}->{}", context.channel().remoteAddress(), ByteBufUtil.hexDump(byteBuf));
//        String deviceName = byteBuf.toString(0, 1024, CharsetUtil.CHARSET_ISO_8859_1);
////        Long deviceId = nettyTcpServerHandler.driverContext.getDeviceIdByName(deviceName);
//        String hexKey = ByteBufUtil.hexDump(byteBuf, 22, 1);
        //获取监听的端口号
        SocketAddress socketAddress = context.channel().localAddress();
        String s = socketAddress.toString();
        String[] addrAndPort = s.split(":");
        String dataStr = new String(dataByteArray,"UTF-8");

        switch (Integer.parseInt(addrAndPort[1])) {
            case 6281:
                Channel channel = context.channel();

//                String jsonStr = byteBuf.toString(CharsetUtil.CHARSET_UTF_8);

                JSONObject jsonObj = JSONObject.parseObject(new String(dataByteArray));
                ActionEnum action = ActionEnum.valueOf("FACERECOGNITION");

                switch (action) {
                    // 人脸识别
                    case FACERECOGNITION:
                        String results = jsonObj.getString("results");
                        List<FaceRecognitionResultVO> faceRecognitionResultVOList = JSONObject.parseArray(results, FaceRecognitionResultVO.class);
                        Map<String,Object> faceMap = new ConcurrentHashMap<>();
                        for (FaceRecognitionResultVO vo: faceRecognitionResultVOList) {
                            String personId = vo.getPerson_id();

                            // TODO 根据personId查询当前所识别的人是否在黑名单中
                            BlacklistEntity  blacklistEntity= blacklistService.getBlackListByPersonId(personId);
                            if ( blacklistEntity != null ) {

                                faceMap.put("faceRecognitionResultVO",vo);
                                faceMap.put("blacklistEntity",blacklistEntity);
                                //TODO 推送人脸报警信息到前台
                                wsHandler.sendMsg(faceMap);
                            }
                        }

                        break;

                        // 异物
                    case FOREIGNOBJECT:
                        break;
                    default:
                        break;

                }


//                channel.writeAndFlush("I got");

                break;
            case 6282:

                break;
            case 6283:

                break;
            default:
        }


        //TODO 简单的例子，用于存储channel，然后配合write接口实现向下发送数据
//        NettyTcpServer.deviceChannelMap.put(deviceId, context.channel());

//        List<PointValue> pointValues = new ArrayList<>();
//        Map<Long, Map<String, AttributeInfo>> pointInfoMap = nettyTcpServerHandler.driverContext.getDevicePointInfoMap().get(deviceId);
//        for (Long pointId : pointInfoMap.keySet()) {
//            Point point = nettyTcpServerHandler.driverContext.getDevicePoint(deviceId, pointId);
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
//                                nettyTcpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(altitude)));
//                        break;
//                    case "速度":
//                        double speed = byteBuf.getDouble(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(speed),
//                                nettyTcpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(speed)));
//                        break;
//                    case "液位":
//                        long level = byteBuf.getLong(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(level),
//                                nettyTcpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(level)));
//                        break;
//                    case "方向":
//                        int direction = byteBuf.getInt(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(direction),
//                                nettyTcpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(direction)));
//                        break;
//                    case "锁定":
//                        boolean lock = byteBuf.getBoolean(start);
//                        pointValue = new PointValue(deviceId, pointId, String.valueOf(lock),
//                                nettyTcpServerHandler.driverService.convertValue(deviceId, pointId, String.valueOf(lock)));
//                        break;
//                    case "经纬":
//                        String lalo = byteBuf.toString(start, end, CharsetUtil.CHARSET_ISO_8859_1).trim();
//                        pointValue = new PointValue(deviceId, pointId, lalo,
//                                nettyTcpServerHandler.driverService.convertValue(deviceId, pointId, lalo));
//                        break;
//                    default:
//                        break;
//                }
//                if (null != pointValue) {
//                    pointValues.add(pointValue);
//                }
//            }
//        }
//        nettyTcpServerHandler.driverService.pointValueSender(pointValues);
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

//    //连接处于活动状态
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        Channel channel = ctx.channel();
//        System.out.println(channel.remoteAddress() +" 上线了");
//    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() +" 下线了");
    }

    public static String getIPString(ChannelHandlerContext ctx){
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1, colonAt);
        return ipString;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // TODO Auto-generated method stub
        super.userEventTriggered(ctx, evt);

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            if (event.state().equals(IdleState.READER_IDLE)) {
                log.debug("------长期未收到服务器反馈数据------");
//                String loginMsg = "test server";//Login.login("admin", "admin");
                ByteBuf sendData = ByteUtils.editSendData(Common.CommandType.DATA,null);
                log.debug("------发送数据------" + sendData+"\r\n");
                ctx.writeAndFlush(sendData);

                //根据具体的情况 在这里也可以重新连接
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                log.debug("------长期未向服务器发送数据 发送心跳------");
                log.debug("------发送心跳包------" + "[LinkTest]\r\n");
//                ctx.writeAndFlush(CodeChange.StringToBuff("[LinkTest]"));
                String sendStr = "heart beat link test";
                ctx.writeAndFlush(getSendByteBuf(sendStr.getBytes()));

            } else if (event.state().equals(IdleState.ALL_IDLE)) {

            }

        }
    }

    private ByteBuf getSendByteBuf(byte[] bytes){

//        return Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
//                new CustomProtocol(123456L,"pong").toString(),CharsetUtil.UTF_8));
        return Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                bytes));
    }

    /**
     * 将byte数组转换为整数
     */
    private static int bytesToInt(byte[] bs) {
        int a = 0;
        for (int i = bs.length - 1; i >= 0; i--) {
            a += bs[i] * Math.pow(0xFF, bs.length - i - 1);
        }
        return a;
    }




}