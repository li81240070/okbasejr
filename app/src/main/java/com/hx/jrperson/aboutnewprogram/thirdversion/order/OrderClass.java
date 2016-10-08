package com.hx.jrperson.aboutnewprogram.thirdversion.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class OrderClass {

    /**
     * code : 200
     * time : 1474859846072
     * message : null
     * dataMap : {"order_num":1,"orders":[{"order_id":"5037","mobile":"15940909208","post_code":"210204","address":" 辽宁省 大连市 沙河口区高新园区硅谷假日40号2单元602","comment":"今天如果不行就算了","service":"空调","order_status":"0","order_time":1471063493000,"adjustable":"1","appoint_time":1471063800000,"images":null,"price":"289.00","timestamp":null}]}
     */

    private int code;
    private long time;
    private Object message;
    /**
     * order_num : 1
     * orders : [{"order_id":"5037","mobile":"15940909208","post_code":"210204","address":" 辽宁省 大连市 沙河口区高新园区硅谷假日40号2单元602","comment":"今天如果不行就算了","service":"空调","order_status":"0","order_time":1471063493000,"adjustable":"1","appoint_time":1471063800000,"images":null,"price":"289.00","timestamp":null}]
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
        private int order_num;
        /**
         * order_id : 5037
         * mobile : 15940909208
         * post_code : 210204
         * address :  辽宁省 大连市 沙河口区高新园区硅谷假日40号2单元602
         * comment : 今天如果不行就算了
         * service : 空调
         * order_status : 0
         * order_time : 1471063493000
         * adjustable : 1
         * appoint_time : 1471063800000
         * images : null
         * price : 289.00
         * timestamp : null
         */

        private List<OrdersBean> orders;

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean implements Serializable {
            private String order_id;
            private String mobile;
            private String post_code;
            private String address;
            private String comment;
            private String service;
            private String order_status;
            private long order_time;
            private String adjustable;
            private long appoint_time;
            private Object images;
            private String price;
            private Object timestamp;

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

            public long getOrder_time() {
                return order_time;
            }

            public void setOrder_time(long order_time) {
                this.order_time = order_time;
            }

            public String getAdjustable() {
                return adjustable;
            }

            public void setAdjustable(String adjustable) {
                this.adjustable = adjustable;
            }

            public long getAppoint_time() {
                return appoint_time;
            }

            public void setAppoint_time(long appoint_time) {
                this.appoint_time = appoint_time;
            }

            public Object getImages() {
                return images;
            }

            public void setImages(Object images) {
                this.images = images;
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
        }
    }
}
