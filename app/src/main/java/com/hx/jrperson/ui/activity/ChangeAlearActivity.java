package com.hx.jrperson.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hx.jrperson.R;
import com.hx.jrperson.controller.adapter.ChangeAddressAdapter;
import com.hx.jrperson.bean.entity.AddressEntity;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeAlearActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView changeAlearLV;
    private List<AddressEntity.DataMapBean.PostCodesBean.SubBean.SubTwoBean> alearList = new ArrayList<>();
    private ChangeAddressAdapter adapter;
    private String title, cityName;
    ///////////////////////////////////
    private RelativeLayout backButtonInAlear;
    private ImageView backbuttonInAlear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_alear);
        showTitle();
        initView();
        initData();
        setListener();
    }

    private void showTitle() {
        Bundle bundle = getIntent().getExtras();
        alearList = (List<AddressEntity.DataMapBean.PostCodesBean.SubBean.SubTwoBean>) bundle.getSerializable("alearList");
        title = bundle.getString("alearName");
        cityName = bundle.getString("cityName");
        showToolBar(title, true, this, false);
    }

    @Override
    protected void initView() {
        changeAlearLV = (ListView) findViewById(R.id.changeAlearLV);
        backButtonInAlear= (RelativeLayout) findViewById(R.id.backButtonInAlear);
        backButtonInAlear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAlearActivity.this.finish();
            }
        });
        backbuttonInAlear= (ImageView) findViewById(R.id.backbuttonInAlear);
        backbuttonInAlear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAlearActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        adapter = new ChangeAddressAdapter(this);
        changeAlearLV.setAdapter(adapter);
        adapter.addDataAlear(alearList, 3);
    }

    @Override
    protected void setListener() {
        changeAlearLV.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        title = title + alearList.get(position).getName();
        Map<String, String> map = new HashMap<>();
        map.put("address", title);
        map.put("code", alearList.get(position).getCode() + "");
        map.put("cityName", cityName);
        map.put("key", "1");
        EventBus.getDefault().post(map);//将最终选择结果
        ChangeProvinceActivity.intance.finish();
        ChangeCityActivity.intance.finish();
        this.finish();
    }
}
