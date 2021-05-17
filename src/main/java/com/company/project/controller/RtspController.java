package com.company.project.controller;

import com.company.project.common.config.WsHandler;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.RobotEntity;
import com.company.project.service.HttpSessionService;
import com.company.project.service.netty.tcp.NettyTcpServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.BinaryMessage;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Controller
@RequestMapping("/rtsp")
public class RtspController {

    @Resource
    private WsHandler wsHandler;

    @Resource
    private HttpSessionService httpSessionService;

    @RequestMapping("/receive")
    @ResponseBody
    public String receive(HttpServletRequest request, Object response) {
//        System.out.println("method:" + request.getMethod());
        log.debug("method:" + request.getMethod());
        try {
            ServletInputStream inputStream = request.getInputStream();
            int len = -1;
            while ((len =inputStream.available()) !=-1) {
                byte[] data = new byte[len];
                inputStream.read(data);
//                for (int i = 0; i < data.length; i++) {
//                    System.out.print(data[i] + " ");
//                }
//                System.out.println("");
//                System.out.println(len);
//                System.out.println("--------------------------------------------------------");
//                wsHandler.sendVideo(data);

//                RobotEntity currentRobot = httpSessionService.getCurrentRobot();
//                if( currentRobot == null || StringUtils.isEmpty(currentRobot.getPcIp()) ) {
//                    log.debug("RtspController类中,当前机器人信息获取失败。");
//                    return null;
//                }
                // 根据机器人IP地址，给摄像头控制的对应指令
                //Channel channel = NettyTcpServer.deviceChannelMap.get(currentRobot.getPcIp()+":6283");
                //Channel channel = NettyTcpServer.deviceChannelMap.get("192.168.1.215:6283");
                //BinaryMessage binaryMessage = new BinaryMessage(data);
                //ByteBuf buf = Unpooled.copiedBuffer(data);
                //channel.writeAndFlush(buf);
                wsHandler.sendVideo(data);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.debug("RtspController类中视频流发送异常");
        }
//        System.out.println("over");
        return "1";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "1";
    }



}
