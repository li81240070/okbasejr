package com.hx.jrperson.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 30秒刷新匠人位置
 * Created by ge on 2016/4/26.
 */
public class WorkerLocationService extends Service {

    private Handler handler;
    private String city;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent){
            String action = intent.getAction();
            if ("com.hx.jrperson.service.WorkerLocationService".equals(action)){
                Bundle bundle = intent.getExtras();
                if (null != bundle){
                    city = bundle.getString(Consts.CITY);
                }else {
                    city = PreferencesUtils.getString(this, Consts.CITY);
                }
                handler.post(runnable);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String url = API.MASTERS;
            Map<String, String> map = new HashMap<>();
            map.put(Consts.VERSION, "1");
            map.put(Consts.CITY, city);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(WorkerLocationService.this).loadGetData(url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int type) {
                    if (type == 200) {
                        Intent intent = new Intent();
                        intent.setAction("com.hx.jrperson.service.WorkerLocationService");
                        Bundle bundle = new Bundle();
                        bundle.putString("marker", resultString);
                        intent.putExtras(bundle);
                        sendBroadcast(intent);
                        handler.postDelayed(runnable, 30000);
                    } else if (type == 401) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WorkerLocationService.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {

                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null){
            handler.removeCallbacks(runnable);
        }
    }


}
