package com.hx.jrperson.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.MyOrdorEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.adapter.OrderAdapter;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.AutoListView;
import com.hx.jrperson.views.DynamicBox;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的订单界面
 * by ge
 **/
public class MyOrdorActivity extends BaseActivity implements AdapterView.OnItemClickListener, AutoListView.OnLoadListener, AutoListView.OnRefreshListener {

    private AutoListView myOrdorLV;
    private OrderAdapter ordorAdapter;
    private MyOrdorEntity entity;
    private List<MyOrdorEntity.DataMapBean.OrdersBean> list = new ArrayList<>();
    private List<MyOrdorEntity.DataMapBean.OrdersBean> allList = new ArrayList<>();

    private final int FIRST_COMING = 0;
    private final int REFRESH = 1;
    private final int LOAD = 2;
    //////////////////////////////////////////
    private RelativeLayout backButtonInMyOrder;
    private ImageView backbuttonInMyOrder;

    // private WaittingDiaolog waittingDiaolog;
    private String pageNo = "1";
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<MyOrdorEntity.DataMapBean.OrdersBean> result = (List<MyOrdorEntity.DataMapBean.OrdersBean>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH://刷新
                    myOrdorLV.onRefreshComplete();
                    ordorAdapter.addData(result);
                    break;
                case AutoListView.LOAD://加载
                    myOrdorLV.onLoadComplete();
                    ordorAdapter.addDataLoad(result);
                    break;
                case AutoListView.FIRST_COMING:
                    myOrdorLV.onRefreshComplete();
                    if (result.isEmpty()) {
                        DynamicBox box = new DynamicBox(MyOrdorActivity.this, myOrdorLV);
                        View customView = getLayoutInflater().inflate(R.layout.layout_list_empty, null, false);
                        box.addCustomView(customView,"no_orders");
                        box.showCustomView("no_orders");
                    } else {
                        ordorAdapter.addData(result);
                    }
                    break;
            }
            myOrdorLV.setResultSize(result.size());
        }
    };

    public static MyOrdorActivity insance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insance = this;
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_my_ordor);
        showToolBar("我的订单", true, this, false);
        initView();
        initData();
        setListener();
        ////////////////////////////////////////////////////
        backButtonInMyOrder= (RelativeLayout) findViewById(R.id.backButtonInMyOrder);
        backButtonInMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdorActivity.this.finish();
            }
        });
        backbuttonInMyOrder= (ImageView) findViewById(R.id.backbuttonInMyOrder);
        backbuttonInMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdorActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        myOrdorLV = (AutoListView) findViewById(R.id.myOrdorLV);
    }

    @Override
    protected void initData() {
        ordorAdapter = new OrderAdapter(this);
        myOrdorLV.setAdapter(ordorAdapter);
        myOrdorLV.setOnItemClickListener(this);
        myOrdorLV.setOnLoadListener(this);
        myOrdorLV.setOnRefreshListener(this);
        addData(AutoListView.FIRST_COMING, FIRST_COMING);
    }

    private void addData(final int what, final int type) {
        String url = API.ORDERLIST;
        final String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        if (phone != null) {
            // waittingDiaolog = new WaittingDiaolog(this);
            // waittingDiaolog.show();
            Date date = new Date();
            final long time = date.getTime();
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USER_ID, phone);
            map.put(Consts.PAGENO, pageNo);
            map.put(Consts.TIMESTAMP, time + "");
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(this).loadGetData(MyOrdorActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {

                    Log.i("cccccc","请求成功"+phone+"电话号"+pageNo+"当前页数"+time+"当前时间"+API.ORDERLIST+"网址");

                    // waittingDiaolog.dismiss();
                    if (code == 200) {
                        Gson gson = new Gson();
                        entity = gson.fromJson(resultString, MyOrdorEntity.class);
                        list = entity.getDataMap().getOrders();
                        Message message = handler.obtainMessage();
                        message.what = what;
                        message.obj = list;
                        if (type == 1) {//刷新
                            allList.clear();
                            allList.addAll(list);
                            myOrdorLV.setResultSize(10);//刷新后让listview可以继续上拉
                        } else {
                            allList.addAll(list);
                        }
                        handler.sendMessage(message);
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyOrdorActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    // waittingDiaolog.dismiss();
                }
            });

        }
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoad() {
        pageNo = pageNoAdd(pageNo);
        addData(AutoListView.LOAD, LOAD);
    }

    /**
     * 下拉刷新
     **/
    @Override
    public void onRefresh() {
        pageNo = "1";
        addData(AutoListView.REFRESH, REFRESH);
    }

    @Override
    protected void setListener() {
    }

    /**
     * 分页加载
     * 上拉时需要将pageNo加一
     **/
    private String pageNoAdd(String pageNo) {
        int a = Integer.valueOf(pageNo);
        a = a + 1;
        return String.valueOf(a);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Integer status) {
        if (status != null) {
            if (status == 2222) {//本页面从新启动时刷新数据
                pageNo = "1";
                addData(AutoListView.REFRESH, 1);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = position - 1;
        if (position == allList.size() + 1) {
            return;
        }
        String status = allList.get(pos).getOrder_status();//订单状态
        if (status.equals("1")) {//未接单
            String time = allList.get(pos).getAppoint_time();
            Intent intent2 = new Intent(this, NegotiatePriceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("orderEntity", allList.get(pos));
            bundle.putString("staute", status);
            intent2.putExtras(bundle);
            startActivity(intent2);
        } else if (status.equals("2") || status.equals("3") || status.equals("4")) {//进行中
            String time = allList.get(pos).getAppoint_time();
            Intent intent2 = new Intent(this, NegotiatePriceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("orderEntity", allList.get(pos));
            bundle.putString("staute", status);
            intent2.putExtras(bundle);
            startActivity(intent2);
        } else if (status.equals("5")) {//待付款
            Intent intent3 = new Intent(this, PayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("orderEntity", allList.get(pos));
            bundle.putInt("position", pos);
            bundle.putString("SUBJECT", allList.get(pos).getComment());
            bundle.putString("BODY", allList.get(pos).getComment());
            bundle.putString("PRICE", allList.get(pos).getPrice());
            bundle.putString("ORDER_ID", allList.get(pos).getOrder_id());
            intent3.putExtras(bundle);
            startActivity(intent3);
        } else if (status.equals("8") || status.equals("7") || status.equals("6")) {//待评价
            Intent intent4 = new Intent(this, PaySuccessActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("orderEntity", allList.get(pos));
            bundle.putString("staute", status);
            intent4.putExtras(bundle);
            startActivity(intent4);
        } else if("99".equals(status)) {
            Toast.makeText(this,"您的订单已完成",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        insance = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}


