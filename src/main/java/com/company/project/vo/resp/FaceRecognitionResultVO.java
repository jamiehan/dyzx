package com.company.project.vo.resp;

import lombok.Data;

/**
 * 人脸识别结果VO
 */
@Data
public class FaceRecognitionResultVO {


    /*"detect_image": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD",
            "timecost": "-1893140613859",
            "camera_id": 0,
            "camera_name": "Senscape",
            "camera_ip": "192.168.10.64",
            "location": "rtsp://admin:dyzx2018@192.168.10.64:554/cam/realmonitor?channel=1&subtype=0",
            "long_timestamp": 130463290,
            "enlarge_factor": 1,
            "u_id": 23,
            "vip_level": 1,
            "distance": 0.1966015100479126,
            "confidence": 0.63686543703079224,
            "similarity": "S:99%",
            "del_uid_array": [],
            "rect_x": 1001,
            "rect_y": 197,
            "rect_width": 255,
            "rect_height": 307,
            "db_image": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAIBAQEBAQIBAQECAgICAgQDAgICAgUEBAMEBgUGBgYFBgYGBwkIBgcJBwYGCAsICQoKCgoKBggLDAsKD",
            "name": "杈圭寮",
            "type": 2,
            "person_id": "23"*/

    private String detect_image;

    private String timecost;

    private String camera_id;

    private String camera_name;

    private String camera_ip;

    private String location;

    private String long_timestamp;

    private String enlarge_factor;

    private String u_id;

    private String vip_level;

    private String distance;

    private String confidence;

    private String similarity;

    private String del_uid_array;

    private String rect_x;

    private String rect_y;

    private String rect_width;
    private String rect_height;

    private String db_image;

    private String name;

    private String type;

    private String person_id;

    public String getDetect_image() {
        return detect_image;
    }

    public void setDetect_image(String detect_image) {
        this.detect_image = detect_image;
    }

    public String getTimecost() {
        return timecost;
    }

    public void setTimecost(String timecost) {
        this.timecost = timecost;
    }

    public String getCamera_id() {
        return camera_id;
    }

    public void setCamera_id(String camera_id) {
        this.camera_id = camera_id;
    }

    public String getCamera_name() {
        return camera_name;
    }

    public void setCamera_name(String camera_name) {
        this.camera_name = camera_name;
    }

    public String getCamera_ip() {
        return camera_ip;
    }

    public void setCamera_ip(String camera_ip) {
        this.camera_ip = camera_ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLong_timestamp() {
        return long_timestamp;
    }

    public void setLong_timestamp(String long_timestamp) {
        this.long_timestamp = long_timestamp;
    }

    public String getEnlarge_factor() {
        return enlarge_factor;
    }

    public void setEnlarge_factor(String enlarge_factor) {
        this.enlarge_factor = enlarge_factor;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getVip_level() {
        return vip_level;
    }

    public void setVip_level(String vip_level) {
        this.vip_level = vip_level;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getDel_uid_array() {
        return del_uid_array;
    }

    public void setDel_uid_array(String del_uid_array) {
        this.del_uid_array = del_uid_array;
    }

    public String getRect_x() {
        return rect_x;
    }

    public void setRect_x(String rect_x) {
        this.rect_x = rect_x;
    }

    public String getRect_y() {
        return rect_y;
    }

    public void setRect_y(String rect_y) {
        this.rect_y = rect_y;
    }

    public String getRect_width() {
        return rect_width;
    }

    public void setRect_width(String rect_width) {
        this.rect_width = rect_width;
    }

    public String getRect_height() {
        return rect_height;
    }

    public void setRect_height(String rect_height) {
        this.rect_height = rect_height;
    }

    public String getDb_image() {
        return db_image;
    }

    public void setDb_image(String db_image) {
        this.db_image = db_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }
}
