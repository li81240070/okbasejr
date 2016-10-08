package com.hx.jrperson.aboutnewprogram.mywollet;

/**
 * Created by Administrator on 2016/9/21.
 * 查询充值记录的具体信息的相关数据类
 */
public class SelectMoneyClass {


    /**
     * code : 200
     * time : 1474299617781
     * message : null
     * dataMap : {"data":{"remine_amount":300}}
     */

    private int code;
    private long time;
    private Object message;
    /**
     * data : {"remine_amount":300}
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
        /**
         * remine_amount : 300.0
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private double remine_amount;

            public double getRemine_amount() {
                return remine_amount;
            }

            public void setRemine_amount(double remine_amount) {
                this.remine_amount = remine_amount;
            }
        }
    }
}
