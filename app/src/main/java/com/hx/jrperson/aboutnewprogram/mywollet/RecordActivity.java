package com.hx.jrperson.aboutnewprogram.mywollet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.aboutnewprogram.preferential.PostCardList;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/22.
 * 此页面内容为钱包功能下记录查询的相关activity
 */
public class RecordActivity extends BaseActivity {
    //显示当前余额的listview
    private PostCardList recordListview;
    //查询余额的listview的适配器
    private RecordAdapter adapter;
    //查询余额的相关数据类
    private ArrayList<RecordDetilClass.DataMapBean.DataBean> data;
    //查询余额的相关地址
    private String url;
    //用户登录时产生的用户身份标志
    private int user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //本地拿到用户userid
        SharedPreferences getSp = getSharedPreferences("TrineaAndroidCommon", MODE_PRIVATE);
        user_id = getSp.getInt("user_id", 0);
        //绑定视图
        setContentView(R.layout.recordpage);
        recordListview = (PostCardList) findViewById(R.id.recordListview);
        adapter = new RecordAdapter(getBaseContext());
        data = new ArrayList<>();
        //获取当前记录的网络请求操作
        url = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/charge/records?userId=" + user_id + "&pageNow=1&pageSize=10";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                //分好的实体类,可以用Gson工具进行直接分解
                RecordDetilClass dataBean = gson.fromJson(response, RecordDetilClass.class);
                for (int i = 0; i < dataBean.getDataMap().getData().size(); i++) {
                    //进行数据的重组和分离
                    data.add(dataBean.getDataMap().getData().get(i));
                }
                adapter.setData(data);
                recordListview.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecordActivity.this, "数据获取错误", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
