package com.hx.jrperson.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;


/**
 * 消息点击进入详情展示（webview）
 * Created by ge on 2016/5/14.
 */
public class ShowInforActivity extends BaseActivity {

    private WebView mWebView;
    private WaittingDiaolog diaolog;
    private RelativeLayout backButtonInMyMoney;
    private ImageView backbuttonInMyMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_infor);
        initView();
        initData();
        setListener();
        backButtonInMyMoney= (RelativeLayout) findViewById(R.id.backButtonInMyMoney);
        backButtonInMyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInforActivity.this.finish();
            }
        });
        backbuttonInMyMoney= (ImageView) findViewById(R.id.backbuttonInMyMoney);
        backbuttonInMyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInforActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
//        showInforGutWV = (WebView) findViewById(R.id.showInforGutWV);
    }

    @Override
    protected void initData() {
        showToolBar("活动详情", true, ShowInforActivity.this, false);

        WebView mWebView = new WebView(getApplicationContext());
        LinearLayout mll = (LinearLayout) findViewById(R.id.showInforGutWV);
        mll.addView(mWebView);

        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
        diaolog = new WaittingDiaolog(ShowInforActivity.this);
        diaolog.show();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);

        String url = API.INFORLIST;

        Intent startIntent = getIntent();
        Bundle startBundle = startIntent.getExtras();
        int type = startBundle.getInt("type");
        int msgId = startBundle.getInt("msgId");
        url = url.concat(msgId + "");
        mWebView.loadUrl(url);
        //设置Web视图
        mWebView.setWebViewClient(new webViewClient ());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100){
                    //加载完成
                    diaolog.dismiss();
                }else {

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null){
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
