package com.company.project.vo.resp;

import lombok.Data;

/**
 * 机器人云台控制VO
 */
@Data
public class PTZControllerVO {

    /* 机器人ID */
    private String robotId;

    /* 机器人IP地址 */
    private String robotIpAddress;

    /* 固定值 ‘PTZ’ */
    private String action;
    /* 0 up；1 down；2 left；3 right；4 zoom in;
       5 zoom  out;10 right top;11 left down;
       12 left top;13 right down;*/
    private int ptzControlType;
    /* 步长 */
    private int ptzStepSize;
    /* 摄像头是否持续运动 （0: 运行 1：停止运行） */
    private int ptzStop;

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getRobotIpAddress() {
        return robotIpAddress;
    }

    public void setRobotIpAddress(String robotIpAddress) {
        this.robotIpAddress = robotIpAddress;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPtzControlType() {
        return ptzControlType;
    }

    public void setPtzControlType(int ptzControlType) {
        this.ptzControlType = ptzControlType;
    }

    public int getPtzStepSize() {
        return ptzStepSize;
    }

    public void setPtzStepSize(int ptzStepSize) {
        this.ptzStepSize = ptzStepSize;
    }

    public int getPtzStop() {
        return ptzStop;
    }

    public void setPtzStop(int ptzStop) {
        this.ptzStop = ptzStop;
    }
}
