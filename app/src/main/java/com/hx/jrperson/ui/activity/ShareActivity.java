package com.hx.jrperson.ui.activity;

import android.os.Bundle;

import com.hx.jrperson.R;
import com.hx.jrperson.views.baseView.BaseActivity;

public class ShareActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        showToolBar("分享", true, this, false);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {


    }
}
