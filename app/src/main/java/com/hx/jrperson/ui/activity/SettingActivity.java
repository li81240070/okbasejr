package com.hx.jrperson.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.views.CallPhoneDialog;
import com.hx.jrperson.views.baseView.BaseActivity;

/**
 * 设置界面
 * by ge
 * **/
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout account_safeRL, helpRL, versionsRL, idearBackRL;
    private TextView nowVersionTV, custom_service_phoneTV;
    private ImageView service_help_phone;
    //////////////////////////////////////////////////////
    private RelativeLayout backButtonInMySetting;
    private ImageView backbuttonInMySetting;
    private RelativeLayout serverStateForCom,serverPrograss,aboutUsCom;
    private TextView backNoneLogin;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        showToolBar("系统设置", true, this, false);
        initView();
        initData();
        setListener();
        //实现跳转维修标准功能
        serverStateForCom= (RelativeLayout) findViewById(R.id.serverStateForCom);
        serverStateForCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, StandardActivity.class);
                startActivity(intent);
            }
        });
        //实现跳转服务流程功能
        serverPrograss= (RelativeLayout) findViewById(R.id.serverPrograss);
        serverPrograss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent liucheng_intent = new Intent(SettingActivity.this, ServiceProcessActivity.class);
                startActivity(liucheng_intent);
            }
        });
        //实现关于我们功能
        aboutUsCom= (RelativeLayout) findViewById(R.id.aboutUsCom);
        aboutUsCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutus_intent = new Intent(SettingActivity.this, AboutUsActivity.class);
                startActivity(aboutus_intent);
            }
        });
        //实现退出登录功能
        backNoneLogin= (TextView) findViewById(R.id.backNoneLogin);
        backNoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("com.example.dllo.broadcast.GoSecondPager");
                //往广播里面放内容
                intent.putExtra("heihei","back");
                //进入广播启动项
                sendBroadcast(intent);

            }
        });

        backButtonInMySetting= (RelativeLayout) findViewById(R.id.backButtonInMySetting);
        backButtonInMySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        backbuttonInMySetting= (ImageView) findViewById(R.id.backbuttonInMySetting);
        backbuttonInMySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        account_safeRL = (RelativeLayout) findViewById(R.id.account_safeRL);//账户与安全
        helpRL = (RelativeLayout) findViewById(R.id.helpRL);
        versionsRL = (RelativeLayout) findViewById(R.id.versionsRL);//版本号行布局
        idearBackRL = (RelativeLayout) findViewById(R.id.idearBackRL);//意见反馈
        nowVersionTV = (TextView) findViewById(R.id.nowVersionTV);//版本号
        service_help_phone = (ImageView) findViewById(R.id.service_help_phone);//打电话
        custom_service_phoneTV = (TextView) findViewById(R.id.custom_service_phoneTV);//客服电话
    }

    @Override
    protected void initData() {
//        String version = PreferencesUtils.getString(SettingActivity.this, Consts.VERSION);
        String version = JrUtils.getAppVersionName(getApplicationContext());
        if (version != null && !version.equals("")){
            nowVersionTV.setText(version);
        }
        custom_service_phoneTV.setText(Consts.CUSTOM_SERVICE);
    }

    @Override
    protected void setListener() {
        account_safeRL.setOnClickListener(this);
        helpRL.setOnClickListener(this);
        versionsRL.setOnClickListener(this);
        idearBackRL.setOnClickListener(this);
        service_help_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_safeRL://账户与安全
                Intent intent = new Intent(this, AccountSafeActivity.class);
                startActivity(intent);
                break;
            case R.id.helpRL://客服与帮助

                break;
            case R.id.versionsRL://版本

                break;
            case R.id.idearBackRL://意见反馈
                Intent intent1 = new Intent(this, IdaerBackActivity.class);
                startActivity(intent1);
                break;
            case R.id.service_help_phone://打电话
                clickCallCustomService();
                break;
        }
    }

    /**
     * 给客服打电话
     * **/
    private void clickCallCustomService() {
        final CallPhoneDialog callPhoneDialog = new CallPhoneDialog(SettingActivity.this, Consts.CUSTOM_SERVICE);
        callPhoneDialog.show();
        callPhoneDialog.setOnClickCancleOrdorListener(new CallPhoneDialog.OnClickCancleOrdorListener() {
            @Override
            public void onClickCancleOrdor(View view) {
                switch (view.getId()){
                    case R.id.cancle_ordor_cancleTV:
                        callPhoneDialog.dismiss();
                        break;
                    case R.id.cancle_ordor_sureTV:
                        callPhoneDialog.dismiss();
                        clickCallPhone(Consts.CUSTOM_SERVICE);
                        break;
                }
            }
        });
    }

    //给匠人打电话
    private void clickCallPhone(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        SettingActivity.this.startActivity(callIntent);
    }


}
