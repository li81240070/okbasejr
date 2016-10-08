package com.hx.jrperson.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.RegisterEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 忘记密码界面
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    //下一步， 发送验证
    private Button nextBtn, sendMessageBtn;
    //输入手机号， 验证码
    private EditText phoneEditET, messageEditET;
    private String phone, message, messagePhone;//手机号验证码
    private boolean isNext = false, isSend = false;
    private String sendMessage;
    private Handler handler;
    private int time = 60;
    public static ForgetPwdActivity inance;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                isSend = false;
                sendMessageBtn.setText(String.valueOf(time).concat("s"));
                time = time - 1;
                handler.postDelayed(runnable, 1000);
            } else {
                time = 60;
                phone = phoneEditET.getText().toString();
                if (phone.length() == 11) {
                    isSend = true;
                    sendMessageBtn.setText("验证");
                    sendMessageBtn.setBackgroundResource(R.mipmap.send_btn);
                } else {
                    isSend = false;
                    sendMessageBtn.setText("验证");
                    sendMessageBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
                }
            }
        }
    };
    private Toast toast;
    /////////////////////////////////
    //忘记密码返回功能按钮
    private RelativeLayout backButtonInFindPassWord;
    private ImageView backbuttonInFindPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        inance = this;
        showToolBar("找回密码", true, this, false);
        initView();
        initData();
        setListener();
        backButtonInFindPassWord= (RelativeLayout) findViewById(R.id.backButtonInFindPassWord);
        backButtonInFindPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPwdActivity.this.finish();
            }
        });
        backbuttonInFindPassWord= (ImageView) findViewById(R.id.backbuttonInFindPassWord);
        backbuttonInFindPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPwdActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        nextBtn = (Button) findViewById(R.id.nextBtn);
        sendMessageBtn = (Button) findViewById(R.id.sendMessageBtn);
        phoneEditET = (EditText) findViewById(R.id.phoneEditET);
        messageEditET = (EditText) findViewById(R.id.messageEditET);
    }

    @Override
    protected void initData() {
        handler = new Handler();
    }

    @Override
    protected void setListener() {
        nextBtn.setOnClickListener(this);
        sendMessageBtn.setOnClickListener(this);
        phoneEditET.setOnClickListener(this);
        messageEditET.setOnClickListener(this);
        phoneEditET.setOnTouchListener(this);
        messageEditET.setOnTouchListener(this);
        phoneEditET.addTextChangedListener(new MyTextWathch(phoneEditET));
        messageEditET.addTextChangedListener(new MyTextWathch(messageEditET));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextBtn://下一步
                clickNextBtn();
                break;
            case R.id.sendMessageBtn://验证
                clickSendMessage();
                break;
            case R.id.phoneEditET://输入手机号
                initStatue(phoneEditET);
                break;
            case R.id.messageEditET: //输入验证码
                initStatue(messageEditET);
                break;
        }
    }

    //下一步
    private void clickNextBtn() {
        if (isNext){
            if (messagePhone == null || "".equals(messagePhone)){
                showToast("请先获取验证码");
                return;
            }
            if(messagePhone.equals(phoneEditET.getText().toString())){
                if (message.equals(sendMessage)) {
                    PreferencesUtils.putString(ForgetPwdActivity.this, Consts.PHONE_PF, phone);
                    Intent intent = new Intent(this, NewPwdActivity.class);
                    intent.putExtra("newPwd", "2");
                    startActivity(intent);
                } else {
                    showToast("验证码输入错误");
                }
            } else {
                showToast("手机号和验证手机不一致");
            }
        }
    }

    /**
     * 点击验证
     **/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void clickSendMessage() {
        if (isSend) {
            phone = phoneEditET.getText().toString();

            boolean isForm = JrUtils.isMobileNO(phone);
            if (!isForm) {
                showToast("手机号格式不正确");
                return;
            }
            handler.post(runnable);
            messagePhone = phone;
            String url = API.FORGETCODE;
            Map<String, String> map = new HashMap<>();
            map.put(Consts.MOBILE, phone);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(this).loadGetData(ForgetPwdActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200) {
                        Log.i("geanwen5", resultString);
                        Gson gson = new Gson();
                        RegisterEntity entity = gson.fromJson(resultString, RegisterEntity.class);
                        if (entity != null) {
                            switch (entity.getCode()) {
                                case 200:
                                    PreferencesUtils.putString(ForgetPwdActivity.this, Consts.PHONE_PF, phone);
//                                    Toast.makeText(ForgetPwdActivity.this, "发送成功，请在一分钟内完成注册", Toast.LENGTH_SHORT).show();
                                    sendMessage = entity.getDataMap().getVaildCode();
                                    break;
                            }
                        }
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ForgetPwdActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    Log.i("geanwen6", failString);
                }
            });
        }
    }


    private void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(ForgetPwdActivity.this, message, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(message);
            toast.show();
        }
    }


    /**
     * edittext触摸监听事件
     **/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        initStatue(v);
        return false;
    }

    //清空状态  并改变点击的控件状态
    private void initStatue(View v) {
        phoneEditET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        messageEditET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        phoneEditET.clearFocus();
        messageEditET.clearFocus();
        v.setBackgroundResource(R.drawable.shape_forget_psw_edit);
        v.requestFocus();
        textWatcherLisener();//监听edittext输入情况
    }

    private void textWatcherLisener() {
        phone = phoneEditET.getText().toString().trim();
        message = messageEditET.getText().toString().trim();
        if ((!phone.equals("")) && (!message.equals("")) && message.length() == 6 && phone.length() == 11){
            isNext = true;
            nextBtn.setBackgroundResource(R.mipmap.bluebutton);
            nextBtn.setTextColor(getResources().getColor(R.color.material_black));
        } else {
            isNext = false;
            nextBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
            nextBtn.setTextColor(getResources().getColor(R.color.material_white));
        }
    }

    //多个edittext监听
    class MyTextWathch implements TextWatcher {

        private EditText editText;

        public MyTextWathch(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            initStatue(editText);
            if (editText == phoneEditET) {
                if (phone.length() == 11) {
                    boolean isForm = JrUtils.isMobileNO(phone);
                    if(!isForm){
                        showToast("手机号格式不正确");
                        return;
                    }
                    isSend = true;
                    sendMessageBtn.setBackgroundResource(R.mipmap.backtohome);
                } else {
                    isSend = false;
                    sendMessageBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        inance = null;
        if (handler != null){
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }
}
