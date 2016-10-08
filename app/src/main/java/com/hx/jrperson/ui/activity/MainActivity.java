package com.hx.jrperson.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.GainMessageEntity;
import com.hx.jrperson.bean.entity.GainNewInfor;
import com.hx.jrperson.bean.entity.IsOnLocationEntity;
import com.hx.jrperson.bean.entity.OrderEntity;
import com.hx.jrperson.bean.entity.PersonalInforEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.aboutnewprogram.AdapterForHomePage;
import com.hx.jrperson.aboutnewprogram.AdapterForMainViewPager;
import com.hx.jrperson.aboutnewprogram.HomePageBean;
import com.hx.jrperson.aboutnewprogram.mywollet.MyWollet;
import com.hx.jrperson.aboutnewprogram.thirdversion.MainActFragment;
import com.hx.jrperson.aboutnewprogram.thirdversion.MainAdapter;
import com.hx.jrperson.aboutnewprogram.thirdversion.MainFirstPageFragment;
import com.hx.jrperson.aboutnewprogram.thirdversion.MainMineFragment;
import com.hx.jrperson.aboutnewprogram.thirdversion.MainOrderFragment;
import com.hx.jrperson.service.WorkerLocationService;
import com.hx.jrperson.utils.JrAnimationsHelp;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.NetWorkUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.CallPhoneDialog;
import com.hx.jrperson.views.ShowBigPhotoDialog;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.hx.jrperson.views.widget.CircleImageView;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 主页面 百度地图 + 侧滑抽屉 + 小球动画 + 抢险抢修
 * by ge
 **/
public class MainActivity extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {
    //抽屉中各个行布局
    private LinearLayout item_personal_order, item_personal_liucheng,
            item_personal_biaozhun, item_personal_set, item_personal_about_us, item_out_login;
    private ImageView navifation_myIV, navifation_messageIV;//toolbar两侧按钮
    private ImageView rush_to_dealIV, start_ball_enterIV, takenPhoneIV;//抢险抢修, 小球进入动画按钮, 接单匠人信息打电话按钮
    private ImageView waterIv, eleIv, houseKeepingIv, homeTrimIv, safeIv, reMoveIv, setupIv, upgradleIv;//八个小球
    private boolean isStart = false;//判断小球是否进入屏幕
    private DrawerLayout mDrawerLayout;//抽屉
    private NavigationView mNavigationView;
    private CircleImageView head_imgIV, workerHeadIV;//头像, 接单匠人师傅的头像
    private TextView nick_nameTV, signatrueTV;//昵称, 签名
    private boolean isLogin = false;//是否登陆
    private MapView mapView;//地图
    //private LocationClient mLocationClient;//定位相关
    private BaiduMap mbaiduMap;
    //private MyLocationListener myLocationListener;
    private boolean isFristIn = true;//是否是第一次定位
    private double mLatitude, mLongtitude;//经纬度
    private BitmapDescriptor mMarker;//覆盖物相关
    private PersonalInforEntity entity;//个人信息
    private Toast toast;
    private int wid, hei;//屏幕宽高
    private RelativeLayout isGoneRl, personal_headRL, isNotOrdorRl;//显示3秒隐藏的布局 相关
    private Handler handler;
    private WaittingDiaolog outLoginDialog;//玩命加载中的dialog
    private ImageView backMainIV;//回到地图
    private boolean isShowing;//是否在当前页面
    private OrderEntity.DataMapBean dataMapBean;//订单被抢时弹出的dialog需要的数据
    private String myAddress = "";//定位后根据经纬度反检索自己的位置 存入缓存 发布订单页面和抢险抢修会用到
    private ImageButton backMyLocationIB;//回到我的位置
    private boolean fristLocation = true;
    public static MainActivity insance = null;
    private boolean lookWorkerLocation = false;
    private LinearLayout head_negivityRL, firstPart_RL;//个人信息总布局
    private LocationReceiver receiver;//接收服务传过来的匠人位置
    private String city;//当前城市
    private boolean isStartService = true;//是否开启后台服务
    private int isBallClick = 1;//记录小球点击的次数 防止多次点击 项目列表页面会多次弹出
    private GainMessageEntity gainEntity;
    private String clickTimes;
    ///////////////////////////////
    private ImageView myHomePage, moreInHomePage;
    private RelativeLayout myHomePage2;
    private RecyclerView recyclerInHomePage;
    private AdapterForHomePage adapter;
    private ArrayList<HomePageBean> data;
    ///////////////////////////////////////
    private SharedPreferences sp;
    private SharedPreferences sp2;
    /////////////////////////////////////
    //二维码相关点击事件
    private ImageView imageView3forcode;
    //文字动画的控件计数器
    private int textNum=0;
    //微信分享注册
    private IWXAPI api;

    //轮播图相关内容
    private ViewPager mainactivityViewPager;
    private ArrayList dataForViewPager;
    private AdapterForMainViewPager adapter2;
    private boolean userTouch = false;//判断用户是否触摸
    private boolean threadAlive = true;//用来销毁线程的
    private Handler handler2;//刷新UI的
    ///////////////////////////////////////////
    //获取当前位置信息
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    MapView mMapView = null;
    private String myAdressOk = "没有地址";
    //光影效果的计数器
    private CountDownTimer timer,timer2;
    private int timerNum=1,timerNum2=1;
    //增加文字的点击动画
    private TextView happyText;
    /////////////////////////////////////
    //第三版布局内容
    private ViewPager mainViewPager;
    private TabLayout mainTablayout;
    private MainAdapter mainAdapter;
    private ArrayList fragments;
    //viewpager自动滑动广播
    private MySendReciver mysendreciver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //////////////////////////////////////
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        super.onCreate(savedInstanceState);
        // 微信注册初始化
        api = WXAPIFactory.createWXAPI(this, "wxaa0851eead9e34ff", true);
        api.registerApp("wxaa0851eead9e34ff");

        //实现接收滑动到viewpager2的广播接受
        //创建新的广播接收者
        mysendreciver = new MySendReciver();
//相当于注册页面的操作
        IntentFilter intentFilter=new IntentFilter();

//里面放的是自定义的内容
        intentFilter.addAction("com.example.dllo.broadcast.GoSecondPager");

//与接收系统的一样
        registerReceiver(mysendreciver,intentFilter);

