package com.hx.jrperson.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.adapter.ChangeAddressAdapter;
import com.hx.jrperson.bean.entity.AddressEntity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择地址省列表详细页面
 * by ge
 * **/
public class ChangeProvinceActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView changeProvinceLV;//省选择列表
//    private List<ProvideCityAreaEntity> list = new ArrayList<>();
    private ChangeAddressAdapter adapter;
    //静态变量赋值为本activity 方便在其他页面管理本activity
    public static ChangeProvinceActivity intance = null;
    private List<AddressEntity> list = new ArrayList<>();
    private AddressEntity entity;
    private Handler handler;
    //////////////////////////////////////////
    private RelativeLayout backButtonInProvince;
    private ImageView backbuttonInProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intance = this;
        setContentView(R.layout.activity_change_province);
        showToolBar("选择地址", true, this, false);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void initView() {
        changeProvinceLV = (ListView) findViewById(R.id.changeProvinceLV);
        /////////////////////////////////////////////////////////
        backButtonInProvince= (RelativeLayout) findViewById(R.id.backButtonInProvince);
        backButtonInProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProvinceActivity.this.finish();
            }
        });

        backbuttonInProvince= (ImageView) findViewById(R.id.backbuttonInProvince);
        backbuttonInProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProvinceActivity.this.finish();
            }
        });
        ////////////////////////////////////////////////////////////
    }

    //解析本地假数据
    @Override
    protected void initData() {
        handler = new Handler();
        adapter = new ChangeAddressAdapter(this);
        changeProvinceLV.setAdapter(adapter);
        String url = API.POSTCODES;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.VERSION, "1");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(ChangeProvinceActivity.this).loadGetData(ChangeProvinceActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200) {
                    Gson gson = new Gson();
                    entity = gson.fromJson(resultString, AddressEntity.class);
                    adapter.addData(entity.getDataMap().getPostCodes(), 1);
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangeProvinceActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwe10", failString);
            }
        });
        changeProvinceLV.setDividerHeight(0);
        changeProvinceLV.setDivider(null);
    }

    @Override
    protected void setListener() {
        changeProvinceLV.setOnItemClickListener(this);
    }

    //省列表行布局点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<AddressEntity.DataMapBean.PostCodesBean.SubBean> cityList = entity.getDataMap().getPostCodes().get(position).getSub();
        String proName = entity.getDataMap().getPostCodes().get(position).getName();
        Intent intent = new Intent(this, ChangeCityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cityList", (Serializable) cityList);//根据点击的省 传给城市页面集合
        bundle.putString("proName", proName);//点击的省 传给城市页面
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
