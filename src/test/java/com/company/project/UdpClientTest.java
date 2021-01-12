package com.company.project;

import com.company.project.service.netty.udp.NettyUdpClient;
import com.company.project.service.netty.udp.NettyUdpClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sound.sampled.*;
import java.net.InetSocketAddress;

@Slf4j
public class UdpClientTest {
//    private static final Logger logger = Logger.getLogger(UdpServerMainTest.class);

    private static final String host = "192.168.1.235";

    private static final int port = 1233;

//    public static void main(String[] args) {
//        try {
//            UdpServer.getInstance().start(host, port);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void nettyClientSendTest() throws Exception{
//        NettyUdpClient.getInstance().start();
////        int i = 0;
////        while( i < 10){
//        NettyUdpClientHandler.sendMessage(new String("你好UdpServer"), new InetSocketAddress(host,port));
////            log.info(i+" client send message is: 你好UdpServer");
////            i++;
////        }

//        play();
    }

    //播放音频文件
    public static void play() {
        try {
            AudioFormat audioFormat =
//                    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F,
//                    8, 1, 1, 44100F, false);
//                    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100F, 16, 2, 4,
//                            44100F, true);
                    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,11025F, 8, 1, 1,
                            11025F, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            final SourceDataLine sourceDataLine;
            info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);
            targetDataLine.start();
            sourceDataLine.start();
            FloatControl fc=(FloatControl)sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            double value=2;
            float dB = (float)
                    (Math.log(value==0.0?0.0001:value)/Math.log(10.0)*20.0);
            fc.setValue(dB);
            int nByte = 0;
            final int bufSize=4100;
            byte[] buffer = new byte[bufSize];
            while (nByte != -1) {
                //System.in.read();
                nByte = targetDataLine.read(buffer, 0, bufSize);
                sourceDataLine.write(buffer, 0, nByte);
            }
            sourceDataLine.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
