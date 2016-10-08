package com.hx.jrperson.aboutnewprogram.preferential;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hx.jrperson.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 * 打折卡显示碎片,嵌入在MyPostCardActivity中
 */
public class ValuePostFragment extends Fragment {
    //打折卡信息的显示页面
    private ListView valuePostListview;
    //打折卡内容的适配器
    private ValueCardAdapter adapter;
    //获取到的打折卡数据的相关数据
    private ArrayList<PostCardClass.DataMapBean.CouponsBean> data;
    //当没有打折卡数据时掉用的文件
    private RelativeLayout noneData2;
    //获取当前打折卡信息的网址
    private String url;
    //联系文件
    private Context context;
    //前往过期打折卡页面的按钮和底栏防止挡住的相关组件
    private TextView goOverData, goAway;
    //在用户登陆时产生的唯一用户识别标志
    private int user_id;
    //自定义的广播类,用于告知全局当前发生的局部点击事件
    private MySendReciver mysendreciver;
    //记录当前是否不使用优惠券
    private int isReal = 0;
    //记录当前是第几次点击打折卡内容
    private int remNum = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.valuepostpage, null);
        //本地拿到用户userid
        SharedPreferences getSp = getActivity().getSharedPreferences("TrineaAndroidCommon", context.MODE_PRIVATE);
        user_id = getSp.getInt("user_id", 0);
        //获取打折卡信息的地址
        url = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/coupon/all?userId=" + user_id + "&couponKind=2";
        //注册接收取消指令的广播
        //创建新的广播接收者
        mysendreciver = new MySendReciver();
        //相当于注册页面的操作
        IntentFilter intentFilter = new IntentFilter();
        //里面放的是自定义的内容
        intentFilter.addAction("com.example.dllo.broadcast.desAll");
        //与接收系统的一样
        getActivity().registerReceiver(mysendreciver, intentFilter);
        //绑定视图中的各个组件
        valuePostListview = (ListView) view.findViewById(R.id.valuePostListview);
        noneData2 = (RelativeLayout) view.findViewById(R.id.noneData2);
        goAway = (TextView) view.findViewById(R.id.goAway);
        //创建新的适配器
        adapter = new ValueCardAdapter(getContext());
        //前往过期的打折卡页面的按钮和前往事件
        goOverData = (TextView) view.findViewById(R.id.goOverData);
        goOverData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), OverDataValueCardActivity.class);
                startActivity(intent);
            }
        });
        data = new ArrayList<>();
        //网络请求数据(关于当前拥有的打折卡信息)
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                PostCardClass postCardClass = gson.fromJson(response, PostCardClass.class);
                //进行符合打折卡显示要求的数据的筛选
                for (int i = 0; i < postCardClass.getDataMap().getCoupons().size(); i++) {
                    if (postCardClass.getDataMap().getCoupons().get(i).getUse_state() == 1) {
                        data.add(postCardClass.getDataMap().getCoupons().get(i));
                    }
                }
                //当前没有符合打折卡显示条件的情况下显示的页面调出
                if (data.size() == 0) {
                    noneData2.setVisibility(View.VISIBLE);
                    goAway.setVisibility(View.GONE);
                }
                adapter.setData(data);
                valuePostListview.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        if (data.size() == 0) {
            noneData2.setVisibility(View.VISIBLE);
        }
        adapter.setData(data);
        valuePostListview.setAdapter(adapter);
        //实现点击事件的监听
        adapter.setChooseValue(new ChooseValue() {
            @Override
            public void chooseOne(int position) {
                //当没有选中不使用优惠券按钮的情况下
                if (isReal == 0) {
                    //判断当前是第几次点击打折卡的子类
                    if (remNum == -1) {
                        //从listview中调出北调记得相关子类
                        RelativeLayout postBackValue = (RelativeLayout) valuePostListview.getChildAt(position).findViewById(R.id.postBackValue);
                        //对子类进行视图上的背景修改
                        postBackValue.setBackgroundResource(R.mipmap.cardchose);
                        //将当前点击过的子类的位置进行记录
                        remNum = position;
                    } else {
                        //从listview中调出北调记得相关子类
                        RelativeLayout postBackValue = (RelativeLayout) valuePostListview.getChildAt(remNum).findViewById(R.id.postBackValue);
                        postBackValue.setBackgroundResource(R.mipmap.valueback);
                        RelativeLayout ppostBackValue = (RelativeLayout) valuePostListview.getChildAt(position).findViewById(R.id.postBackValue);
                        ppostBackValue.setBackgroundResource(R.mipmap.cardchose);
                        remNum = position;
                    }
                }
            }
        });
        return view;
    }

    //带值广播接收者,用于全局情况下点击事件反应的响应
    class MySendReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //当前不使用优惠券按钮被取消
            String data = intent.getStringExtra("des");
            if (data.equals("取消")) {
                desAll();
                isReal = 1;
            }
            //当前不使用优惠券按钮被选中
            if (data.equals("选中")) {
                isReal = 0;
            }
        }
    }
    //销毁当前页面时主动销毁动态注册的广播接收者
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mysendreciver);
    }

    //一键取消所有优惠券选择
    private void desAll() {
        if (remNum != -1) {
            RelativeLayout postBackValue = (RelativeLayout) valuePostListview.getChildAt(remNum).findViewById(R.id.postBackValue);
            postBackValue.setBackgroundResource(R.mipmap.valueback);
        }
    }
}
