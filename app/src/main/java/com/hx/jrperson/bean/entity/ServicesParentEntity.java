package com.hx.jrperson.bean.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 项目列表 一级数据实体类
 * Created by ge on 2016/3/30.
 */
public class ServicesParentEntity implements Serializable{


    /**
     * code : 0
     * time : 0
     * message : null
     * dataMap : {"services":[{"update_id":1,"srv_code":1008,"price_min":0,"service_type":1,"unit":"","update_time":1459217471000,"decription":"花洒喷头","create_id":1,"service":"花洒喷头","service_id":16,"parent_code":1001,"adjustable":0,"price_max":0,"status":1}]}
     */

    private int code;
    private long time;
    private Object message;
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

    public static class DataMapBean implements Serializable{
        /**
         * update_id : 1
         * srv_code : 1008
         * price_min : 0
         * service_type : 1
         * unit :
         * update_time : 1459217471000
         * decription : 花洒喷头
         * create_id : 1
         * service : 花洒喷头
         * service_id : 16
         * parent_code : 1001
         * adjustable : 0
         * price_max : 0
         * status : 1
         */

        private List<ServicesBean> services;

        public List<ServicesBean> getServices() {
            return services;
        }

        public void setServices(List<ServicesBean> services) {
            this.services = services;
        }

        public static class ServicesBean implements Serializable{
            private int update_id;
            private int srv_code;
            private int price_min;
            private int service_type;
            private String unit;
            private long update_time;
            private String decription;
            private int create_id;
            private String service;
            private int service_id;
            private int parent_code;
            private int adjustable;
            private int price_max;
            private int status;

            public int getUpdate_id() {
                return update_id;
            }

            public void setUpdate_id(int update_id) {
                this.update_id = update_id;
            }

            public int getSrv_code() {
                return srv_code;
            }

            public void setSrv_code(int srv_code) {
                this.srv_code = srv_code;
            }

            public int getPrice_min() {
                return price_min;
            }

            public void setPrice_min(int price_min) {
                this.price_min = price_min;
            }

            public int getService_type() {
                return service_type;
            }

            public void setService_type(int service_type) {
                this.service_type = service_type;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public long getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(long update_time) {
                this.update_time = update_time;
            }

            public String getDecription() {
                return decription;
            }

            public void setDecription(String decription) {
                this.decription = decription;
            }

            public int getCreate_id() {
                return create_id;
            }

            public void setCreate_id(int create_id) {
                this.create_id = create_id;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public int getService_id() {
                return service_id;
            }

            public void setService_id(int service_id) {
                this.service_id = service_id;
            }

            public int getParent_code() {
                return parent_code;
            }

            public void setParent_code(int parent_code) {
                this.parent_code = parent_code;
            }

            public int getAdjustable() {
                return adjustable;
            }

            public void setAdjustable(int adjustable) {
                this.adjustable = adjustable;
            }

            public int getPrice_max() {
                return price_max;
            }

            public void setPrice_max(int price_max) {
                this.price_max = price_max;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
