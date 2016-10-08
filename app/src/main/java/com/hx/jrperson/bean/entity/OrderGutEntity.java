package com.hx.jrperson.bean.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情数据实体类
 * Created by ge on 2016/4/4.
 */
public class OrderGutEntity implements Serializable {


    /**
     * code : 200
     * time : 1459753427090
     * message : null
     * dataMap : {"update_id":0,"order_comment":"","constomer_post_code":0,"order_time":1459649225000,"points":10,"order_status":3,"worker_create_time":1459491407000,"create_id":0,"worker_gender":0,"locked":0,"order_type":1001,"create_time":1459747404000,"worker_name":"邹利强","worker_post_code":116085,"worker_update_id":1,"indentity_card":"210282189763215469","server_id":0,"credentialsSalt":"4157c3feef4a6ed91b2c28cf4392f2d1","worker_id":1,"worker_status":2,"income_total":0,"nick_name":"王","adjustable":0,"region":0,"vip_level":0,"order_id":118,"customer_comment":"","status":1,"constomer_status":1,"custom_sign":"这个家伙很懒，什么都没留下~~","constomer_update_id":0,"constomer_create_id":0,"constomer_create_time":1459649080000,"update_time":1459733534000,"service_id":1008,"alias":"67c9abbb15b14cdbb5b70dfd5a3abf46","pay_type":0,"worker_avatar":"0","appoint_time":1459707000,"server_comment":"","amount":579,"address":"大连市高新园区黄浦路901号","total_price":1,"star":0,"mobile":"15512341234","avatar":"0","worker_create_id":1,"constomer_gender":0,"ava_amount":0,"worker_no":"201502031102","service":{"group_code":1008,"group_name":"花洒喷头","children":[{"service_code":1009,"num":3,"service_name":"组合花洒更换/维修","unit_price":89},{"service_code":1010,"num":4,"service_name":"手持花洒更换/维修","unit_price":69}]},"post_code":210202,"x":38.867346,"xyTime":1459132370000,"y":121.529551,"worker_mobile":"13940569873","worker_update_time":1459749550000,"customer_id":49,"income_worker":0,"worker_star":1,"constomer_update_time":1459649080000}
     */

    private int code;
    private long time;
    private Object message;
    /**
     * update_id : 0
     * order_comment :
     * constomer_post_code : 0
     * order_time : 1459649225000
     * points : 10
     * order_status : 3
     * worker_create_time : 1459491407000
     * create_id : 0
     * worker_gender : 0
     * locked : 0
     * order_type : 1001
     * create_time : 1459747404000
     * worker_name : 邹利强
     * worker_post_code : 116085
     * worker_update_id : 1
     * indentity_card : 210282189763215469
     * server_id : 0
     * credentialsSalt : 4157c3feef4a6ed91b2c28cf4392f2d1
     * worker_id : 1
     * worker_status : 2
     * income_total : 0
     * nick_name : 王
     * adjustable : 0
     * region : 0
     * vip_level : 0
     * order_id : 118
     * customer_comment :
     * status : 1
     * constomer_status : 1
     * custom_sign : 这个家伙很懒，什么都没留下~~
     * constomer_update_id : 0
     * constomer_create_id : 0
     * constomer_create_time : 1459649080000
     * update_time : 1459733534000
     * service_id : 1008
     * alias : 67c9abbb15b14cdbb5b70dfd5a3abf46
     * pay_type : 0
     * worker_avatar : 0
     * appoint_time : 1459707000
     * server_comment :
     * amount : 579
     * address : 大连市高新园区黄浦路901号
     * total_price : 1
     * star : 0
     * mobile : 15512341234
     * avatar : 0
     * worker_create_id : 1
     * constomer_gender : 0
     * ava_amount : 0
     * worker_no : 201502031102
     * service : {"group_code":1008,"group_name":"花洒喷头","children":[{"service_code":1009,"num":3,"service_name":"组合花洒更换/维修","unit_price":89},{"service_code":1010,"num":4,"service_name":"手持花洒更换/维修","unit_price":69}]}
     * post_code : 210202
     * x : 38.867346
     * xyTime : 1459132370000
     * y : 121.529551
     * worker_mobile : 13940569873
     * worker_update_time : 1459749550000
     * customer_id : 49
     * income_worker : 0
     * worker_star : 1
     * constomer_update_time : 1459649080000
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

    public static class DataMapBean implements Serializable {
        private int update_id;
        private String order_comment;
        private int constomer_post_code;
        private long order_time;
        private int points;
        private int order_status;
        private long worker_create_time;
        private int create_id;
        private int worker_gender;
        private int locked;
        private int order_type;
        private long dispatch_time;
        private long create_time;
        private String worker_name;
        private int worker_post_code;
        private int worker_update_id;
        private String indentity_card;
        private int server_id;
        private String credentialsSalt;
        private String finish_time;
        private int worker_id;
        private int worker_status;
        private long start_time;
        private int income_total;
        private String nick_name;
        private int adjustable;
        private int region;
        private int vip_level;
        private int order_id;
        private String customer_comment;
        private int status;
        private long comment_time;
        private int constomer_status;
        private String custom_sign;
        private int constomer_update_id;
        private int constomer_create_id;
        private long constomer_create_time;
        private long update_time;
        private int service_id;
        private String alias;
        private int pay_type;
        private String worker_avatar;
        private long appoint_time;
        private long accept_time;
        private String server_comment;
        private int amount;
        private String address;
        private int total_price;
        private int star;
        private String mobile;
        private String avatar;
        private int worker_create_id;
        private int constomer_gender;
        private int ava_amount;
        private String worker_no;
        /**
         * group_code : 1008
         * group_name : 花洒喷头
         * children : [{"service_code":1009,"num":3,"service_name":"组合花洒更换/维修","unit_price":89},{"service_code":1010,"num":4,"service_name":"手持花洒更换/维修","unit_price":69}]
         */