        //光影效果
        timer=new CountDownTimer(Integer.MAX_VALUE,30000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (timerNum2 != 1) {

                    //具体的图标闪光控制
                    timer2 = new CountDownTimer(1000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (timerNum % 10 == 1) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo1);
                            } else if (timerNum % 10 == 2) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo2);
                            } else if (timerNum % 10 == 3) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo3);
                            } else if (timerNum % 10 == 4) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo4);
                            } else if (timerNum % 10 == 5) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo5);
                            } else if (timerNum % 10 == 6) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo6);
                            } else if (timerNum % 10 == 7) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo7);
                            } else if (timerNum % 10 == 8) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo8);
                            } else if (timerNum % 10 == 9) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo1);
                            } else if (timerNum % 10 == 0) {
                                imageView3forcode.setBackgroundResource(R.mipmap.newlogo);
                            }

                            timerNum++;
                        }

                        @Override
                        public void onFinish() {
                            imageView3forcode.setBackgroundResource(R.mipmap.newlogo);
                            timerNum = 1;
                            timer2.cancel();
                        }
                    }.start();
                }
                timerNum2++;
            }

            @Override
            public void onFinish() {

            }
        }.start();
        //////////////////////////////////////////////////////////




        insance = this;//给本页面设置个静态变量  方便其他页面控制本页面的生命周期（又问题：静态变量消耗内存 待改善）
        /////////////////////////////////////
        SDKInitializer.initialize(getApplicationContext());
        //////////////////////////////////////
        setContentView(R.layout.activity_main);
        /////////////////////////////
        //获取是否为第一次进入该页面
        SharedPreferences sps = getSharedPreferences("ok", MODE_PRIVATE);


//向硬盘中存储,需要获得editor对象
        SharedPreferences.Editor editor2 = sps.edit();

//放数据

        editor2.putString("isfirst", "中华小当家");


//提交数据
        editor2.commit();



        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        //地图选项
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        /////////////////////////////////
        mLocationClient.start();
        initLocation();
        //数据持久化
        sp2 = getSharedPreferences("test2", MODE_PRIVATE);

        /////////////////////////////////
        //本地数据持久化
        sp = getSharedPreferences("test", MODE_PRIVATE);
        //////////////////////////////////////////////
        //设置本地轮播图相关逻辑
        mainactivityViewPager = (ViewPager) findViewById(R.id.mainactivityViewPager);
        dataForViewPager = new ArrayList();
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.mipmap.firstpicture);
        ImageView imageView2 = new ImageView(this);
        imageView2.setBackgroundResource(R.mipmap.thirdpicture);
        ImageView imageView3 = new ImageView(this);
        imageView3.setBackgroundResource(R.mipmap.secondpicture);
//        ImageView imageView4=new ImageView(this);
//        imageView4.setImageResource(R.mipmap.homebackpicture);
//        dataForViewPager.add(imageView4);
        dataForViewPager.add(imageView);
        dataForViewPager.add(imageView2);
        dataForViewPager.add(imageView3);
        adapter2 = new AdapterForMainViewPager();
        adapter2.setImageViewList(dataForViewPager);
        mainactivityViewPager.setAdapter(adapter2);

        imageView3forcode= (ImageView) findViewById(R.id.imageView3forcode);
        imageView3forcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //二维码相关
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this, CodeActivity.class);
//                startActivity(intent);
                //分享
               // shareYourIdea();
                //钱包页面相关
                Intent intent=new Intent();
               intent.setClass(MainActivity.this, MyWollet.class);
              startActivity(intent);
            }
        });
        mainactivityViewPager.setCurrentItem(Integer.MAX_VALUE % 3);
        //////
        ///////////////////////////////////////////////////////////
        //文字动画预留位置
        happyText= (TextView) findViewById(R.id.happyText);
        happyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textNum==0) {

                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    //设置动画的重复次数
                    rotateAnimation.setRepeatCount(3);
                    //设置每次动画的持续时间
                    rotateAnimation.setDuration(100);
                    //给动画绑定内容
                    happyText.startAnimation(rotateAnimation);
                    textNum=1;

                }else{
                    textNum=0;
                }

            }


        });
        ///////////////////////////////////////////////////////


        mainactivityViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //当用户触摸轮播图时
                        userTouch = true;
                        break;
                    case MotionEvent.ACTION_UP:
//当用户没有触摸轮播图时
                        userTouch = false;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadAlive) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!userTouch) {
                        handler2.sendEmptyMessage(1);
                    }
                }
            }
        }).start();
        handler2 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //将viewPage刷新到下一页
                if (msg.what == 1) {
                    mainactivityViewPager.setCurrentItem(mainactivityViewPager.getCurrentItem() + 1);
                }
                return false;
            }
        });


        ////////////////////////////////////
        myHomePage2 = (RelativeLayout) findViewById(R.id.myHomePage2);
        myHomePage = (ImageView) findViewById(R.id.myHomePage);
        moreInHomePage = (ImageView) findViewById(R.id.moreInHomePage);
        recyclerInHomePage = (RecyclerView) findViewById(R.id.recyclerInHomePage);
        adapter = new AdapterForHomePage(this);
        data = new ArrayList<>();
        data.add(new HomePageBean(R.mipmap.myhome, "我家装修"));
        data.add(new HomePageBean(R.mipmap.water, "水维修"));
        data.add(new HomePageBean(R.mipmap.electricity, "电维修"));
        data.add(new HomePageBean(R.mipmap.monitoring, "装修监控"));
        data.add(new HomePageBean(R.mipmap.maintenance, "居家小修"));
        data.add(new HomePageBean(R.mipmap.installation, "居家安装"));
        data.add(new HomePageBean(R.mipmap.van, "货车力工"));
        data.add(new HomePageBean(R.mipmap.appliance, "家电清洗"));
        data.add(new HomePageBean(R.mipmap.service, "联系客服"));
        adapter.setData(data);
        recyclerInHomePage.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerInHomePage.setAdapter(adapter);
        //Favourable activity detil button
        moreInHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toolbar右侧信息按钮
                PreferencesUtils.putString(MainActivity.this, Consts.TIME, clickTimes);//存入点击的时间，以便下次传时间到服务器，获取未查看的广告信息
                Intent intent = new Intent(MainActivity.this, InforGutActivity.class);
                startActivity(intent);
            }
        });
        myHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNavifation();
            }
        });
        myHomePage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNavifation();
            }
        });
        /////////////////////////////////
        boolean isOpean = JrUtils.isOpen(this);
        if (!isOpean) {//没开定位权限
            SharedPreferences getSp = getSharedPreferences("test", MODE_PRIVATE);
            String name1 = getSp.getString("name1", "默认");
            if (name1.equals("默认")) {

                /////////////////////////////////////////////////////////////////
                final AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                //设置图标
                alert1.setIcon(R.mipmap.newlogo);
                //设置标题
                alert1.setTitle("是否打开手机的定位功能");
                //设置主体信息
                alert1.setMessage("我们将引导您打开手机的定位功能,方便匠人为您更好的服务.");
                alert1.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                //设置消极按钮
                alert1.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });//开始,显示
                alert1.show();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name1", "中华小当家");
                editor.commit();
            }
            //showToast("请在手机设置中开启定位功能");
        }
        if (!NetWorkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "请检查您的网络", Toast.LENGTH_SHORT).show();
        }
        JrController.getVersion(MainActivity.this);//版本更新
