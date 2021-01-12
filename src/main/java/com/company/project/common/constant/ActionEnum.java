package com.company.project.common.constant;

public enum ActionEnum {
/*
    CarData	车辆车牌
    ThermalImage	热像仪
    Safetyhelmet	安全帽
    CellGate	单元门
    FrockStandard	工装标准化
    FaceRecognition	人脸识别
    EquipmentDamage	设备脱落损坏
    ForeignObject	异物检测
    DustDetection	灰尘检测

    CARDATA	车辆车牌
    THERMALIMAGE	热像仪
    SAFETYHELMET	安全帽
    CELLGATE	单元门
    FROCKSTANDARD	工装标准化
    FACERECOGNITION	人脸识别
    EQUIPMENTDAMAGE	设备脱落损坏
    FOREIGNOBJECT	异物检测
    DUSTDETECTION	灰尘检测

 */
    /*CARDATA("车辆车牌",""),
    THERMALIMAGE("热像仪",""),
    SAFETYHELMET("安全帽",""),
    CELLGATE("单元门",""),
    FROCKSTANDARD("工装标准化",""),
    FACERECOGNITION("人脸识别","recog_result"),
    EQUIPMENTDAMAGE("设备脱落损坏",""),
    FOREIGNOBJECT("异物检测",""),
    DUSTDETECTION("灰尘检测","");*/

    CARDATA(""),
    THERMALIMAGE(""),
    SAFETYHELMET(""),
    CELLGATE(""),
    FROCKSTANDARD(""),
    FACERECOGNITION("recog_result"),
    EQUIPMENTDAMAGE(""),
    FOREIGNOBJECT(""),
    DUSTDETECTION("");

//    private String name;

    private String code;


    ActionEnum(String code){
        this.code = code;
    }

/*    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }









}