        private ServiceBean service;
        private int post_code;
        private double x;
        private long xyTime;
        private double y;
        private String worker_mobile;
        private long worker_update_time;
        private int customer_id;
        private int income_worker;
        private int worker_star;
        private long constomer_update_time;

        public int getUpdate_id() {
            return update_id;
        }

        public void setUpdate_id(int update_id) {
            this.update_id = update_id;
        }

        public String getOrder_comment() {
            return order_comment;
        }

        public void setOrder_comment(String order_comment) {
            this.order_comment = order_comment;
        }

        public int getConstomer_post_code() {
            return constomer_post_code;
        }

        public void setConstomer_post_code(int constomer_post_code) {
            this.constomer_post_code = constomer_post_code;
        }

        public long getOrder_time() {
            return order_time;
        }

        public void setOrder_time(long order_time) {
            this.order_time = order_time;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public long getWorker_create_time() {
            return worker_create_time;
        }

        public void setWorker_create_time(long worker_create_time) {
            this.worker_create_time = worker_create_time;
        }

        public int getCreate_id() {
            return create_id;
        }

        public void setCreate_id(int create_id) {
            this.create_id = create_id;
        }

        public int getWorker_gender() {
            return worker_gender;
        }

        public void setWorker_gender(int worker_gender) {
            this.worker_gender = worker_gender;
        }

        public int getLocked() {
            return locked;
        }

        public void setLocked(int locked) {
            this.locked = locked;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getWorker_name() {
            return worker_name;
        }

        public void setWorker_name(String worker_name) {
            this.worker_name = worker_name;
        }

        public int getWorker_post_code() {
            return worker_post_code;
        }

        public void setWorker_post_code(int worker_post_code) {
            this.worker_post_code = worker_post_code;
        }

        public int getWorker_update_id() {
            return worker_update_id;
        }

        public void setWorker_update_id(int worker_update_id) {
            this.worker_update_id = worker_update_id;
        }

        public String getIndentity_card() {
            return indentity_card;
        }

        public void setIndentity_card(String indentity_card) {
            this.indentity_card = indentity_card;
        }

        public int getServer_id() {
            return server_id;
        }

        public void setServer_id(int server_id) {
            this.server_id = server_id;
        }

        public String getCredentialsSalt() {
            return credentialsSalt;
        }

        public void setCredentialsSalt(String credentialsSalt) {
            this.credentialsSalt = credentialsSalt;
        }

        public int getWorker_id() {
            return worker_id;
        }

        public void setWorker_id(int worker_id) {
            this.worker_id = worker_id;
        }

        public int getWorker_status() {
            return worker_status;
        }

        public void setWorker_status(int worker_status) {
            this.worker_status = worker_status;
        }

        public int getIncome_total() {
            return income_total;
        }

        public void setIncome_total(int income_total) {
            this.income_total = income_total;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public int getAdjustable() {
            return adjustable;
        }

        public void setAdjustable(int adjustable) {
            this.adjustable = adjustable;
        }

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }

        public int getVip_level() {
            return vip_level;
        }

        public void setVip_level(int vip_level) {
            this.vip_level = vip_level;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getCustomer_comment() {
            return customer_comment;
        }

        public void setCustomer_comment(String customer_comment) {
            this.customer_comment = customer_comment;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getConstomer_status() {
            return constomer_status;
        }

        public void setConstomer_status(int constomer_status) {
            this.constomer_status = constomer_status;
        }

        public String getCustom_sign() {
            return custom_sign;
        }

        public void setCustom_sign(String custom_sign) {
            this.custom_sign = custom_sign;
        }

        public int getConstomer_update_id() {
            return constomer_update_id;
        }

        public void setConstomer_update_id(int constomer_update_id) {
            this.constomer_update_id = constomer_update_id;
        }

        public int getConstomer_create_id() {
            return constomer_create_id;
        }

        public void setConstomer_create_id(int constomer_create_id) {
            this.constomer_create_id = constomer_create_id;
        }

        public long getConstomer_create_time() {
            return constomer_create_time;
        }

        public void setConstomer_create_time(long constomer_create_time) {
            this.constomer_create_time = constomer_create_time;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public String getWorker_avatar() {
            return worker_avatar;
        }

        public void setWorker_avatar(String worker_avatar) {
            this.worker_avatar = worker_avatar;
        }

        public long getDispatch_time() {
            return dispatch_time;
        }

        public void setDispatch_time(long dispatch_time) {
            this.dispatch_time = dispatch_time;
        }

        public long getAppoint_time() {
            return appoint_time;

        }

        public void setAppoint_time(long appoint_time) {
            this.appoint_time = appoint_time;
        }

        public String getServer_comment() {
            return server_comment;
        }

        public void setServer_comment(String server_comment) {
            this.server_comment = server_comment;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
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

        public int getWorker_create_id() {
            return worker_create_id;
        }

        public void setWorker_create_id(int worker_create_id) {
            this.worker_create_id = worker_create_id;
        }

        public int getConstomer_gender() {
            return constomer_gender;
        }

        public void setConstomer_gender(int constomer_gender) {
            this.constomer_gender = constomer_gender;
        }

        public int getAva_amount() {
            return ava_amount;
        }

        public void setAva_amount(int ava_amount) {
            this.ava_amount = ava_amount;
        }

        public String getWorker_no() {
            return worker_no;
        }

        public void setWorker_no(String worker_no) {
            this.worker_no = worker_no;
        }

        public ServiceBean getService() {
            return service;
        }

        public void setService(ServiceBean service) {
            this.service = service;
        }

        public int getPost_code() {
            return post_code;
        }

        public void setPost_code(int post_code) {
            this.post_code = post_code;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public long getXyTime() {
            return xyTime;
        }

        public void setXyTime(long xyTime) {
            this.xyTime = xyTime;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public String getWorker_mobile() {
            return worker_mobile;
        }

        public void setWorker_mobile(String worker_mobile) {
            this.worker_mobile = worker_mobile;
        }

        public long getWorker_update_time() {
            return worker_update_time;
        }

        public void setWorker_update_time(long worker_update_time) {
            this.worker_update_time = worker_update_time;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public int getIncome_worker() {
            return income_worker;
        }

        public void setIncome_worker(int income_worker) {
            this.income_worker = income_worker;
        }

        public int getWorker_star() {
            return worker_star;
        }

        public void setWorker_star(int worker_star) {
            this.worker_star = worker_star;
        }

        public long getConstomer_update_time() {
            return constomer_update_time;
        }

        public void setConstomer_update_time(long constomer_update_time) {
            this.constomer_update_time = constomer_update_time;
        }

        public static class ServiceBean implements Serializable {
            private int group_code;
            private String group_name;
            /**
             * service_code : 1009
             * num : 3
             * service_name : 组合花洒更换/维修
             * unit_price : 89
             */

            private List<ChildrenBean> children;

            public int getGroup_code() {
                return group_code;
            }

            public void setGroup_code(int group_code) {
                this.group_code = group_code;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            public static class ChildrenBean implements Serializable {
                private int service_code;
                private int num;
                private String service_name;
                private int unit_price;

                public int getService_code() {
                    return service_code;
                }

                public void setService_code(int service_code) {
                    this.service_code = service_code;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public String getService_name() {
                    return service_name;
                }

                public void setService_name(String service_name) {
                    this.service_name = service_name;
                }

                public int getUnit_price() {
                    return unit_price;
                }

                public void setUnit_price(int unit_price) {
                    this.unit_price = unit_price;
                }
            }
        }
    }
}
