package com.hx.jrperson.bean.entity;

/**
 * 微信支付实体类
 * Created by ge on 2016/4/13.
 */
public class PayWeiXinEntity {


    /**
     * appid :
     * partnerid :
     * prepayid :
     * package_ :
     * noncestr :
     * timestamp :
     * sign :
     * outtradeno :
     */

    private DataMapBean dataMap;
    /**
     * dataMap : {"appid":"","partnerid":"","prepayid":"","package_":"","noncestr":"","timestamp":"","sign":"","outtradeno":""}
     * code : 200
     * time : 1456934041215
     */

    private int code;
    private long time;

    public DataMapBean getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMapBean dataMap) {
        this.dataMap = dataMap;
    }

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

    public static class DataMapBean {
        private String appid;
        private String partnerid;
        private String prepayid;
        private String package_;
        private String noncestr;
        private String timestamp;
        private String sign;
        private String outtradeno;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackage_() {
            return package_;
        }

        public void setPackage_(String package_) {
            this.package_ = package_;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getOuttradeno() {
            return outtradeno;
        }

        public void setOuttradeno(String outtradeno) {
            this.outtradeno = outtradeno;
        }
    }
}
