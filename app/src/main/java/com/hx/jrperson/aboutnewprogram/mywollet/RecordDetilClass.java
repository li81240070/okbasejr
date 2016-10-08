package com.hx.jrperson.aboutnewprogram.mywollet;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 * 充值记录相关的数据类
 */
public class RecordDetilClass {

    /**
     * code : 200
     * time : 1474299409537
     * message : null
     * dataMap : {"data":[{"id":4,"amount":32,"content":"充值失败。","trade_no":"111","create_time":1474210209000,"user_id":1,"trade_type":1},{"id":3,"amount":32,"content":"充值失败。","trade_no":"111","create_time":1474209496000,"user_id":1,"trade_type":1}]}
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
         * id : 4
         * amount : 32
         * content : 充值失败。
         * trade_no : 111
         * create_time : 1474210209000
         * user_id : 1
         * trade_type : 1
         */

        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            private int id;
            private double amount;
            private String content;
            private String trade_no;
            private long create_time;
            private int user_id;
            private int trade_type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTrade_no() {
                return trade_no;
            }

            public void setTrade_no(String trade_no) {
                this.trade_no = trade_no;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getTrade_type() {
                return trade_type;
            }

            public void setTrade_type(int trade_type) {
                this.trade_type = trade_type;
            }
        }
    }
}
