package com.hx.jrperson.bean.entity;

import java.io.Serializable;

/**
 * 登陆成功返回数据
 * Created by ge on 2016/4/1.
 */
public class LoginEntity implements Serializable{


    /**
     * code : 200
     * time : 1459496951596
     * message : null
     * dataMap : {"mobile":"13664266902","userId":36,"token":"cd4b414f-a9bc-45c5-832a-3c9d722e2267"}
     */

    private int code;
    private long time;
    private Object message;
    /**
     * mobile : 13664266902
     * userId : 36
     * token : cd4b414f-a9bc-45c5-832a-3c9d722e2267
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
        private String mobile;
        private int userId;
        private String token;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
