package com.hx.jrperson.aboutnewprogram.preferential;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * 显示优惠券的相关内容的碎片文件,整体嵌入在MyPostcardActivity文件中
 */
public class DiscountPostFragment extends Fragment {
    //联系文件
    private Context context;
    //优惠券显示的listview
    private ListView discountPostListview;
    //显示优惠券具体信息的listview的适配器
    private PostCardDetilAdapter adapter;
    //优惠券信息的具体数据类
    private ArrayList<PostCardClass.DataMapBean.CouponsBean> data;
    //查看过期优惠券和无优惠券的点击按钮
    private RelativeLayout lookOverData, noneData;
    //查看用户登录时产生的唯一身份标志
    private int user_id;
    //查询当前优惠券信息的地址
    private String url;
    //为了底栏不挡住显示内容加高的部分
    private TextView getOut;
    //数值记录器,记录当前点击事件是第几次点击优惠券
    private int remNum = -1;
    //广播类,用于传递当前的点击事件到工程的各个位置,方便进行操作
    private MySendReciver mysendreciver;
    //记录当前是否不使用优惠券
    private int isReal = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.discountpostpage, null);
        //本地拿到用户userid
        SharedPreferences getSp = getActivity().getSharedPreferences("TrineaAndroidCommon", context.MODE_PRIVATE);
        user_id = getSp.getInt("user_id", 0);
        //用于获取当前优惠券信息的地址
        url = "http://123.57.185.241:8180/ZhenjiangrenManagement/api/v1/coupon/all?userId=" + user_id + "&couponKind=1";
        //注册接收取消指令的广播
        //创建新的广播接收者
        mysendreciver = new MySendReciver();
        //相当于注册页面的操作
        IntentFilter intentFilter = new IntentFilter();
        //里面放的是自定义的内容
        intentFilter.addAction("com.example.dllo.broadcast.desAll");
        //与接收系统的一样
        getActivity().registerReceiver(mysendreciver, intentFilter);
        //绑定视图内的各个组件
        lookOverData = (RelativeLayout) view.findViewById(R.id.lookOverData);
        discountPostListview = (ListView) view.findViewById(R.id.discountPostListview);
        noneData = (RelativeLayout) view.findViewById(R.id.noneData);
        adapter = new PostCardDetilAdapter(getContext());
        getOut = (TextView) view.findViewById(R.id.getOut);
        data = new ArrayList<>();
        //网络请求数据
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                PostCardClass postCardClass = gson.fromJson(response, PostCardClass.class);
                //从获取的全部数据中分理处符合当前页面显示条件的有用数据
                for (int i = 0; i < postCardClass.getDataMap().getCoupons().size(); i++) {
                    if (postCardClass.getDataMap().getCoupons().get(i).getUse_state() == 1) {
                        data.add(postCardClass.getDataMap().getCoupons().get(i));
                    }
                }
                //当前没有符合条件的数据时,调用无数据显示的页面
                if (data.size() == 0) {
                    noneData.setVisibility(View.VISIBLE);
                    getOut.setVisibility(View.GONE);
                }
                adapter.setData(data);
                discountPostListview.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        //跳转到查看已过期券的页面
        lookOverData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), OverDataActivity.class);
                startActivity(intent);
            }
        });
        //实现点击事件的监听
        adapter.setChooseCard(new ChooseCard() {
            @Override
            public void chooseOne(int position, ArrayList<PostCardClass.DataMapBean.CouponsBean> data) {
                //查询点击时间是否满足条件
                if (isReal == 0) {
                    if (remNum == -1) {
                        RelativeLayout postBack = (RelativeLayout) discountPostListview.getChildAt(position).findViewById(R.id.postBack);
                        postBack.setBackgroundResource(R.mipmap.or);
                        //取得优惠券的面值
                        Log.i("nnnnnn", data.get(position).getCoupon_id() + "  " + data.get(position).getCoupon_price());
                        Intent intent = new Intent("com.example.dllo.broadcast.sendPrice");
                        //往广播里面放内容
                        intent.putExtra("id", data.get(position).getCoupon_id() + "");
                        intent.putExtra("price", data.get(position).getCoupon_price() + "");
                        //进入广播启动项
                        getActivity().sendBroadcast(intent);
                        remNum = position;
                    } else {
                        //获取listview内各个部分的代理权
                        RelativeLayout postBack = (RelativeLayout) discountPostListview.getChildAt(remNum).findViewById(R.id.postBack);
                        postBack.setBackgroundResource(R.mipmap.detilpostfirst);
                        RelativeLayout postBack2 = (RelativeLayout) discountPostListview.getChildAt(position).findViewById(R.id.postBack);
                        postBack2.setBackgroundResource(R.mipmap.or);
                        //取得优惠券的面值
                        Intent intent = new Intent("com.example.dllo.broadcast.sendPrice");
                        //往广播里面放内容
                        intent.putExtra("id", data.get(position).getCoupon_id() + "");
                        intent.putExtra("price", data.get(position).getCoupon_price() + "");
                        //进入广播启动项
                        getActivity().sendBroadcast(intent);
                        remNum = position;
                    }
                }
            }
        });
        return view;
    }

    //一键取消所有优惠券选择的方法,点击后全工程销毁已记录的优惠券选择信息
    private void desAll() {
        if (remNum != -1) {
            RelativeLayout postBack = (RelativeLayout) discountPostListview.getChildAt(remNum).findViewById(R.id.postBack);
            postBack.setBackgroundResource(R.mipmap.detilpostfirst);
        }
    }

    //带值广播接收者
    class MySendReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("des");
            if (data.equals("取消")) {
                desAll();
                isReal = 1;
            }
            if (data.equals("选中")) {
                isReal = 0;
            }
        }
    }

    //当该页面销毁时,销毁动态注册的广播类
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mysendreciver);
    }
}
