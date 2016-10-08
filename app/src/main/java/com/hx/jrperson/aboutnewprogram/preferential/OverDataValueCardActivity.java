package com.hx.jrperson.aboutnewprogram.preferential;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/21.
 */
public class OverDataValueCardActivity extends BaseActivity {
    private ListView overDataValueCard;
    private PostCardDetilAdapter adapter;
    private ArrayList<PostCardClass.DataMapBean.CouponsBean> data;
    private String url;
    private int user_id;
    private RelativeLayout backButtonInOverData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overdatadis);
        //本地拿到用户userid
        SharedPreferences getSp=getSharedPreferences("TrineaAndroidCommon",MODE_PRIVATE);
        user_id=getSp.getInt("user_id", 0);
        url="http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/coupon/all?userId="+user_id+"&couponKind=2";
        overDataValueCard= (ListView)findViewById(R.id.overDataValueCard);
        backButtonInOverData= (RelativeLayout) findViewById(R.id.backButtonInOverData);
        backButtonInOverData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverDataValueCardActivity.this.finish();
            }
        });
        adapter=new PostCardDetilAdapter(getBaseContext());


        data=new ArrayList<>();

        //网络请求数据
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Gson gson=new Gson();
                PostCardClass postCardClass=gson.fromJson(response,PostCardClass.class);

                for (int i = 0; i <postCardClass.getDataMap().getCoupons().size(); i++) {
                    if (postCardClass.getDataMap().getCoupons().get(i).getUse_state()!=1) {
                        data.add(postCardClass.getDataMap().getCoupons().get(i));
                    }
                }

                adapter.setData(data);
                overDataValueCard.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

        adapter.setData(data);
        overDataValueCard.setAdapter(adapter);
    }
}