//        JPushInterface.stopPush(MainActivity.this);
        isShowing = true;//当前页面
//        showToolBar("", true, this, true);
        EventBus.getDefault().register(this);

        initView();
        initData();
        setListener();
//        startLocation();//开始定位 相关初始化
        ////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////
        //第三版布局内容
        mainViewPager= (ViewPager) findViewById(R.id.mainViewPager);
        mainTablayout= (TabLayout) findViewById(R.id.mainTablayout);
        mainAdapter = new MainAdapter(getSupportFragmentManager());
        fragments = new ArrayList<>();
        fragments.add(new MainFirstPageFragment());
        fragments.add(new MainOrderFragment());
        fragments.add(new MainActFragment());
        fragments.add(new MainMineFragment());

        mainAdapter.setFragments(fragments);
        String[] titles = {"首页", "订单","活动","我的"};
        mainAdapter.setTitles(titles);
        mainViewPager.setAdapter(mainAdapter);
        //给tabLayout设置viewpager
        mainTablayout.setupWithViewPager(mainViewPager);
        //设置引导线颜色
        mainTablayout.setSelectedTabIndicatorColor(0xffffffff);
        //改变选中字体和未选中字体颜色
        mainTablayout.setTabTextColors(0xff868686,0xff3399ff);
        //设置下部滑动图标
       mainTablayout.getTabAt(0).setIcon(R.drawable.myhomechange);
        mainTablayout.getTabAt(1).setIcon(R.drawable.myorderchange);
        mainTablayout.getTabAt(2).setIcon(R.drawable.myactchange);
        mainTablayout.getTabAt(3).setIcon(R.drawable.myminechange);

// tabLayout.getTabAt(0).setIcon(R.drawable.tab1);

       // View view = LayoutInflater.from(this).inflate(R.layout.image,null);
//实现tablayout的附带图标功能
     //   tabLayout.getTabAt(1).setCustomView(view);



    }
