//package com.company.project.service.impl;
//
//import com.company.project.service.AsyncTaskService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
///**
// * @description
// */
//@Service
//public class AsyncTaskServiceImpl implements AsyncTaskService {
////    @Async
////    public void executeAsyncTask(int i) {
////        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：" + i);
////    }
//
//    private Process process;
//
//    @Value("${robot.ffmpeg.path}")
//    private String ffmpegPath;
//
//    @Value("${robot.rtsp.src.addr}")
//    private String rtspAddr;
//
//    @Value("${robot.rtsp.src.user}")
//    private String rtspUser;
//
//    @Value("${robot.rtsp.src.pwd}")
//    private String rtspPwd;
//
//    @Value("${robot.rtsp.dest.addr}")
//    private String destAddr;
//
//    @Value("${robot.rtsp.dest.port}")
//    private String destPort;
//
//    /**
//     * 从摄像头往服务器推流
//     * @param ipAddr
//     * @return
//     */
//    @Async
//    public Integer pushVideoAsRTSP(String ipAddr){
//
//        int flag = -1;
//        // ffmpeg位置，最好写在配置文件中
////        String ffmpegPath = "E:\\webset\\ffmpeg\\ffmpeg-20200213-6d37ca8-win64-static\\bin\\";
////        String ffmpegPath = "D:\\software\\ffmpeg\\bin\\";
//        try {
//            // 视频切换时，先销毁进程，全局变量Process process，方便进程销毁重启，即切换推流视频
//            if(process != null){
//                process.destroy();
//                System.out.println(">>>>>>>>>>推流视频切换<<<<<<<<<<");
//            }
//            String videoSrc = "rtsp://" + rtspUser + ":" + rtspPwd + "@" + ipAddr;
////            String videoDest = "rtmp://127.0.0.1:1935/live/test";
//            String videoDest = "http://" + destAddr + ":" + destPort + "/rtsp/receive";
//            // cmd命令拼接，注意命令中存在空格
//            String command = ffmpegPath; // ffmpeg位置
//            command += "ffmpeg "; // ffmpeg开头，-re代表按照帧率发送，在推流时必须有
//            command += " -i \"" + videoSrc + "\""; // 指定要推送的视频
//            command += " -q 0 -f mpegts -codec:v mpeg1video -s 200x150 " + videoDest; // 指定推送服务器，-f：指定格式
//
////            command = ffmpegPath + "ffmpeg -i " + videoSrc + " -vcodec copy -acodec copy -f flv " + videoDest;
//            System.out.println("ffmpeg推流命令：" + command);
//
//            // 运行cmd命令，获取其进程
//            process = Runtime.getRuntime().exec(command);
//            // 输出ffmpeg推流日志
//            BufferedReader br= new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                System.out.println("视频推流信息[" + line + "]");
//            }
//            flag = process.waitFor();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return flag;
//    }
//}