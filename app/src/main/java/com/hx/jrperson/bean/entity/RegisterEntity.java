package com.hx.jrperson.bean.entity;

/**
 * 用户注册返回信息实体类
 * Created by ge on 2016/3/29.
 */
public class RegisterEntity {

    /**
     * code : 200
     * time : 1411111111111
     * message : null
     * dataMap : {}
     */

    private int code;
    private long time;
    private Object message;
    /**
     * vaildCode : 979775
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

    public void setTime(int time) {
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
        private String vaildCode;

        public String getVaildCode() {
            return vaildCode;
        }

        public void setVaildCode(String vaildCode) {
            this.vaildCode = vaildCode;
        }
    }
}
