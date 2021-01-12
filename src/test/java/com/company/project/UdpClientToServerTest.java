package com.company.project;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClientToServerTest {

    public static final String SERVER_HOSTNAME = "192.168.1.135";
    // 服务器端口
    public static final int SERVER_PORT = 1233;
    // 本地发送端口
    public static final int LOCAL_PORT = 10001;


    @Test
    public void udpClient(){
        try {
            // 1，创建udp服务。通过DatagramSocket对象。
            DatagramSocket socket = new DatagramSocket(LOCAL_PORT);
            // 2，确定数据，并封装成数据包。DatagramPacket(byte[] buf, int length, InetAddress
            // address, int port)


            byte[] buf = "你好，世界".getBytes();
            DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(SERVER_HOSTNAME),
                    SERVER_PORT);
            // 3，通过socket服务，将已有的数据包发送出去。通过send方法。
            socket.send(dp);
            // 4，关闭资源。
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
