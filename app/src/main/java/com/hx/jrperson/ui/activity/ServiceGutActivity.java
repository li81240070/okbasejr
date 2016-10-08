package com.hx.jrperson.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.ServiceThreeEntity;
import com.hx.jrperson.bean.entity.ServicesParentEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.adapter.ServiceParentsAdapter;
import com.hx.jrperson.aboutnewprogram.secondversion.LiListView;
import com.hx.jrperson.aboutnewprogram.secondversion.SecondAdapterForText;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.PersonalListView;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.hx.jrperson.views.widget.RecyclerViewDivider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 点击气泡跳转到本页面
 * 本页面是具体的维修项目列表页面
 * by ge
 **/
public class ServiceGutActivity extends BaseActivity implements ServiceParentsAdapter.OnClickServiceParentsLisener,SecondAdapterForText.OnClickIssueOrdorListener {
    private RecyclerView serviceGutForChoose;
    private GridLayoutManager manager;
    private ServiceParentsAdapter adapter;
    private String parentCode;
    private String mainCode;
    private WaittingDiaolog diaolog;
    private List<ServicesParentEntity.DataMapBean.ServicesBean> list = new ArrayList<>();
    //当前所在位置信息记录
    private int myLocation ;
    public static ServiceGutActivity inance = null;
    private List<ServiceThreeEntity.DataMapBean.ServicesBean> serviceList = new ArrayList<>();
    private Handler handler;
    ////////////////////////////////////
    private ImageView backButtonForListview;
    private TextView textviewForListview;
    private String title = "";
    private RelativeLayout backButtonInFatherList;
    //第二次改版的相关声明
    private TabLayout tablayoutInChoose;
    //第二次改版小页面相关内容
    private LiListView serviceListViewInChoose;
    private SecondAdapterForText serviceAdapter;
    ////注册广播相关内容
    private MySendReciver mysendreciver;
    //本地判断下拉打开按钮状态
    private ArrayList<Boolean> isOpen = new ArrayList();
    //本地记录当前页面的选中状态
    private ArrayList<Integer> isChoose = new ArrayList();
    //计价相关
    private double allPrice = 0;//
    private TextView allPriceTV;
    //灰色计数卡
    private int garyNum=0;
    //跳转到下单页面的按钮
    private TextView goForOrder;

