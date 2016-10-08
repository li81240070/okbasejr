package com.hx.jrperson.aboutnewprogram.mywollet;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 * 请求服务器关于钱包内数据的数据类
 */
public class PreferentialClass {

    /**
     * code : 200
     * time : 1474298202973
     * message : null
     * dataMap : {"data":[{"gift_amount":20,"id":1,"coupon_content":"充200送20","update_time":1474297307000,"create_time":1474297301000,"create_user":1,"coupon_state":1,"need_amount":200}]}
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
         * gift_amount : 20
         * id : 1
         * coupon_content : 充200送20
         * update_time : 1474297307000
         * create_time : 1474297301000
         * create_user : 1
         * coupon_state : 1
         * need_amount : 200
         */

        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            private int gift_amount;
            private int id;
            private String coupon_content;
            private long update_time;
            private long create_time;
            private int create_user;
            private int coupon_state;
            private int need_amount;

            public int getGift_amount() {
                return gift_amount;
            }

            public void setGift_amount(int gift_amount) {
                this.gift_amount = gift_amount;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCoupon_content() {
                return coupon_content;
            }

            public void setCoupon_content(String coupon_content) {
                this.coupon_content = coupon_content;
            }

            public long getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(long update_time) {
                this.update_time = update_time;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public int getCreate_user() {
                return create_user;
            }

            public void setCreate_user(int create_user) {
                this.create_user = create_user;
            }

            public int getCoupon_state() {
                return coupon_state;
            }

            public void setCoupon_state(int coupon_state) {
                this.coupon_state = coupon_state;
            }

            public int getNeed_amount() {
                return need_amount;
            }

            public void setNeed_amount(int need_amount) {
                this.need_amount = need_amount;
            }
        }
    }
}