////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void initView() {
        navifation_myIV = (ImageView) findViewById(R.id.navifation_myIV);//toolbar左侧图标
        navifation_messageIV = (ImageView) findViewById(R.id.navifation_messageIV);//toolbar右侧图标
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//设置抽屉DrawerLayout
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);//抽屉屏幕布局
        //rush_to_dealIV = (ImageView) findViewById(R.id.rush_to_dealIV);//抢险抢修
        // start_ball_enterIV = (ImageView) findViewById(R.id.start_ball_enterIV);//小球进入动画
        //mapView = (MapView) findViewById(R.id.mapView); //地图
        //waterIv = (ImageView) findViewById(R.id.waterIv);//水维修
        //eleIv = (ImageView) findViewById(R.id.eleIv);//电维修
        //houseKeepingIv = (ImageView) findViewById(R.id.houseKeepingIv);//家电清洗
        //homeTrimIv = (ImageView) findViewById(R.id.homeTrimIv);//居家小修
        //safeIv = (ImageView) findViewById(R.id.safeIv);//装修监控
        //reMoveIv = (ImageView) findViewById(R.id.reMoveIv);//货车力工
        //setupIv = (ImageView) findViewById(R.id.setupIv);//居家安装
        //upgradleIv = (ImageView) findViewById(R.id.upgradleIv);//我家装修
        //isGoneRl = (RelativeLayout) findViewById(R.id.isGoneRl);//通知抢险抢修后出现的布局
        firstPart_RL = (LinearLayout) findViewById(R.id.firstPart_RL);//个人信息
        item_personal_order = (LinearLayout) findViewById(R.id.item_personal_order);//我的订单
        item_personal_liucheng = (LinearLayout) findViewById(R.id.item_personal_liucheng);//服务流程
        item_personal_biaozhun = (LinearLayout) findViewById(R.id.item_personal_biaozhun);//维修标准
        item_personal_set = (LinearLayout) findViewById(R.id.item_personal_set);//设置
        item_personal_about_us = (LinearLayout) findViewById(R.id.item_personal_about_us);//关于我们
        item_out_login = (LinearLayout) findViewById(R.id.item_out_login);//退出登录
        //isNotOrdorRl = (RelativeLayout) findViewById(R.id.isNotOrdorRl);//未接单提示布局
        head_imgIV = (CircleImageView) findViewById(R.id.head_imgIV);//自己的头像
        //workerHeadIV = (CircleImageView) findViewById(R.id.workerHeadIV);//匠人师傅的头像
        nick_nameTV = (TextView) findViewById(R.id.nick_nameTV);//自己的昵称
        signatrueTV = (TextView) findViewById(R.id.signatrueTV);//自己的签名
        // backMainIV = (ImageView) findViewById(R.id.backMainIV);//回到主页
        //backMyLocationIB = (ImageButton) findViewById(R.id.backMyLocationIB);//回到我的位置
        head_negivityRL = (LinearLayout) findViewById(R.id.head_negivityRL);//抽屉总布局
    }

    @Override
    protected void initData() {
        final ShowBigPhotoDialog showBigPhotoDialog;
        WindowManager manager = this.getWindowManager();
        wid = manager.getDefaultDisplay().getWidth();
        hei = manager.getDefaultDisplay().getHeight();
        JrUtils.removeNavigationViewScrollbar(mNavigationView);
        PreferencesUtils.putInt(MainActivity.this, Consts.WID, wid);
        PreferencesUtils.putInt(MainActivity.this, Consts.HEI, hei);
        String isFrist = PreferencesUtils.getString(this, Consts.ISFRIST);
        if (isFrist == null || "".equals(isFrist)) {
            PreferencesUtils.putString(MainActivity.this, Consts.ISFRIST, "1");
            showBigPhotoDialog = new ShowBigPhotoDialog(this, "", wid, hei, 2);
            // showBigPhotoDialog.show();
            showBigPhotoDialog.setOnClickBigPhotoListener(new ShowBigPhotoDialog.OnClickBigPhotoListener() {
                @Override
                public void onClickBigPhotol(View view) {
                    showBigPhotoDialog.dismiss();
                    gainIsNewMessage();//获取是否有新的广告消息
                }
            });
        } else {
            gainIsNewMessage();//获取是否有新的广告消息
        }
        PreferencesUtils.putBoolean(this, Consts.ISLOGIN, false);
        // mbaiduMap = mapView.getMap();
        handler = new Handler();
        //关闭抽屉的手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        login();//自动登录
        receiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hx.jrperson.service.WorkerLocationService");
        MainActivity.this.registerReceiver(receiver, filter);
        gainIsNewInfor();//获取是否有新的消息（toolbar右侧按钮下一级页面的消息）
    }

    /**
     * 获取是否有新的消息内容
     **/
    private void gainIsNewInfor() {
        String url = API.GAININFOR;
        String times = "";
        times = PreferencesUtils.getString(MainActivity.this, Consts.TIME);
        Date date = new Date();
        long time = date.getTime();
        url = url.concat(times.equals("") ? "?timeStamp=" + time : "?timeStamp=" + times);
        NetLoader.getInstance(MainActivity.this).loadGetData(url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int type) {
                if (type == 200) {
                    Gson gson = new Gson();
                    final GainNewInfor entitys = gson.fromJson(resultString, GainNewInfor.class);
                    if (entitys.getCode() == 200) {
                        clickTimes = entitys.getDataMap().getTimeStamp();//暂时存储时间变量，在点击的时候在将本变量存入缓存，记录时间
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (entitys.getDataMap().isHaveUpdate()) {
//                                    navifation_messageIV.setImageResource(R.mipmap.have_message);
                                } else {
                                    //                                  navifation_messageIV.setImageResource(R.mipmap.have_no_message);
                                }
                            }
                        });

                    } else if (entitys.getCode() == 500) {

                    }
                } else if (type == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {

            }
        });

    }

    /**
     * 获取是否有新的广告消息
     **/
    private void gainIsNewMessage() {
        String api = API.GAINMESSAGE;
        NetLoader.getInstance(MainActivity.this).loadGetData(api, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int type) {
                if (type == 200) {
                    if (!resultString.equals("")) {
                        Gson gson = new Gson();
                        GainMessageEntity entity = gson.fromJson(resultString, GainMessageEntity.class);
                        gainEntity = entity;
                        if (null != entity.getDataMap().getActivityPictureUrl() && !"".equals(entity.getDataMap().getActivityPictureUrl())) {

                            handler.post(showFristRunnable);

                        }
                    }
                } else if (type == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            relacedAccount();//退出登录后需要的操作
                            Toast.makeText(MainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {

            }
        });
    }

    /**
     * 自动登陆
     **/
    private void login() {
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        String psw = PreferencesUtils.getString(this, Consts.PSW);
        final String token = PreferencesUtils.getString(this, Consts.TOKEN);
        if (phone != null && psw != null && !phone.equals("") && !psw.equals("") && token != null && !token.equals("")) {
            getPersonalInfor();//获取个人信息
            JPushInterface.resumePush(getApplicationContext());
            PreferencesUtils.putBoolean(MainActivity.this, Consts.ISLOGIN, true);//是否登陆
            final int userid = PreferencesUtils.getInt(MainActivity.this, Consts.USER_ID);
            JPushInterface.setAlias(getApplicationContext(), phone, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                }
            });
            final String registrationId = PreferencesUtils.getString(MainActivity.this, Consts.REGISTRATIONID);
            if (null != registrationId) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendRegistrationId(userid, registrationId, token);
                    }
                }.start();
            }
            isLogin = true;
        }
    }

    /**
     * 向服务器发送registrationId
     **/
    private void sendRegistrationId(int userid, String regis, String token) {
        String url = API.REGISTIONID;
        JSONObject object = new JSONObject();
        try {
            object.put(Consts.CUSTOMERID, userid + "");
            object.put(Consts.REGISTRATIONID, regis);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            JrController.setCertificates(MainActivity.this, okHttpClient, MainActivity.this.getAssets().open("zhenjren.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, object.toString());
        Request request = new Request.Builder().url(url).post(body).addHeader(Consts.TOKEN, token).build();
        Response response = null;
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        try {
            response = okHttpClient.newCall(request).execute();
            String s = response.body().string().toString();
            if (response.isSuccessful()) {
                if (200 == response.code()) {
                    Log.i("geanwen发送registrationId", s);
                }
            } else if (response.code() == 401) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        relacedAccount();//退出登录后需要的操作
                        Toast.makeText(MainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "未能连接到服务器", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //获取个人信息
    public void getPersonalInfor() {
        String url = API.DETAIL;
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        if (phone != null && !phone.equals("")) {
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USER_ID, phone);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(MainActivity.this).loadGetData(MainActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code != 401 && code != 404 && code == 200) {
                        Gson gson = new Gson();
                        entity = gson.fromJson(resultString, PersonalInforEntity.class);
                        EventBus.getDefault().post(entity);
                    } else if (code == 401) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                relacedAccount();//退出登录后需要的操作
                                Toast.makeText(MainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void setListener() {
//        navifation_myIV.setOnClickListener(this);
        //  navifation_messageIV.setOnClickListener(this);
//        rush_to_dealIV.setOnClickListener(this);
//        start_ball_enterIV.setOnClickListener(this);
//        waterIv.setOnClickListener(this);
        //       eleIv.setOnClickListener(this);
//       houseKeepingIv.setOnClickListener(this);
//        homeTrimIv.setOnClickListener(this);
//        safeIv.setOnClickListener(this);
//        reMoveIv.setOnClickListener(this);
//        setupIv.setOnClickListener(this);
//        upgradleIv.setOnClickListener(this);
        item_personal_order.setOnClickListener(this);
        item_personal_liucheng.setOnClickListener(this);
        item_personal_biaozhun.setOnClickListener(this);
        item_personal_set.setOnClickListener(this);
        item_personal_about_us.setOnClickListener(this);
        item_out_login.setOnClickListener(this);
//        backMainIV.setOnClickListener(this);
//        backMyLocationIB.setOnClickListener(this);
        firstPart_RL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navifation_myIV://toolbar左侧图标
                clickNavifation();
                break;
            case R.id.navifation_messageIV://toolbar右侧图标
                clickRightBtn();
                break;
//            case R.id.rush_to_dealIV://抢险抢修
//                clickCallCustomService();
//                break;
            //  case R.id.start_ball_enterIV://小球进入按钮
            //    clickStartBallEnter();
            //   break;
            // case R.id.waterIv://水维修
            //      clickWaterIv(waterIv);
            //   break;
            //  case R.id.eleIv://电维修
            //     clickWaterIv(eleIv);
            //     break;
            //   case R.id.houseKeepingIv://家政力工
            //     clickWaterIv(houseKeepingIv);
            //     break;
            //  case R.id.homeTrimIv://居家小修
            //     clickWaterIv(homeTrimIv);
            //      break;
            //  case R.id.safeIv://安全检测
            //    clickWaterIv(safeIv);
            //     break;
            // case R.id.reMoveIv://货车搬家
            //      clickWaterIv(reMoveIv);
            //      break;
            //    case R.id.setupIv://居家安装
            //      clickWaterIv(setupIv);
            //      break;
            //  case R.id.upgradleIv://我家升级
            //     clickWaterIv(upgradleIv);
            //     break;
            case R.id.firstPart_RL://个人信息
                clickPersonal();
                break;
            case R.id.item_personal_order://我的订单
                Intent intent = new Intent(this, MyOrdorActivity.class);
                startActivityForResult(intent, 4);
                //  mDrawerLayout.closeDrawers();//关闭抽屉
                break;
            case R.id.item_personal_liucheng://服务流程
                Intent liucheng_intent = new Intent(this, ServiceProcessActivity.class);
                startActivity(liucheng_intent);
                // mDrawerLayout.closeDrawers();//关闭抽屉
                break;
            case R.id.item_personal_biaozhun://维修标准
                clickBiaoZhun();
                break;
            case R.id.item_personal_set://设置
                Intent set_intent = new Intent(this, SettingActivity.class);
                startActivity(set_intent);
                //   mDrawerLayout.closeDrawers();//关闭抽屉
                break;
            case R.id.item_personal_about_us://关于我们
                Intent aboutus_intent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutus_intent);
                // mDrawerLayout.closeDrawers();//关闭抽屉
                break;
            case R.id.item_out_login://退出登录
                outLoginDialog = new WaittingDiaolog(this);
                outLoginDialog.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        clickOutLogin();
                    }
                }.start();
                break;
            case R.id.takenPhoneIV://打电话
                clickCallPhone();
                break;
            //    case R.id.backMainIV://回到主页
            //    cliakBackMain();
            //      break;
            //     case R.id.backMyLocationIB://回到我的位置
            //  double x = Double.valueOf(PreferencesUtils.getString(MainActivity.this, Consts.X));
            //    double y = Double.valueOf(PreferencesUtils.getString(MainActivity.this, Consts.Y));
            //     LatLng latLng = new LatLng(x, y);
            //     myLocation(latLng);
            //     break;
        }
    }

    /**
     * 给客服打电话
     **/
    private void clickCallCustomService() {
        final CallPhoneDialog callPhoneDialog = new CallPhoneDialog(MainActivity.this, Consts.CUSTOM_SERVICE);
        callPhoneDialog.show();
        callPhoneDialog.setOnClickCancleOrdorListener(new CallPhoneDialog.OnClickCancleOrdorListener() {
            @Override
            public void onClickCancleOrdor(View view) {
                switch (view.getId()) {
                    case R.id.cancle_ordor_cancleTV:
                        callPhoneDialog.dismiss();
                        break;
                    case R.id.cancle_ordor_sureTV:
                        callPhoneDialog.dismiss();
                        clickCallPhone(Consts.CUSTOM_SERVICE);
                        break;
                }
            }
        });
    }

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
        MainActivity.this.startActivity(callIntent);
    }

//    //回到主页
//    private void cliakBackMain() {
//        lookWorkerLocation = false;//轮询线程关闭记录
//        backMainIV.setVisibility(View.GONE);
//        start_ball_enterIV.setVisibility(View.VISIBLE);
//        handler.removeCallbacks(workerLocateRun);
//        Intent intent = new Intent(MainActivity.this, WorkerLocationService.class);
//        intent.setAction("com.hx.jrperson.service.WorkerLocationService");
//        startService(intent);
//    }

    //服务标准
    private void clickBiaoZhun() {
        // mDrawerLayout.closeDrawers();//关闭抽屉
        Intent intent = new Intent(MainActivity.this, StandardActivity.class);
        startActivity(intent);
    }

    //登陆主页面修改状态
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Boolean event) {
        if (event != null) {
            login();
            final String token = PreferencesUtils.getString(this, Consts.TOKEN);
            getPersonalInfor();//获取个人信息
            PreferencesUtils.putBoolean(MainActivity.this, Consts.ISLOGIN, true);//是否登陆
            final int userid = PreferencesUtils.getInt(MainActivity.this, Consts.USER_ID);
            final String registrationId = PreferencesUtils.getString(MainActivity.this, Consts.REGISTRATIONID);
            if (null != registrationId) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendRegistrationId(userid, registrationId, token);
                    }
                }.start();
            }
            isLogin = true;
        }
    }

    //定位后查询地址  保存到缓存
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(LatLng latlng) {
        if (latlng != null) {
            //经纬度查询地址
            GeoCoder coder = GeoCoder.newInstance();
            coder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
            coder.setOnGetGeoCodeResultListener(this);
        }
    }

    /**
     * 定位后按照自己的经纬度
     * 查询详细地址回调方法 并存入缓存
     * 给发布订单页面默认地址
     **/
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showToast("查不到");
                }
            });
            return;
        } else {
            myAddress = reverseGeoCodeResult.getAddress();
            PreferencesUtils.putString(MainActivity.this, Consts.CITY, myAddress);
            if (isStartService) {
                city = reverseGeoCodeResult.getAddressDetail().city;
                isStartService = false;
                //定位反检索后开启服务获得身边匠人位置
                Intent serviceLocation = new Intent(MainActivity.this, WorkerLocationService.class);
                Bundle bundle = new Bundle();
                bundle.putString(Consts.CITY, myAddress);
                serviceLocation.putExtras(bundle);
                serviceLocation.setAction("com.hx.jrperson.service.WorkerLocationService");
                startService(serviceLocation);
            }
            if (fristLocation) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String url = API.VALIDATEADDRESS;
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(Consts.CONTENT, myAddress);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        OkHttpClient okHttpClient = new OkHttpClient();
                        try {
                            JrController.setCertificates(MainActivity.this, okHttpClient, MainActivity.this.getAssets().open("zhenjren.cer"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                        Request request = new Request.Builder().url(url).post(body).build();
                        Response response;
                        try {
                            response = okHttpClient.newCall(request).execute();
                            if (response.isSuccessful()) {
                                String result = response.body().string().toString();
                                JSONObject object = new JSONObject(result);
                                JSONObject obj = object.getJSONObject("dataMap");
                                Gson gson = new Gson();
                                IsOnLocationEntity locationEntity = gson.fromJson(obj.toString(), IsOnLocationEntity.class);
                                if (!locationEntity.isValidateFlag()) {//不在服务区
                                    showToast("该地区暂时没有开通服务");
                                }
                            } else if (response.code() == 401) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        relacedAccount();//退出登录后需要的操作
                                        Toast.makeText(MainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                fristLocation = false;
            }
            PreferencesUtils.putString(MainActivity.this, Consts.ADDRESSMYLOCATION, reverseGeoCodeResult.getAddress());
        }
    }


    //订单发布成功
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Integer staute) {
        if (staute != null) {
            if (staute == 1000) {//此功能取消
//                handler.post(notOrdorRunnable);
                showToast("发单成功，请耐心等待匠人接单");
            } else if (staute == 1212) {
                gainIsNewInfor();
            } else if (staute == 3333) {//个人设置界面结束时发送的消息 通知主页面刷新一次
                getPersonalInfor();
            }
        }
    }


    //个人信息
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(PersonalInforEntity event) {
        if (event != null) {
            if (null != event.getDataMap().getNick_name() && !"".equals(event.getDataMap().getNick_name())) {
                nick_nameTV.setText(event.getDataMap().getNick_name());
            } else {
                nick_nameTV.setText("");
            }
            if (null != event.getDataMap().getCustom_sign() && !"".equals(event.getDataMap().getCustom_sign())) {
                signatrueTV.setText(event.getDataMap().getCustom_sign());
            } else {
                signatrueTV.setText("");
            }
            if (null != event.getDataMap().getAvatar() && !"".equals(event.getDataMap().getAvatar())) {
                final String avater = API.AVATER + event.getDataMap().getAvatar() + "_200.jpg";
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            getAvater(avater);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }

    }

//    //查看将人位置（地图上只有一个将人）
//    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
//    public void onUserEvent(OrderEntity.DataMapBean eventData) {
//        dataMapBean = eventData;
//        //停止刷新服务
//        Intent intent = new Intent(MainActivity.this, WorkerLocationService.class);
//        intent.setAction("com.hx.jrperson.service.WorkerLocationService");
//        stopService(intent);
//        if (eventData != null) {
//            mbaiduMap.clear();
//            lookWorkerLocation = true;//线程开启记录
//            double xlocate = dataMapBean.getX();
//            double ylocate = dataMapBean.getY();
//            LatLng latLng = new LatLng(xlocate, ylocate);
//            myLocation(latLng);//移动到自己位置
//            addWorkerLocate(xlocate, ylocate);
//            backMainIV.setVisibility(View.VISIBLE);//底部按钮切换
//            start_ball_enterIV.setVisibility(View.GONE);
//            addWorkerslocate(eventData);
//        }
//    }

//    //添加匠人位置(已接单的匠人)
//    private void addWorkerslocate(final OrderEntity.DataMapBean eventData) {
//        String url = API.WORKERLOCATION;
//        Map<String, String> map = new HashMap<>();
//        map.put(Consts.ORDERID, eventData.getOrder_id() + "");
//        url = JrUtils.appendParams(url, map);
//        NetLoader.getInstance(MainActivity.this).loadGetData(MainActivity.this, url, new NetLoader.NetResponseListener() {
//            @Override
//            public void success(String resultString, int code) {
//                if (code == 200) {
//                    try {
//                        JSONObject object = new JSONObject(resultString);
//                        JSONObject jsonObject = object.getJSONObject("dataMap");
//                        Gson gson = new Gson();
//                        WorkerLocateEntity locateEntity = gson.fromJson(jsonObject.toString(), WorkerLocateEntity.class);
//                        double xlocate = locateEntity.getX();
//                        double ylocate = locateEntity.getY();
//                        addWorkerLocate(xlocate, ylocate);
//                        handler.postDelayed(workerLocateRun, 5000);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else if (code == 401) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            relacedAccount();//退出登录后需要的操作
//                            Toast.makeText(MainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void fail(String failString, Exception e) {
//                Log.i("geanwen", failString);
//            }
//        });
//    }

    //  private Runnable workerLocateRun = new Runnable() {
//        @Override
//        public void run() {
//            addWorkerslocate(dataMapBean);
//        }
//    };

    /**
     * 请求匠人位置
     * 设置到地图上
     **/
    private void addWorkerLocate(double x, double y) {
        mbaiduMap.clear();//清除上面图层
        mMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_worker_head_img);
        LatLng latLng = new LatLng(x, y);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(mMarker);
        mbaiduMap.addOverlay(options);
    }


    //toolbar右侧信息按钮
    private void clickRightBtn() {
        PreferencesUtils.putString(MainActivity.this, Consts.TIME, clickTimes);//存入点击的时间，以便下次传时间到服务器，获取未查看的广告信息
        Intent intent = new Intent(this, InforGutActivity.class);
        startActivity(intent);
    }

    //给匠人打电话
    private void clickCallPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "13664266902"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        MainActivity.this.startActivity(callIntent);
    }

    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //退出登录
    private void clickOutLogin() {
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        String token = PreferencesUtils.getString(MainActivity.this, Consts.TOKEN);
        String url = API.LOGOUT;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.USER_ID, phone);
        map.put(Consts.TOKEN, token);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(MainActivity.this).loadOrdinaryPostData(MainActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();//关闭抽屉
                        isLogin = false;
                        relacedAccount();//退出登录后需要的操作
                        outLoginDialog.dismiss();
                        Intent out_intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(out_intent);
                    }
                });
            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen退出登陆", failString);
                outLoginDialog.dismiss();
                mDrawerLayout.closeDrawers();//关闭抽屉
            }
        }, null);
    }

    /**
     * 账号被顶替后
     **/
    public void relacedAccount() {
        PreferencesUtils.putBoolean(MainActivity.this, Consts.ISLOGIN, false);
        PreferencesUtils.clear(MainActivity.this, Consts.PHONE_PF);
        PreferencesUtils.clear(MainActivity.this, Consts.PSW);
        PreferencesUtils.clear(MainActivity.this, Consts.TOKEN);
//        JPushInterface.stopPush(getApplicationContext());//推送取消
        //抽屉关闭手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //点击toolbar左侧按钮
    private void clickNavifation() {
        if (isLogin) {
            mDrawerLayout.openDrawer(mNavigationView);//弹出抽屉
        } else {
            closeDrawerLayout();
        }
    }

    //未登陆时跳转到登陆页面
    private void closeDrawerLayout() {
        Intent out_intent = new Intent(this, LoginActivity.class);
        startActivityForResult(out_intent, 3);
        mDrawerLayout.closeDrawers();//关闭抽屉
    }

    //个人信息点击事件
    private void clickPersonal() {
        Intent intent = new Intent(this, PersonalSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("personal", entity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    //定位相关初始化方法
//    private void startLocation() {
//        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(17.0f);//设置打开地图 标识
////       mbaiduMap.setMapStatus(update);
//        mLocationClient = new LocationClient(getApplicationContext());
//        myLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(myLocationListener);//注册
//        LocationClientOption option = new LocationClientOption();
//        option.setCoorType("bd09ll");
//        option.setNeedDeviceDirect(true);
//        option.setOpenGps(true);//打开gps
//        option.setScanSpan(10000);//相隔时间请求一次
//        mLocationClient.setLocOption(option);
//        mLocationClient.start();//开始定位
//        mLocationClient.requestLocation();
//    }

    //点击小球出现按钮
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void clickStartBallEnter() {
        WindowManager manager = this.getWindowManager();
        wid = manager.getDefaultDisplay().getWidth();
        hei = manager.getDefaultDisplay().getHeight();
        if (isStart) {//小球散开
            AnimatorSet set = JrAnimationsHelp.Help(this, wid, hei, false,
                    waterIv, eleIv, houseKeepingIv, homeTrimIv, safeIv, reMoveIv, setupIv, upgradleIv);
            set.start();
            isStart = !isStart;
        } else {//小球进入
            AnimatorSet sett = JrAnimationsHelp.Help(this, wid, hei, true,
                    waterIv, eleIv, houseKeepingIv, homeTrimIv, safeIv, reMoveIv, setupIv, upgradleIv);
            sett.start();
            isStart = !isStart;
        }
    }

    //跳转到点击的具体项目列表页面
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void clickWaterIv(final ImageView imageView) {
        if (isBallClick == 1) {
            isBallClick = isBallClick + 1;
            AnimatorSet set = JrAnimationsHelp.Help(this, wid, hei, false,
                    waterIv, eleIv, houseKeepingIv, homeTrimIv, safeIv, reMoveIv, setupIv, upgradleIv);
            set.start();
            isStart = !isStart;
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (imageView == waterIv) {
                        initToast("水维修", "1001");
                    } else if (imageView == eleIv) {
                        initToast("电维修", "2001");
                    } else if (imageView == houseKeepingIv) {
                        initToast("家电清洗", "7001");
                    } else if (imageView == homeTrimIv) {
                        initToast("居家小修", "3001");
                    } else if (imageView == safeIv) {
                        initToast("装修监控", "5001");
                    } else if (imageView == reMoveIv) {
                        initToast("货车力工", "8001");
                    } else if (imageView == setupIv) {
                        initToast("居家安装", "4001");
                    } else if (imageView == upgradleIv) {
                        initToast("我家装修", "6001");
                    }
                }
            });
        }
    }

    //弹出toast
    private void initToast(String show, String parentCode) {
        Intent intent = new Intent(this, ServiceGutActivity.class);
        intent.putExtra("title", show);
        intent.putExtra("parentCode", parentCode);
        startActivity(intent);
    }

    private void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(str);
            toast.show();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
////        mbaiduMap.setMyLocationEnabled(true);
//        if (mLocationClient.isStarted())
//            mLocationClient.start();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        insance = this;
        fristLocation = true;
        // startLocation();
        if (!lookWorkerLocation) {//不是查看一个匠人位置
            Intent intent = new Intent(MainActivity.this, WorkerLocationService.class);
            intent.setAction("com.hx.jrperson.service.WorkerLocationService");
            startService(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, WorkerLocationService.class);
            intent.setAction("com.hx.jrperson.service.WorkerLocationService");
            stopService(intent);
        }
        boolean isOpean = JrUtils.isOpen(this);
        if (!isOpean) {//没开定位权限
            SharedPreferences getSp = getSharedPreferences("test", MODE_PRIVATE);
            String name1 = getSp.getString("name1", "默认");
            if (name1.equals("默认")) {

                /////////////////////////////////////////////////////////////////
                final AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                //设置图标
                alert1.setIcon(R.mipmap.newlogo);
                //设置标题
                alert1.setTitle("是否打开手机的定位功能");
                //设置主体信息
                alert1.setMessage("我们将引导您打开手机的定位功能,方便匠人为您更好的服务.");
                alert1.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                //设置消极按钮
                alert1.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });//开始,显示
                alert1.show();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name1", "中华小当家");
                editor.commit();
            }
            ////////////////////////////////////////////////////////////
        }
        getPersonalInfor();//重新获得个人信息
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        isBallClick = 1;//恢复小球点击次数
//        if (isStart) {//小球散开
//            AnimatorSet set = JrAnimationsHelp.Help(this, wid, hei, false,
//                    waterIv, eleIv, houseKeepingIv, homeTrimIv, safeIv, reMoveIv, setupIv, upgradleIv);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                set.start();
//            }
//            isStart = !isStart;
//        }
//        Intent intent = new Intent(MainActivity.this, WorkerLocationService.class);
//        intent.setAction("com.hx.jrperson.service.WorkerLocationService");
//        stopService(intent);
//        PreferencesUtils.putBoolean(MainActivity.this, Consts.ISSHWOING, false);
////      mbaiduMap.setMyLocationEnabled(false);//停止定位
////        mLocationClient.stop();
//        if (lookWorkerLocation) {
//            handler.removeCallbacks(workerLocateRun);
//        }
//        insance = null;
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        PreferencesUtils.putInt(MainActivity.this, Consts.NUMBERMESSAGE, 0);//新消息数量+1
////        BadgeUtil.resetBadgeCount(getApplicationContext());
//        isBallClick = 1;//恢复小球点击次数
////        mapView.onResume();
//        if (lookWorkerLocation) {
//            handler.post(workerLocateRun);
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4://订单返回状态
                if (data != null) {
                    Bundle bundle1 = data.getExtras();
                    int statue = bundle1.getInt("noOrdor");
                    if (statue == 100) {//未接单
                        handler.post(notOrdorRunnable);
                    }
                }
                break;
        }
    }

