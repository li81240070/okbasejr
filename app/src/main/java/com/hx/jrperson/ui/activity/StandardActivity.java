package com.hx.jrperson.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
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
 * Created by ge on 2016/3/31.
 */
public class StandardActivity extends BaseActivity {
    private WebView standardWV;
    private String standard;
    private Handler handler;
    /////////////////////////////////////////////////
    private RelativeLayout backButtonInMyStandard;
    private ImageView backbuttonInMyStandard;
    //////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Integer staute) {
        if (staute == 10002) {
            standard = standard.replaceAll("&amp;", "");
            standard = standard.replaceAll("quot;", "\"");
            standard = standard.replaceAll("lt;", "<");
            standard = standard.replaceAll("gt;", ">");
            standardWV.loadDataWithBaseURL(null, standard, "text/html", "utf-8", null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        showToolBar("维修标准", true, this, false);
        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
        backbuttonInMyStandard= (ImageView) findViewById(R.id.backbuttonInMyStandard);
        backbuttonInMyStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandardActivity.this.finish();
            }
        });
        backButtonInMyStandard= (RelativeLayout) findViewById(R.id.backButtonInMyStandard);
        backButtonInMyStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandardActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        standardWV = (WebView) findViewById(R.id.standardWV);

        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                standardWV.removeJavascriptInterface("searchBoxJavaBridge_");
                standardWV.removeJavascriptInterface("accessibility");
                standardWV.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }

    @Override
    protected void initData() {
        handler = new Handler();
        String url = API.STANDARD;
        Map<String, String> map = new HashMap<String, String>();
        map.put(Consts.VERSION, "0");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(StandardActivity.this).loadGetData(StandardActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int codes) {
                if (codes == 200){
                    try {
                        JSONObject json = new JSONObject(resultString);
                        int code =json.getInt("code");
                        if(code==200){
                            JSONObject jsonDataMap = json.getJSONObject("dataMap");
                            standard = jsonDataMap.getString("standard");
                            EventBus.getDefault().post(10002);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (codes == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StandardActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
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
    protected void setListener() {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
