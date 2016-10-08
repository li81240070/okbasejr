package com.hx.jrperson.aboutnewprogram.thirdversion.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.MyOrdorEntity;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.aboutnewprogram.preferential.PostCardList;
import com.hx.jrperson.ui.activity.LoginActivity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 */
public class AllOrderFragment extends Fragment{
    private Context context;
    private PostCardList allOrderListView;
    private OrderAdapterForAll adapter;
    private ArrayList<MyOrdorEntity.DataMapBean.OrdersBean>data;
    private MySendReciver mysendreciver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view=LayoutInflater.from(getContext()).inflate(R.layout.allorderpage,null);
        allOrderListView= (PostCardList) view.findViewById(R.id.allOrderListView);
        //注册广播接受者
        //创建新的广播接收者
        mysendreciver = new MySendReciver();
//相当于注册页面的操作
        IntentFilter intentFilter=new IntentFilter();

//里面放的是自定义的内容
        intentFilter.addAction("com.example.dllo.broadcast.reflushYourOrder");

//与接收系统的一样
       getActivity().registerReceiver(mysendreciver,intentFilter);

        //拉取订单数据信息
        String url ="https://123.57.185.198/ZhenjiangrenManagement/api/v1/customer/orderlist";
        final String phone = PreferencesUtils.getString(getContext(), Consts.PHONE_PF);
        if (phone.equals("15940909208")) {
            getDateInALL();
            }else{

            Intent intent=new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
        }



        return view;
    }


    //重新获得登陆后的数据信息
    private void getDateInALL(){
        //拉取订单数据信息
        String url ="https://123.57.185.198/ZhenjiangrenManagement/api/v1/customer/orderlist";
        final String phone = PreferencesUtils.getString(getContext(), Consts.PHONE_PF);

        final Date date = new Date();
        final long time = date.getTime();
        Map<String, String> map = new HashMap<>();
        map.put(Consts.USER_ID,phone);
        map.put(Consts.PAGENO, "1");
        map.put(Consts.TIMESTAMP, time + "");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(getContext()).loadGetData(getActivity(), url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {

                Gson gson = new Gson();
                MyOrdorEntity orderClass = gson.fromJson(resultString, MyOrdorEntity.class);
                adapter = new OrderAdapterForAll(getContext());
                data = new ArrayList<MyOrdorEntity.DataMapBean.OrdersBean>();
                for (int i = 0; i < orderClass.getDataMap().getOrders().size(); i++) {
                    data.add(orderClass.getDataMap().getOrders().get(i));
                }
                adapter.setData(data);
                adapter.notifyDataSetChanged();
                allOrderListView.setAdapter(adapter);

            }

            @Override
            public void fail(String failString, Exception e) {
                // waittingDiaolog.dismiss();
                Toast.makeText(getActivity(), "暂无订单信息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //带值广播接收者
    class MySendReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String data=intent.getStringExtra("order");
            if (data.equals("刷新当前菜单")){
                getDateInALL();
            }



        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mysendreciver);
    }


}
