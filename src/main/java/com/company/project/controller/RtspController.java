package com.company.project.controller;

import com.company.project.common.config.WsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
