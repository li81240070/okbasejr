package com.hx.jrperson.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.ImageUriEntity;
import com.hx.jrperson.bean.entity.OrderDate;
import com.hx.jrperson.bean.entity.ServiceThreeEntity;
import com.hx.jrperson.bean.entity.ServicesParentEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.controller.adapter.AddPhotoAdapter;
import com.hx.jrperson.controller.adapter.IssueOrdorGutAdapter;
import com.hx.jrperson.controller.adapter.ServiceNormAdapter;
import com.hx.jrperson.aboutnewprogram.preferential.MyPostCardActivity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.views.FullyGridLayoutManager;
import com.hx.jrperson.views.PersonalListView;
import com.hx.jrperson.views.ShowBigPhotoDialog;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.hx.jrperson.views.widget.OnWheelChangedListener;
import com.hx.jrperson.views.widget.WheelView;
import com.hx.jrperson.views.widget.adapters.ArrayWheelAdapter;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 发布订单 详细选项界面
 * by ge
 **/
public class IssueOrdorGutActivity extends BaseActivity implements View.OnClickListener, AddPhotoAdapter.ButtonClickListener, OnWheelChangedListener, IssueOrdorGutAdapter.OnClickIssueOrdorListener, TextWatcher {

    private PersonalListView issue_ordor_gutLV;//项目列表
    private IssueOrdorGutAdapter adapter;//项目列表适配器
    private PersonalListView service_normLV;//服务标准列表
    private ServiceNormAdapter serviceNormAdapter;//服务标准适配器
    private TextView allPriceTV, timeOrdorTV, nowAddressTV, toastTV;//总价， 预约时间, 项目发单上方提示文字
    private double allPrice = 0;//
    private List<ServiceThreeEntity.DataMapBean.ServicesBean> serviceList = new ArrayList<ServiceThreeEntity.DataMapBean.ServicesBean>();
    private Dialog dialog, changeTimeDialog;
    private Button pop_window_head_photeBT, pop_window_head_audioBT, pop_window_head_cancleBT;
    private ImageButton creatBtn;
    private RecyclerView addPhotoRV;//添加照片列表
    private FullyGridLayoutManager manager;//列表事物管理器
    private AddPhotoAdapter photoAdapter;//添加照片列表适配器
    private Button canclePhotoBtn;//取消按钮
    private List<ImageUriEntity> list = new ArrayList<ImageUriEntity>();
    private RelativeLayout changeTimeRL, priceRL, allPriceRL, describeLL;//时间选择,价格文字行布局,总价格行布局, 描述行布局
    private LinearLayout inputAddressRL;//修改地址行布局
    private Handler handler;

    //优惠券折扣卡使用相关
    private TextView reduceWhat,reduceHowMuch;

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
    private int wid, hei;//屏幕宽高
    private TextView change_time_cancleBtn, change_time_sureBtn, addressTV, titleMyHomeTV;//时间选择取消和确定按钮, 地址, 我家升级标题
    private WheelView id_dataWV, id_hoursWV, id_minWV;//时间选择三个列表
    private boolean isNowHour = true;//时间状态
    private int hourHours = 0, dateDate = 0, minminute = 0;
    private String strHour, strDate, strMinute, strWorkerNum;
    private List<String> listDate, minuteList, hoursList;//日期集合, 分钟集合, 小时集合
    private String imgPath;//拍照后照片输出路径
    private ServicesParentEntity.DataMapBean.ServicesBean bean;
    private String srv_code, addressStr = "", userCommin = "", timeStr = "", codeStr = "", addressGut = "";//二级菜单code， 地址字符串
    private Toast toast;
    private EditText userComminET, appointET;//客户说明, 指定匠人输入号
    private double x, y;
    private String parent_code, timePostStr = "";//一级列表cod值, 时间转换为时间戳
    private WaittingDiaolog creatDiaolog;
    private long time;
    private String adjustable = null, mainCode = "";//是否可修改, 小球的主code(一级分类)
    private int count = 0;//项目数量
    private int sureBtn = 1;
    ///////////////////////////////////////////
    private ImageView giveUsDetil;//下拉详情按钮
    private int numBus = 0;//计数器
    private ImageView backButton;//返回按钮
    private RelativeLayout backButtonInDetil;
    private TextView auxiliaryText;
    private TextView moHomeTe, moHomeTe2; //我家装修里面需要隐藏的内容
    private RelativeLayout apointWorker;//预约匠人内容
    ////注册广播相关内容
    private MySendReciver mysendreciver;
    //本地判断下拉打开按钮状态
    private ArrayList<Boolean> isOpen = new ArrayList();
    //从上个页面继承过来的选中表
    private ArrayList<String> isChoose = new ArrayList();