    //总价
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String allPriceS = String.format("%.2f", allPrice);
            String myPrice = allPriceS.replace(".00", "");
            allPriceTV.setText(myPrice);
            allPriceTV.setTextSize(30);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册本页的见听广播
        //创建新的广播接收者
        mysendreciver = new MySendReciver();
//相当于注册页面的操作
        IntentFilter intentFilter = new IntentFilter();

//里面放的是自定义的内容
        intentFilter.addAction("com.hx.jrperson.broadcast.MY_BROAD");

//与接收系统的一样
        registerReceiver(mysendreciver,intentFilter);
        inance = this;
        setContentView(R.layout.activity_service_gut);
        showTitle();
        initView();
        initData();
        setListener();
        //计价组件绑定
        allPriceTV = (TextView) findViewById(R.id.allPriceTV);//总价
        //跳转到下单页面
        goForOrder= (TextView) findViewById(R.id.goForOrder);
        goForOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ServiceGutActivity.this, IssueOrdorGutActivity.class);
                Bundle bundle = new Bundle();
                //进行选中数据传递
                for (int i = 0; i < isChoose.size(); i++) {

                        bundle.putString(String.valueOf(i),isChoose.get(i).toString());

                }

                //////////////////////////////////
                bundle.putSerializable("serviceParent", list.get(myLocation));
                bundle.putString("parent_code", parentCode);
                bundle.putString("mainCode", mainCode);
                bundle.putSerializable("serviceList", (Serializable) serviceList);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        //返回按钮相关代码
        ///////////////////////////////////////////////
        backButtonForListview = (ImageView) findViewById(R.id.backButtonForListview);

        backButtonForListview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceGutActivity.this.finish();
            }
        });
        backButtonInFatherList = (RelativeLayout) findViewById(R.id.backButtonInFatherList);
        backButtonInFatherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceGutActivity.this.finish();
            }
        });
        ///////////////////////////////////////////
        //第二次更新版本相关操作
        //注册代码
        tablayoutInChoose = (TabLayout) findViewById(R.id.tablayoutInChoose);
        TabLayout.Tab tab1 = tablayoutInChoose.newTab().setText("我家装修");
        tablayoutInChoose.addTab(tab1);
        TabLayout.Tab tab2 = tablayoutInChoose.newTab().setText("水维修");
        tablayoutInChoose.addTab(tab2);
        TabLayout.Tab tab3 = tablayoutInChoose.newTab().setText("电维修");
        tablayoutInChoose.addTab(tab3);
        TabLayout.Tab tab4 = tablayoutInChoose.newTab().setText("装修监控");
        tablayoutInChoose.addTab(tab4);
        TabLayout.Tab tab5 = tablayoutInChoose.newTab().setText("居家小修");
        tablayoutInChoose.addTab(tab5);
        TabLayout.Tab tab6 = tablayoutInChoose.newTab().setText("居家安装");
        tablayoutInChoose.addTab(tab6);
        TabLayout.Tab tab7 = tablayoutInChoose.newTab().setText("货车力工");
        tablayoutInChoose.addTab(tab7);
        TabLayout.Tab tab8 = tablayoutInChoose.newTab().setText("家电清洗");
        tablayoutInChoose.addTab(tab8);

        //设置引导线颜色
        tablayoutInChoose.setSelectedTabIndicatorColor(0xff3399ff);
        //改变选中字体和未选中字体颜色
        tablayoutInChoose.setTabTextColors(0xff828282, 0xff3399ff);

        //显示小分页相关内容
        serviceListViewInChoose = (LiListView) findViewById(R.id.serviceListViewInChoose);
        serviceAdapter = new SecondAdapterForText(this);
        //为各个按钮设置对应的页面刷新
        tablayoutInChoose.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("cccccc","当前tab为"+tab.getPosition());
                //我家装修
                if (tab.getPosition()==0){
                    parentCode="6001";
                    initData();
                }
                //水维修
                else if (tab.getPosition()==1){
                    parentCode="1001";
                    initData();
                }
                //电维修
                else if (tab.getPosition()==2){
                    parentCode="2001";
                    initData();
                }
                //装修监控
                else if (tab.getPosition()==3){
                    parentCode="5001";
                    initData();
                }
                //居家小修
                else if (tab.getPosition()==4){
                    parentCode="3001";
                    initData();
                }
                //居家安装
                else if (tab.getPosition()==5){
                    parentCode="4001";
                    initData();
                }
                //货车力工
                else if (tab.getPosition()==6){
                    parentCode="8001";
                    initData();
                }
                //家电清洗
                else if (tab.getPosition()==7){
                    parentCode="7001";
                    initData();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    //进入页面设置
    private void showTitle() {
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            parentCode = intent.getStringExtra("parentCode");
            mainCode = intent.getStringExtra("parentCode");
            showToolBar(title, true, this, false);
        } else {

        }
    }

    @Override
    protected void initView() {
        serviceGutForChoose = (RecyclerView) findViewById(R.id.serviceGutLV);
        textviewForListview = (TextView) findViewById(R.id.textviewForListview);
        ///////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////
    }

    @Override
    protected void initData() {
        handler = new Handler();
        diaolog = new WaittingDiaolog(ServiceGutActivity.this);
        diaolog.show();
        adapter = new ServiceParentsAdapter(ServiceGutActivity.this);
        serviceGutForChoose.setAdapter(adapter);
        manager = new GridLayoutManager(this, 1);
        serviceGutForChoose.setLayoutManager(manager);
        serviceGutForChoose.addItemDecoration(new RecyclerViewDivider(ServiceGutActivity.this, LinearLayoutManager.VERTICAL));
        String url = API.SUBSERCIVE;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.SUBSERVICES, parentCode);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(ServiceGutActivity.this).loadGetData(ServiceGutActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                list.clear();
                //      Log.i("geanwen找timeout:resultString", code + "");
                diaolog.dismiss();
                if (code != 401) {
                    if (code == 200) {
                        Gson gson = new Gson();
                        ServicesParentEntity entity = gson.fromJson(resultString, ServicesParentEntity.class);
                        if (entity != null) {
                            /////////////////////////////////////////////
                            for (int i = 0; i < 20; i++) {
                                for (int j = 0; j < entity.getDataMap().getServices().size(); j++) {
                                    if (entity.getDataMap().getServices().get(j).getService().length() <= i) {
                                        list.add(entity.getDataMap().getServices().get(j));
                                        entity.getDataMap().getServices().remove(j);

                                        j--;
                                    }
                                }
                            }
                            //////////////////////////////////////////////
                          adapter.addData(list);

                            adapter.notifyDataSetChanged();

                            setListener();

                            //第一次进入该页面刷新出来的内容
                            getDataInFirst(0);
                            garyNum=0;
                            //进行弹出归零
                            isOpen.clear();
                            isChoose.clear();

                        }
                    }
                } else if (code == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ServiceGutActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                // Log.i("geanwen找timeout:failString", e.toString());
                diaolog.dismiss();
            }
        });
        ////////////////////////////
        textviewForListview.setText(title);
        //////////////////////////////////////
    }


    @Override
    protected void setListener() {

        adapter.setOnClickServiceParentsLisener(this);
        //增加对加减号按钮的操作监听

    }

    @Override
    public void OnClickServiceParents(View v, final int Position) {

        Log.i("dddddd",serviceGutForChoose.getChildCount()+"******");
                //改变灰色选项卡的技术
//                serviceGutForChoose.getChildAt(garyNum).setBackgroundColor(0xffffffff);
//                serviceGutForChoose.getChildAt(Position).setBackgroundColor(0xffeeeeee);
//                garyNum = Position;
//                Log.i("cccccc", Position + "*************");

        //进行弹出归零
        isOpen.clear();
        isChoose.clear();

        Log.i("tttttt",list.size()+"******");
        parentCode = list.get(Position).getSrv_code() + "";
        final WaittingDiaolog waittingDiaolog = new WaittingDiaolog(this);
        waittingDiaolog.show();
        String url = API.SUBSERCIVE;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.SUBSERVICES, list.get(Position).getSrv_code() + "");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(ServiceGutActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                waittingDiaolog.dismiss();
                if (code == 200) {

                    Gson gson = new Gson();
                    ServiceThreeEntity entity = gson.fromJson(resultString, ServiceThreeEntity.class);
                    serviceList = entity.getDataMap().getServices();
                    Intent intent = new Intent(ServiceGutActivity.this, IssueOrdorGutActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("serviceParent", list.get(Position));
                    bundle.putString("parent_code", parentCode);
                    bundle.putString("mainCode", mainCode);
                    bundle.putSerializable("serviceList", (Serializable) serviceList);
                    intent.putExtras(bundle);
                    //确定跳转页面的位置
                    myLocation=Position;
                   // startActivity(intent);
                    serviceAdapter=new SecondAdapterForText(ServiceGutActivity.this);
                    serviceAdapter.addData(serviceList);

                    serviceListViewInChoose.setAdapter(serviceAdapter);
                    serviceAdapter.notifyDataSetChanged();

                    setListViewHeightBasedOnChildren(serviceListViewInChoose);

                    for (int i = 0; i < serviceList.size(); i++) {

                        isOpen.add(false);
                        isChoose.add(0);

                    }
                    //进行价格计算适配
                    allPrice=0;

                    serviceAdapter.setOnClickIssueOrdorListener(ServiceGutActivity.this);


                } else if (code == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ServiceGutActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                waittingDiaolog.dismiss();
            }
        });

    }


    //第一次进入该页面默认显示内容
    public void getDataInFirst(int number) {


        String url = API.SUBSERCIVE;
        Map<String, String> map = new HashMap<>();
        //一次菜单选择项
        map.put(Consts.SUBSERVICES, list.get(number).getSrv_code() + "");
        //设置跳转的方向
        myLocation=number;
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(ServiceGutActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {


                Gson gson = new Gson();
                ServiceThreeEntity entity = gson.fromJson(resultString, ServiceThreeEntity.class);
                serviceList=null;
                serviceList = entity.getDataMap().getServices();

                serviceAdapter.addData(serviceList);

                serviceListViewInChoose.setAdapter(serviceAdapter);
                serviceAdapter.notifyDataSetChanged();

                setListViewHeightBasedOnChildren(serviceListViewInChoose);

                for (int i = 0; i < serviceList.size(); i++) {
                    isOpen.add(false);
                    isChoose.add(0);
                }
                //进行价格计算适配
                allPrice=0;

                serviceAdapter.setOnClickIssueOrdorListener(ServiceGutActivity.this);
            }
            @Override
            public void fail(String failString, Exception e) {

            }

        });
    }

    /**
     * 当ListView外层有ScrollView时，需要动态设置ListView高度
     *
     * @param listView
     */
    protected void setListViewHeightBasedOnChildren(PersonalListView listView) {
        if (listView == null) return;
        SecondAdapterForText adapter = (SecondAdapterForText) listView.getAdapter();
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
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1) + 50);
        listView.setLayoutParams(params);
    }

    //组件增减长度广播接收
    class MySendReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("test");
            String jieguo = data.substring(6, data.indexOf("*"));
            int positionForChange = Integer.parseInt(jieguo);
            String changeNum = data.substring(data.indexOf("*") + 1, data.length());
            int muNum = Integer.parseInt(changeNum);


            if (data.contains("组件高度增加") && isOpen.get(positionForChange) == false) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) serviceListViewInChoose.getLayoutParams();
                params.height = serviceListViewInChoose.getHeight() + (20 + muNum * 50);
                serviceListViewInChoose.setLayoutParams(params);
                isOpen.set(positionForChange, true);
            }
            if (data.contains("组件高度减小") && isOpen.get(positionForChange) == true) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) serviceListViewInChoose.getLayoutParams();
                params.height = serviceListViewInChoose.getHeight() - (20+ muNum * 50);
                serviceListViewInChoose.setLayoutParams(params);
                isOpen.set(positionForChange, false);
            }

        }

    }

    //创建监听点击菜单加减
    @Override
    public void onClickIssueOrdor(View v, double all, int position, int befor) {
        switch (v.getId()) {
            case R.id.issue_addIB://加
                befor = befor + 1;
                //记录当前页面项目的选中状态
                isChoose.set(position,isChoose.get(position)+1);
                serviceList.get(position).setBeforCount(befor);
                serviceAdapter.addData(serviceList);
                allPrice = all + allPrice;
                handler.post(runnable);
                Log.i("nnnnnn","记录当前"+position+"打开状态为"+isChoose.get(position).toString());
                break;
            case R.id.issue_subIB://减
                befor = befor - 1;
                //记录当前页面项目的选中状态
                if (isChoose.get(position)>0) {
                    isChoose.set(position, isChoose.get(position) - 1);
                }
                serviceList.get(position).setBeforCount(befor);
                serviceAdapter.addData(serviceList);
                allPrice = all + allPrice;
                handler.post(runnable);
                Log.i("nnnnnn","记录当前"+position+"打开状态为"+isChoose.get(position).toString());
                break;
        }
    }
}