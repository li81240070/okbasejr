package com.hx.jrperson.aboutnewprogram.thirdversion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hx.jrperson.ui.activity.ShowInforActivity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.AutoListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 * 住页面布局中活动碎片部分,整体包含于mainactivity中,从原活动页面分离出来的内容,以下注释为个人理解
 */
public class MainActFragment extends Fragment {
    //联系文件
    private Context context;
    //当前消息的整体列表
    private AutoListView inforGutLVInMain;//消息列表
    //整体消息列表的适配器
    private InforGutAuAdapter adapter;//消息列表适配器
    //第一次进入该页面时显示的活动的页数
    private int page = 1;
    private RelativeLayout backButtonInReferential;
    private ImageView backbuttonInpReferential;
    //请求到的数据类
    private List<NewInforEntity.DataMapBean.ActivitylistBean> list = new ArrayList<>();
    //请求到的数据类
    private List<NewInforEntity.DataMapBean.ActivitylistBean> loadList = new ArrayList<>();
    //上拉刷新和下拉加载的handler信息传递
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AutoListView.REFRESH:
                    loadList.clear();
                    loadList.addAll(list);
                    adapter.addData(list);
                    inforGutLVInMain.onRefreshComplete();
                    break;
                case AutoListView.LOAD:
                    loadList.addAll(list);
                    adapter.addDataLoad(list);
                    inforGutLVInMain.onLoadComplete();
                    break;
            }
            inforGutLVInMain.setResultSize(list.size());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.mainactpage, null);
        inforGutLVInMain = (AutoListView) view.findViewById(R.id.inforGutLVInMain);
        //载入数据封装的方法
        initData();
        //设置监听封装的方法
        setListener();
        return view;
    }


    //具体的封装的载入数据方法,内容为第一版内容,需要自己理解下
    private void initData() {
        inforGutLVInMain.setDividerHeight(0);
        inforGutLVInMain.setDivider(null);
        adapter = new InforGutAuAdapter(getContext());
        inforGutLVInMain.setAdapter(adapter);
        addData(AutoListView.REFRESH, 1);
    }

    private void addData(final int what, final int type) {
        String url = API.GAININFORGUT;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.PAGE, page + "");
        map.put(Consts.STEP, "10");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(getActivity()).loadGetData(url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int type) {
                if (type == 200) {
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
                } else if (type == 401) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {

            }
        });

    }

    //设置各种监听事件封装的方法,为第一版内容
    private void setListener() {
        inforGutLVInMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position - 1;
                if (position == loadList.size() + 1) {
                    return;
                }
                int msgId = loadList.get(pos).getActivity_id();
                Intent intent = new Intent(getActivity(), ShowInforActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("msgId", msgId);
                bundle.putInt("type", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        inforGutLVInMain.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                addData(AutoListView.REFRESH, 1);
            }
        });
        inforGutLVInMain.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                addData(AutoListView.LOAD, 2);
            }
        });
    }
}
