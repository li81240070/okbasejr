package com.hx.jrperson.bean.entity;

/**
 * 取消订单次数
 * Created by ge on 2016/4/8.
 */
public class CancleCountEntity {


    /**
     * code : 200
     * time : 1460373223403
     * message : null
     * dataMap : {"cancel_count":1}
     */

    private int code;
    private long time;
    private Object message;
    /**
     * cancel_count : 1
     */

    private DataMapBean dataMap;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public DataMapBean getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMapBean dataMap) {
        this.dataMap = dataMap;
    }

    public static class DataMapBean {
        private int cancel_count;

        public int getCancel_count() {
            return cancel_count;
        }

        public void setCancel_count(int cancel_count) {
            this.cancel_count = cancel_count;
        }
    }
}
