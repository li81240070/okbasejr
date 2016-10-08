package com.hx.jrperson.bean.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 我的订单 实体类
 * Created by ge on 2016/4/1.
 */
public class MyOrdorEntity implements Serializable{


    /**
     * code : 200
     * time : 1459492471013
     * message : null
     * dataMap : {"orders":[{"order_id":"78","mobile":"13664266902","post_code":"210202","address":"回到家附近方法","comment":"就打击打击","service":null,"order_status":"1","order_time":null,"adjustable":"0","appoint_time":"1970-01-01 08:00:00.0","images":["/resource/ORDER/2016/3/31/428/ec99caba6cd00cd1","/resource/ORDER/2016/3/31/428/ec99caba6cd00cd1"],"price":"237.00","timestamp":null}]}
     */

    private int code;
    private long time;
    private String message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
         * order_id : 78
         * mobile : 13664266902
         * post_code : 210202
         * address : 回到家附近方法
         * comment : 就打击打击
         * service : null
         * order_status : 1
         * order_time : null
         * adjustable : 0
         * appoint_time : 1970-01-01 08:00:00.0
         * images : ["/resource/ORDER/2016/3/31/428/ec99caba6cd00cd1","/resource/ORDER/2016/3/31/428/ec99caba6cd00cd1"]
         * price : 237.00
         * timestamp : null
         */

        private List<OrdersBean> orders;

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean implements Serializable{
            private String order_id;
            private String mobile;
            private String post_code;
            private String address;
            private String comment;
            private String service;
            private String order_status;
            private Object order_time;
            private String adjustable;
            private String appoint_time;
            private String price;
            private Object timestamp;
            private List<String> images;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getPost_code() {
                return post_code;
            }

            public void setPost_code(String post_code) {
                this.post_code = post_code;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public Object getOrder_time() {
                return order_time;
            }

            public void setOrder_time(Object order_time) {
                this.order_time = order_time;
            }

            public String getAdjustable() {
                return adjustable;
            }

            public void setAdjustable(String adjustable) {
                this.adjustable = adjustable;
            }

            public String getAppoint_time() {
                return appoint_time;
            }

            public void setAppoint_time(String appoint_time) {
                this.appoint_time = appoint_time;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public Object getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(Object timestamp) {
                this.timestamp = timestamp;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }
    }
}
