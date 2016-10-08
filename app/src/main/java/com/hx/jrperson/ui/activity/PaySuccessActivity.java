package com.hx.jrperson.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.adapter.AddImpressAdapter;
import com.hx.jrperson.bean.entity.JudgeEntity;
import com.hx.jrperson.bean.entity.MyOrdorEntity;
import com.hx.jrperson.bean.entity.OrderEntity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.FullyGridLayoutManager;
import com.hx.jrperson.views.SharePopupwindow;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.squareup.okhttp.MediaType;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付成功界面
 * by ge
 **/
public class PaySuccessActivity extends BaseActivity implements View.OnClickListener, AddImpressAdapter.OnAddImpressClickListener, TextWatcher {

    private LinearLayout mainLayout;
    private RecyclerView evaluateRV;
    private FullyGridLayoutManager manager;
    private AddImpressAdapter adapter;
    private TextView sendEvaluteTV, allPriceTV;//添加印象按钮
    private RatingBar starBtn;//评星星
    private TextView shareTV;
    private TextView complainTV;
    private SharePopupwindow mSharePopupwindow;
    private EditText evauateET;//评价输入的文字
    private MyOrdorEntity.DataMapBean.OrdersBean bean;
    private OrderEntity entity = null;
    private Handler handler;
    private Runnable inforRunnable = new Runnable() {
        @Override
        public void run() {
            double d = entity.getDataMap().getTotal_price();
            String st= String.format("%.2f", d);
            allPriceTV.setText(st + "元");//总价钱
        }
    };
    private List<JudgeEntity> list = new ArrayList<>();
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        showToolBar("待评价", true, this, false);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void initView() {
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        evaluateRV = (RecyclerView) findViewById(R.id.evaluateRV);
        sendEvaluteTV = (TextView) findViewById(R.id.sendEvaluteTV);
        starBtn = (RatingBar) findViewById(R.id.starBtn);
        evauateET = (EditText) findViewById(R.id.evauateET);
        allPriceTV = (TextView) findViewById(R.id.allPriceTV);
        shareTV = (TextView) findViewById(R.id.baseactivity_share_TV);
        complainTV = (TextView) findViewById(R.id.baseactivity_complain_TV);
    }

    @Override
    protected void initData() {
        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            bean = (MyOrdorEntity.DataMapBean.OrdersBean) bundle.getSerializable("orderEntity");
        }
        if (bean != null) {
            final String priceShowing = bean.getPrice().concat("元");
            allPriceTV.setText(priceShowing);
        }

        manager = new FullyGridLayoutManager(this, 3);
        evaluateRV.setLayoutManager(manager);
        adapter = new AddImpressAdapter(this);
        evaluateRV.setAdapter(adapter);
        String url = API.EVALUATIONS;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.VERSION, "0");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(PaySuccessActivity.this).loadGetData(PaySuccessActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200) {
                    try {
                        JSONObject object = new JSONObject(resultString);
                        JSONObject object1 = object.getJSONObject("dataMap");
                        JSONArray array = object1.getJSONArray("order_status");
                        list = new ArrayList<JudgeEntity>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object2 = (JSONObject) array.get(i);
                            JudgeEntity entity = new JudgeEntity();
                            entity.setCode(JrUtils.analyzeJsonToArray(object2, "key"));
                            entity.setGut(JrUtils.analyzeJsonToArray(object2, "value"));
                            list.add(entity);
                        }
                        adapter.addData(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaySuccessActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
    protected void setListener() {
        sendEvaluteTV.setOnClickListener(this);
        adapter.setOnAddImpressClickListener(this);
        complainTV.setOnClickListener(this);
        shareTV.setOnClickListener(this);
        adapter.setOnAddImpressClickListener(this);
        evauateET.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendEvaluteTV://添加印象按钮
//                reachView();//刷新
                clickSendBtn();
                break;
            case R.id.baseactivity_share_TV:// 分享
                if (mSharePopupwindow != null && mSharePopupwindow.isShowing()) {
                    mSharePopupwindow.dismiss();
                    mSharePopupwindow = null;
                }
                mSharePopupwindow = new SharePopupwindow(this);
                mSharePopupwindow.showAtLocation(mainLayout, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.baseactivity_complain_TV:// 反馈
                Intent intent = new Intent(PaySuccessActivity.this, ComPlainActivity.class);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("order_id", bean.getOrder_id());
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                break;
        }
    }

    public void showToast(String string) {
        if (toast == null) {
            toast = Toast.makeText(PaySuccessActivity.this, string, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(string);
            toast.show();
        }
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //添加印象按钮
    private void clickSendBtn() {
        String evauate = evauateET.getText().toString().trim();
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            JudgeEntity entity = list.get(i);
            if (entity.getIsClick() == 1) {
                if (str.equals("")) {
                    String[] s = entity.getCode();
                    for (int j = 0; j < s.length; j++) {
                        str = str + s[j];
                    }
                } else {
                    String[] s = entity.getCode();
                    for (int j = 0; j < s.length; j++) {
                        str = str + s[j];
                    }
                }
            }
        }
        String url = API.COO;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDERID, bean.getOrder_id()  );
        map.put(Consts.COMMENT, evauate);
        map.put(Consts.LABELIDS, str);
        Map<String, Integer> map1 = new HashMap<>();
        map1.put(Consts.STAR, (int) starBtn.getRating());
        url = JrUtils.appendParams(url, map);
        url = JrUtils.appendParamss(url, map1);
        NetLoader.getInstance(PaySuccessActivity.this).loadOrdinaryPostData(PaySuccessActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(final String resultString, int code) {
                if (code == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(PaySuccessActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaySuccessActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {

            }
        }, null);
    }


    //刷新页面
    private void reachView() {
        final WaittingDiaolog waittingDiaolog = new WaittingDiaolog(this);
        waittingDiaolog.show();
        String order_id = bean.getOrder_id();
        String url = API.ORDER_DETAIL;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.ORDER_ID, order_id);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(PaySuccessActivity.this).loadGetData(PaySuccessActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                waittingDiaolog.dismiss();
                if (code == 200) {
                    Gson gson = new Gson();
                    entity = gson.fromJson(resultString, OrderEntity.class);
                    handler.post(inforRunnable);
                    String staute = entity.getDataMap().getOrder_status() + "";
                    if (staute.equals("6")) {
                        showToast("工人支付未完成，请等待支付完成后进行评价。");
                    } else {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                clickSendBtn();
                            }
                        }.start();
                    }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaySuccessActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
    public void onAddImpressClick(View view, int position, List<JudgeEntity> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    @Override
    protected void onDestroy() {
        if (null != MyOrdorActivity.insance){
            MyOrdorActivity.insance.finish();
        }
        EventBus.getDefault().post(2222);
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = evauateET.getText().toString();
        String str = JrUtils.stringFilter(editable);
        if (!editable.equals(str)) {
            evauateET.setText(str);
            showToast("不能输入特殊字符");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        int index = evauateET.getSelectionStart() - 1;
        if (index > 0) {
            if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                Editable edit = evauateET.getText();
                edit.delete(index, index + 1);
                showToast("输入内容不能包含表情符号");
            }
        }
    }
}
