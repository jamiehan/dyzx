package com.company.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.project.common.utils.RedisUtils;
import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.io.UnsupportedEncodingException;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
//        ((ByteBuf) msg).release(); // (3)
//        ByteBuf in = (ByteBuf) msg;
        try {
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
            ByteBuf buf = (ByteBuf)msg;
            buf.skipBytes(13);
            byte[] bytes = new byte[buf.readableBytes()-12];
            buf.getBytes(buf.readerIndex(),bytes);
            try {
                String jsonStr = new String(bytes,"UTF-8");
                JSONObject jsonObj = JSONObject.parseObject(jsonStr);
                String robotId = jsonObj.getString("Robot_ID");
//                System.out.println("robotId>>>>>>>>" + robotId);
//                String info = jsonObj.getString("Info");
//                System.out.println("info>>>>>>>>" + info);
//                JSONObject jsonInfoObj = JSONObject.parseObject(info);
//                String cameraIp = jsonInfoObj.getString("CameraIp");
//                System.out.println("cameraIp>>>>>>>>" + cameraIp);
                RedisUtils.set(robotId, jsonStr);

                ctx.writeAndFlush(jsonStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            for(int i = 8; i < buf.capacity()-4; i++) {
//                buf.get.getBytes(i);
//            }
//            String jsonStr = buf.toString(CharsetUtil.UTF_8);
//            JSONObject jsonObj = JSONObject.parseObject(jsonStr);
//            String robotId = jsonObj.getString("Robot_ID");

//            byte[] req = new byte[buf.readableBytes()];
//            buf.readBytes(req);
//            String body = new String(req,"UTF-8");
//            for(int i = 0; i < buf.readableBytes(); i++) {
//                buf.getBytes(i);
//            }
//            System.out.println("robotId>>>>>>>>" + robotId);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}