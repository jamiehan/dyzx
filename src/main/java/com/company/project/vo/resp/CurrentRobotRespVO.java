package com.company.project.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CurrentRobotRespVO {

    @ApiModelProperty(value = "机器人编号")
    private String code;
    @ApiModelProperty(value = "机器人IP")
    private String ipAddress;
    @ApiModelProperty(value = "云台摄像机IP")
    private String cameraIp;
    @ApiModelProperty(value = "工控机IP")
    private String pcIp;
    @ApiModelProperty(value = "型号")
    private String type;
    @ApiModelProperty(value = "在线状态")
    private String state;
    @ApiModelProperty(value = "工作地点")
    private String location;
    @ApiModelProperty(value = "所属单位")
    private String company;
    @ApiModelProperty(value = "联系人")
    private String contact;
    @ApiModelProperty(value = "电话")
    private String telephone;
}
