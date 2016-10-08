package com.hx.jrperson.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.adapter.CancleOrdorBecauseAdapter;
import com.hx.jrperson.bean.entity.CancleBecauseEntity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户取消订单原因页面
 * by ge
 **/
public class CancleBecauseActivity extends BaseActivity implements CancleOrdorBecauseAdapter.OnClickCancleBecauseListener, View.OnClickListener {

    private TextView cancle_becauseIV;
    private RecyclerView CancleOrdorBecuseRV;
    private GridLayoutManager manager;
    private CancleOrdorBecauseAdapter adapter;
    private CancleBecauseEntity entity;
    private List<CancleBecauseEntity> list;
    private String parmes = "";
    private String staute = "";
    private Handler handler;
    /////////////////////////////////////
    private RelativeLayout backButtonInBack;
    private ImageView backbuttonInBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancle_because);
        showToolBar("用户取消", true, this, false);
        initView();
        initData();
        setListener();
        backButtonInBack= (RelativeLayout) findViewById(R.id.backButtonInBack);
        backButtonInBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancleBecauseActivity.this.finish();
            }
        });
        backbuttonInBack= (ImageView) findViewById(R.id.backbuttonInBack);
        backbuttonInBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancleBecauseActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        //确定发送按钮
        cancle_becauseIV = (TextView) findViewById(R.id.cancle_becauseIV);
        //取消订单按钮
        CancleOrdorBecuseRV = (RecyclerView) findViewById(R.id.CancleOrdorBecuseRV);
    }

    @Override
    protected void initData() {
        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        staute = bundle.getString("staute");
        manager = new GridLayoutManager(this, 2);
        CancleOrdorBecuseRV.setLayoutManager(manager);
        adapter = new CancleOrdorBecauseAdapter(this);
        CancleOrdorBecuseRV.setAdapter(adapter);
        getCancelTags();
    }

    @Override
    protected void setListener() {
        adapter.setOnClickCancleBecauseListener(this);
        cancle_becauseIV.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle_becauseIV://确定取消按钮
                clickCancleBtn();
                break;
        }
    }

    //确定
    private void clickCancleBtn() {
        for (int i = 0; i < list.size(); i++){
            CancleBecauseEntity becauseEntity = list.get(i);
            if (becauseEntity.getIsClick() == 0){
                if (parmes.equals("")){
                    parmes = parmes +  becauseEntity.getCode();
                }else {
                    parmes = parmes + "," + becauseEntity.getCode();
                }
            }
        }

        if (!parmes.equals("")){
            String url = API.ALTERORDERSTAUTE;
            Map<String, String> map = new HashMap<>();
            map.put(Consts.ORDER_ID, staute);
            map.put(Consts.ORDER_STATUS, "0");
            map.put(Consts.PARAM, parmes);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(this).loadGetData(CancleBecauseActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200){
                        if (code != 401){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CancleBecauseActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(CancleBecauseActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            CancleBecauseActivity.this.finish();
                        }
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CancleBecauseActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    Log.i("geanwen", failString);
                }
            });
        }else {
            Toast.makeText(this, "请选择一个原因", Toast.LENGTH_SHORT).show();
        }

    }


    private void getCancelTags() {
        String url = API.CANCELTAGS;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.VERSION, "0");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(CancleBecauseActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if(code == 200){
                        try {
                            JSONObject jsonObject = new JSONObject(resultString);
                            JSONObject jsonMap = jsonObject.getJSONObject("dataMap");
                            JSONArray jsonArray = jsonMap.getJSONArray("order_status");
                            list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonOrder_status = jsonArray.getJSONObject(i);
                                entity = new CancleBecauseEntity();
                                entity.setCode(jsonOrder_status.getString("code"));
                                entity.setContent(jsonOrder_status.getString("content"));
                                list.add(entity);
                            }
                            if (list.size() > 0) {
                                adapter.addData(list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CancleBecauseActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
     * 列表中行布局点击回调
     **/
    @Override
    public void onClickCancleBecause(View view, int position) {

    }
}


