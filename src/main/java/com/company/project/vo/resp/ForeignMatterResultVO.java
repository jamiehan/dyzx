package com.company.project.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * 异物结果VO
 */
@Data
public class ForeignMatterResultVO {

    private String action;

    private String Robot_ID;

    private String Pos_ID;

    private String Flag;

    private String Type;

    private String State;

    private String FlagM;

    private String timeStamp;
    //分值越小差异越大，一般小于0.5认为有差异
    private String EPD_Score;

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

    public String getEPD_Score() {
        return EPD_Score;
    }

    public void setEPD_Score(String EPD_Score) {
        this.EPD_Score = EPD_Score;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
