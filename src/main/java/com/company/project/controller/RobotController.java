package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.entity.BlacklistEntity;
import com.company.project.service.HttpSessionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import com.company.project.common.utils.DataResult;

import com.company.project.entity.RobotEntity;
import com.company.project.service.RobotService;

import javax.annotation.Resource;
//import com.company.project.service.AsyncTaskService;


/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-01-12 10:42:11
 */
@Slf4j
@Controller
@RequestMapping("/")
public class RobotController {

    @Resource
    private RobotService robotService;

    @Resource
    private HttpSessionService httpSessionService;

    private Process process;

    @Value("${robot.ffmpeg.path}")
    private String ffmpegPath;

    @Value("${robot.rtsp.src.addr}")
    private String rtspAddr;

    @Value("${robot.rtsp.src.port}")
    private String rtspPort;

    @Value("${robot.rtsp.src.user}")
    private String rtspUser;

    @Value("${robot.rtsp.src.pwd}")
    private String rtspPwd;

    @Value("${robot.rtsp.src.channel}")
    private String rtspChannel;

    @Value("${robot.rtsp.src.stream}")
    private String rtspStream;

    @Value("${robot.rtsp.dest.addr}")
    private String destAddr;

    @Value("${robot.rtsp.dest.port}")
    private String destPort;

//    @Resource
//    private AsyncTaskService asyncTaskService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/robot")
    public String robot() {
        return "robot/list";
    }

    @ApiOperation(value = "获取当前机器人信息")
    @GetMapping("robot/getCurrentRobot")
    @ResponseBody
    public DataResult getCurrentRobotInfo(String robotCode){

        RobotEntity robotEntity = null;
        LambdaQueryWrapper<RobotEntity> queryWrapper = Wrappers.lambdaQuery();
        //根据机器人编号查询
        if(StringUtils.isNotBlank(robotCode)) {
            queryWrapper.eq(RobotEntity::getBianhao, robotCode);
            robotEntity = robotService.getOne(queryWrapper);

            //保存当前选中的机器人信息
            httpSessionService.setCurrentRobot(robotEntity);

       }

        return DataResult.success(robotEntity);
    }

    @ApiOperation(value = "获取当前机器人视频")
    @GetMapping("robot/getCurrentRobotVideo")
    @ResponseBody
    public DataResult getCurrentRobotVideo(String robotCode){

        RobotEntity robotEntity = httpSessionService.getCurrentRobot();

        if( robotEntity != null ) {
            this.pushVideoAsRTSP(robotEntity.getCameraIp());
        }

        return DataResult.success(robotEntity);
    }

    @ApiOperation(value = "新增")
    @PostMapping("robot/add")
    @RequiresPermissions("robot:add")
    @ResponseBody
    public DataResult add(@RequestBody RobotEntity robot){
        robotService.save(robot);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("robot/delete")
    @RequiresPermissions("robot:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        robotService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("robot/update")
    @RequiresPermissions("robot:update")
    @ResponseBody
    public DataResult update(@RequestBody RobotEntity robot){
        robotService.updateById(robot);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("robot/listByPage")
    @RequiresPermissions("robot:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody RobotEntity robot){
        Page page = new Page(robot.getPage(), robot.getLimit());
        LambdaQueryWrapper<RobotEntity> queryWrapper = Wrappers.lambdaQuery();
        //根据机器人编号模糊查询
        if(StringUtils.isNotBlank(robot.getBianhao())) {
            queryWrapper.like(RobotEntity::getBianhao, robot.getBianhao());
        }
        IPage<RobotEntity> iPage = robotService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    /*
     * 从摄像头往服务器推流
     * @param ipAddr
     * @return
     */
    public Integer pushVideoAsRTSP(String ipAddr){

        int flag = -1;
        // ffmpeg位置，最好写在配置文件中
//        String ffmpegPath = "E:\\webset\\ffmpeg\\ffmpeg-20200213-6d37ca8-win64-static\\bin\\";
//        String ffmpegPath = "D:\\software\\ffmpeg\\bin\\";
        try {
            // 视频切换时，先销毁进程，全局变量Process process，方便进程销毁重启，即切换推流视频
            if(process != null){
                process.destroy();
                log.debug(">>>>>>>>>>推流视频切换<<<<<<<<<<");
//                System.out.println(">>>>>>>>>>推流视频切换<<<<<<<<<<");
            }
//            String videoSrc = "rtsp://" + rtspUser + ":" + rtspPwd + "@" + ipAddr;
            //rtsp://192.168.1.10:554/user=admin&password=dyzx2018&channel=1&stream=0.sdp?
            String videoSrc = "rtsp://" + ipAddr + ":" + rtspPort + "/user=" + rtspUser + "&password="
                    + rtspPwd + "&channel=" + rtspChannel + "&stream=" + rtspStream + ".sdp?";
//            String videoDest = "rtmp://127.0.0.1:1935/live/test";
            String videoDest = "http://" + destAddr + ":" + destPort + "/rtsp/receive";
            // cmd命令拼接，注意命令中存在空格
            String command = ffmpegPath; // ffmpeg位置
            command += "ffmpeg "; // ffmpeg开头，-re代表按照帧率发送，在推流时必须有
            command += " -i \"" + videoSrc + "\""; // 指定要推送的视频
            command += " -q 0 -f mpegts -codec:v mpeg1video -s 400x300 " + videoDest; // 指定推送服务器，-f：指定格式

//            command = ffmpegPath + "ffmpeg -i " + videoSrc + " -vcodec copy -acodec copy -f flv " + videoDest;
//            System.out.println("ffmpeg推流命令：" + command);
            log.debug("ffmpeg推流命令：" + command);
            // 运行cmd命令，获取其进程
            process = Runtime.getRuntime().exec(command);
            // 输出ffmpeg推流日志
            BufferedReader br= new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                log.debug("视频推流信息[" + line + "]");
//                System.out.println("视频推流信息[" + line + "]");
            }
            flag = process.waitFor();
        }catch (Exception e){
//            e.printStackTrace();
            log.debug("RobotController类中视频推流失败。");
        }
        return flag;
    }

}
