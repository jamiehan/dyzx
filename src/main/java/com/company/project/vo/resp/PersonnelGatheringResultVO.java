package com.company.project.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * 人员聚集结果VO
 */
@Data
public class PersonnelGatheringResultVO {

    /*{
        "action":       "Object",
            "Robot_ID":     "",
            "Pos_ID":       0,
            "Flag": 1,
            "Type": 0,
            "State":        0,
            "FlagM":        0,
            "timeStamp":    0,
            "Info": [],
        "personNum":    0,
            "Image":        "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAIBAQEBAQIBAQECAgICAgQDAgICAgUEBAMEBgUGBgYFBgYGBwkIBgcJBwYGCAsICQoKCgoKBggLDAsKDAkKCgr/2wBDAQICAgICAgUDAwUKBwYHCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgr/wAARCAEsAZADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD5Dh+Btj5v+vuak1L9nXybXEE8kkf+s8qveIdHg1K1kvrGCX91N/z2/wBVVe88Na59l8/7D9ps4v8AXTeTL+6ir5/6zWPqfq1A+Y/FXwN1XTbW4ngg+0+VD5nlQ/62vO5rzSv+W9hexf8AbGvtT/hFZ5pZIPIljuPJlk/5518vza98ObOL7D4q8VfYfK8r979jluf/AEVXdhqvtjzauGPP5ofDk0sc8GreXJ/02hlr0jwT5E0sZsfEdtL+5rk9e8N2J164uP8AhMdJsY5Zv9DtLvzfN/79V654P8B+HIdL/wCJ5PpsWoeTF51p9si82u2nsc2JpHB+PJvEem+Xrlj/AKz/AFcM0NR6D+0J8YtBi/4/vN/c/wDPnFX0Jpvwx8KzeDbOx+wxS/vpZJovJ/1Vc3qXwl8Kzf6/Srb97/0x8uuKrjqJ20sNWPO9H/bL+JvhvWbfVfsFjcyWsPl+Td2flf8AoqWvUPgn+3t4q+JHi6z+HNj8CPtuoS/8ttO1jyov/IsVcvqXwB0M2El95H7v/rtUn7Lv7PfhzUv2kvA9hrljqUul3Xiq1ttY060vJY5ZbXzf3v72L/plWdWrRrUTX2Vb2x9WfD2b4qeNvFsfhyx/Ze8f6leRQ/aZv7Es7W9iii/6ay+bFX6Gfsx+Cb7wH+zxp/gfXII4tQ0vwTayalaTTfvbWWXzbqWL91/zyili82vkv4D/ALVH7KHw9l+JEHwr8R2UesXXiS/07R7SGzuvKsLCWK6i/dSy/wCtii82KvrCz8Sfsvf8K0t4PB3xi8LeINYv7y1try7l8VRRyy+bLF5v/XL915tZYbYMThq3/Po7T/hFfFWm/wBh6rPquiaTJpcMX2OWW88zypfK/e1uXn/CY/E6LT54bGLTdHsP3dndw/6T5v8A01l/55f9cv8A0VXD+CdBn+M11c
*/

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
