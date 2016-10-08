package com.hx.jrperson.bean.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ge on 2016/4/7.
 */
public class OrderEntity implements Serializable{


    /**
     * code : 200
     * time : 1460000425134
     * message : null
     * dataMap : {"service_id":1033,"worker_post_code":134005,"worker_star":0,"adjustable":1,"constomer_create_id":0,"constomer_update_time":1459986571000,"nick_name":"葛安文","worker_name":"王弘宇","amount":1200,"xyTime":1460000374000,"appoint_time":1460000400000,"worker_gender":1,"create_id":0,"pay_type":0,"order_time":1459999857000,"points":1510,"income_worker":0,"custom_sign":"我的天","status":1,"credentialsSalt":"2e7aba38ab041cd9c805ce92de99730b","ava_amount":0,"avatar":"/2016/4/7/808/81f8457763e4bcd5","total_price":576.1,"order_status":3,"constomer_update_id":0,"update_time":1459999857000,"create_time":1460000001000,"images":["/resource/ORDER/2016/4/7/472/2d0248cfbc540f36"],"worker_create_time":1459963343000,"locked":0,"worker_id":1,"mobile":"13664266902","income_total":0,"region":0,"vip_level":0,"worker_address":"大连市甘井子区泉水接到","worker_update_id":1,"order_id":360,"worker_mobile":"15104156258","server_comment":"","worker_no":"201502031101","constomer_status":1,"constomer_gender":0,"constomer_post_code":0,"accept_time":1459999889000,"customerName":"葛安文","server_id":0,"customer_comment":"友情","alias":"37c9556cc9c24a6e978d6177939146cf","post_code":0,"star":0,"order_comment":"","worker_avatar":"d:/resource/AVATAR/2016/4/5/814/","worker_status":2,"work_status":1,"update_id":0,"constomer_create_time":1459986571000,"address":"辽宁省大连市中山区动物园","worker_create_id":1,"worker_update_time":1459986190000,"service":{"group_name":"洁具维修","group_code":1033,"children":[{"service_code":1037,"num":2,"service_name":"浴缸漏水维修","unit_price":119},{"service_code":1038,"num":2,"service_name":"浴缸下水维修","unit_price":169},{"service_code":1039,"num":1,"service_name":"洁具其它维修","unit_price":0}]},"indentity_card":"210828136945698","y":121.529709,"customer_id":65,"x":38.867241,"order_type":1001}
     */

