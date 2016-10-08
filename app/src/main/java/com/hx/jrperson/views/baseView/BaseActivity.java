package com.hx.jrperson.views.baseView;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.ui.activity.PaySuccessActivity;
import com.hx.jrperson.utils.common.util.crashUtil.HxBaseCrashHandler;
import com.hx.jrperson.utils.common.view.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.List;

/**
 * 程序的基类activity
 * 主要实现的功能：
 * 1：实现连续点击退出程序
 * 2:设置页面的titlebar是否显示和标题的内容
 * 3:设置状态栏的颜色
 * 4：存储异常的日志
 */
public class BaseActivity extends AppCompatActivity {

    public static boolean isActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = Color.parseColor("#000000");
        StatusBarCompat.compat(this, color);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (!isActivity) {
        //app 从后台唤醒，进入前台

            isActivity = true;
//        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            isActivity = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化布局
     */
    protected void initView(){}

    /**
     * 初始化数据
     */
    protected void initData(){}

    /**
     * 设置监听事件
     */
    protected void setListener(){}

    /**
     * 设置状态栏的颜色
     * @param isShow            是否显示
     * @param statuBarColor    状态栏的颜色
     * @param activity          Activity
     */
    protected void showStatuBar(boolean isShow,int statuBarColor, Activity activity){
        if (isShow){
            StatusBarCompat.compat(activity, statuBarColor);
        }
    }

    /**
     * 是否打印错误日志
     * @param isSave     //是否保存异常日志
     */
    protected void saveCrashLog(boolean isSave){
        //sd卡根目录下新建文件夹
        String path = Environment.getExternalStorageDirectory() + "/crashLocatFile";
        File storePath = new File(path);
        //判断是否存在
        if (!storePath.exists()){
            storePath.mkdirs();
        }
        //异常捕获处理类
        if (isSave){
            HxBaseCrashHandler hxBaseCrashHandler = HxBaseCrashHandler.INSTANCE;
            hxBaseCrashHandler.initCrashHandler(this, path, null);
        }
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 设置是否显示toolbar
     * @param titleName         标题的名字
     * @param isShow            是否显示
     */
    protected void showToolBar(String titleName,boolean isShow,Activity activity, boolean isMainAC){

        Toolbar toolbar= (Toolbar)activity.findViewById(R.id.toolbar);
        if (isShow){
         toolbar.setVisibility(View.GONE);
        }else {
            toolbar.setVisibility(View.GONE);
        }
        TextView baseactivity_title_TV= (TextView)activity.findViewById(R.id.baseactivity_title_TV);
        TextView baseactivity_share= (TextView)activity.findViewById(R.id.baseactivity_share_TV);
        TextView baseactivity_complain= (TextView)activity.findViewById(R.id.baseactivity_complain_TV);
        ImageView navifation_myIV = (ImageView) activity.findViewById(R.id.navifation_myIV);
        ImageView centerIV = (ImageView) activity.findViewById(R.id.centerIV);
        ImageView navifation_messageIV = (ImageView) activity.findViewById(R.id.navifation_messageIV);
        if (isMainAC){
        navifation_myIV.setVisibility(View.VISIBLE);
           centerIV.setVisibility(View.VISIBLE);
    navifation_messageIV.setVisibility(View.VISIBLE);
       baseactivity_title_TV.setVisibility(View.GONE);
        }else {
            baseactivity_title_TV.setText(titleName);
            toolbar.setNavigationIcon(R.mipmap.ic_back_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (activity instanceof PaySuccessActivity) {
            baseactivity_share.setVisibility(View.VISIBLE);
            baseactivity_complain.setVisibility(View.VISIBLE);
        }else {
          baseactivity_share.setVisibility(View.GONE);
            baseactivity_complain.setVisibility(View.GONE);
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

}
