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
import com.hx.jrperson.bean.entity.NewInforEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.adapter.InforGutAuAdapter;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.AutoListView;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信息详情界面
 * by ge
 * **/
public class InforGutActivity extends BaseActivity implements AdapterView.OnItemClickListener, AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

    private AutoListView inforGutLV;//消息列表
    private InforGutAuAdapter adapter;//消息列表适配器
    private int page = 1;
    ////////////////////////////////////////
    private RelativeLayout backButtonInReferential;
    private ImageView backbuttonInpReferential;
    ////////////////////////////////////////
    private List<NewInforEntity.DataMapBean.ActivitylistBean> list = new ArrayList<>();
    private List<NewInforEntity.DataMapBean.ActivitylistBean> loadList = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case AutoListView.REFRESH:
                    loadList.clear();
                    loadList.addAll(list);
                    adapter.addData(list);
                    inforGutLV.onRefreshComplete();
                    break;
                case AutoListView.LOAD:
                    loadList.addAll(list);
                    adapter.addDataLoad(list);
                    inforGutLV.onLoadComplete();
                    break;
            }
            inforGutLV.setResultSize(list.size());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_gut);
        showToolBar("优惠活动", true, this, false);
        initView();
        initData();
        setListener();
        backButtonInReferential= (RelativeLayout) findViewById(R.id.backButtonInReferential);
        backButtonInReferential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InforGutActivity.this.finish();
            }
        });
        backbuttonInpReferential= (ImageView) findViewById(R.id.backbuttonInpReferential);
        backbuttonInpReferential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InforGutActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        inforGutLV = (AutoListView) findViewById(R.id.inforGutLV);
    }

    @Override
    protected void initData() {
        inforGutLV.setDividerHeight(0);
        inforGutLV.setDivider(null);
        adapter = new InforGutAuAdapter(this);
        inforGutLV.setAdapter(adapter);
        addData(AutoListView.REFRESH, 1);
    }

    private void addData(final int what, final int type) {
        String url = API.GAININFORGUT;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.PAGE, page + "");
        map.put(Consts.STEP, "10");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(InforGutActivity.this).loadGetData(url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int type) {
                if (type == 200){
                    Gson gson = new Gson();
                    NewInforEntity newInforEntity = gson.fromJson(resultString, NewInforEntity.class);
                    if (newInforEntity.getCode() == 200) {
                        list.clear();
                        list.addAll(newInforEntity.getDataMap().getActivitylist());
                        Message message = handler.obtainMessage();
                        message.what = what;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }else if (type == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InforGutActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {

            }
        });

    }


    @Override
    protected void setListener() {
        inforGutLV.setOnItemClickListener(this);
        inforGutLV.setOnRefreshListener(this);
        inforGutLV.setOnLoadListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = position - 1;
        if (position == loadList.size() + 1){
            return;
        }
        int msgId = loadList.get(pos).getActivity_id();
        Intent intent = new Intent(InforGutActivity.this, ShowInforActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("msgId", msgId);
        bundle.putInt("type", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 1;
        addData(AutoListView.REFRESH, 1);
    }

    @Override
    public void onLoad() {
        page++;
        addData(AutoListView.LOAD, 2);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(1212);
        super.onDestroy();
    }
}
