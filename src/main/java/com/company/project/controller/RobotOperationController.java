package com.company.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.common.utils.ByteUtils;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.SysUser;
import com.company.project.service.netty.tcp.NettyTcpServer;
import com.company.project.service.netty.udp.NettyUdpServer;
import com.company.project.service.netty.udp.NettyUdpServerHandler;
import com.company.project.vo.resp.PTZControllerVO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/robot")
@Api(tags = "机器人操控")
public class RobotOperationController {

    @PostMapping("/cameractrl")
    @ApiOperation(value = "控制机器人摄像头接口")
    public DataResult cameraCtrl(@RequestBody PTZControllerVO vo) {

        // 根据机器人IP地址，给摄像头控制的对应指令
        Channel channel = NettyTcpServer.deviceChannelMap.get(vo.getRobotIpAddress());

        PTZControllerVO ptzControllerVO = new PTZControllerVO();
        ptzControllerVO.setAction(vo.getAction());
        ptzControllerVO.setPtzControlType(vo.getPtzControlType());
        ptzControllerVO.setPtzStepSize(vo.getPtzStepSize());
        // 0: running 1:stop
        ptzControllerVO.setPtzStop(vo.getPtzStop());

        channel.writeAndFlush(editSendData(ptzControllerVO));
        //通过access_token拿userId
//        String userId = httpSessionService.getCurrentUserId();
        DataResult result = DataResult.success();
//        result.setData(homeService.getHomeInfo(userId));


        return result;
    }

    /**
     * 编辑发送数据
     * @param t
     * @param <T>
     * @return
     */
    public <T> ByteBuf editSendData(T t) {
        // 帧头
        byte[] header= new byte[]{0x5B,0x5B};
        // 方向（0x00:算法 -> 后台发送数据；0x01:后台 -> 算法发送数据）
        byte[] direct = new byte[]{0x01};
        // 类型 (0x90:命令帧)
        byte[] type = new byte[]{(byte)0x90};
        // CRC校验位
        byte[] crc = new byte[]{0x00,0x00};
        // 帧尾
        byte[] tail= new byte[]{(byte) 0xB5,(byte) 0xB5};
        // 获取
        String contStr = JSONArray.toJSON(t).toString();
        byte[] content = contStr.getBytes();
        int length = content.length;
        byte[] size = ByteBuffer.allocate(4).putInt(length).array();

        byte[] mergedData = ByteUtils.byteMergerAll(header, direct, type, size, content, crc, tail);

        return Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(mergedData));
    }


}
