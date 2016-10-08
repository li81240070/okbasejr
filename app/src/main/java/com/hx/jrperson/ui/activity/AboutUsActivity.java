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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AboutUsActivity extends BaseActivity {
    private WebView aboutUsTV;
    private String about;
    private Handler handler;
    //////////////////////////////////////
    private RelativeLayout backButtonAboutUs;
    private ImageView backbuttonAboutUs;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Integer staute) {
        if (staute == 10003) {
            about = about.replaceAll("&amp;", "");
            about = about.replaceAll("quot;", "\"");
            about = about.replaceAll("lt;", "<");
            about = about.replaceAll("gt;", ">");
            aboutUsTV.loadDataWithBaseURL(null, about, "text/html", "utf-8", null);
        }
    }


    //das打双打撒
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        showToolBar("关于我们", true, this, false);
        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
        backButtonAboutUs= (RelativeLayout) findViewById(R.id.backButtonAboutUs);
        backButtonAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
        backbuttonAboutUs= (ImageView) findViewById(R.id.backbuttonAboutUs);
        backbuttonAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        aboutUsTV = (WebView) findViewById(R.id.aboutUsTV);

        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                aboutUsTV.removeJavascriptInterface("searchBoxJavaBridge_");
                aboutUsTV.removeJavascriptInterface("accessibility");
                aboutUsTV.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }

    }

    @Override
    protected void initData() {
        handler = new Handler();
        String url = API.ABOUT;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.VERSION, "0");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(AboutUsActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int type) {
                if (type != 401){
                    try {
                        JSONObject json = new JSONObject(resultString);
                        int code = json.getInt("code");
                        if (code == 200) {
                            JSONObject jsonDataMap = json.getJSONObject("dataMap");
                            about = jsonDataMap.getString("about");
                            EventBus.getDefault().post(10003);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AboutUsActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen1", failString);
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