    private int code;
    private long time;
    private String message;
    /**
     * service_id : 1033
     * worker_post_code : 134005
     * worker_star : 0
     * adjustable : 1
     * constomer_create_id : 0
     * constomer_update_time : 1459986571000
     * nick_name : 葛安文
     * worker_name : 王弘宇
     * amount : 1200
     * xyTime : 1460000374000
     * appoint_time : 1460000400000
     * worker_gender : 1
     * create_id : 0
     * pay_type : 0
     * order_time : 1459999857000
     * points : 1510
     * income_worker : 0
     * custom_sign : 我的天
     * status : 1
     * credentialsSalt : 2e7aba38ab041cd9c805ce92de99730b
     * ava_amount : 0
     * avatar : /2016/4/7/808/81f8457763e4bcd5
     * total_price : 576.1
     * order_status : 3
     * constomer_update_id : 0
     * update_time : 1459999857000
     * create_time : 1460000001000
     * images : ["/resource/ORDER/2016/4/7/472/2d0248cfbc540f36"]
     * worker_create_time : 1459963343000
     * locked : 0
     * worker_id : 1
     * mobile : 13664266902
     * income_total : 0
     * region : 0
     * vip_level : 0
     * worker_address : 大连市甘井子区泉水接到
     * worker_update_id : 1
     * order_id : 360
     * worker_mobile : 15104156258
     * server_comment :
     * worker_no : 201502031101
     * constomer_status : 1
     * constomer_gender : 0
     * constomer_post_code : 0
     * accept_time : 1459999889000
     * customerName : 葛安文
     * server_id : 0
     * customer_comment : 友情
     * alias : 37c9556cc9c24a6e978d6177939146cf
     * post_code : 0
     * star : 0
     * order_comment :
     * worker_avatar : d:/resource/AVATAR/2016/4/5/814/
     * worker_status : 2
     * work_status : 1
     * update_id : 0
     * constomer_create_time : 1459986571000
     * address : 辽宁省大连市中山区动物园
     * worker_create_id : 1
     * worker_update_time : 1459986190000
     * service : {"group_name":"洁具维修","group_code":1033,"children":[{"service_code":1037,"num":2,"service_name":"浴缸漏水维修","unit_price":119},{"service_code":1038,"num":2,"service_name":"浴缸下水维修","unit_price":169},{"service_code":1039,"num":1,"service_name":"洁具其它维修","unit_price":0}]}
     * indentity_card : 210828136945698
     * y : 121.529709
     * customer_id : 65
     * x : 38.867241
     * order_type : 1001
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
        private int service_id;
        private int worker_post_code;
        private int worker_star;
        private int adjustable;
        private int constomer_create_id;
        private long constomer_update_time;
        private String nick_name;
        private String worker_name;
        private double amount;
        private long xyTime;
        private long appoint_time;
        private int worker_gender;
        private int create_id;
        private int pay_type;
        private long order_time;
        private int points;
        private int income_worker;
        private String custom_sign;
        private int status;
        private String credentialsSalt;
        private int ava_amount;
        private String avatar;
        private double total_price;
        private int order_status;
        private int constomer_update_id;
        private long update_time;
        private long create_time;
        private long worker_create_time;
        private int locked;
        private int worker_id;
        private String mobile;
        private int income_total;
        private int region;
        private int vip_level;
        private String worker_address;
        private int worker_update_id;
        private int order_id;
        private String worker_mobile;
        private String server_comment;
        private String worker_no;
        private int constomer_status;
        private int constomer_gender;
        private int constomer_post_code;
        private long accept_time;
        private String customerName;
        private int server_id;
        private String customer_comment;
        private String alias;
        private int post_code;
        private int star;
        private String order_comment;
        private String worker_avatar;
        private int worker_status;
        private int work_status;
        private int update_id;
        private long constomer_create_time;
        private String address;
        private int worker_create_id;
        private long worker_update_time;
        /**
         * group_name : 洁具维修
         * group_code : 1033
         * children : [{"service_code":1037,"num":2,"service_name":"浴缸漏水维修","unit_price":119},{"service_code":1038,"num":2,"service_name":"浴缸下水维修","unit_price":169},{"service_code":1039,"num":1,"service_name":"洁具其它维修","unit_price":0}]
         */

        private ServiceBean service;
        private String indentity_card;
        private double y;
        private int customer_id;
        private double x;
        private int order_type;
        private List<String> images;

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public int getWorker_post_code() {
            return worker_post_code;
        }

        public void setWorker_post_code(int worker_post_code) {
            this.worker_post_code = worker_post_code;
        }

        public int getWorker_star() {
            return worker_star;
        }

        public void setWorker_star(int worker_star) {
            this.worker_star = worker_star;
        }

        public int getAdjustable() {
            return adjustable;
        }

        public void setAdjustable(int adjustable) {
            this.adjustable = adjustable;
        }

        public int getConstomer_create_id() {
            return constomer_create_id;
        }

        public void setConstomer_create_id(int constomer_create_id) {
            this.constomer_create_id = constomer_create_id;
        }

        public long getConstomer_update_time() {
            return constomer_update_time;
        }