//    //自定义 定位回调方法
//    public class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            MyLocationData data = new MyLocationData.Builder()//
//                    .accuracy(0)//去掉光圈
//                    .latitude(bdLocation.getLatitude())//
//                    .longitude(bdLocation.getLongitude())
//                    .build();
//            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_my_location);
//            MyLocationConfiguration config = new MyLocationConfiguration(null, true, mCurrentMarker);
////            mbaiduMap.setMyLocationConfigeration(config);
////            mbaiduMap.setMyLocationData(data);
//            mLatitude = bdLocation.getLatitude();
//            mLongtitude = bdLocation.getLongitude();
//            PreferencesUtils.putString(MainActivity.this, Consts.X, mLatitude + "");
//            PreferencesUtils.putString(MainActivity.this, Consts.Y, mLongtitude + "");
//            LatLng latLng = new LatLng(mLatitude, mLongtitude);
//            EventBus.getDefault().post(latLng);
//            if (isFristIn) {//是否第一次定位
//                //获取自己的位置：经纬度
//            //    myLocation(latLng);
//                isFristIn = false;
//            }
//        }
//    }


//    //定位到我的位置
//    private void myLocation(final LatLng latLng) {
//        if (null != latLng) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    MapStatusUpdate updatOut = MapStatusUpdateFactory.zoomTo(14.0f);//设置打开地图 标识
////                    mbaiduMap.animateMapStatus(updatOut);//设置动画显示
//                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
//                    mbaiduMap.animateMapStatus(msu);//设置动画显示
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            MapStatusUpdate updateIn = MapStatusUpdateFactory.zoomTo(16.0f);//设置打开地图 标识
//                            mbaiduMap.animateMapStatus(updateIn);//设置动画显示
//                        }
//                    }, 300);
//                }
//            });
//        } else {
//            showToast("定位失败");
//        }
//    }

    /**
     * 下载头像
     **/
    public void getAvater(String avatarUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JrController.setCertificates(MainActivity.this, client, MainActivity.this.getAssets().open("zhenjren.cer"));
        try {
            Request request = new Request.Builder().url(avatarUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is = response.body().byteStream();
                final Bitmap bm = BitmapFactory.decodeStream(is);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != bm) {
                            head_imgIV.setImageBitmap(bm);
                        }
                    }
                });
            } else if (response.code() == 401) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        relacedAccount();//退出登录后需要的操作
                        Toast.makeText(MainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (response.code() == 404) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(MainActivity.this).load(R.mipmap.ic_personal_head_img).error(R.mipmap.ic_personal_head_img).into(head_imgIV);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isIn = true;
    private Runnable runnable = new Runnable() {//抢险抢修提示
        @Override
        public void run() {
            if (isIn) {
                isGoneRl.setVisibility(View.VISIBLE);
                isIn = false;
                handler.postDelayed(runnable, 30000);
            } else {
                isGoneRl.setVisibility(View.GONE);
                isIn = true;
            }
        }
    };
    private Runnable notOrdorRunnable = new Runnable() {//未接单
        @Override
        public void run() {
            if (isIn) {
                isNotOrdorRl.setVisibility(View.VISIBLE);
                isIn = false;
                handler.postDelayed(notOrdorRunnable, 3000);
            } else {
                isNotOrdorRl.setVisibility(View.GONE);
                isIn = true;
            }
        }
    };

    /**
     * 刚进入app时 弹出的广告dialog
     **/
    private Runnable showFristRunnable = new Runnable() {


        @Override
        public void run() {

            //数据持久化相关操作
            //  记录当前是否为第一次进入该应用
            SharedPreferences getSp = getSharedPreferences("test", MODE_PRIVATE);
            String name1 = getSp.getString("name1", "默认");
            if (name1.equals("默认")) {

                WindowManager manager = MainActivity.this.getWindowManager();
                wid = manager.getDefaultDisplay().getWidth();
                hei = manager.getDefaultDisplay().getHeight();
                final ShowBigPhotoDialog showBigPhotoTwoDialog = new ShowBigPhotoDialog(MainActivity.this, gainEntity.getDataMap().getActivityPictureUrl(), wid, hei, 1);
                showBigPhotoTwoDialog.show();
                showBigPhotoTwoDialog.setOnClickBigPhotoListener(new ShowBigPhotoDialog.OnClickBigPhotoListener() {
                    @Override
                    public void onClickBigPhotol(View view) {
                        showBigPhotoTwoDialog.dismiss();
                        switch (view.getId()) {
                            case R.id.newInforIV:
                                int msgId = gainEntity.getDataMap().getActivityId();
                                Intent intent = new Intent(MainActivity.this, ShowInforActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("msgId", msgId);
                                bundle.putInt("type", 2);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                        }
                    }
                });
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name1", "中华小当家");
                editor.commit();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
//   mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name1", "默认");
        editor.commit();
       this.unregisterReceiver(mysendreciver);
        //关闭商标闪光计时器
        timer.cancel();

        PreferencesUtils.putBoolean(MainActivity.this, Consts.ISLOGIN, false);
        // mapView.onDestroy();
        MainActivity.this.unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            handler.removeCallbacks(showFristRunnable);
        }
        if (isStartService) {
            Intent intent = new Intent(MainActivity.this, WorkerLocationService.class);
            intent.setAction("com.hx.jrperson.service.WorkerLocationService");
            stopService(intent);
        }
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 服务发过来的数据
     **/
    public class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String resultString = bundle.getString("marker");
            if (null != resultString) {
                try {
                    JSONObject object = new JSONObject(resultString);
                    JSONObject object1 = object.getJSONObject("dataMap");
                    JSONObject object2 = object1.getJSONObject("wrks");
                    Iterator iterator = object2.keys();
//                    mbaiduMap.clear();//清除上面图层
                    mMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_worker_head_img);
                    while (iterator.hasNext()) {
                        String st = iterator.next().toString();
                        JSONArray array = object2.getJSONArray(st);
                        String str = array.getString(2);//是否开工1:开工,2:收工;收工不显示
                        if (str.equals("2")) {
                            continue;
                        }
                        String xStr = array.getString(3);//经度
                        String yStr = array.getString(4);//纬度
                        double x = Double.valueOf(xStr);
                        double y = Double.valueOf(yStr);
                        LatLng latLng = new LatLng(x, y);
                        OverlayOptions options = new MarkerOptions().position(latLng).icon(mMarker);
//                      mbaiduMap.addOverlay(options);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    ///////////////////////////////////////////////////////////
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }

            }
            myAdressOk = location.getAddrStr();
            getAdress();
        }


    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

    }

    private void getAdress() {
        SharedPreferences.Editor editor = sp2.edit();
        editor.putString("myAdress", myAdressOk + "  ");
        editor.commit();
        Log.i("ffffff", myAdressOk + "   ");
    }

    //分享相关代码
    private void shareYourIdea(){
        //初始化一个对象,填写url
        WXWebpageObject webpage=new WXWebpageObject();
        webpage.webpageUrl="http://zjrkeji.com/index.do";
        //用对象初始化一个message 对象 ,填写标题/描述
        WXMediaMessage msg=new WXMediaMessage(webpage);
        msg.title="真匠人分享测试";
        msg.description="真匠人测试文本正文内容";
       Bitmap thumb=BitmapFactory.decodeResource(getResources(),R.mipmap.iconlogo);
        msg.setThumbImage(thumb);
        //构造一个req
        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }

    //带值广播接收者
    class MySendReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String data=intent.getStringExtra("heihei");
            if (data.equals("走你")){

                mainViewPager.setCurrentItem(1);
            }else if (data.equals("back")){
                outLoginDialog = new WaittingDiaolog(MainActivity.this);
                outLoginDialog.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        clickOutLogin();
                    }
                }.start();
            }


        }
    }

}


