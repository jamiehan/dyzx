package com.company.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.common.constant.Common;
import com.company.project.common.utils.ByteUtils;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.RobotEntity;
import com.company.project.entity.SysUser;
import com.company.project.service.HttpSessionService;
import com.company.project.service.RobotService;
import com.company.project.service.netty.tcp.NettyTcpServer;
import com.company.project.service.netty.udp.NettyUdpServer;
import com.company.project.service.netty.udp.NettyUdpServerHandler;
import com.company.project.vo.resp.PTZControllerVO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/robot")
@Api(tags = "机器人操控")
public class RobotOperationController {

    @Resource
    private HttpSessionService httpSessionService;

    @PostMapping("/cameractrl")
    @ApiOperation(value = "控制机器人摄像头接口")
    public DataResult cameraCtrl(@RequestBody PTZControllerVO vo) {

        RobotEntity currentRobot = httpSessionService.getCurrentRobot();
        if( currentRobot == null || StringUtils.isEmpty(currentRobot.getPcIp()) ) {
            return DataResult.fail("当前机器人信息获取失败。");
        }

        // 根据机器人IP地址，给摄像头控制的对应指令
        Channel channel = NettyTcpServer.deviceChannelMap.get(currentRobot.getPcIp());

        PTZControllerVO ptzControllerVO = new PTZControllerVO();
        ptzControllerVO.setAction(vo.getAction());
        ptzControllerVO.setPtzControlType(vo.getPtzControlType());
        ptzControllerVO.setPtzStepSize(vo.getPtzStepSize());
        // 0: running 1:stop
        ptzControllerVO.setPtzStop(vo.getPtzStop());

        channel.writeAndFlush(ByteUtils.editSendData(Common.CommandType.CMD, ptzControllerVO));
        //通过access_token拿userId
//        String userId = httpSessionService.getCurrentUserId();
        DataResult result = DataResult.success();
//        result.setData(homeService.getHomeInfo(userId));


        return result;
    }




}
