package com.hx.jrperson;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.OkHttpClientManager;
import com.hx.jrperson.utils.common.util.PreferencesUtils;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

/**
 * 匠人应用程序的application
 * <p/>
 * Created by dave on 2016/2/29 0029.
 */
public class JrApplication extends Application {

    public static JrApplication application = null;


    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        SDKInitializer.initialize(this);//百度地图
        JPushInterface.init(this);//推送
        String s = JPushInterface.getRegistrationID(this);
        //如果不是第一次向激光注册 则会在本回调方法中返回registrationId 并保存到缓存中
        PreferencesUtils.putString(this, Consts.REGISTRATIONID, s);
//        JPushInterface.setDebugMode(true);/**/
//        NetLoader.getInstance();//okhttp初始化
        try {
            OkHttpClientManager.getInstance()
                    .setCertificates(getAssets().open("zhenjren.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        application = null;
    }
}
