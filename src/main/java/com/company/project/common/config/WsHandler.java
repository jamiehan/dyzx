package com.company.project.common.config;

import com.alibaba.fastjson.JSON;
import com.company.project.service.BlacklistService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import javax.annotation.Resource;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WsHandler extends BinaryWebSocketHandler {

    private static int LOCAL_PORT = 10001;

    private static int CLIENT_AUDIO_PORT = 1233;

    /**
     * 存放所有在线的客户端
     */
    private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    public Map<String, String> audioAddressMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("新的加入");
        clients.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        clients.remove(session.getId());
    }

    public void sendAudio(byte[] data, String audioAddress) {
        BinaryMessage binaryMessage = new BinaryMessage(data);
        for (Map.Entry<String, WebSocketSession> sessionEntry : clients.entrySet()) {
            try {
                WebSocketSession session = sessionEntry.getValue();
                if (session.isOpen() && session.getUri().getPath().endsWith("/onekeyalarm")) {
                    //session.sendMessage(binaryMessage);

                    DatagramSocket socket = new DatagramSocket();
                    InetAddress address = session.getRemoteAddress().getAddress();

                    java.net.DatagramPacket dp = new java.net.DatagramPacket(data, data.length, address,
                            CLIENT_AUDIO_PORT);
                    System.out.println("一键报警session的address信息：" + address.toString());
                    System.out.println("声音已从机器人发送到客户端：客户端IP地址：" + address.getHostAddress() + ",端口号：" + CLIENT_AUDIO_PORT);
                    // 3，通过socket服务，将已有的数据包发送出去。通过send方法。
                    socket.send(dp);
                    // 4，关闭资源。
                    socket.close();

                    audioAddressMap.put(session.getId(), audioAddress);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendVideo(byte[] data) {
        BinaryMessage binaryMessage = new BinaryMessage(data);
        for (Map.Entry<String, WebSocketSession> sessionEntry : clients.entrySet()) {
            try {
                WebSocketSession session = sessionEntry.getValue();
                if (session.isOpen() && session.getUri().getPath().endsWith("/video")) {
                    synchronized (session) {
                        session.sendMessage(binaryMessage);
                    }
                }
            } catch (Exception e) {
                System.out.println("发送视频异常");
                //e.printStackTrace();
            }
        }
    }

    /**
     * 发送人脸报警数据
     * @param data
     * @param <T>
     */
    public <T> void sendFaceAlarmMsg(Map<String,T> data) {
//        BinaryMessage binaryMessage = new BinaryMessage(null);
//        WebSocketMessage newMessage = new SocketMessage(data);
        TextMessage textMessage = new TextMessage(JSON.toJSONString(data));

        for (Map.Entry<String, WebSocketSession> sessionEntry : clients.entrySet()) {
            try {
                WebSocketSession session = sessionEntry.getValue();
                if (session.isOpen()) {
//                    session.sendMessage(binaryMessage);
                    synchronized (session) {
                        session.sendMessage(textMessage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T> void sendMsg(Map<String,T> data) {
//        BinaryMessage binaryMessage = new BinaryMessage(null);
//        WebSocketMessage newMessage = new SocketMessage(data);
        TextMessage textMessage = new TextMessage(JSON.toJSONString(data));

        for (Map.Entry<String, WebSocketSession> sessionEntry : clients.entrySet()) {
            try {
                WebSocketSession session = sessionEntry.getValue();
                if (session.isOpen()) {
//                    session.sendMessage(binaryMessage);
                    synchronized (session) {
                        session.sendMessage(textMessage);
                    }
                }
            } catch (Exception e) {
                System.out.println("发送消息异常");
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所有连接的客户端
     * @return
     */
    public Map<String, WebSocketSession> getClients() {
        return clients;
    }
}
