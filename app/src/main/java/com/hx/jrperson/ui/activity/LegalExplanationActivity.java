package com.hx.jrperson.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hx.jrperson.R;
import com.hx.jrperson.views.baseView.BaseActivity;

public class LegalExplanationActivity extends BaseActivity {

    private WebView legalExplanationWV;
    /////////////////////////////////////////
    private RelativeLayout backButtonInLegal;
    private ImageView backbuttonInLegal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_explanation);
        showToolBar("法律声明及隐私条款", true, this, false);
        initView();
        initData();
        ///////////////////////////////////////////
        backButtonInLegal= (RelativeLayout) findViewById(R.id.backButtonInLegal);
        backButtonInLegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LegalExplanationActivity.this.finish();
            }
        });
        backbuttonInLegal= (ImageView) findViewById(R.id.backbuttonInLegal);
        backbuttonInLegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LegalExplanationActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        legalExplanationWV = (WebView) findViewById(R.id.legalExplanationWV);
    }

    @Override
    protected void initData() {
        super.initData();
        WebSettings webSettings = legalExplanationWV.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);

        legalExplanationWV.loadUrl("file:///android_asset/law.html");
        legalExplanationWV.setWebViewClient(new webViewClient ());
    }

    @Override
    protected void setListener() {
        super.setListener();
    }


    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        legalExplanationWV = null;
        super.onDestroy();
    }
}
