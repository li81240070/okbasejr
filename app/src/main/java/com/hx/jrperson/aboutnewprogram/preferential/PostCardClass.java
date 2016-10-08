package com.hx.jrperson.aboutnewprogram.preferential;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 * 优惠券信息的具体数据类
 */
public class PostCardClass {


    /**
     * code : 200
     * time : 1474424807554
     * message : null
     * dataMap : {"coupons":[{"coupon_kind":1,"coupon_share_state":0,"use_state":1,"coupon_id":3,"service_type":1019,"coupon_name":"通用抵值券","coupon_price":100,"rule_kind":2,"coupon_no":"20160900003","coupon_content":"通用洗抵值","rule_price":0,"rule_condition":1,"coupon_start_time":"2016-09-20","coupon_end_time":"2016-09-30"},{"coupon_kind":1,"coupon_share_state":0,"use_state":1,"coupon_id":2,"service_type":0,"coupon_name":"油烟机清洗抵值券","coupon_price":100,"rule_kind":1,"coupon_no":"20160900002","coupon_content":"油烟机清洗抵值","rule_price":50,"rule_condition":2,"coupon_start_time":"2016-09-20","coupon_end_time":"2016-09-30"},{"coupon_kind":1,"coupon_share_state":0,"use_state":1,"coupon_id":1,"service_type":0,"coupon_name":"空调清洗抵值券","coupon_price":100.1,"rule_kind":1,"coupon_no":"20160900001","coupon_content":"空调清洗抵值","rule_price":0,"rule_condition":1,"coupon_start_time":"2016-09-20","coupon_end_time":"2016-09-30"},{"coupon_kind":1,"coupon_share_state":0,"use_state":1,"coupon_id":4,"service_type":1019,"coupon_name":"通用抵值券","coupon_price":100,"rule_kind":2,"coupon_no":"20160900004","coupon_content":"通用洗抵值","rule_price":50,"rule_condition":2,"coupon_start_time":"2016-09-20","coupon_end_time":"2016-09-30"}]}
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
        /**
         * coupon_kind : 1
         * coupon_share_state : 0
         * use_state : 1
         * coupon_id : 3
         * service_type : 1019
         * coupon_name : 通用抵值券
         * coupon_price : 100.0
         * rule_kind : 2
         * coupon_no : 20160900003
         * coupon_content : 通用洗抵值
         * rule_price : 0
         * rule_condition : 1
         * coupon_start_time : 2016-09-20
         * coupon_end_time : 2016-09-30
         */

        private List<CouponsBean> coupons;

        public List<CouponsBean> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<CouponsBean> coupons) {
            this.coupons = coupons;
        }

        public static class CouponsBean {
            private int coupon_kind;
            private int coupon_share_state;
            private int use_state;
            private int coupon_id;
            private int service_type;
            private String coupon_name;
            private double coupon_price;
            private int rule_kind;
            private String coupon_no;
            private String coupon_content;
            private int rule_price;
            private int rule_condition;
            private String coupon_start_time;
            private String coupon_end_time;

            public int getCoupon_kind() {
                return coupon_kind;
            }

            public void setCoupon_kind(int coupon_kind) {
                this.coupon_kind = coupon_kind;
            }

            public int getCoupon_share_state() {
                return coupon_share_state;
            }

            public void setCoupon_share_state(int coupon_share_state) {
                this.coupon_share_state = coupon_share_state;
            }

            public int getUse_state() {
                return use_state;
            }

            public void setUse_state(int use_state) {
                this.use_state = use_state;
            }

            public int getCoupon_id() {
                return coupon_id;
            }

            public void setCoupon_id(int coupon_id) {
                this.coupon_id = coupon_id;
            }

            public int getService_type() {
                return service_type;
            }

            public void setService_type(int service_type) {
                this.service_type = service_type;
            }

            public String getCoupon_name() {
                return coupon_name;
            }

            public void setCoupon_name(String coupon_name) {
                this.coupon_name = coupon_name;
            }

            public double getCoupon_price() {
                return coupon_price;
            }

            public void setCoupon_price(double coupon_price) {
                this.coupon_price = coupon_price;
            }

            public int getRule_kind() {
                return rule_kind;
            }

            public void setRule_kind(int rule_kind) {
                this.rule_kind = rule_kind;
            }

            public String getCoupon_no() {
                return coupon_no;
            }

            public void setCoupon_no(String coupon_no) {
                this.coupon_no = coupon_no;
            }

            public String getCoupon_content() {
                return coupon_content;
            }

            public void setCoupon_content(String coupon_content) {
                this.coupon_content = coupon_content;
            }

            public int getRule_price() {
                return rule_price;
            }

            public void setRule_price(int rule_price) {
                this.rule_price = rule_price;
            }

            public int getRule_condition() {
                return rule_condition;
            }

            public void setRule_condition(int rule_condition) {
                this.rule_condition = rule_condition;
            }

            public String getCoupon_start_time() {
                return coupon_start_time;
            }

            public void setCoupon_start_time(String coupon_start_time) {
                this.coupon_start_time = coupon_start_time;
            }

            public String getCoupon_end_time() {
                return coupon_end_time;
            }

            public void setCoupon_end_time(String coupon_end_time) {
                this.coupon_end_time = coupon_end_time;
            }
        }
    }
}