    /**
     * 填充Wheel的数据源对象。
     */
    private OrderDate orderDate;
    //我的优惠券相关
    private RelativeLayout myPostCard;
    private MySendReciver2 mysendreciver2;
    private MySendReciver3 mysendreciver3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_issue_ordor_gut);
        showTitle();
        initView();
        initData();
        setListener();
        ///////////////////////
        //存储本地位置信息拿出来
        SharedPreferences getSp=getSharedPreferences("test2",MODE_PRIVATE);
        String name1=getSp.getString("myAdress","默认");
        if (!name1.equals("默认")){
            addressTV.setText(name1.trim().substring(2));
            addressStr=addressTV.getText().toString();
        }
        /////////////////////////////////////////////////////////////////////
        giveUsDetil = (ImageView) findViewById(R.id.giveUsDetil);

        //创建新的广播接收者
        mysendreciver = new MySendReciver();
        //相当于注册页面的操作
        IntentFilter intentFilter = new IntentFilter();
        //里面放的是自定义的内容
        intentFilter.addAction("com.hx.jrperson.broadcast.MY_BROAD");
        //与接收系统的一样
        registerReceiver(mysendreciver,intentFilter);
        //创建新的广播接收者
                mysendreciver2 =new MySendReciver2();
        //相当于注册页面的操作
        IntentFilter intentFilter2 = new IntentFilter();
        //里面放的是自定义的内容
        intentFilter2.addAction("com.example.dllo.broadcast.sendPrice");
        //与接收系统的一样
        registerReceiver(mysendreciver2,intentFilter2);

        //创建新的广播接收者
        mysendreciver3 = new MySendReciver3();
        //相当于注册页面的操作
        IntentFilter intentFilter3 = new IntentFilter();
        //里面放的是自定义的内容
        intentFilter3.addAction("com.example.dllo.broadcast.desAll");
        //与接收系统的一样
        registerReceiver(mysendreciver3,intentFilter3);


        ///////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        giveUsDetil = (ImageView) findViewById(R.id.giveUsDetil);
        backButton = (ImageView) findViewById(R.id.backButton);

        auxiliaryText = (TextView) findViewById(R.id.auxiliaryText);
        //扩大返回热区按钮
        backButtonInDetil = (RelativeLayout) findViewById(R.id.backButtonInDetil);
        backButtonInDetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IssueOrdorGutActivity.this.finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IssueOrdorGutActivity.this.finish();
            }
        });


        ///////////////////////////////////////////////////////////////
        //我的优惠券相关
        myPostCard= (RelativeLayout) findViewById(R.id.myPostCard);
        myPostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(IssueOrdorGutActivity.this, MyPostCardActivity.class);
                startActivity(intent);
            }
        });

        //优惠券折扣卡使用相关
        reduceHowMuch= (TextView) findViewById(R.id.reduceHowMuch);
        reduceWhat= (TextView) findViewById(R.id.reduceWhat);

    }


    //进入页面设置
    private void showTitle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle.get("serviceParent") != null) {
                bean = (ServicesParentEntity.DataMapBean.ServicesBean) bundle.get("serviceParent");
            }
            if (bundle.getString("parent_code") != null) {
                parent_code = bundle.getString("parent_code");
            }
            if (bundle.getString("mainCode") != null) {
                mainCode = bundle.getString("mainCode");
            }
            if (bean != null) {
                showToolBar(bean.getService(), true, this, false);
                srv_code = bean.getSrv_code() + "";
            }

            if (bundle.get("serviceParent") != null) {
                serviceList = (List<ServiceThreeEntity.DataMapBean.ServicesBean>) bundle.get("serviceList");

                ///////////////////////////////////////////////
                for (int i = 0; i < serviceList.size(); i++) {
                    isOpen.add(false);
                    isChoose.add(i,"0");
                    isChoose.set(i, bundle.get(String.valueOf(i)).toString());
                    int yourNumber= Integer.parseInt(bundle.get(String.valueOf(i)).toString());
                    if (yourNumber!=0) {
                        serviceList.get(i).setBeforCount(yourNumber);
                        allPrice=allPrice+serviceList.get(i).getPrice_min()*yourNumber;

                    }

                    Log.i("llllll","这是第"+i+"位,所在位置数量为"+isChoose.get(i));

                }
                //////////////////////////////////////////////

            }




        }
    }

    @Override
    protected void initView() {
        issue_ordor_gutLV = (PersonalListView) findViewById(R.id.issue_ordor_gutLV);//项目列表
        issue_ordor_gutLV.setFocusable(false);
        allPriceTV = (TextView) findViewById(R.id.allPriceTV);//总价
        int myPrice= (int) allPrice;
        allPriceTV.setText(myPrice+"");
        addPhotoRV = (RecyclerView) findViewById(R.id.addPhotoRV);//添加照片列表
        inputAddressRL = (LinearLayout) findViewById(R.id.inputAddressRL);//修改地址行布局
        canclePhotoBtn = (Button) findViewById(R.id.canclePhotoBtn);//取消按钮
        changeTimeRL = (RelativeLayout) findViewById(R.id.changeTimeRL);//时间选择
        timeOrdorTV = (TextView) findViewById(R.id.timeOrdorTV);//预约时间
        service_normLV = (PersonalListView) findViewById(R.id.service_normLV);//服务标准列表
        creatBtn = (ImageButton) findViewById(R.id.creatBtn);//确认发单
        userComminET = (EditText) findViewById(R.id.userComminET);//客户说明
        addressTV = (TextView) findViewById(R.id.addressTV);//地址
        appointET = (EditText) findViewById(R.id.appointET);//指定匠人输入
        priceRL = (RelativeLayout) findViewById(R.id.priceRL);//总价格文字行布局行布局
        allPriceRL = (RelativeLayout) findViewById(R.id.allPriceRL);//总价格行布局
        titleMyHomeTV = (TextView) findViewById(R.id.titleMyHomeTV);//我家升级页面标题
        nowAddressTV = (TextView) findViewById(R.id.nowAddressTV);//更改按钮
        toastTV = (TextView) findViewById(R.id.toastTV);//项目发单上方提示文字
//        describeLL = (RelativeLayout) findViewById(R.id.describeLL);//描述的行布局
        ///////////////////////////////////////////////////
        moHomeTe = (TextView) findViewById(R.id.moHomeTe);
        moHomeTe2 = (TextView) findViewById(R.id.moHomeTe2);
        apointWorker = (RelativeLayout) findViewById(R.id.apointWorker);

    }

    @Override
    protected void initData() {
        handler = new Handler();
        adapter = new IssueOrdorGutAdapter(this);//项目列表适配器


        /////////////////////////////////
        adapter.addData(serviceList);

        //添加下拉状态监听
        for (int i = 0; i < serviceList.size(); i++) {
            isOpen.add(false);
        }
        /////////////////////////////////

        issue_ordor_gutLV.setAdapter(adapter);
        setListViewHeightBasedOnChildren(issue_ordor_gutLV);

        ///////////////////////////////////////////////
        manager = new FullyGridLayoutManager(this, 4);
        addPhotoRV.setLayoutManager(manager);
        photoAdapter = new AddPhotoAdapter(this);
        addPhotoRV.setAdapter(photoAdapter);
        serviceNormAdapter = new ServiceNormAdapter(this);
        service_normLV.setAdapter(serviceNormAdapter);
        if (!mainCode.equals("") && mainCode.equals("6001")) {//如果是我家升级
            issue_ordor_gutLV.setVisibility(View.GONE);
            moHomeTe2.setVisibility(View.GONE);
            apointWorker.setVisibility(View.GONE);
            moHomeTe.setVisibility(View.GONE);
            priceRL.setVisibility(View.GONE);
            allPriceRL.setVisibility(View.GONE);
//            titleMyHomeTV.setVisibility(View.VISIBLE);
        }
        /////////////////////////////////////////////////////////
        if (!mainCode.equals("") && mainCode.equals("5001")) {
            apointWorker.setVisibility(View.GONE);
        }
        //////////////////////////////////////////////////////////////
        //默认地址(定位后自己的位置)
        if (PreferencesUtils.getString(IssueOrdorGutActivity.this, Consts.ADDRESSMYLOCATION) != null) {
            addressStr = PreferencesUtils.getString(IssueOrdorGutActivity.this, Consts.ADDRESSMYLOCATION);
        }
        if (!addressStr.equals("")) {
            addressTV.setText(addressStr.trim());//地址
        }

//        adapter.addData(serviceList);
        serviceNormAdapter.addData(serviceList);
        if (!mainCode.equals("") && mainCode.equals("6001")) {//如果是我家升级

            serviceList.get(0).setBeforCount(1);//我家升级默认项目数量为1
            toastTV.setText("真匠人设计师会免费上门量房设计。");
        } else if (!mainCode.equals("") && mainCode.equals("5001")) {
            toastTV.setText("装修设计师立即出发为您服务。");
        }
        EventBus.getDefault().post(1);


        String xl = PreferencesUtils.getString(IssueOrdorGutActivity.this, Consts.X);
        String yl = PreferencesUtils.getString(IssueOrdorGutActivity.this, Consts.Y);
//        x = Double.valueOf(xl);
        //      y = Double.valueOf(yl);

        issue_ordor_gutLV.setDividerHeight(0);
        issue_ordor_gutLV.setDivider(null);
        service_normLV.setDividerHeight(0);
        service_normLV.setDivider(null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Map<String, String> map) {
        Log.i("mmmmmm","这个方法运行了");
        if (map != null) {
            String key = map.get("key");//这是
            if (key.equals("1")) {
                return;
            }
            addressStr = map.get("address");
            addressTV.setText(addressStr);
            codeStr = map.get("code");
            addressGut = map.get("addressGut");
            String cityName = map.get("cityName");
            GeoCodeOption option = new GeoCodeOption();
            option.city(cityName);
            option.address(addressStr);
            GeoCoder coder = GeoCoder.newInstance();
            coder.geocode(option);
            coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
//                        Toast toast = Toast.makeText(IssueOrdorGutActivity.this, "查不到", Toast.LENGTH_LONG);
//                       toast.setGravity(Gravity.CENTER, 0, 40);
//                        toast.show();
                        return;
                    } else {
                        LatLng latLng = geoCodeResult.getLocation();
                        x = latLng.latitude;
                        y = latLng.longitude;
                    }
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                }
            });
        }
    }


    @Override
    protected void setListener() {
        adapter.setOnClickIssueOrdorListener(this);
        inputAddressRL.setOnClickListener(this);
        photoAdapter.setButtonClickListener(this);
        canclePhotoBtn.setOnClickListener(this);
        changeTimeRL.setOnClickListener(this);
        creatBtn.setOnClickListener(this);
        userComminET.addTextChangedListener(this);
        addressTV.setOnClickListener(this);
        nowAddressTV.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nowAddressTV://修改地址
                Intent intent = new Intent(this, InputAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.canclePhotoBtn://取消
                photoAdapter.addData(list, 0);
                canclePhotoBtn.setVisibility(View.GONE);
                break;
            case R.id.changeTimeRL://选择时间行布局
                changeTimeDialog = creatChangeDataDialog(this, R.style.MyDialogStyleBottom);
                changeTimeDialog.show();
                break;
            case R.id.creatBtn://确认发单按钮
                if (timeOrdorTV.getText().toString().equals("点击选择时间")){
                    showToast("请选择预约时间");
                }else {
                    clickTimer();
                }
                break;
        }
    }

    public void clickTimer() {
        Timer timer = null;
        if (sureBtn == 1) {
            sureBtn = 0;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sureBtn = 1;
                }
            }, 10000);
            clickCreatBtn();
        } else {
            showToast("您的操作频率过快");
        }
    }


    //确认发单按钮
    private void clickCreatBtn() {
        /////////////////////////////


        String preCommText = userComminET.getText().toString();
        userComminET.setSelection(userComminET.getText().toString().length());
        ///////////////////////////////

        //////////////////////////////
        if (preCommText.length() > 50) {
            showToast("描述详情不超过50个字");
            return;
        }
        boolean isLogin = PreferencesUtils.getBoolean(this, Consts.ISLOGIN);
        if (isLogin) {
            Log.i("mmmmmm",timeOrdorTV.getText().toString());


            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).getBeforCount() == 0) {
                    continue;
                }
                count++;
                int adj = serviceList.get(i).getAdjustable();
                if (adj == 1) {
                    adjustable = "1";
                }
            }
            if (count > 0) {
                if (!addressStr.isEmpty() && !"".equals(addressStr)) {
                    creatDiaolog = new WaittingDiaolog(IssueOrdorGutActivity.this);
                    creatDiaolog.show();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                                creatOrder();

                        }
                    }.start();
                } else {

                    showToast("请填写地址");
                }
            } else {
                showToast("请先选择维修项目");
            }
            ///////////////////////////////////////////////////////

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }





    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //上传订单
    private void creatOrder() {
        Log.i("dddddd",timeOrdorTV.getText().toString());
        if (timeOrdorTV.getText().equals(" 点击选择时间")){
            showToast("请选择预约时间");
            return;
        }
        userCommin = userComminET.getText().toString().trim();
        strWorkerNum = appointET.getText().toString().trim();//指定匠人工号
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        if (null != timePostStr && !timePostStr.equals("")) {
            time = Long.valueOf(timePostStr) * 1000;
        } else {//没有选择时间  默认系统当前时间
            //////////////////////////////////////////////
            //在没有选择时间的情况弹出Dilog
            Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
            time = now.getTime();

//            AlertDialog.Builder alert2=new AlertDialog.Builder(this);
//            View nameView= LayoutInflater.from(this).inflate(R.layout.prompt,null);
//            alert2.setView(nameView);
//            alert2.show();
//            finish();

        }
        long b = Long.valueOf(phone);
        JSONObject object = new JSONObject();
        try {
            object.put(Consts.SEVICE_ID, parent_code);
            object.put(Consts.ORDERTYPE, bean.getParent_code());
            object.put(Consts.ADJUSTABLE, adjustable);
            if (!userCommin.equals("")) {
                object.put(Consts.COMMENT, userCommin);
            }
            if (!strWorkerNum.equals("") && strWorkerNum != null) {
                object.put(Consts.WORKER_NO, strWorkerNum);
            } else {
                object.put(Consts.WORKER_NO, "");
            }
            object.put(Consts.APPOINTTIME, time);
            object.put(Consts.USER_ID, b);
            object.put(Consts.POST_CODE, "000000");
            object.put(Consts.ADDRESS, addressStr);
            object.put(Consts.X, x);
            object.put(Consts.Y, y);
            object.put(Consts.TOTALPRICE, allPrice);
            JSONObject jsonObject = null;
            JSONArray array = new JSONArray();
            if (parent_code.equals("6001")) {//如果是我家升级 默认加一 转到服务器
                jsonObject = new JSONObject();
                ServiceThreeEntity.DataMapBean.ServicesBean bean = serviceList.get(0);
                jsonObject.put(Consts.SEVICE_ID, bean.getSrv_code() + "");
                jsonObject.put(Consts.NUMBER, serviceList.get(0).getBeforCount());
                array.put(jsonObject);
            } else {//不是我家升级 正常拼接
                for (int i = 0; i < serviceList.size(); i++) {
                    if (serviceList.get(i).getBeforCount() == 0) {//数量等于零 跳过  不传
                        continue;
                    }
                    jsonObject = new JSONObject();
                    ServiceThreeEntity.DataMapBean.ServicesBean bean = serviceList.get(i);
                    jsonObject.put(Consts.SEVICE_ID, bean.getSrv_code() + "");
                    jsonObject.put(Consts.NUMBER, serviceList.get(i).getBeforCount());
                    array.put(jsonObject);
                }
            }
            object.put(Consts.SALES, array);
            JSONArray imgArray = new JSONArray();
            JSONArray jsonArray = null;
            if (list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    jsonArray = new JSONArray();
                    Bitmap bitmap;
                    bitmap = getBitmapFromUri(Uri.parse(list.get(j).getLargImgPath()));
                    jsonArray = JrUtils.bitmapToBase64(bitmap);
                    imgArray.put(jsonArray);
                }
                object.put(Consts.IMGDATA, imgArray);
            } else {
                jsonArray = new JSONArray();
                object.put(Consts.IMGDATA, jsonArray);//没有图片时候传空
            }
            OkHttpClient httpClient = new OkHttpClient();
            JrController.setCertificates(IssueOrdorGutActivity.this, httpClient, IssueOrdorGutActivity.this.getAssets().open("zhenjren.cer"));
            String token = PreferencesUtils.getString(IssueOrdorGutActivity.this, Consts.TOKEN);
            String url = API.CREATORDER;
            RequestBody body = RequestBody.create(JSON, object.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("token", token)
                    .tag(this)
                    .build();
            httpClient.setConnectTimeout(15, TimeUnit.SECONDS);
            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        creatDiaolog.dismiss();
                        EventBus.getDefault().post(1000);
                        if (null != ServiceGutActivity.inance) {
                            ServiceGutActivity.inance.finish();
                        }
                        IssueOrdorGutActivity.this.finish();
                    }
                } else if (response.code() == 401) {
                    creatDiaolog.dismiss();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(IssueOrdorGutActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    creatDiaolog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
                creatDiaolog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(IssueOrdorGutActivity.this, "未能连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    //toast
    private void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(str);
            toast.show();
        }
    }

    //增加减小当前页面的选项
    @Override
    public void onClickIssueOrdor(View v, double all, int position, int befor) {
        switch (v.getId()) {
            case R.id.issue_addIB://加

                befor = befor + 1;
                serviceList.get(position).setBeforCount(befor);
                adapter.addData(serviceList);
                allPrice = all + allPrice;
                handler.post(runnable);
                break;
            case R.id.issue_subIB://减
                befor = befor - 1;
                serviceList.get(position).setBeforCount(befor);
                adapter.addData(serviceList);
                allPrice = all + allPrice;
                handler.post(runnable);
                break;
        }
    }

    //创建底部菜单dialog
    private Dialog creatDialog(Context context, int stytle) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.buttom_popupwindow_head, null);
        pop_window_head_photeBT = (Button) dialogView.findViewById(R.id.pop_window_head_photeBT);
        pop_window_head_audioBT = (Button) dialogView.findViewById(R.id.pop_window_head_audioBT);
        pop_window_head_cancleBT = (Button) dialogView.findViewById(R.id.pop_window_head_cancleBT);
        popWindowListener();

        final Dialog customDialog = new Dialog(context, stytle);

        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        localLayoutParams.x = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.y = WindowManager.LayoutParams.MATCH_PARENT;
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        dialogView.setMinimumWidth(screenWidth);

        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setContentView(dialogView, localLayoutParams);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                customDialog.show();
            }
        }
        return customDialog;
    }

    Uri imgUri;//更改拍照后照片输出路径

    private void popWindowListener() {
        //拍照
        pop_window_head_photeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    imgPath = Environment.getExternalStorageDirectory().getPath() + "/jr_ordor";
                    File dir = new File(imgPath);//图片路径
                    if (!dir.exists()) {//判断文件夹是否存在
                        dir.mkdir();
                    }
                    //取时间为文件名
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddssSSS");
                    String fileName = "JR" + (dateFormat.format(new Date())) + ".jpg";
                    imgPath = imgPath + "/" + fileName;//文件路径
                    imgUri = Uri.fromFile(new File(imgPath));
                    PreferencesUtils.putString(IssueOrdorGutActivity.this, Consts.IMGURI, imgUri.toString());
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//照完照片的输出路径更改
                    startActivityForResult(takePhotoIntent, 31);
                    dialog.dismiss();
                } else {
                    Toast.makeText(IssueOrdorGutActivity.this, "找不到SD卡", Toast.LENGTH_LONG).show();
                }
            }
        });
        //相册
        pop_window_head_audioBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 21);
                dialog.dismiss();
            }
        });
        //取消
        pop_window_head_cancleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri path = null;
        ContentResolver contentResolver = IssueOrdorGutActivity.this.getContentResolver();
        Bitmap bitmap = null;
        if (resultCode == 0) {
            return;
        }
        InputStream inputStream = null;
        switch (requestCode) {
            case 31://拍照
                try {
                    imgUri = Uri.parse(PreferencesUtils.getString(IssueOrdorGutActivity.this, Consts.IMGURI) != null ? PreferencesUtils.getString(IssueOrdorGutActivity.this, Consts.IMGURI) : "");
                    inputStream = contentResolver.openInputStream(imgUri);
                    PreferencesUtils.clear(IssueOrdorGutActivity.this, Consts.IMGURI);
                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inTempStorage = new byte[70 * 1024];
                    options.inPurgeable = true;
                    options.inSampleSize = 4;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    path = JrUtils.creatFile(bitmap);
                    bitmap.recycle();
                    bitmap = null;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                addUriToAdapter(path);
                break;
            case 21://相册
                Uri uri = data.getData();
                try {
                    inputStream = contentResolver.openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = 4;
                    options.inPurgeable = true;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    path = JrUtils.creatFile(bitmap);
                    bitmap.recycle();
                    bitmap = null;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                addUriToAdapter(path);
                break;
        }
    }



    //将返回的照片uri添加到集合中，和适配器中
    public void addUriToAdapter(Uri largUri) {
        if (largUri != null) {
            ImageUriEntity entity = new ImageUriEntity();
            entity.setLargImgPath(largUri.toString());//大图地址
            photoAdapter.addItem(entity);
            if (entity != null && list != null){
                list.add(entity);
            }
        }
    }

    //点击加号 加照片
    @Override
    public void plus() {
        dialog = creatDialog(this, R.style.MyDialogStyleBottom);
        dialog.show();
    }

    //删除某张图片
    @Override
    public void remove(int position) {
        photoAdapter.removeItem(position);
        list.remove(position);
    }

    //长按图片切换布局
    private int type;

    @Override
    public void onLongType(int type) {
        this.type = type;
        photoAdapter.addData(list, type);
        canclePhotoBtn.setVisibility(View.VISIBLE);//切换布局, 取消按钮出现
    }

    //点击图片
    @Override
    public void onClickTouch(String uriStr) {
        WindowManager manager = this.getWindowManager();
        wid = manager.getDefaultDisplay().getWidth();
        hei = manager.getDefaultDisplay().getHeight();
        final ShowBigPhotoDialog dialog = new ShowBigPhotoDialog(this, uriStr, wid, hei, 3);
        dialog.show();
        dialog.setOnClickBigPhotoListener(new ShowBigPhotoDialog.OnClickBigPhotoListener() {
            @Override
            public void onClickBigPhotol(View view) {
                dialog.dismiss();
            }
        });
    }


    //创建底部时间选择dialog
    private Dialog creatChangeDataDialog(Context context, int stytle) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.buttom_popupwindow_change_data, null);
        change_time_cancleBtn = (TextView) dialogView.findViewById(R.id.change_time_cancleBtn);
        change_time_sureBtn = (TextView) dialogView.findViewById(R.id.change_time_sureBtn);
        id_dataWV = (WheelView) dialogView.findViewById(R.id.id_dataWV);
        id_hoursWV = (WheelView) dialogView.findViewById(R.id.id_hoursWV);
        id_minWV = (WheelView) dialogView.findViewById(R.id.id_minWV);
        id_dataWV.setVisibleItems(3);//设置可见条目
        id_hoursWV.setVisibleItems(3);
        id_minWV.setVisibleItems(3);
        initTime();
        changTimeListener();

        final Dialog customDialog = new Dialog(context, stytle);

        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        localLayoutParams.x = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.y = WindowManager.LayoutParams.MATCH_PARENT;
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        dialogView.setMinimumWidth(screenWidth);

        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setContentView(dialogView, localLayoutParams);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                customDialog.show();
            }
        }
        return customDialog;
    }

    //初始化整体时间
    private void initTime() {
        orderDate = new OrderDate();
        List<String> allDays = orderDate.getDaysShowInWheel();

        boolean isToday = orderDate.isToday(new Date(System.currentTimeMillis()));
        orderDate.setCurHoursList(orderDate.remainsHours(isToday));
        orderDate.setCurMinList(orderDate.remainsMin(orderDate.isSameHour(isToday)));

        /* 设置3个适配器。 */
        id_dataWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this, allDays.toArray()));
        id_hoursWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this, orderDate.getCurHoursList().toArray()));
        id_minWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this, orderDate.getCurMinList().toArray()));
    }


    //选择时间dialog上的按钮点击
    private void changTimeListener() {
        change_time_cancleBtn.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                changeTimeDialog.dismiss();
            }
        });
        change_time_sureBtn.setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View v) {
                OrderDate.UserRecorder userRecorder = orderDate.getUserRecorder();
                int userSelection = userRecorder.getUserSelection();

                Date date = orderDate.getStandardDates().get(userSelection);

                int hourSelection = userRecorder.getHourSelection();
                int minSelection = userRecorder.getMinSelection();

                String curHour = orderDate.getCurHoursList().get(hourSelection);
                String curMin = orderDate.getCurMinList().get(minSelection);

                int hour = curHour.lastIndexOf(OrderDate.UNIT_HOUR) != -1 ?
                        Integer.valueOf(curHour.substring(0, curHour.lastIndexOf(OrderDate.UNIT_HOUR))) : 0;
                int min = curMin.lastIndexOf(OrderDate.UNIT_MIN) != -1 ?
                        Integer.valueOf(curMin.substring(0, curMin.lastIndexOf(OrderDate.UNIT_MIN))) : 0;

                curHour = IssueOrdorGutActivity.hourMinTransformer(hour);
                curMin = IssueOrdorGutActivity.hourMinTransformer(min);

                if (!curHour.isEmpty() && !curMin.isEmpty()) {
                    String[] resArray = OrderDate.getYearMonthDay(date,
                            OrderDate.DAY_FILTER_PATTERN, OrderDate.SPLITTER.toString()).split("_");
                    String dateResult = orderDateGenerator(resArray[0], resArray[1], resArray[2], curHour, curMin);
                    Log.i("Result", dateResult);

                    timeOrdorTV.setText(dateResult);
                    timePostStr = JrUtils.data(dateResult);
                    Log.i("ResultResult", timePostStr);
                    changeTimeDialog.dismiss();
                }
            }
        });
        id_dataWV.addChangingListener(IssueOrdorGutActivity.this);
        id_hoursWV.addChangingListener(IssueOrdorGutActivity.this);
        id_minWV.addChangingListener(IssueOrdorGutActivity.this);
    }



    //wheelview滑动监听
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        switch (wheel.getId()) {
            case R.id.id_dataWV: {
                // 记录用户选择的位置。
                orderDate.getUserRecorder().setUserSelection(newValue);
                orderDate.getUserRecorder().setUserDate(orderDate.getStandardDates().get(newValue));

                // 如果是今天的话，那么就去更新 分 和 小时 两个Wheel。
                Date userSelectedDate = orderDate.getStandardDates().get(newValue);
                boolean isToday = orderDate.isToday(userSelectedDate);

                Date userSelectedDate_old = orderDate.getStandardDates().get(oldValue);
                boolean isToday_old = orderDate.isToday(userSelectedDate_old);

                if (!isToday && !isToday_old) {
                    // 移动到第一个位置。
                    id_hoursWV.setCurrentItem(0);
                    id_minWV.setCurrentItem(0);
                } else {
                    if (!isToday_old) {
                        List<String> allDays = orderDate.getDaysShowInWheel();
                        orderDate.setCurHoursList(orderDate.remainsHours(true));
                        orderDate.setCurMinList(orderDate.remainsMin(orderDate.isSameHour(true)));

                        /* 设置3个适配器。 */
                        id_dataWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this, allDays.toArray()));
                        id_hoursWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this, orderDate.getCurHoursList().toArray()));
                        id_minWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this, orderDate.getCurMinList().toArray()));

                        id_hoursWV.setCurrentItem(0);
                        id_minWV.setCurrentItem(0);
                    } else {
                        orderDate.setCurHoursList(orderDate.getHoursShowInWheel());
                        orderDate.setCurMinList(orderDate.getMinShowInWheel());

                        id_hoursWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this,
                                orderDate.getCurHoursList().toArray()));
                        id_minWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this,
                                orderDate.getCurMinList().toArray()));

                        // 移动到第一个位置。
                        id_hoursWV.setCurrentItem(0);
                        id_minWV.setCurrentItem(0);
                    }
                }
                break;
            }

            case R.id.id_hoursWV: {
                OrderDate.UserRecorder userRecorder = orderDate.getUserRecorder();
                if (userRecorder.isSelectedToday(userRecorder.getUserSelection())) {
                    if (newValue == 0) {
                        orderDate.setCurMinList(orderDate.remainsMin(orderDate.isSameHour(true)));
                        id_minWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this,
                                orderDate.getCurMinList().toArray()));
                        id_minWV.setCurrentItem(0);
                    } else {
                        if (oldValue == 0) {
                            orderDate.setCurMinList(orderDate.getMinShowInWheel());
                            id_minWV.setViewAdapter(new ArrayWheelAdapter<>(IssueOrdorGutActivity.this,
                                    orderDate.getCurMinList().toArray()));
                        }
                        id_minWV.setCurrentItem(0);
                    }
                } else {
                    id_minWV.setCurrentItem(0);
                }
                break;
            }

            case R.id.id_minWV: {
                break;
            }

            default: {
                break;
            }
        }

        OrderDate.UserRecorder userRecorder = orderDate.getUserRecorder();
        userRecorder.setUserSelection(id_dataWV.getCurrentItem());
        userRecorder.setHourSelection(id_hoursWV.getCurrentItem());
        userRecorder.setMinSelection(id_minWV.getCurrentItem());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.unregisterReceiver(mysendreciver);
        this.unregisterReceiver(mysendreciver2);
        this.unregisterReceiver(mysendreciver3);
    }


    /**
     * 用户描述输入框输入监听
     **/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = userComminET.getText().toString();
        userComminET.setSelection(userComminET.getText().toString().length());
        String str = JrUtils.stringFilter(editable);
        if (!editable.equals(str)) {
            userComminET.setText(str);
            userComminET.setSelection(userComminET.getText().toString().length());
            showToast("不能输入特殊字符");
            auxiliaryText.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        auxiliaryText.setVisibility(View.GONE);
        int index = userComminET.getSelectionStart() - 1;
        if (index > 0) {
            if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                Editable edit = userComminET.getText();
                userComminET.setSelection(userComminET.getText().toString().length());
                edit.delete(index, index + 1);
                showToast("输入内容不能包含表情符号");

            }
        }

        if (s.length() == 50) {
            Toast toast1 = Toast.makeText(IssueOrdorGutActivity.this, "您已经输入五十个字了", Toast.LENGTH_LONG);
            toast1.show();
        }
    }

    private String orderDateGenerator(String year, String month, String day, String hour, String min) {
        return year + "年" + month + "月" + day + "日" + hour + "点" + min + "分";
    }

    private static String hourMinTransformer(int hourOrMin) {
        if (hourOrMin >= 10) return String.valueOf(hourOrMin);
        else {
            return "0".concat(String.valueOf(hourOrMin));
        }
    }
    /////////////////////////////////
    class MySendReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data=intent.getStringExtra("test");
            String jieguo = data.substring(6,data.indexOf("*"));
            int positionForChange= Integer.parseInt(jieguo);
            String changeNum=data.substring(data.indexOf("*")+1,data.length());
            int muNum= Integer.parseInt(changeNum);



            if (data.contains("组件高度增加")&&isOpen.get(positionForChange)==false){
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) issue_ordor_gutLV.getLayoutParams();
                params.height=issue_ordor_gutLV.getHeight()+(30+muNum*50);
                issue_ordor_gutLV.setLayoutParams(params);
                isOpen.set(positionForChange,true);
            }
            if (data.contains("组件高度减小")&&isOpen.get(positionForChange)==true){
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) issue_ordor_gutLV.getLayoutParams();
                params.height=issue_ordor_gutLV.getHeight()-(30+muNum*50);
                issue_ordor_gutLV.setLayoutParams(params);
                isOpen.set(positionForChange,false);
            }





        }
        //////////////////////////////////////////

    }
    /////////////////////////////////////////////////////
    /**
     * 当ListView外层有ScrollView时，需要动态设置ListView高度
     * @param listView
     */
    protected void setListViewHeightBasedOnChildren(PersonalListView listView) {
        if(listView == null) return;
        IssueOrdorGutAdapter adapter = (IssueOrdorGutAdapter) listView.getAdapter();
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

    //接收选择优惠券页的相关信息
    class MySendReciver2 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String myPrice=intent.getStringExtra("price");
            String myid=intent.getStringExtra("id");
            String myName=intent.getStringExtra("name");
            Log.i("gggggg",myPrice+"************");


                reduceWhat.setText("使用抵值券");
                reduceWhat.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dizhiq,0,0,0);
                reduceHowMuch.setText("￥"+myPrice);



        }
    }


    //接收选择优惠券页的相关信息
    class MySendReciver3 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String myChange=intent.getStringExtra("des");

            if (myChange.equals("取消")){
                reduceWhat.setText("不使用卡券");
                reduceWhat.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                reduceHowMuch.setText("");
            }




        }
    }




}
