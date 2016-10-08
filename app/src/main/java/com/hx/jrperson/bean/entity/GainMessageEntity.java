package com.hx.jrperson.bean.entity;

import java.io.Serializable;

/**
 * 是否有新消息返回数据
 * Created by ge on 5/16/16.
 */
public class GainMessageEntity implements Serializable{

    private int code;
    private String message;
    private long time;
    private DataMapBean dataMap;

    public DataMapBean getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMapBean dataMap) {
        this.dataMap = dataMap;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class DataMapBean implements Serializable {

        private int activityId;
        private String activityPictureUrl;

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public String getActivityPictureUrl() {
            return activityPictureUrl;
        }

        public void setActivityPictureUrl(String activityPictureUrl) {
            this.activityPictureUrl = activityPictureUrl;
        }
    }


}
