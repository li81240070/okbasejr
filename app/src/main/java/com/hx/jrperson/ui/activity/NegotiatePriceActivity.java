package com.hx.jrperson.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.CancleCountEntity;
import com.hx.jrperson.bean.entity.MyOrdorEntity;
import com.hx.jrperson.bean.entity.OrderEntity;
import com.hx.jrperson.bean.entity.PushOrder;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.controller.adapter.NegotiateListAdapter;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.CancleOrdorDialog;
import com.hx.jrperson.views.PersonalListView;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 价格协商界面 和支付界面
 * by ge
 **/
public class NegotiatePriceActivity extends BaseActivity implements View.OnClickListener {

    private TextView cancle_ordorTV, reashTV;
   // private CircleImageView price_workerHeadIV;//匠人头像
    //姓名， 工号， 电话
    private TextView price_workerNickName, price_workerNumberTV;
    private TextView allPriceGutTV;//共计价钱
    private TextView sendMoneyIV;//付款按钮
    private TextView service_nameTV;//类别
    private TextView service_timeTV;//时间
    private MyListView serviceSubjectLV;//维修项目和数量以及价格列表
    private NegotiateListAdapter adapter;
    private Handler handler;
    private TextView takenPhoneIV;//打电话
    private String staute;//订单状态
    private Runnable inforRunnable = new Runnable() {
        @Override
        public void run() {
            String str = entity.getDataMap().getWorker_avatar();
            final String path = API.AVATER + str + "_200.jpg";
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        getAvater(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            staute = entity.getDataMap().getOrder_status() + "";
            if (!staute.equals("1")) {
                if (entity.getDataMap().getWorker_name() != null) {
                    price_workerNickName.setText(entity.getDataMap().getWorker_name());
                }
                if (entity.getDataMap().getWorker_no() != null) {
                    price_workerNumberTV.setText(entity.getDataMap().getWorker_no());
                }
                sendMoneyIV.setVisibility(View.VISIBLE);
            }
            double d = entity.getDataMap().getTotal_price();
            String st= String.format("%.2f", d);
            final String allPrice = "".concat(st).concat("");
            allPriceGutTV.setText(allPrice);//价格
            service_nameTV.setText(entity.getDataMap().getService().getGroup_name());
            String time = JrUtils.times(entity.getDataMap().getAppoint_time() / 1000 + "");
            service_timeTV.setText(time);
            if (entity.getDataMap().getOrder_status() == 5) {
                sendMoneyIV.setText("付款");
                isClick = 2;
            }else if (entity.getDataMap().getOrder_status() == 2 || entity.getDataMap().getOrder_status() == 3){
                sendMoneyIV.setText("开工");
                isClick = 0;
            }
            String title = JrUtils.orderStaute(staute);
          //  baseactivity_title_TV.setText(title);
        }
    };
    private MyOrdorEntity addData;
    private MyOrdorEntity.DataMapBean.OrdersBean bean;
    private TextView lookWorkerLocation;//查看匠人位置行布局
    private OrderEntity entity = null;
    public static NegotiatePriceActivity insance = null;
    private int isClick = 0;
    private Toast toast;
    private WaittingDiaolog startDialog;
    private String orderId = "";
    private int position = 999;
    private CancleCountEntity count;
    private int cancleCount, isUpdateMyHome = 0;//取消次数, 是否被接单0:不是。1：是
    private TextView baseactivity_title_TV;
    private boolean isLogin = true;
    /////////////////////////////////
    private ImageView backbuttonInOrder;
    private  EventBus myEventBus;
    private TextView getMyLocation,myOrderState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.ordercontent);
        getMyLocation= (TextView) findViewById(R.id.getMyLocation);
        myOrderState= (TextView) findViewById(R.id.myOrderState);
        insance = this;
//        showToolBar("", true, this, false);
        initView();
        initData();
        setListener();
        getMyLocation.setText(bean.getAddress());
        //判断当前订单头文件
        if (bean.getOrder_status().equals("0")){
            myOrderState.setText("订单状态:已取消");
        }else if (bean.getOrder_status().equals("1")){
            myOrderState.setText("订单状态:未接单");
        }else if (bean.getOrder_status().equals("2")||bean.getOrder_status().equals("3")||bean.getOrder_status().equals("4")){
            myOrderState.setText("订单状态:进行中");
        }else if (bean.getOrder_status().equals("5")){
            myOrderState.setText("订单状态:待付款");
        }else if (bean.getOrder_status().equals("6")||bean.getOrder_status().equals("7")||bean.getOrder_status().equals("8")){
            myOrderState.setText("订单状态:待评价");
        }else if (bean.getOrder_status().equals("99")){
            myOrderState.setText("订单状态:已完成");
        }
    }

    @Override
    protected void initView() {
        cancle_ordorTV = (TextView) findViewById(R.id.cancle_ordorTV);//取消
        reashTV = (TextView) findViewById(R.id.reashTV);//刷新
       // price_workerHeadIV = (CircleImageView) findViewById(R.id.price_workerHeadIV);//匠人头像
        price_workerNickName = (TextView) findViewById(R.id.price_workerNickName);//匠人昵称
        price_workerNumberTV = (TextView) findViewById(R.id.price_workerNumberTV);//匠人工号
        serviceSubjectLV = (MyListView) findViewById(R.id.serviceSubjectLV);//维修项目数量，价格列表
        allPriceGutTV = (TextView) findViewById(R.id.allPriceGutTV);//共计价格
        sendMoneyIV = (TextView) findViewById(R.id.sendMoneyIV);//付款按钮
        lookWorkerLocation = (TextView) findViewById(R.id.lookWorkerLocation);//查看匠人位置行布局
        service_nameTV = (TextView) findViewById(R.id.service_nameTV);//类别
        service_timeTV = (TextView) findViewById(R.id.service_timeTV);//时间
        takenPhoneIV = (TextView) findViewById(R.id.takenPhoneIV);//打电话
        baseactivity_title_TV = (TextView) findViewById(R.id.baseactivity_title_TV);
        ////////////////////////////////////////////////////////////
        backbuttonInOrder= (ImageView) findViewById(R.id.backbuttonInOrder);
        backbuttonInOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NegotiatePriceActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        handler = new Handler();
        adapter = new NegotiateListAdapter(NegotiatePriceActivity.this);
        serviceSubjectLV.setDividerHeight(0);
        serviceSubjectLV.setDivider(null);
        serviceSubjectLV.setAdapter(adapter);
        ///////////////////////////////////////////////

        ///////////////////////////////////////////////
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bean = (MyOrdorEntity.DataMapBean.OrdersBean) bundle.get("orderEntity");
        staute = bundle.getString("staute");
        String statue = JrUtils.orderStaute(staute);
//        showToolBar(statue, true, this, false);
        if (null != staute) {
            if ("4".equals(staute)) {
                isClick = 1;
                sendMoneyIV.setClickable(false);
                sendMoneyIV.setText("进行中");
                cancle_ordorTV.setVisibility(View.GONE);
            } else if ("1".equals(staute)) {//未接单
                sendMoneyIV.setVisibility(View.GONE);
                isUpdateMyHome = 1;
            } else if ("5".equals(staute)) {
                sendMoneyIV.setVisibility(View.VISIBLE);
                sendMoneyIV.setText("付款");
                cancle_ordorTV.setVisibility(View.GONE);
                isClick = 2;
            } else if ("2".equals(staute)) {
                isClick = 0;
            }
        }
        if (null != bean) {
            String order_id = bean.getOrder_id();
            orderId = bean.getOrder_id();
            reachView(order_id);
        } else {
            position = bundle.getInt("position");
            addData();//bean为空 请求下数据  防止直接从本页面跳转到支付页面数据为空
            orderId = bundle.getString("orderid");
            reachView(orderId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(PushOrder bean) {
        if (null != bean) {
            orderId = bean.getOrderId();
            reachView(orderId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Integer staute) {
        if (null != staute) {
            if (2000 == staute) {//价格修改
                reachView(orderId);
            } else if (2001 == staute) {//匠人完成
                reachView(orderId);
            } else if (2010 == staute) {//在本页面的时候有人抢单
                reachView(orderId);
                sendMoneyIV.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getAvater(String avatarUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JrController.setCertificates(NegotiatePriceActivity.this, client, NegotiatePriceActivity.this.getAssets().open("zhenjren.cer"));
        try {
            Request request = new Request.Builder().url(avatarUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is = response.body().byteStream();
                final Bitmap bm = BitmapFactory.decodeStream(is);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //头像相关
                      //  price_workerHeadIV.setImageBitmap(bm);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addData() {
        String url = API.ORDERLIST;
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        if (null != phone) {
            Date date = new Date();
            long time = date.getTime();
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USER_ID, phone);
            map.put(Consts.PAGENO, "1");
            map.put(Consts.TIMESTAMP, time + "");
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(this).loadGetData(NegotiatePriceActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200) {
                        Gson gson = new Gson();
                        addData = gson.fromJson(resultString, MyOrdorEntity.class);
                        bean = addData.getDataMap().getOrders().get(position);
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                isLogin = false;
                                Toast.makeText(NegotiatePriceActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
    }

    //刷新页面
    private void reachView(String order_id) {
        final WaittingDiaolog waittingDiaolog = new WaittingDiaolog(this);
        waittingDiaolog.show();
        String url = API.ORDER_DETAIL;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDER_ID, order_id);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(NegotiatePriceActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                waittingDiaolog.dismiss();
                if (code == 200) {
                    Gson gson = new Gson();
                    entity = gson.fromJson(resultString, OrderEntity.class);
                    if (entity.getDataMap() != null) {
                        if (null != entity.getDataMap().getService().getChildren()) {
                            adapter.addData(entity.getDataMap().getService().getChildren());
                        }
                        handler.post(inforRunnable);
                    }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            isLogin = false;
                            Toast.makeText(NegotiatePriceActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                waittingDiaolog.dismiss();
                Log.i("geanwen", failString);
            }
        });
    }

    @Override
    protected void setListener() {
        cancle_ordorTV.setOnClickListener(this);
        reashTV.setOnClickListener(this);
        sendMoneyIV.setOnClickListener(this);
        lookWorkerLocation.setOnClickListener(this);
        takenPhoneIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle_ordorTV://取消
                if (isLogin){
                    if (isUpdateMyHome == 0) {//单已经被接
                        clickCancle();
                    } else {
                        cancleDialog();//没人接单
                    }
                }else {
                    showToast("此账号已在别处登录");
                }
                break;
            case R.id.reashTV://刷新
                if (isLogin){
                    if (bean != null) {
                        reachView(orderId);
                    } else {
                        reachView(orderId);
                    }
                }else {
                    showToast("此账号已在别处登录");
                }
                break;
            case R.id.sendMoneyIV://干活
                if (isLogin){
                    if (isClick == 0) {
                        if (((int)entity.getDataMap().getTotal_price()) == 0){
                            showToast("请等待匠人调整价格");
                        }else {
                            startWorker();
                        }
                    } else if (isClick == 2) {//付款
                        Intent intent = new Intent(NegotiatePriceActivity.this, PayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderEntity", bean);
//                    bundle.putString("SUBJECT", bean.getComment());
//                    bundle.putString("BODY", bean.getComment());
//                    bundle.putString("PRICE", bean.getPrice());
                        bundle.putString("ORDER_ID", bean.getOrder_id());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        this.finish();
                    } else if (isClick == 1) {
                        showToast("工人正在作业，请等待完工！");
                    }
                }else {
                    showToast("此账号已在别处登录");
                }
                break;
            case R.id.lookWorkerLocation://查看匠人位置
                if (isLogin){
                    if (!staute.equals("1")) {
                        clickLocation();
                    } else {
                        showToast("订单处于未接单状态");
                    }
                } else if (isClick == 1) {
                    showToast("工人正在作业，请等待完工！");
                }
                break;
            case R.id.takenPhoneIV://打电话
                if (isLogin){
                    if (!staute.equals("1")) {
                        clickCallPhone(entity.getDataMap().getWorker_mobile());
                    } else {
                        showToast("订单处于未接单状态");
                    }
                } else if (isClick == 1) {
                    showToast("工人正在作业，请等待完工！");
                }
                break;
        }
    }

    //没人接单情况下 可以直接取消
    private void cancleDialog() {
        //取消订单dialog 1:进行中取消 2:未接单取消
        final CancleOrdorDialog cancleOrdorDialog = new CancleOrdorDialog(this, cancleCount, 2);
        cancleOrdorDialog.show();
        cancleOrdorDialog.setOnClickCancleOrdorListener(new CancleOrdorDialog.OnClickCancleOrdorListener() {
            @Override
            public void onClickCancleOrdor(View view) {
                switch (view.getId()) {
                    case R.id.cancle_ordor_cancleTV://取消
                        cancleOrdorDialog.dismiss();
                        break;
                    case R.id.cancle_ordor_sureTV://确认
                        cancleOrdorDialog.dismiss();
                        String url = API.ALTERORDERSTAUTE;
                        Map<String, String> map = new HashMap<>();
                        map.put(Consts.ORDER_ID, entity.getDataMap().getOrder_id() + "");
                        map.put(Consts.ORDER_STATUS, "0");
                        map.put(Consts.PARAM, "");
                        url = JrUtils.appendParams(url, map);
                        NetLoader.getInstance(NegotiatePriceActivity.this).loadGetData(NegotiatePriceActivity.this, url, new NetLoader.NetResponseListener() {
                            @Override
                            public void success(String resultString, int code) {
                                if (code == 200) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(NegotiatePriceActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    NegotiatePriceActivity.this.finish();
                                }else if (code == 401){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(NegotiatePriceActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void fail(String failString, Exception e) {
                                Log.i("geanwen", failString);
                            }
                        });
                        break;
                }
            }
        });

    }

    private void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(NegotiatePriceActivity.this, str, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(str);
            toast.show();
        }
    }

    //查看匠人位置

    private void clickLocation() {
        EventBus.getDefault().post(entity.getDataMap());
        Intent intent = new Intent(NegotiatePriceActivity.this, OtherTest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle=new Bundle();
        bundle.putSerializable("user",entity.getDataMap());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    //开始干活
    private void startWorker() {
        startDialog = new WaittingDiaolog(NegotiatePriceActivity.this);
        startDialog.show();
        String url = API.ALTERORDERSTAUTE;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDER_ID, entity.getDataMap().getOrder_id() + "");
        map.put(Consts.ORDER_STATUS, "4");
        map.put(Consts.PARAM, "");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(NegotiatePriceActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                startDialog.dismiss();
                if (code == 200) {
                    isClick = 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            baseactivity_title_TV.setText("进行中");
                            sendMoneyIV.setText("进行中");
                            cancle_ordorTV.setVisibility(View.GONE);
                            showToast("已开工");
                        }
                    });
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NegotiatePriceActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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

    //取消dialog
    private void clickCancle() {
        String url = API.CANCLEFLAG;
        String phone = PreferencesUtils.getString(NegotiatePriceActivity.this, Consts.PHONE_PF);
        Map<String, String> map = new HashMap<>();
        map.put(Consts.MOBILE, phone);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(NegotiatePriceActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200) {
                    Gson gson = new Gson();
                    count = gson.fromJson(resultString, CancleCountEntity.class);
                    cancleCount = count.getDataMap().getCancel_count();
                    if (cancleCount < 2) {
                        showCancleDialog(cancleCount);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showToast("取消次数已达上限,不可取消");
                            }
                        });
                    }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NegotiatePriceActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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

    /**
     * 去后台请求本帐号今天取消订单得次数
     * 如果大于二 则不能取消订单
     * 小于二则走入本方法
     **/
    private void showCancleDialog(int cancleCount) {
        final CancleOrdorDialog cancleOrdorDialog = new CancleOrdorDialog(this, cancleCount, 1);
        cancleOrdorDialog.show();
        cancleOrdorDialog.setOnClickCancleOrdorListener(new CancleOrdorDialog.OnClickCancleOrdorListener() {
            @Override
            public void onClickCancleOrdor(View view) {
                switch (view.getId()) {
                    case R.id.cancle_ordor_cancleTV://取消
                        cancleOrdorDialog.dismiss();
                        break;
                    case R.id.cancle_ordor_sureTV://确认
                        cancleOrdorDialog.dismiss();
                        Intent intent = new Intent(NegotiatePriceActivity.this, CancleBecauseActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("staute", entity.getDataMap().getOrder_id() + "");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    //给匠人打电话
    private void clickCallPhone(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NegotiatePriceActivity.this.startActivity(callIntent);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(2222);
        insance = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }/////////////////////////////////////////////////////
    /**
     * 当ListView外层有ScrollView时，需要动态设置ListView高度
     * @param listView
     */
    protected void setListViewHeightBasedOnChildren(PersonalListView listView) {
        if(listView == null) return;
        NegotiateListAdapter adapter = (NegotiateListAdapter) listView.getAdapter();
        if (adapter == null) {

            Log.i("wwwwww", "adapter is null");
            return;
        }
        Log.i("wwwwww", "adapter not null");
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1)+50);
        listView.setLayoutParams(params);
    }

}
