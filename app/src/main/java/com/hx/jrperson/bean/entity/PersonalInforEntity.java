package com.hx.jrperson.bean.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/31.
 */
public class PersonalInforEntity implements Serializable{


    /**
     * code : 200
     * time : 1459395872358
     * message : null
     * dataMap : {"nick_name":"hello world","custom_sign":"这个家伙很懒，什么都没留下~~","mobile":"13664266902","avatar":"0","vip_level":0}
     */

    private int code;
    private long time;
    private Object message;
    /**
     * nick_name : hello world
     * custom_sign : 这个家伙很懒，什么都没留下~~
     * mobile : 13664266902
     * avatar : 0
     * vip_level : 0
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

    public static class DataMapBean implements Serializable{
        private String nick_name;
        private String custom_sign;
        private String mobile;
        private String avatar;
        private int vip_level;

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getCustom_sign() {
            return custom_sign;
        }

        public void setCustom_sign(String custom_sign) {
            this.custom_sign = custom_sign;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getVip_level() {
            return vip_level;
        }

        public void setVip_level(int vip_level) {
            this.vip_level = vip_level;
        }
    }
}
