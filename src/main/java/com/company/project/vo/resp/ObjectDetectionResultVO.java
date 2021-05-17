package com.company.project.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * 目标物体检测结果VO
 */
@Data
public class ObjectDetectionResultVO {

    private String action;

    private String Robot_ID;

    private String Pos_ID;

    private String Flag;

    private String Type;

    private String State;

    private String FlagM;

    private String timeStamp;

    private List<PersonInfoVO> Info;

    private String personNum;

    private String Image;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRobot_ID() {
        return Robot_ID;
    }

    public void setRobot_ID(String robot_ID) {
        Robot_ID = robot_ID;
    }

    public String getPos_ID() {
        return Pos_ID;
    }

    public void setPos_ID(String pos_ID) {
        Pos_ID = pos_ID;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getFlagM() {
        return FlagM;
    }

    public void setFlagM(String flagM) {
        FlagM = flagM;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<PersonInfoVO> getInfo() {
        return Info;
    }

    public void setInfo(List<PersonInfoVO> info) {
        Info = info;
    }

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
