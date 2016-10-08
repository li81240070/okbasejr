package com.hx.jrperson.aboutnewprogram.mywollet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.PayWxInfo;
import com.hx.jrperson.ui.activity.PayActivity;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.squareup.okhttp.MediaType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MyWollet extends BaseActivity {
    //收到参数的数据类
    private ArrayList<PreferentialClass.DataMapBean.DataBean> data;
    //视图中对应的recyclerview显示
    private RecyclerView inputMoneyRecyclerView;
    //recyclerview的适配器
    private InputMoneyAdapter adapter;
    //查询优惠信息的相关请求地址
    private String url = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/charge/coupons?userId=1&pageNow=1&pageSize=10";
    //充值按钮
    private Button giveMoneyButton;
    //充值请求地址
    private String urlForMoney = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/payment/charge/start";
    private String HowMany;
    //用户输入的金额框
    private EditText userGive;
    //支付方式选择记录,默认为微信支付(1微信支付,2支付宝支付)
    private int ChooseWhat = 1;
    //默认上传给服务器的参数规则
    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //获取当前余额的地址
    private String urlForSelect;
    //余额显示的按钮
    private TextView textView16;
    //查询充值记录
    private TextView recordForWollet;
    //弹出的支付手段选择页面
    private LinearLayout popUp, byeChoose;
    private TextView changeChoose;
    //弹出的微信和支付宝所在页面选项
    private RelativeLayout chooseWeixin, chooseZhifubao;
    private ImageView weixinAllRight, zhifubaoAllRight;
    //弹出框计数器
    private int popNum = 0;
    //记录当前选择的支付手段是微信支付还是支付宝支付,0微信支付,1支付宝支付
    private int payStyle = 0;
    //页面上的返回按钮
    private RelativeLayout backButtonInWollet;
    private ImageView backbuttonInWollet;
    //选择优惠券的计数器
    private int chooseNum = -1;
    //记录当前选择的优惠ID
    private int couponsIdForChoose = 0;
    //吊起支付接口
    // 微信支付信息
    private PayWxInfo wxInfo;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //本地拿到用户userid
        SharedPreferences getSp = getSharedPreferences("TrineaAndroidCommon", MODE_PRIVATE);
        user_id = getSp.getInt("user_id", 0);
        //绑定视图
        setContentView(R.layout.mywolletpage);
        //绑定视图内的各个组件
        inputMoneyRecyclerView = (RecyclerView) findViewById(R.id.inputMoneyRecyclerView);
        adapter = new InputMoneyAdapter(this);
        data = new ArrayList();
        userGive = (EditText) findViewById(R.id.userGive);
        textView16 = (TextView) findViewById(R.id.textView16);
        //弹出框注册
        popUp = (LinearLayout) findViewById(R.id.popUp);
        byeChoose = (LinearLayout) findViewById(R.id.byeChoose);
        chooseWeixin = (RelativeLayout) findViewById(R.id.chooseWeixin);
        weixinAllRight = (ImageView) findViewById(R.id.weixinAllRight);
        chooseZhifubao = (RelativeLayout) findViewById(R.id.chooseZhifubao);
        zhifubaoAllRight = (ImageView) findViewById(R.id.zhifubaoAllRight);
        //返回按钮的相关销毁事件
        backButtonInWollet = (RelativeLayout) findViewById(R.id.backButtonInWollet);
        backButtonInWollet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWollet.this.finish();
            }
        });
        backbuttonInWollet = (ImageView) findViewById(R.id.backbuttonInWollet);
        backbuttonInWollet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWollet.this.finish();
            }
        });
        //点击选择框按钮调出选择页面和相对应的显示隐藏逻辑
        changeChoose = (TextView) findViewById(R.id.changeChoose);
        changeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popNum == 0) {
                    popUp.setVisibility(View.VISIBLE);
                    popNum = 1;
                }
            }
        });
        byeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popNum == 1) {
                    popUp.setVisibility(View.GONE);
                    popNum = 0;
                }

            }
        });
        //弹出框中微信部分的点击事件
        //当在选择框选择微信支付时页面其他部分产生的相关变化逻辑
        chooseWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payStyle = 0;
                changeChoose.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.weixin, 0, R.mipmap.changeword, 0);
                changeChoose.setText("微信支付");
                weixinAllRight.setVisibility(View.VISIBLE);
                zhifubaoAllRight.setVisibility(View.GONE);
                popUp.setVisibility(View.GONE);
                ChooseWhat = 1;
                popNum = 0;
            }
        });
        //弹出框中的支付宝部分的点击事件
        //当在选择框选择支付宝支付时页面其他部分产生的相关变化逻辑
        chooseZhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payStyle = 1;
                changeChoose.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.zhifubaoball, 0, R.mipmap.changeword, 0);
                changeChoose.setText("支付宝支付");
                weixinAllRight.setVisibility(View.GONE);
                zhifubaoAllRight.setVisibility(View.VISIBLE);
                popUp.setVisibility(View.GONE);
                ChooseWhat = 2;
                popNum = 0;
            }
        });

        //网络请求数据,请求优惠信息列表
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Gson gson = new Gson();
                PreferentialClass preferentialClass = gson.fromJson(response, PreferentialClass.class);


                for (int i = 0; i < preferentialClass.getDataMap().getData().size(); i++) {

                    data.add(preferentialClass.getDataMap().getData().get(i));

                }
                //添加数据
                adapter.setData(data);
                //创建一个线性布局管理器

                //设置布局的方向
                inputMoneyRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                inputMoneyRecyclerView.setAdapter(adapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyWollet.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);


        //充值按钮的点击事件
        giveMoneyButton = (Button) findViewById(R.id.giveMoneyButton);
        giveMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //对输入框中的金额进行判断
                if (!userGive.getText().toString().equals("")) {


                    final double userChoose = Double.valueOf(userGive.getText().toString());
                    HowMany = null;
                    //最后一个参数为优惠代码,默认为0


                    //开启充值的网络请求
                    Map params = new HashMap();
                    params.put("customerId", user_id);
                    params.put("payType", ChooseWhat);
                    //优惠券id暂时为临时
                    params.put("couponId", userGive.getText());
                    params.put("chargeAmount", 0.01);
//                    final JSONObject jsonObject = new JSONObject(params);
//                    RequestQueue queue = Volley.newRequestQueue(MyWollet.this);
//                    StringRequest stringRequest=new StringRequest(Request.Method.POST, urlForMoney, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.i("rrrrrr",response.toString());
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.i("rrrrrr",error.toString());
//                        }
//                    }){
//                        @Override
//                        public byte[] getBody() throws AuthFailureError {
//                            return jsonObject.toString().getBytes();
//                        }
//                    };
//                    queue.add(stringRequest);
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    JSONObject jsonObject = new JSONObject(params);
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, urlForMoney, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    PayActivity payActivity = new PayActivity();

                                    payActivity.getPay(response.toString(), MyWollet.this, ChooseWhat, MyWollet.this);


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("pppppp", error.toString());
                        }
                    }) {


                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Accept", "application/json");
                            headers.put("Content-Type", "application/json; charset=UTF-8");

                            return headers;
                        }
                    };
                    requestQueue.add(jsonRequest);

                } else {
                    Toast.makeText(MyWollet.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                }

            }
        });
        urlForSelect = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/charge/remain?userId=" + user_id;
        //获取当前余额的网络申请
        StringRequest stringRequest2 = new StringRequest(urlForSelect, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Gson gson = new Gson();
                SelectMoneyClass selectMoneyClass = gson.fromJson(response, SelectMoneyClass.class);


                textView16.setText("￥" + selectMoneyClass.getDataMap().getData().getRemine_amount());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyWollet.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest2);
        //查询充值记录相关操作
        recordForWollet = (TextView) findViewById(R.id.recordForWollet);
        recordForWollet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyWollet.this, RecordActivity.class);
                startActivity(intent);
            }
        });
        //实现接口相关,各个优惠券的点击效果
        adapter.setChangeMyColor(new ChangeMyColor() {
            @Override
            public void clickMe(int position, InputMoneyAdapter.myViewHolder viewHolder) {
                if (chooseNum == -1) {

                    userGive.setText("");
                    giveMoneyButton.setText("立即充值" + "(实际到账" + (data.get(position).getGift_amount() + data.get(position).getNeed_amount()) + "元)");
                    //向输入框进行赋值
                    viewHolder.introduceBack.setBackgroundResource(R.mipmap.choosemoney);
                    viewHolder.chooseHowMuch.setTextColor(0XFF3399FF);
                    chooseNum = position;
                } else {
                    userGive.setText("");
                    giveMoneyButton.setText("立即充值" + "(实际到账" + (data.get(position).getGift_amount() + data.get(position).getNeed_amount()) + "元)");
                    inputMoneyRecyclerView.getChildAt(chooseNum).setBackgroundResource(R.mipmap.notchoosemoney);
                    TextView textView = (TextView) inputMoneyRecyclerView.getChildAt(chooseNum).findViewById(R.id.chooseHowMuch);
                    textView.setTextColor(0xff868686);
                    viewHolder.introduceBack.setBackgroundResource(R.mipmap.choosemoney);
                    viewHolder.chooseHowMuch.setTextColor(0XFF3399FF);
                    chooseNum = position;
                }

            }
        });

        //点击输入框的情况下清空所有已选择内容
        userGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseNum != -1) {
                    inputMoneyRecyclerView.getChildAt(chooseNum).setBackgroundResource(R.mipmap.notchoosemoney);
                    TextView textView = (TextView) inputMoneyRecyclerView.getChildAt(chooseNum).findViewById(R.id.chooseHowMuch);
                    textView.setTextColor(0xff868686);
                    //清空输入框

                    giveMoneyButton.setText("立即充值");
                }
            }
        });
        //加入输入内容监听
        userGive.addTextChangedListener(watcher);
    }

    //强制竖屏

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    //文字转化功能的监听,监控当前用户输入金额时的输入状态
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            if (userGive.getText().toString().length() > 0) {

                Log.i("gggggg", s.toString() + "*****");

                int realPrice = Integer.parseInt(userGive.getText().toString());

                int myNum = 0;
                for (int i = 0; i < data.size(); i++) {
                    if (realPrice == data.get(i).getNeed_amount()) {
                        giveMoneyButton.setText("立即充值" + "(实际到账" + (data.get(i).getGift_amount() + data.get(i).getNeed_amount()) + "元)");
                        myNum = 1;
                    }

                }
                if (myNum == 0) {
                    giveMoneyButton.setText("立即充值" + "(实际到账" + userGive.getText() + "元)");
                }
            }
        }
    };


}
