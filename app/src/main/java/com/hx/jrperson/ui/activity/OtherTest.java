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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
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
import com.hx.jrperson.bean.entity.WorkerLocateEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.service.WorkerLocationService;
import com.hx.jrperson.utils.BadgeUtil;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/8/2.
 */
public class OtherTest extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {
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
    private LocationClient mLocationClient;//定位相关
    private BaiduMap mbaiduMap;
    private MyLocationListener myLocationListener;
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
    public static OtherTest insance = null;
    private boolean lookWorkerLocation = false;
    private LinearLayout head_negivityRL, firstPart_RL;//个人信息总布局
    private LocationReceiver receiver;//接收服务传过来的匠人位置
    private String city;//当前城市
    private boolean isStartService = false;//是否开启后台服务
    private int isBallClick = 1;//记录小球点击的次数 防止多次点击 项目列表页面会多次弹出
    private GainMessageEntity gainEntity;
    private String clickTimes;
    ////////////////////////////////////////////////
    private TextView backToMyLiST;
    private OrderEntity.DataMapBean entityForMe = null;
    //地图中的返回按钮
    private ImageView myHomePageInMap;
    private ImageView callInMyMap;



    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent1 = new Intent(OtherTest.this, WorkerLocationService.class);
        intent1.setAction("com.hx.jrperson.service.WorkerLocationService");
        stopService(intent1);
        insance = this;//给本页面设置个静态变量  方便其他页面控制本页面的生命周期（又问题：静态变量消耗内存 待改善）
        setContentView(R.layout.otherpage);

        /////////////////////////
        boolean isOpean = JrUtils.isOpen(this);
        if (!isOpean) {//没开定位权限
//            Intent intent = new Intent(OtherTest.this, NotOpeaLocationActivity.class);
//            startActivity(intent);
//            OtherTest.this.finish();
            //强制开启定位功能
            final AlertDialog.Builder alert1=new AlertDialog.Builder(this);
            //设置图标
            alert1.setIcon(R.mipmap.newlogo);
            //设置标题
            alert1.setTitle("是否打开手机的定位功能");
            //设置主体信息
            alert1.setMessage("我们将引导您打开手机的定位功能,方便匠人为您更好的服务.");
            alert1.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =  new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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

        }
        if (!NetWorkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "请检查您的网络", Toast.LENGTH_SHORT).show();
        }
        JrController.getVersion(OtherTest.this);//版本更新
