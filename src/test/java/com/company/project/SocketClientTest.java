package com.company.project;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClientTest {

    @Test
    public void SocketClientTest(){

//        // 要连接的服务端IP地址和端口
//        String host = "192.168.1.163";
//        int port = 1010;
//        // 与服务端建立连接
//        Socket socket = null;
//        try {
//            socket = new Socket(host, port);
//
//            // 建立连接后获得输出流
//            OutputStream outputStream = socket.getOutputStream();
//            String message="ZZ\u0001ﾃ     \u0002\u001A{\"Robot_ID\":\"000001\",\"Info\":{\"Robot_X\":9.4,\"Robot_Y\":-4.01,\"Robot_Yaw\":2.80148,\"Robot_Stat\":1,\"Robot_Cap\":73,\"CameraIp\":\"192.168.10.64\",\"Locat_Port\":32847,\"iport\":\"\",\"Env_Noise\":0,\"GasInfo\":{\"Gas_Smell\":0,\"Gas_PM2.5\":0,\"Gas_CO\":0,\"Gas_jiawan\":0,\"Gas_jiaquan\":0,\"Gas_PM10\":0,\"Env_temp\":22.799999237060547,\"Env_humi\":21.600000381469727},\"SelfTestInfo\":{\"sensor_core\":true,\"motor_core_right\":true,\"motor_core_left\":true,\"radio_core\":true,\"power_core\":true,\"depth_camera\":true,\"laser\":true,\"IMU\":true,\"CAN\":true,\"internet\":true,\"odom\":true}}}ﾱ￨ﾥﾥ";
//            socket.getOutputStream().write(message.getBytes("UTF-8"));
//
////            InputStream inputStream = socket.getInputStream();
////            byte[] byteArr = new byte[1000];
////            inputStream.read(byteArr);
////            System.out.println(byteArr.toString());
////            inputStream.close();
//            outputStream.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