        public void setConstomer_update_time(long constomer_update_time) {
            this.constomer_update_time = constomer_update_time;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getWorker_name() {
            return worker_name;
        }

        public void setWorker_name(String worker_name) {
            this.worker_name = worker_name;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public long getXyTime() {
            return xyTime;
        }

        public void setXyTime(long xyTime) {
            this.xyTime = xyTime;
        }

        public long getAppoint_time() {
            return appoint_time;
        }

        public void setAppoint_time(long appoint_time) {
            this.appoint_time = appoint_time;
        }

        public int getWorker_gender() {
            return worker_gender;
        }

        public void setWorker_gender(int worker_gender) {
            this.worker_gender = worker_gender;
        }

        public int getCreate_id() {
            return create_id;
        }

        public void setCreate_id(int create_id) {
            this.create_id = create_id;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
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

        public int getIncome_worker() {
            return income_worker;
        }

        public void setIncome_worker(int income_worker) {
            this.income_worker = income_worker;
        }

        public String getCustom_sign() {
            return custom_sign;
        }

        public void setCustom_sign(String custom_sign) {
            this.custom_sign = custom_sign;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCredentialsSalt() {
            return credentialsSalt;
        }

        public void setCredentialsSalt(String credentialsSalt) {
            this.credentialsSalt = credentialsSalt;
        }

        public int getAva_amount() {
            return ava_amount;
        }

        public void setAva_amount(int ava_amount) {
            this.ava_amount = ava_amount;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public int getConstomer_update_id() {
            return constomer_update_id;
        }

        public void setConstomer_update_id(int constomer_update_id) {
            this.constomer_update_id = constomer_update_id;
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

        public long getWorker_create_time() {
            return worker_create_time;
        }

        public void setWorker_create_time(long worker_create_time) {
            this.worker_create_time = worker_create_time;
        }

        public int getLocked() {
            return locked;
        }

        public void setLocked(int locked) {
            this.locked = locked;
        }

        public int getWorker_id() {
            return worker_id;
        }

        public void setWorker_id(int worker_id) {
            this.worker_id = worker_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getIncome_total() {
            return income_total;
        }

        public void setIncome_total(int income_total) {
            this.income_total = income_total;
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

        public String getWorker_address() {
            return worker_address;
        }

        public void setWorker_address(String worker_address) {
            this.worker_address = worker_address;
        }

        public int getWorker_update_id() {
            return worker_update_id;
        }

        public void setWorker_update_id(int worker_update_id) {
            this.worker_update_id = worker_update_id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getWorker_mobile() {
            return worker_mobile;
        }

        public void setWorker_mobile(String worker_mobile) {
            this.worker_mobile = worker_mobile;
        }

        public String getServer_comment() {
            return server_comment;
        }

        public void setServer_comment(String server_comment) {
            this.server_comment = server_comment;
        }

        public String getWorker_no() {
            return worker_no;
        }

        public void setWorker_no(String worker_no) {
            this.worker_no = worker_no;
        }

        public int getConstomer_status() {
            return constomer_status;
        }

        public void setConstomer_status(int constomer_status) {
            this.constomer_status = constomer_status;
        }

        public int getConstomer_gender() {
            return constomer_gender;
        }

        public void setConstomer_gender(int constomer_gender) {
            this.constomer_gender = constomer_gender;
        }

        public int getConstomer_post_code() {
            return constomer_post_code;
        }

        public void setConstomer_post_code(int constomer_post_code) {
            this.constomer_post_code = constomer_post_code;
        }

        public long getAccept_time() {
            return accept_time;
        }

        public void setAccept_time(long accept_time) {
            this.accept_time = accept_time;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public int getServer_id() {
            return server_id;
        }

        public void setServer_id(int server_id) {
            this.server_id = server_id;
        }

        public String getCustomer_comment() {
            return customer_comment;
        }

        public void setCustomer_comment(String customer_comment) {
            this.customer_comment = customer_comment;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public int getPost_code() {
            return post_code;
        }

        public void setPost_code(int post_code) {
            this.post_code = post_code;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public String getOrder_comment() {
            return order_comment;
        }

        public void setOrder_comment(String order_comment) {
            this.order_comment = order_comment;
        }

        public String getWorker_avatar() {
            return worker_avatar;
        }

        public void setWorker_avatar(String worker_avatar) {
            this.worker_avatar = worker_avatar;
        }

        public int getWorker_status() {
            return worker_status;
        }

        public void setWorker_status(int worker_status) {
            this.worker_status = worker_status;
        }

        public int getWork_status() {
            return work_status;
        }

        public void setWork_status(int work_status) {
            this.work_status = work_status;
        }

        public int getUpdate_id() {
            return update_id;
        }

        public void setUpdate_id(int update_id) {
            this.update_id = update_id;
        }

        public long getConstomer_create_time() {
            return constomer_create_time;
        }

        public void setConstomer_create_time(long constomer_create_time) {
            this.constomer_create_time = constomer_create_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getWorker_create_id() {
            return worker_create_id;
        }

        public void setWorker_create_id(int worker_create_id) {
            this.worker_create_id = worker_create_id;
        }

        public long getWorker_update_time() {
            return worker_update_time;
        }

        public void setWorker_update_time(long worker_update_time) {
            this.worker_update_time = worker_update_time;
        }

        public ServiceBean getService() {
            return service;
        }

        public void setService(ServiceBean service) {
            this.service = service;
        }

        public String getIndentity_card() {
            return indentity_card;
        }

        public void setIndentity_card(String indentity_card) {
            this.indentity_card = indentity_card;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "DataMapBean{" +
                    "service_id=" + service_id +
                    ", worker_post_code=" + worker_post_code +
                    ", worker_star=" + worker_star +
                    ", adjustable=" + adjustable +
                    ", constomer_create_id=" + constomer_create_id +
                    ", constomer_update_time=" + constomer_update_time +
                    ", nick_name='" + nick_name + '\'' +
                    ", worker_name='" + worker_name + '\'' +
                    ", amount=" + amount +
                    ", xyTime=" + xyTime +
                    ", appoint_time=" + appoint_time +
                    ", worker_gender=" + worker_gender +
                    ", create_id=" + create_id +
                    ", pay_type=" + pay_type +
                    ", order_time=" + order_time +
                    ", points=" + points +
                    ", income_worker=" + income_worker +
                    ", custom_sign='" + custom_sign + '\'' +
                    ", status=" + status +
                    ", credentialsSalt='" + credentialsSalt + '\'' +
                    ", ava_amount=" + ava_amount +
                    ", avatar='" + avatar + '\'' +
                    ", total_price=" + total_price +
                    ", order_status=" + order_status +
                    ", constomer_update_id=" + constomer_update_id +
                    ", update_time=" + update_time +
                    ", create_time=" + create_time +
                    ", worker_create_time=" + worker_create_time +
                    ", locked=" + locked +
                    ", worker_id=" + worker_id +
                    ", mobile='" + mobile + '\'' +
                    ", income_total=" + income_total +
                    ", region=" + region +
                    ", vip_level=" + vip_level +
                    ", worker_address='" + worker_address + '\'' +
                    ", worker_update_id=" + worker_update_id +
                    ", order_id=" + order_id +
                    ", worker_mobile='" + worker_mobile + '\'' +
                    ", server_comment='" + server_comment + '\'' +
                    ", worker_no='" + worker_no + '\'' +
                    ", constomer_status=" + constomer_status +
                    ", constomer_gender=" + constomer_gender +
                    ", constomer_post_code=" + constomer_post_code +
                    ", accept_time=" + accept_time +
                    ", customerName='" + customerName + '\'' +
                    ", server_id=" + server_id +
                    ", customer_comment='" + customer_comment + '\'' +
                    ", alias='" + alias + '\'' +
                    ", post_code=" + post_code +
                    ", star=" + star +
                    ", order_comment='" + order_comment + '\'' +
                    ", worker_avatar='" + worker_avatar + '\'' +
                    ", worker_status=" + worker_status +
                    ", work_status=" + work_status +
                    ", update_id=" + update_id +
                    ", constomer_create_time=" + constomer_create_time +
                    ", address='" + address + '\'' +
                    ", worker_create_id=" + worker_create_id +
                    ", worker_update_time=" + worker_update_time +
                    ", service=" + service +
                    ", indentity_card='" + indentity_card + '\'' +
                    ", y=" + y +
                    ", customer_id=" + customer_id +
                    ", x=" + x +
                    ", order_type=" + order_type +
                    ", images=" + images +
                    '}';
        }

        public static class ServiceBean implements Serializable{
            private String group_name;
            private int group_code;
            /**
             * service_code : 1037
             * num : 2
             * service_name : 浴缸漏水维修
             * unit_price : 119
             */

            private List<ChildrenBean> children;

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public int getGroup_code() {
                return group_code;
            }

            public void setGroup_code(int group_code) {
                this.group_code = group_code;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            public static class ChildrenBean implements Serializable{
                private int service_code;
                private int num;
                private String service_name;
                private double unit_price;

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

                public double getUnit_price() {
                    return unit_price;
                }

                public void setUnit_price(double unit_price) {
                    this.unit_price = unit_price;
                }

                @Override
                public String toString() {
                    return "ChildrenBean{" +
                            "service_code=" + service_code +
                            ", num=" + num +
                            ", service_name='" + service_name + '\'' +
                            ", unit_price=" + unit_price +
                            '}';
                }
            }
        }
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "code=" + code +
                ", time=" + time +
                ", message='" + message + '\'' +
                ", dataMap=" + dataMap +
                '}';
    }
}
