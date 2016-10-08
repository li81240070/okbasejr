package com.hx.jrperson.bean.entity;

import java.io.Serializable;

/**
 * 支付宝信息实体类
 * Created by ge on 2016/4/13.
 */
public class PayZhiFuBaoEntity implements Serializable{

    /**
     * partner :
     * sellerid :
     * outtradeno :
     * subject :
     * body :
     * totalfee :
     * notifyurl :
     * service :
     * paymenttype :
     * inputcharset :
     * itbpay :
     * showurl :
     * sign :
     * signType :
     */

    private DataMapBean dataMap;
    /**
     * dataMap : {"partner":"","sellerid":"","outtradeno":"","subject":"","body":"","totalfee":"","notifyurl":"","service":"","paymenttype":"","inputcharset":"","itbpay":"","showurl":"","sign":"","signType":""}
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
        private String partner;
        private String sellerid;
        private String outtradeno;
        private String subject;
        private String body;
        private String totalfee;
        private String notifyurl;
        private String service;
        private String paymenttype;
        private String inputcharset;
        private String itbpay;
        private String showurl;
        private String sign;
        private String signType;

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getSellerid() {
            return sellerid;
        }

        public void setSellerid(String sellerid) {
            this.sellerid = sellerid;
        }

        public String getOuttradeno() {
            return outtradeno;
        }

        public void setOuttradeno(String outtradeno) {
            this.outtradeno = outtradeno;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTotalfee() {
            return totalfee;
        }

        public void setTotalfee(String totalfee) {
            this.totalfee = totalfee;
        }

        public String getNotifyurl() {
            return notifyurl;
        }

        public void setNotifyurl(String notifyurl) {
            this.notifyurl = notifyurl;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getPaymenttype() {
            return paymenttype;
        }

        public void setPaymenttype(String paymenttype) {
            this.paymenttype = paymenttype;
        }

        public String getInputcharset() {
            return inputcharset;
        }

        public void setInputcharset(String inputcharset) {
            this.inputcharset = inputcharset;
        }

        public String getItbpay() {
            return itbpay;
        }

        public void setItbpay(String itbpay) {
            this.itbpay = itbpay;
        }

        public String getShowurl() {
            return showurl;
        }

        public void setShowurl(String showurl) {
            this.showurl = showurl;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }
    }
}
