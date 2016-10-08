package com.hx.jrperson.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ServiceProcessActivity extends BaseActivity {
    private WebView serviceProcessTV;
    private String standard;
    private Handler handler;
    //////////////////////////////////
    private RelativeLayout backButtonInMyService;
    private ImageView backbuttonInMyService;
    /////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Integer staute) {
        if (staute == 10001) {
            standard = standard.replaceAll("&amp;", "");
            standard = standard.replaceAll("quot;", "\"");
            standard = standard.replaceAll("lt;", "<");
            standard = standard.replaceAll("gt;", ">");
            serviceProcessTV.loadDataWithBaseURL(null, standard, "text/html", "utf-8", null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_process);
        EventBus.getDefault().register(this);
        showToolBar("服务流程", true, this, false);
        initView();
        initData();
        setListener();
        ////////////////////////////////////////////
        backButtonInMyService= (RelativeLayout) findViewById(R.id.backButtonInMyService);
        backButtonInMyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceProcessActivity.this.finish();

            }
        });
        backbuttonInMyService= (ImageView) findViewById(R.id.backbuttonInMyService);
        backbuttonInMyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceProcessActivity.this.finish();
            }
        });
    }

    //进入页面设置


    @Override
    protected void initView() {
        serviceProcessTV = (WebView) findViewById(R.id.serviceProcessTV);

        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                serviceProcessTV.removeJavascriptInterface("searchBoxJavaBridge_");
                serviceProcessTV.removeJavascriptInterface("accessibility");
                serviceProcessTV.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }




    @Override
    protected void initData() {
        handler = new Handler();
        String url = API.FLOW;
        Map<String, String> map = new HashMap<String, String>();
        map.put(Consts.VERSION, "0");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(ServiceProcessActivity.this).loadGetData(ServiceProcessActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int codes) {
                if (codes == 200){
                    try {
                        JSONObject json = new JSONObject(resultString);
                        int code=json.getInt("code");
                        if(code==200){
                            JSONObject jsonDataMap = json.getJSONObject("dataMap");
                            standard = jsonDataMap.getString("flow");
                            EventBus.getDefault().post(10001);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (codes == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ServiceProcessActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen9", failString);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