//        JPushInterface.stopPush(MainActivity.this);
        isShowing = true;//当前页面
        showToolBar("", true, this, true);

        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
        startLocation();//开始定位 相关初始化
        //////////////////////////////////
        mbaiduMap.clear();
        Intent intent = this.getIntent();
        entityForMe = (OrderEntity.DataMapBean) intent.getSerializableExtra("user");
        onUserEvent(entityForMe);
        addWorkerslocate(entityForMe);
        /////////////////////////////////

    }

    @Override
    protected void initView() {
        navifation_myIV = (ImageView) findViewById(R.id.navifation_myIV);//toolbar左侧图标
        navifation_messageIV = (ImageView) findViewById(R.id.navifation_messageIV);//toolbar右侧图标
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//设置抽屉DrawerLayout
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);//抽屉屏幕布局
        rush_to_dealIV = (ImageView) findViewById(R.id.rush_to_dealIV);//抢险抢修
        start_ball_enterIV = (ImageView) findViewById(R.id.start_ball_enterIV);//小球进入动画
        mapView = (MapView) findViewById(R.id.mapView); //地图
        waterIv = (ImageView) findViewById(R.id.waterIv);//水维修
        eleIv = (ImageView) findViewById(R.id.eleIv);//电维修
        houseKeepingIv = (ImageView) findViewById(R.id.houseKeepingIv);//家电清洗
        homeTrimIv = (ImageView) findViewById(R.id.homeTrimIv);//居家小修
        safeIv = (ImageView) findViewById(R.id.safeIv);//装修监控
        reMoveIv = (ImageView) findViewById(R.id.reMoveIv);//货车力工
        setupIv = (ImageView) findViewById(R.id.setupIv);//居家安装
        upgradleIv = (ImageView) findViewById(R.id.upgradleIv);//我家装修
        isGoneRl = (RelativeLayout) findViewById(R.id.isGoneRl);//通知抢险抢修后出现的布局
        firstPart_RL = (LinearLayout) findViewById(R.id.firstPart_RL);//个人信息
        item_personal_order = (LinearLayout) findViewById(R.id.item_personal_order);//我的订单
        item_personal_liucheng = (LinearLayout) findViewById(R.id.item_personal_liucheng);//服务流程
        item_personal_biaozhun = (LinearLayout) findViewById(R.id.item_personal_biaozhun);//维修标准
        item_personal_set = (LinearLayout) findViewById(R.id.item_personal_set);//设置
        item_personal_about_us = (LinearLayout) findViewById(R.id.item_personal_about_us);//关于我们
        item_out_login = (LinearLayout) findViewById(R.id.item_out_login);//退出登录
        isNotOrdorRl = (RelativeLayout) findViewById(R.id.isNotOrdorRl);//未接单提示布局
        head_imgIV = (CircleImageView) findViewById(R.id.head_imgIV);//自己的头像
        workerHeadIV = (CircleImageView) findViewById(R.id.workerHeadIV);//匠人师傅的头像
        nick_nameTV = (TextView) findViewById(R.id.nick_nameTV);//自己的昵称
        signatrueTV = (TextView) findViewById(R.id.signatrueTV);//自己的签名
        backMainIV = (ImageView) findViewById(R.id.backMainIV);//回到主页
        backMyLocationIB = (ImageButton) findViewById(R.id.backMyLocationIB);//回到我的位置
        head_negivityRL = (LinearLayout) findViewById(R.id.head_negivityRL);//抽屉总布局
        //////////////////////////////
        backToMyLiST = (TextView) findViewById(R.id.backToMyLiST);
        backToMyLiST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(OtherTest.this,MainActivity.class);
                startActivity(intent);
                OtherTest.this.finish();
            }
        });
        /////////////////////////////////////////
        myHomePageInMap= (ImageView) findViewById(R.id.myHomePageInMap);
        myHomePageInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherTest.this.finish();
            }
        });
        callInMyMap= (ImageView) findViewById(R.id.callInMyMap);
        callInMyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallPhone();
            }
        });
    }

    @Override
    protected void initData() {
        final ShowBigPhotoDialog showBigPhotoDialog;
        WindowManager manager = this.getWindowManager();
        wid = manager.getDefaultDisplay().getWidth();
        hei = manager.getDefaultDisplay().getHeight();
        JrUtils.removeNavigationViewScrollbar(mNavigationView);
        PreferencesUtils.putInt(OtherTest.this, Consts.WID, wid);
        PreferencesUtils.putInt(OtherTest.this, Consts.HEI, hei);
        String isFrist = PreferencesUtils.getString(this, Consts.ISFRIST);
        if (isFrist == null || "".equals(isFrist)) {
            PreferencesUtils.putString(OtherTest.this, Consts.ISFRIST, "1");
            showBigPhotoDialog = new ShowBigPhotoDialog(this, "", wid, hei, 2);
            showBigPhotoDialog.show();
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
        mbaiduMap = mapView.getMap();
        handler = new Handler();
        //关闭抽屉的手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        login();//自动登录
        receiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hx.jrperson.service.WorkerLocationService");
        OtherTest.this.registerReceiver(receiver, filter);
        gainIsNewInfor();//获取是否有新的消息（toolbar右侧按钮下一级页面的消息）
    }

    /**
     * 获取是否有新的消息内容
     **/
    private void gainIsNewInfor() {
        String url = API.GAININFOR;
        String times = "";
        times = PreferencesUtils.getString(OtherTest.this, Consts.TIME);
        Date date = new Date();
        long time = date.getTime();
        url = url.concat(times.equals("") ? "?timeStamp=" + time : "?timeStamp=" + times);
        NetLoader.getInstance(OtherTest.this).loadGetData(url, new NetLoader.NetResponseListener() {
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
                                    navifation_messageIV.setImageResource(R.mipmap.have_message);
                                } else {
                                    navifation_messageIV.setImageResource(R.mipmap.have_no_message);
                                }
                            }
                        });

                    } else if (entitys.getCode() == 500) {

                    }
                } else if (type == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OtherTest.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
        NetLoader.getInstance(OtherTest.this).loadGetData(api, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int type) {
                if (type == 200) {
                    if (!resultString.equals("")) {
                        Gson gson = new Gson();
                        GainMessageEntity entity = gson.fromJson(resultString, GainMessageEntity.class);
                        gainEntity = entity;
                        if (null != entity.getDataMap().getActivityPictureUrl() && !"".equals(entity.getDataMap().getActivityPictureUrl())) {
                            //  handler.post(showFristRunnable);
                        }
                    }
                } else if (type == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            relacedAccount();//退出登录后需要的操作
                            Toast.makeText(OtherTest.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
            PreferencesUtils.putBoolean(OtherTest.this, Consts.ISLOGIN, true);//是否登陆
            final int userid = PreferencesUtils.getInt(OtherTest.this, Consts.USER_ID);
            JPushInterface.setAlias(getApplicationContext(), phone, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                }
            });
            final String registrationId = PreferencesUtils.getString(OtherTest.this, Consts.REGISTRATIONID);
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
            JrController.setCertificates(OtherTest.this, okHttpClient, OtherTest.this.getAssets().open("zhenjren.cer"));
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
                        Toast.makeText(OtherTest.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OtherTest.this, "未能连接到服务器", Toast.LENGTH_SHORT).show();
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
            NetLoader.getInstance(OtherTest.this).loadGetData(OtherTest.this, url, new NetLoader.NetResponseListener() {
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
                                Toast.makeText(OtherTest.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
        navifation_myIV.setOnClickListener(this);
        navifation_messageIV.setOnClickListener(this);
        rush_to_dealIV.setOnClickListener(this);
        start_ball_enterIV.setOnClickListener(this);
        waterIv.setOnClickListener(this);
        eleIv.setOnClickListener(this);
        houseKeepingIv.setOnClickListener(this);
        homeTrimIv.setOnClickListener(this);
        safeIv.setOnClickListener(this);
        reMoveIv.setOnClickListener(this);
        setupIv.setOnClickListener(this);
        upgradleIv.setOnClickListener(this);
        item_personal_order.setOnClickListener(this);
        item_personal_liucheng.setOnClickListener(this);
        item_personal_biaozhun.setOnClickListener(this);
        item_personal_set.setOnClickListener(this);
        item_personal_about_us.setOnClickListener(this);
        item_out_login.setOnClickListener(this);
        backMainIV.setOnClickListener(this);
        backMyLocationIB.setOnClickListener(this);
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
            case R.id.rush_to_dealIV://抢险抢修
                clickCallCustomService();
                break;
            case R.id.start_ball_enterIV://小球进入按钮
                clickStartBallEnter();
                break;
            case R.id.waterIv://水维修
                clickWaterIv(waterIv);
                break;
            case R.id.eleIv://电维修
                clickWaterIv(eleIv);
                break;
            case R.id.houseKeepingIv://家政力工
                clickWaterIv(houseKeepingIv);
                break;
            case R.id.homeTrimIv://居家小修
                clickWaterIv(homeTrimIv);
                break;
            case R.id.safeIv://安全检测
                clickWaterIv(safeIv);
                break;
            case R.id.reMoveIv://货车搬家
                clickWaterIv(reMoveIv);
                break;
            case R.id.setupIv://居家安装
                clickWaterIv(setupIv);
                break;
            case R.id.upgradleIv://我家升级
                clickWaterIv(upgradleIv);
                break;
            case R.id.firstPart_RL://个人信息
                clickPersonal();
                break;
            case R.id.item_personal_order://我的订单
                Intent intent = new Intent(this, MyOrdorActivity.class);
                startActivityForResult(intent, 4);
                mDrawerLayout.closeDrawers();//关闭抽屉
                break;
            case R.id.item_personal_liucheng://服务流程
                Intent liucheng_intent = new Intent(this, ServiceProcessActivity.class);
                startActivity(liucheng_intent);
                mDrawerLayout.closeDrawers();//关闭抽屉
                break;
            case R.id.item_personal_biaozhun://维修标准
                clickBiaoZhun();
                break;
            case R.id.item_personal_set://设置
                Intent set_intent = new Intent(this, SettingActivity.class);
                startActivity(set_intent);
                mDrawerLayout.closeDrawers();//关闭抽屉
                break;
            case R.id.item_personal_about_us://关于我们
                Intent aboutus_intent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutus_intent);
                mDrawerLayout.closeDrawers();//关闭抽屉
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
            case R.id.backMainIV://回到主页
                cliakBackMain();
                break;
            case R.id.backMyLocationIB://回到我的位置
                double x = Double.valueOf(PreferencesUtils.getString(OtherTest.this, Consts.X));
                double y = Double.valueOf(PreferencesUtils.getString(OtherTest.this, Consts.Y));
                LatLng latLng = new LatLng(x, y);
                myLocation(latLng);
                break;
        }
    }

    /**
     * 给客服打电话
     **/
    private void clickCallCustomService() {
        final CallPhoneDialog callPhoneDialog = new CallPhoneDialog(OtherTest.this, Consts.CUSTOM_SERVICE);
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
        OtherTest.this.startActivity(callIntent);
    }

    //回到主页
    private void cliakBackMain() {
        lookWorkerLocation = false;//轮询线程关闭记录
        backMainIV.setVisibility(View.GONE);
        start_ball_enterIV.setVisibility(View.VISIBLE);
        handler.removeCallbacks(workerLocateRun);
        Intent intent = new Intent(OtherTest.this, WorkerLocationService.class);
        intent.setAction("com.hx.jrperson.service.WorkerLocationService");
        startService(intent);
    }

    //服务标准
    private void clickBiaoZhun() {
        mDrawerLayout.closeDrawers();//关闭抽屉
        Intent intent = new Intent(OtherTest.this, StandardActivity.class);
        startActivity(intent);
    }

    //登陆主页面修改状态
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Boolean event) {
        if (event != null) {
//            login();
            final String token = PreferencesUtils.getString(this, Consts.TOKEN);
            getPersonalInfor();//获取个人信息
            PreferencesUtils.putBoolean(OtherTest.this, Consts.ISLOGIN, true);//是否登陆
            final int userid = PreferencesUtils.getInt(OtherTest.this, Consts.USER_ID);
            final String registrationId = PreferencesUtils.getString(OtherTest.this, Consts.REGISTRATIONID);
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
            PreferencesUtils.putString(OtherTest.this, Consts.CITY, myAddress);
//            if (isStartService) {
//                city = reverseGeoCodeResult.getAddressDetail().city;
//                isStartService = false;
//                //定位反检索后开启服务获得身边匠人位置
////                Intent serviceLocation = new Intent(OtherTest.this, WorkerLocationService.class);
////                Bundle bundle = new Bundle();
////                bundle.putString(Consts.CITY, myAddress);
////                serviceLocation.putExtras(bundle);
////                serviceLocation.setAction("com.hx.jrperson.service.WorkerLocationService");
////                startService(serviceLocation);
//            }
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
                            JrController.setCertificates(OtherTest.this, okHttpClient, OtherTest.this.getAssets().open("zhenjren.cer"));
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
                                        Toast.makeText(OtherTest.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
            PreferencesUtils.putString(OtherTest.this, Consts.ADDRESSMYLOCATION, reverseGeoCodeResult.getAddress());
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

    //查看将人位置（地图上只有一个将人）
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(OrderEntity.DataMapBean eventData) {
        dataMapBean = eventData;
        mbaiduMap.clear();
        //停止刷新服务
        Intent intent = new Intent(OtherTest.this, WorkerLocationService.class);
        intent.setAction("com.hx.jrperson.service.WorkerLocationService");
        stopService(intent);
        if (eventData != null) {
            mbaiduMap.clear();
            lookWorkerLocation = true;//线程开启记录
            double xlocate = dataMapBean.getX();
            double ylocate = dataMapBean.getY();
            LatLng latLng = new LatLng(xlocate, ylocate);
            myLocation(latLng);//移动到自己位置
            addWorkerLocate(xlocate, ylocate);
            //  backMainIV.setVisibility(View.VISIBLE);//底部按钮切换
            start_ball_enterIV.setVisibility(View.GONE);
            addWorkerslocate(eventData);
        }
    }

    //添加匠人位置(已接单的匠人)
    private void addWorkerslocate(final OrderEntity.DataMapBean eventData) {
        mbaiduMap.clear();
        String url = API.WORKERLOCATION;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDERID, eventData.getOrder_id() + "");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(OtherTest.this).loadGetData(OtherTest.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200) {
                    try {
                        JSONObject object = new JSONObject(resultString);
                        JSONObject jsonObject = object.getJSONObject("dataMap");
                        Gson gson = new Gson();
                        WorkerLocateEntity locateEntity = gson.fromJson(jsonObject.toString(), WorkerLocateEntity.class);
                        double xlocate = locateEntity.getX();
                        double ylocate = locateEntity.getY();
                        addWorkerLocate(xlocate, ylocate);
                        handler.postDelayed(workerLocateRun, 5000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            relacedAccount();//退出登录后需要的操作
                            Toast.makeText(OtherTest.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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

    private Runnable workerLocateRun = new Runnable() {
        @Override
        public void run() {
            addWorkerslocate(dataMapBean);
        }
    };

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
        PreferencesUtils.putString(OtherTest.this, Consts.TIME, clickTimes);//存入点击的时间，以便下次传时间到服务器，获取未查看的广告信息
        Intent intent = new Intent(this, InforGutActivity.class);
        startActivity(intent);
    }

    //给匠人打电话
    private void clickCallPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "041184542809"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        OtherTest.this.startActivity(callIntent);
    }

    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //退出登录
    private void clickOutLogin() {
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        String token = PreferencesUtils.getString(OtherTest.this, Consts.TOKEN);
        String url = API.LOGOUT;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.USER_ID, phone);
        map.put(Consts.TOKEN, token);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(OtherTest.this).loadOrdinaryPostData(OtherTest.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();//关闭抽屉
                        isLogin = false;
                        relacedAccount();//退出登录后需要的操作
                        outLoginDialog.dismiss();
                        Intent out_intent = new Intent(OtherTest.this, LoginActivity.class);
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
        PreferencesUtils.putBoolean(OtherTest.this, Consts.ISLOGIN, false);
        PreferencesUtils.clear(OtherTest.this, Consts.PHONE_PF);
        PreferencesUtils.clear(OtherTest.this, Consts.PSW);
        PreferencesUtils.clear(OtherTest.this, Consts.TOKEN);
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

    //定位相关初始化方法
    private void startLocation() {
        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(17.0f);//设置打开地图 标识
        mbaiduMap.setMapStatus(update);
        mLocationClient = new LocationClient(getApplicationContext());
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);//注册
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);//打开gps
        option.setScanSpan(10000);//相隔时间请求一次
        mLocationClient.setLocOption(option);
        mLocationClient.start();//开始定位
        mLocationClient.requestLocation();
    }

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
            toast = Toast.makeText(OtherTest.this, str, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(str);
            toast.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mbaiduMap.setMyLocationEnabled(true);
        if (mLocationClient.isStarted())
            mLocationClient.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        insance = this;
        fristLocation = true;
        startLocation();
        if (!lookWorkerLocation) {//不是查看一个匠人位置
//            Intent intent = new Intent(OtherTest.this, WorkerLocationService.class);
//            intent.setAction("com.hx.jrperson.service.WorkerLocationService");
//           // startService(intent);
        } else {
            Intent intent = new Intent(OtherTest.this, WorkerLocationService.class);
            intent.setAction("com.hx.jrperson.service.WorkerLocationService");
            stopService(intent);
        }
        boolean isOpean = JrUtils.isOpen(this);
        if (!isOpean) {//没开定位权限
            //强制开启定位功能
            final AlertDialog.Builder alert1=new AlertDialog.Builder(this);
            //设置图标
            alert1.setIcon(R.mipmap.newlogo);
            //设置标题
            alert1.setTitle("是否打开手机的定位功能");
            //设置主体信息
            alert1.setMessage("我们将引导您打开手机的定位功能,方便匠人为您更好的服务.");
            alert1.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =  new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
        }
        getPersonalInfor();//重新获得个人信息
    }

    @Override
    protected void onStop() {
        super.onStop();
        isBallClick = 1;//恢复小球点击次数
        if (isStart) {//小球散开
            AnimatorSet set = JrAnimationsHelp.Help(this, wid, hei, false,
                    waterIv, eleIv, houseKeepingIv, homeTrimIv, safeIv, reMoveIv, setupIv, upgradleIv);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                set.start();
            }
            isStart = !isStart;
        }
        Intent intent = new Intent(OtherTest.this, WorkerLocationService.class);
        intent.setAction("com.hx.jrperson.service.WorkerLocationService");
        stopService(intent);
        PreferencesUtils.putBoolean(OtherTest.this, Consts.ISSHWOING, false);
        mbaiduMap.setMyLocationEnabled(false);//停止定位
        mLocationClient.stop();
        if (lookWorkerLocation) {
            handler.removeCallbacks(workerLocateRun);
        }
        insance = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferencesUtils.putInt(OtherTest.this, Consts.NUMBERMESSAGE, 0);//新消息数量+1
        BadgeUtil.resetBadgeCount(getApplicationContext());
        isBallClick = 1;//恢复小球点击次数
        mapView.onResume();
        if (lookWorkerLocation) {
            handler.post(workerLocateRun);
        }
    }


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

    //自定义 定位回调方法
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data = new MyLocationData.Builder()//
                    .accuracy(0)//去掉光圈
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())
                    .build();
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_my_location);
            MyLocationConfiguration config = new MyLocationConfiguration(null, true, mCurrentMarker);
            mbaiduMap.setMyLocationConfigeration(config);
            mbaiduMap.setMyLocationData(data);
            mLatitude = bdLocation.getLatitude();
            mLongtitude = bdLocation.getLongitude();
            PreferencesUtils.putString(OtherTest.this, Consts.X, mLatitude + "");
            PreferencesUtils.putString(OtherTest.this, Consts.Y, mLongtitude + "");
            LatLng latLng = new LatLng(mLatitude, mLongtitude);
            EventBus.getDefault().post(latLng);
            if (isFristIn) {//是否第一次定位
                //获取自己的位置：经纬度
                myLocation(latLng);
                isFristIn = false;
            }
        }
    }


    //定位到我的位置
    private void myLocation(final LatLng latLng) {
        if (null != latLng) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MapStatusUpdate updatOut = MapStatusUpdateFactory.zoomTo(14.0f);//设置打开地图 标识
                    mbaiduMap.animateMapStatus(updatOut);//设置动画显示
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                    mbaiduMap.animateMapStatus(msu);//设置动画显示
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MapStatusUpdate updateIn = MapStatusUpdateFactory.zoomTo(16.0f);//设置打开地图 标识
                            mbaiduMap.animateMapStatus(updateIn);//设置动画显示
                        }
                    }, 300);
                }
            });
        } else {
            showToast("定位失败");
        }
    }

    /**
     * 下载头像
     **/
    public void getAvater(String avatarUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JrController.setCertificates(OtherTest.this, client, OtherTest.this.getAssets().open("zhenjren.cer"));
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
                        Toast.makeText(OtherTest.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (response.code() == 404) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(OtherTest.this).load(R.mipmap.ic_personal_head_img).error(R.mipmap.ic_personal_head_img).into(head_imgIV);
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
//    private Runnable showFristRunnable = new Runnable() {
//        @Override
//        public void run() {
//            WindowManager manager = OtherTest.this.getWindowManager();
//            wid = manager.getDefaultDisplay().getWidth();
//            hei = manager.getDefaultDisplay().getHeight();
//            final ShowBigPhotoDialog showBigPhotoTwoDialog = new ShowBigPhotoDialog(OtherTest.this, gainEntity.getDataMap().getActivityPictureUrl(), wid, hei, 1);
//            showBigPhotoTwoDialog.show();
//            showBigPhotoTwoDialog.setOnClickBigPhotoListener(new ShowBigPhotoDialog.OnClickBigPhotoListener() {
//                @Override
//                public void onClickBigPhotol(View view) {
//                    showBigPhotoTwoDialog.dismiss();
//                    switch (view.getId()){
//                        case R.id.newInforIV:
//                            int msgId = gainEntity.getDataMap().getActivityId();
//                            Intent intent = new Intent(OtherTest.this, ShowInforActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("msgId", msgId);
//                            bundle.putInt("type", 2);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            break;
//                    }
//                }
//            });
//        }
//    };
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        PreferencesUtils.putBoolean(OtherTest.this, Consts.ISLOGIN, false);
        mapView.onDestroy();
        OtherTest.this.unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            //  handler.removeCallbacks(showFristRunnable);
        }
        if (isStartService) {
//            Intent intent = new Intent(OtherTest.this, WorkerLocationService.class);
//            intent.setAction("com.hx.jrperson.service.WorkerLocationService");
//            stopService(intent);
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
                   // mbaiduMap.clear();//清除上面图层
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
                       // mbaiduMap.addOverlay(options);
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

}
