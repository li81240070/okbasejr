package com.hx.jrperson.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.MyOrdorEntity;
import com.hx.jrperson.bean.entity.PayAliInfo;
import com.hx.jrperson.bean.entity.PayWxInfo;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.controller.OkHttpClientManager;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.alipay.PayResult;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.SurePayDialog;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 付款页面
 **/
public class PayActivity extends BaseActivity implements View.OnClickListener {
    //支付宝， 微信， 现金行布局
    private LinearLayout pay_zhifubaoRL, pay_weixinRL, pay_xianjinRL;
    //    private String SUBJECT = null;//订单信息
//    private String BODY = null;//订单详情
//    private String PRICE = null;//金额
    private String ORDER_ID = null;//订单ID,具体的某项服务ID

    private String payType = null;//支付方式
    private PayAliInfo aliInfo;   // 支付宝支付信息
    private PayWxInfo wxInfo;     // 微信支付信息
    public static PayActivity insance = null;
    private MyOrdorEntity.DataMapBean.OrdersBean bean;
    private PayBrodcastReceiver receiver; // 微信支付结果
    private String status;       // APP端支付状态
    private boolean isClickWX = true;
    private boolean isClickZFB = true;
    //////////////////////////////////////
    private RelativeLayout backButtonInPay;
    private ImageView backbuttonInPay;
    //微信支付注册
    //注册微信后台
    private static final String APP_ID="wxaa0851eead9e34ff";
    public IWXAPI api;
    public void regToWx(){
        api= WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1101:
                    Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderEntity", bean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    PayActivity.this.finish();
                    break;
            }
        }

    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), PaySuccessActivity.class);
//                        startActivity(intent);
                        sendAliPay();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //向后台注册微信支付
        regToWx();
        setContentView(R.layout.activity_pay);
        showToolBar("付款", true, this, false);
        EventBus.getDefault().register(this);
        insance = this;
        initView();
        initData();
        setListener();
        backButtonInPay= (RelativeLayout) findViewById(R.id.backButtonInPay);
        backButtonInPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayActivity.this.finish();
            }
        });
        backbuttonInPay= (ImageView) findViewById(R.id.backbuttonInPay);
        backbuttonInPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        pay_zhifubaoRL = (LinearLayout) findViewById(R.id.pay_zhifubaoRL);
        pay_weixinRL = (LinearLayout) findViewById(R.id.pay_weixinRL);
        pay_xianjinRL = (LinearLayout) findViewById(R.id.pay_xianjinRL);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bean = (MyOrdorEntity.DataMapBean.OrdersBean) bundle.get("orderEntity");
//        SUBJECT = bundle.getString("SUBJECT");
//        BODY = bundle.getString("BODY");
//        PRICE = bundle.getString("PRICE");
        ORDER_ID = bundle.getString("ORDER_ID");
