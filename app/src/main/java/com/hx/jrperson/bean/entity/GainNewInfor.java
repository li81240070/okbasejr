package com.hx.jrperson.bean.entity;

import java.io.Serializable;

/**
 * Created by lxx on 5/16/16.
 */
public class GainNewInfor implements Serializable {

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

        private String timeStamp;
        private boolean isHaveUpdate;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public boolean isHaveUpdate() {
            return isHaveUpdate;
        }

        public void setHaveUpdate(boolean haveUpdate) {
            isHaveUpdate = haveUpdate;
        }
    }

}
