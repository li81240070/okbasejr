package com.hx.jrperson.consts;

/**
 * 接口地址类
 * Created by ge on 2016/3/26.
 */
public class API {
    // 王猛API
//    public static final String TEST_API = "http://192.168.0.146:8080/ZhenJiangren_Management_v0.1-SNAPSHOT";
    public static final String LIGEAPI = "https://123.57.185.198/ZhenjiangrenManagement";
    public static final String GAINNEWMESSAGE = "https://123.57.185.198/ZhenjiangrenManagement";
    //外网
//    public static final String LIGEAPI = "http://192.168.0.128:8088/ZhenjiangrenManagement";

    // 小强哥API
    public static final String XIAOQIANGGEAPI = "http://192.168.0.11:8080/ZhenjiangrenManagement";

    // 小强哥2API
    public static final String XIAOQIANGGEAPI2 = "http://192.168.0.144:8080/ZhenjiangrenManagement";

    // 李哥API
    public static final String WAI = "http://192.168.0.144:8080/ZhenjiangrenManagement";

    // 侯福勇API  王雪
    public static final String HONGFUYONGAPI = "http://192.168.0.163:8080/ZhenjiangrenManagement";

    //注册接口
    public static final String CUSTOMER = LIGEAPI + "/api/v1/customer/register";

    // 用户注册获取验证码
    public static final String QUERYCODE = LIGEAPI + "/api/v1/user/queryCode";

    // 找回密码(忘记密码)
    public static final String FORGETPWD = LIGEAPI + "/api/v1/customer/forgetPwd";

    //    修改密码
    public static final String MODIFYPWD = LIGEAPI + "/api/v1/customer/pwd";

    // 找回密码获取验证码
    public static final String FORGETCODE = LIGEAPI + "/api/v1/user/forgetCode";

    //登录
    public static final String LOGIN = LIGEAPI + "/api/v1/customer/login";

    //http://192.168.0.119:8080/ZhenjiangrenManagement/api/v1/common/subServices?parentCode=1001
    //小球点击请求项目列表接口
    public static final String SUBSERCIVE = LIGEAPI + "/api/v1/common/subServices";

    //具体一个项目内容接口
    public static final String SERVICEGUT = LIGEAPI + "/api/v1/common/services";

    // 关于我们
    public static final String ABOUT = LIGEAPI + "/api/v1/common/about";

    // 用户注销
    public static final String LOGOUT = LIGEAPI + "/api/v1/customer/logout";

    // 抢险抢修
    public static final String EMERGENCY = LIGEAPI + "/api/v1/customer/emergency";

    // 修改头像
    public static final String ALTER_IMG = LIGEAPI + "/api/v1/customer/image";

    // 消息列表
    public static final String MESSAGE = LIGEAPI + "/api/v1/customer/messages";

    // 维修标准
    public static final String STANDARD = LIGEAPI + "/api/v1/common/standard";

    // 个人信息
    public static final String DETAIL = LIGEAPI + "/api/v1/customer/detail";

    // 订单详细信息
    public static final String ORDER_DETAIL = LIGEAPI + "/api/v1/common/detail";

    // 订单列表
    public static final String ORDERLIST = LIGEAPI + "/api/v1/customer/orderlist";

    // 创建订单
    public static final String CREATORDER = LIGEAPI + "/api/v1/customer/order";

//    //订单状态
//    public static final String ORDERSTATUE = LIGEAPI + "/api/v1/order/status";

    // 地区列表
    public static final String POSTCODES = LIGEAPI + "/api/v1/common/postcodes";

    // 修改个人信息
    public static final String ALTERINFOR = LIGEAPI + "/api/v1/customer/customer";

    public static final String API_V1_CUSTOMER_MASTERS = "/api/v1/customer/masters";
    // 获取匠人位置
    public static final String MASTERS = LIGEAPI + API_V1_CUSTOMER_MASTERS;

    //http://192.168.0.119:8080/resource/ORDER/2016/4/1/458/17eb78c3ad3f8de6_640.jpg
    //头像
    public static final String AVATER = "https://123.57.185.198";

    // 意见反馈
    public static final String FEEDBACK = LIGEAPI + "/api/v1/customer/feedback";

    //    支付
    public static final String PAY = LIGEAPI + "/api/v1/common/pay";

    // 取消原因
    public static final String CANCELTAGS = LIGEAPI + "/api/v1/customer/canceltags";

    //修改订单状态
    public static final String ALTERORDERSTAUTE = LIGEAPI + "/api/v1/order/status";

    //获取评价标签
    public static final String EVALUATIONS = LIGEAPI + "/api/v1/customer/evaluations";

    //评价
    public static final String COO = LIGEAPI + "/api/v1/customer/comment";

    //http://192.168.0.144:8080/ZhenjiangrenManagement/api/v1/common/version?os=1&type=1
    //获取版本号
    public static final String VERSION = LIGEAPI + "/api/v1/common/version";

    //服务流程
    public static final String FLOW = LIGEAPI + "/api/v1/common/flow";

    //取消订单数量
    public static final String CANCLEFLAG = LIGEAPI + "/api/v1/customer/cancelFlag";

    //取得匠人位置
    public static final String WORKERLOCATION = LIGEAPI + "/api/v1/customer/locate";

    //查询支付订单状态
    //传入参数  orderId,status
    public static final String ORDERPAYSTATUS = LIGEAPI + "/api/v1/payment/orderPayStatus";

    //修改支付状态接口(post)
    //传入参数  orderId,status
    public static final String PAYSTATUS = LIGEAPI + "/api/v1/payment/orderPayStatus";

    //获取支付信息接口
    //传入参数  orderId,customerId,workerId,payType
    public static final String TEMP = LIGEAPI + "/api/v1/payment/temp";

    //投诉
    public static final String COMPLAIN = LIGEAPI + "/api/v1/customer/complaints";

    //查看是否在服务区
    public static final String VALIDATEADDRESS = LIGEAPI + "/api/v1/common/validateAddress";

    //激光注册 registrationId
    public static final String REGISTIONID = LIGEAPI + "/api/v1/msg/registration";

    //加载消息的网页
    public static final String LOADURLINFORGUT = "http://192.168.0.128:8082/ZhenjiangrenManagement/general/editor";

    //获取是否有最新消息
    public static final String GAINMESSAGE = GAINNEWMESSAGE + "/api/v1/activity/index.shtml";

    //获取是否有消息
    public static final String GAININFOR = GAINNEWMESSAGE + "/api/v1/activity/inspectUpdate.shtml";

    //获取是否有消息
    public static final String GAININFORGUT = GAINNEWMESSAGE + "/api/v1/activity/list.shtml";

    //新广告图片头
    public static final String NEWMESSAGEINFORHEAD = "http://123.57.185.198:8088";

    //消息列表
    public static final String INFORLIST = "http://123.57.185.198:8088/ZhenjiangrenManagement/activity/details.shtml?activityId=";
}