//        Log.i("geanwen", ORDER_ID + "");
    }

    @Override
    protected void setListener() {
        pay_zhifubaoRL.setOnClickListener(this);
        pay_weixinRL.setOnClickListener(this);
        pay_xianjinRL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_zhifubaoRL://支付宝
                if (isClickZFB) {
                    payType = "2";
                    new Thread(getPayInfoTask).start();
                }
                break;
            case R.id.pay_weixinRL://微信支付
                if (isClickWX) {
                    clickOut();
                    payType = "1";
                    new Thread(getPayInfoTask).start();
                }
                break;
            case R.id.pay_xianjinRL://现金
                payType = "0";
                clickXianJin();
                break;
        }
    }

    /**
     * 防止用户狂点
     * 设置微信和支付宝3-5秒后可以点击第二次
     * **/
    private void clickOut() {
        Timer timer = null;
        if (isClickWX || isClickZFB){
            isClickZFB = false;
            isClickWX = false;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isClickZFB = true;
                    isClickWX = true;
                }
            }, 4000);
        }
    }

    /**
     * 创建支付宝订单
     */
    private String getOrderInfo() {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + aliInfo.getPartner()
                + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + aliInfo.getSellerid()
                + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + aliInfo.getOuttradeno()
                + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + aliInfo.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + aliInfo.getBody() + "\"";

        double amount = aliInfo.getTotalfee();
        String totalFee = String.format(Locale.getDefault(), "%.2f", amount);
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + totalFee + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + aliInfo.getNotifyurl()
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=" + "\"" + aliInfo.getService() + "\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=" + "\""
                + aliInfo.getPaymenttype() + "\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=" + "\""
                + aliInfo.getInputcharset() + "\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=" + "\"" + aliInfo.getItbpay() + "\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&show_url=" + "\"" + aliInfo.getShowurl()
                + "\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        Log.d("PayActivity", orderInfo);
        return orderInfo;
    }

    /**
     * 构建微信吊起支付接口的参数
     */
    public PayReq buildWechatPayReq() {
        PayReq payReq = new PayReq();
        payReq.appId = wxInfo.getAppid();
        payReq.partnerId = wxInfo.getPartnerid();
        payReq.prepayId = wxInfo.getPrepayid();
        payReq.packageValue = wxInfo.getPackage_();
        payReq.nonceStr = wxInfo.getNoncestr();
        payReq.timeStamp = wxInfo.getTimestamp();
        payReq.sign = wxInfo.getSign();
        Log.i("bbbbbb","appid="+wxInfo.getAppid()+"  partnerId="+wxInfo.getPartnerid()+"  prepayId="+wxInfo.getPrepayid()+"  packageValue="+wxInfo.getPackage_()+"  nonceStr="+wxInfo.getNoncestr()+"  timeStamp="+wxInfo.getTimestamp()+"  sign"+wxInfo.getSign());
        return payReq;
    }

    /**
     * 支付宝部分
     */
    private void aliPay() {
//        // 构造PayTask 对象
//        PayTask alipay = new PayTask(PayActivity.this);

        // 签名
        String sign = aliInfo.getSign();
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /* 完整的符合支付宝参数规范的订单信息 */
        String orderInfo = getOrderInfo();
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + "sign_type=\"" + aliInfo.getSignType() + "\"";

        Log.d("PayActivity", payInfo);
//        // 调用支付接口，获取支付结果
//        String result = alipay.pay(payInfo, true);
//        Log.d("PayActivity", result);

//        Message msg = new Message();
//        msg.what = 6;
//        msg.obj = result;
//        handler.sendMessage(msg);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Log.d("PayActivity", result);

                Message msg = new Message();
                msg.what = 6;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
        new Thread(payRunnable).start();
    }

    public class PayBrodcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("wxPay")) {

                int result = intent.getIntExtra("errCode", 0);
                switch (result) {
                    case -1: // 取消支付
                        status = "0";
                        new Thread(payStatusTask).start();
                        break;
                    case 0: // 支付完成
                        status = "1";
                        new Thread(payStatusTask).start();
//                        new Thread(payInfoTask).start(); // 查询交易信息
                        break;
                    case 1: // 支付失败

                    default:
                        status = "0";
//                        new Thread(payStatusTask).start();
                        break;
                }
            }
        }
    }

    /**
     * 接收支付结果信息的广播
     */
    public void registerBroadcastReceiver() {
        // 注册广播
        IntentFilter counterActionFilter = new IntentFilter("wxPay");
        receiver = new PayBrodcastReceiver();
        registerReceiver(receiver, counterActionFilter);
    }

    /**
     * 支付完成后
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 支付失败
                    Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    break;

                case 1: // 获取微信支付信息成功 进行支付
                    if (wxInfo != null) {
                        IWXAPI api = WXAPIFactory.createWXAPI(PayActivity.this, wxInfo.getAppid(), false);
                        // 真正的支付
                        api.sendReq(buildWechatPayReq());
                    } else {
                        Toast.makeText(PayActivity.this, "获取支付信息失败", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2: // 获取支付宝支付信息成功 进行支付
                    aliPay();
                    break;

                case 3: // 获取支付信息失败
                    Toast.makeText(PayActivity.this, "获取支付信息失败", Toast.LENGTH_SHORT).show();
                    break;

                case 4: // 微信支付成功
//                    new Thread(wxPayTask).start();
//                    JPushInterface.resumePush(getApplicationContext());
//                    finish();
//                    EventBus.getDefault().post("refresh");
                    break;

                case 5://成功完全查询
                    new Thread(payInfoTask).start(); // 查询交易信息
                    break;

                case 6: // 支付宝支付信息
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                       // Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        status = "1";
                        new Thread(payStatusTask).start();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                           // Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            status = "0";
//                            new Thread(payStatusTask).start();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                         //   Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            status = "0";
//                            new Thread(payStatusTask).start();
                        }
                    }
                    break;
                case 7:

                    break;
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        isClickWX = true;
        isClickZFB = true;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * 获取支付信息
     */
    Runnable getPayInfoTask = new Runnable() {
        @Override
        public void run() {
            Message message = Message.obtain();
            JSONObject object = new JSONObject();
            try {
                object.put("customerId", "1");
                object.put("orderId", ORDER_ID);
                object.put("payType", payType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient httpClient = new OkHttpClient();
            try {
                JrController.setCertificates(PayActivity.this, httpClient, PayActivity.this.getAssets().open("zhenjren.cer"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String token = PreferencesUtils.getString(PayActivity.this, Consts.TOKEN);
            RequestBody body = RequestBody.create(JSON, object.toString());
            Request request = new Request.Builder()
                    .url(API.TEMP)
                    .post(body)
                    .addHeader("token", token)
                    .tag(this)
                    .build();
            httpClient.setConnectTimeout(40, TimeUnit.SECONDS);
            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                if (response.code() != 401) {
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        if ("1".equals(payType)) {
                            String res = response.body().string();
                            wxInfo = gson.fromJson(res, PayWxInfo.class);
                            message.what = 1;


                        } else { // 支付宝支付
                            String res = response.body().string();
                            aliInfo = gson.fromJson(res, PayAliInfo.class);
                            message.what = 2;
                        }
                    } else {
//                    showToast("订单发布失败..");
                    }
                }else if (response.code() == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PayActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 微信支付

//                }
            handler.sendMessage(message);
        }
    };

    /**
     * 修改订单状态
     */
    Runnable payStatusTask = new Runnable() {
        @Override
        public void run() {
            Message message = Message.obtain();
            Response response;
            try {
                if (bean != null) {
                    OkHttpClientManager.Param params[] = new OkHttpClientManager.Param[]{
                            new OkHttpClientManager.Param(Consts.ORDERID, bean.getOrder_id())
                            , new OkHttpClientManager.Param(Consts.STATUS, status)
                    };
                    response = OkHttpClientManager.post(API.PAYSTATUS, PayActivity.this, params[0], params[1]);
                    Log.d("payStatusTask", response.toString());
                    if (response.isSuccessful()) {
                        message.what = 5;
                        handler.sendMessage(message);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
//            message.what = 0; // 支付失败

        }
    };

    /**
     * 向服务器查询支付信息
     */
    Runnable payInfoTask = new Runnable() {
        @Override
        public void run() {

            String url = API.ORDERPAYSTATUS;
            Map<String, String> map = new HashMap<>();
            map.put(Consts.ORDERID, bean.getOrder_id());
            map.put(Consts.STATUS, status);
            url = JrUtils.appendParams(url, map);

            NetLoader.getInstance(PayActivity.this).loadOrdinaryPostData(PayActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int type) {
                    if (type == 200) {
                        Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderEntity", bean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        PayActivity.this.finish();
                    }else if (type == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PayActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {

                }
            }, null);
        }
    };

    /**
     * 现金支付
     **/
    private void clickXianJin() {
        final SurePayDialog dialog = new SurePayDialog(this);
        dialog.show();
        dialog.setOnRushClickBtnLinsener(new SurePayDialog.OnRushClickBtnLinsener() {
            @Override
            public void onRushClickBtn(View v) {
                switch (v.getId()) {
                    case R.id.payCancleBtn://取消
                        dialog.dismiss();
                        break;
                    case R.id.paySureBtn://确定
                        dialog.dismiss();
                        sendAliPay();
//                        startWorker();
                        break;
                }
            }
        });
    }

    private WaittingDiaolog startDialog;

    private void startWorker() {
        startDialog = new WaittingDiaolog(PayActivity.this);
        String url = API.ALTERORDERSTAUTE;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDER_ID, bean.getOrder_id());
        map.put(Consts.ORDER_STATUS, "6");
        map.put(Consts.PARAM, "");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(PayActivity.this).loadGetData(PayActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                startDialog.dismiss();
                if (code == 200) {
                    Log.i("geanwen666", "resultString : " + resultString);
                    Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderEntity", bean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    PayActivity.this.finish();
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PayActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                startDialog.dismiss();
                Log.i("geanwen", "stauteAltear : " + failString);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND) //在ui线程执行
    public void onUserEvent(String str) {
        if (str != null) {
            if (str.equals("wx0")) {
                status = "1";
                new Thread(payStatusTask).start();
//                sendAliPay();
            }
        }
    }

    private void sendAliPay() {
        String url = API.PAY;
        Map<String, String> map = new HashMap<>();
//        TODO 需要真实数据 ORDER_ID
        map.put(Consts.ORDER_ID, bean.getOrder_id());
        map.put(Consts.PAYTYPE, payType);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(PayActivity.this).loadGetData(PayActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200) {
                    Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderEntity", bean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    PayActivity.this.finish();
//                    handler.sendEmptyMessage(1101);
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PayActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen", failString);
            }
        });
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(2222);
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver); // 取消广播
        }
        EventBus.getDefault().unregister(this);
    }


    public void getPay(String data, final Context context, int ChooseWhat, final Activity activity){

        if(ChooseWhat==1){
            //微信支付
            Gson gson=new Gson();

            wxInfo = gson.fromJson(data, PayWxInfo.class);
            IWXAPI api = WXAPIFactory.createWXAPI(context, wxInfo.getAppid(), false);
            // 真正的支付
            PayReq payReq = new PayReq();
            payReq.appId = wxInfo.getAppid();
            payReq.partnerId = wxInfo.getPartnerid();
            payReq.prepayId = wxInfo.getPrepayid();
            payReq.packageValue = wxInfo.getPackage_();
            payReq.nonceStr = wxInfo.getNoncestr();
            payReq.timeStamp = wxInfo.getTimestamp();
            payReq.sign = wxInfo.getSign();
            api.sendReq(payReq);
        }else if (ChooseWhat==2){
            //支付宝支付

           Gson gson=new Gson();
            aliInfo = gson.fromJson(data, PayAliInfo.class);
            // 签名
            String sign = aliInfo.getSign();
            try {
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        /* 完整的符合支付宝参数规范的订单信息 */
            String orderInfo = getOrderInfo();
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                    + "sign_type=\"" + aliInfo.getSignType() + "\"";

            Log.d("PayActivity", payInfo);
//        // 调用支付接口，获取支付结果
//        String result = alipay.pay(payInfo, true);
//        Log.d("PayActivity", result);

//        Message msg = new Message();
//        msg.what = 6;
//        msg.obj = result;
//        handler.sendMessage(msg);

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(activity);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);
                    Log.d("PayActivity", result);

                    Message msg = new Message();
                    msg.what = 6;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            };
            // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
            new Thread(payRunnable).start();
        }


    }



}
