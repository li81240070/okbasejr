package com.hx.jrperson.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.bean.entity.LoginEntity;
import com.hx.jrperson.bean.entity.RegisterEntity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.DigestUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 注册界面
 * by ge
 **/
public class GoRegisterNowActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private EditText register_phoneET, register_messageET,
            register_inputPswET, register_putPswTwoET, register_inputNameET;//手机号， 验证码, 输入密码， 再次输入密码, 昵称
    private Button register_sendMessageBtn, register_nextBtn;//发送， 开始
    private TextView legalExplanationBtn;//法律声明
    private String phone, psw, message, pswTwo, nickName;//输入的内容
    private int time = 60;//验证时间
    private String sendMessage, messagePhone;//后台传回的验证码, 发送验证码的手机号
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                register_sendMessageBtn.setText(time + "s");
                register_sendMessageBtn.setClickable(false);
                register_sendMessageBtn.setBackgroundResource(R.mipmap.send_btn);
                time = time - 1;
                handler.postDelayed(runnable, 1000);
            } else {
                time = 60;
                phone = register_phoneET.getText().toString();
                if (phone.length() == 11) {
                    register_sendMessageBtn.setClickable(true);
                    register_sendMessageBtn.setText("验证");
                } else {
                    register_sendMessageBtn.setClickable(false);
                    register_sendMessageBtn.setText("验证");
                    register_sendMessageBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
                }
            }
        }
    };
    private Toast toast;
    private WaittingDiaolog registerDiaolog, loginDialog;
    //////////////////////////////////
    private RelativeLayout backButtonInRegistered;
    private ImageView backbuttonInRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_register_now);
        showToolBar("注册账号", true, this, false);
        initView();
        initData();
        setListener();
        backButtonInRegistered= (RelativeLayout) findViewById(R.id.backButtonInRegistered);
        backButtonInRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoRegisterNowActivity.this.finish();
            }
        });
        backbuttonInRegistered= (ImageView) findViewById(R.id.backbuttonInRegistered);
        backbuttonInRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoRegisterNowActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        register_phoneET = (EditText) findViewById(R.id.register_phoneET);//手机号
        register_messageET = (EditText) findViewById(R.id.register_messageET);//验证码
        register_sendMessageBtn = (Button) findViewById(R.id.register_sendMessageBtn);//发送按钮
        register_nextBtn = (Button) findViewById(R.id.register_nextBtn);//开始
        register_inputPswET = (EditText) findViewById(R.id.register_inputPswET);//输入密码
        register_putPswTwoET = (EditText) findViewById(R.id.register_putPswTwoET);//再次输入密码
        register_inputNameET = (EditText) findViewById(R.id.register_inputNameET);//填写昵称
        legalExplanationBtn = (TextView) findViewById(R.id.legalExplanationBtn);//法律声明
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        register_nextBtn.setClickable(false);
        register_sendMessageBtn.setClickable(false);
        register_sendMessageBtn.setOnClickListener(this);
        register_nextBtn.setOnClickListener(this);
        register_phoneET.setOnClickListener(this);
        register_messageET.setOnClickListener(this);
        register_inputPswET.setOnClickListener(this);
        register_putPswTwoET.setOnClickListener(this);
        register_inputNameET.setOnClickListener(this);
        register_phoneET.setOnTouchListener(this);
        register_messageET.setOnTouchListener(this);
        register_inputPswET.setOnTouchListener(this);
        register_putPswTwoET.setOnTouchListener(this);
        register_inputNameET.setOnTouchListener(this);
        register_messageET.addTextChangedListener(new MyTextWathch(register_messageET));
        register_phoneET.addTextChangedListener(new MyTextWathch(register_phoneET));
        register_inputPswET.addTextChangedListener(new MyTextWathch(register_inputPswET));
        register_putPswTwoET.addTextChangedListener(new MyTextWathch(register_putPswTwoET));
        register_inputNameET.addTextChangedListener(new MyTextWathch(register_inputNameET));
        legalExplanationBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_sendMessageBtn:
                clickSendMessageBtn();
                break;
            case R.id.register_nextBtn://开始
                clickLoginBtn();
                break;
            case R.id.register_phoneET:
                initStatue(register_phoneET);
                break;
            case R.id.register_messageET:
                initStatue(register_messageET);
                break;
            case R.id.register_inputPswET:
                initStatue(register_inputPswET);
                break;
            case R.id.register_putPswTwoET:
                initStatue(register_putPswTwoET);
                break;
            case R.id.register_inputNameET:
                initStatue(register_inputNameET);
                break;
            case R.id.legalExplanationBtn:
                clickLegaExpanationBtn();
                break;
        }
    }

    private void clickLegaExpanationBtn() {
        Intent intent = new Intent(this, LegalExplanationActivity.class);
        startActivity(intent);
    }


    //点击下一步按钮
    private void clickLoginBtn() {
        phone = register_phoneET.getText().toString().trim();
        message = register_messageET.getText().toString().trim();
        psw = register_inputPswET.getText().toString().trim();
        pswTwo = register_putPswTwoET.getText().toString().trim();
        nickName = register_inputNameET.getText().toString().trim();
        if ((!phone.equals("")) && (!message.equals("")) && (!psw.equals("")) && (!pswTwo.equals(""))) {
            if ("".equals(messagePhone) || messagePhone == null){
                showToast("请获取验证码");
                return;
            }
            if (!messagePhone.equals(phone)){
                showToast("手机号与验证码不符");
                return;
            }
            if (psw.length() < 6 || pswTwo.length() < 6){//  密码验证 5/16日
                showToast(getString(R.string.login_yanzheng_psw));
                return;
            }
            if (message.equals(sendMessage)) {
                if (psw.equals(pswTwo)) {
                    registerDiaolog = new WaittingDiaolog(GoRegisterNowActivity.this);
                    registerDiaolog.show();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            String url = API.CUSTOMER;
                            Map<String, String> map = new HashMap<>();
                            map.put(Consts.MOBILE, phone);
                            map.put(Consts.OS, "2");
                            map.put(Consts.NICK_NAME, nickName);
                            map.put(Consts.PASSWORD, DigestUtils.md5(pswTwo));
                            url = JrUtils.appendParams(url, map);
                            NetLoader.getInstance(GoRegisterNowActivity.this).loadGetData(GoRegisterNowActivity.this, url, new NetLoader.NetResponseListener() {
                                @Override
                                public void success(String resultString, int code) {
                                    registerDiaolog.dismiss();
                                    if (code == 200){
                                        Gson gson = new Gson();
                                        String res = resultString.toString();
                                        RegisterEntity entity = gson.fromJson(res, RegisterEntity.class);
                                        if (entity != null) {
                                            switch (entity.getCode()) {
                                                case 200://注册成功
//                                                    LoginActivity.intace.finish();
                                                    Toast.makeText(GoRegisterNowActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                                                    Intent intent = new Intent(GoRegisterNowActivity.this, LoginActivity.class);
//                                                    startActivity(intent);
                                                    GoRegisterNowActivity.this.finish();
//                                                    login();
                                                    break;
                                            }
                                        }
                                    }else if (code == 401){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(GoRegisterNowActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void fail(String failString, Exception e) {
                                    registerDiaolog.dismiss();
                                    Log.i("geanwen3", failString);
                                }
                            });
                        }
                    }.start();
                } else {
                    showToast("两次输入密码不一致");
                }
            } else {
                showToast("验证码输入错误");
            }
        } else {
            showToast("请检查您的输入!");
        }
    }


    //点击发送验证码按钮
    private void clickSendMessageBtn() {
        hideInput(GoRegisterNowActivity.this, register_phoneET);
        if (register_phoneET.getText().length() == 11) {
            boolean isForm = JrUtils.isMobileNO(register_phoneET.getText().toString());
            if(!isForm){
                showToast("手机号格式不正确");
                return;
            }
            handler.post(runnable);
            phone = register_phoneET.getText().toString().trim();
            String url = API.QUERYCODE;
            Map<String, String> map = new HashMap<>();
            map.put(Consts.MOBILE, phone);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(GoRegisterNowActivity.this).loadGetData(GoRegisterNowActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200){
                        Gson gson = new Gson();
                        RegisterEntity entity = gson.fromJson(resultString, RegisterEntity.class);
                        if (entity != null) {
                            switch (entity.getCode()) {
                                case 1001://已注册
                                    handler.removeCallbacks(runnable);
                                    Toast.makeText(GoRegisterNowActivity.this, "该用户已经注册过了", Toast.LENGTH_SHORT).show();
                                    handler.removeCallbacks(runnable);
                                    register_sendMessageBtn.setText("验证");
                                    register_sendMessageBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
                                    break;
                                case 200://发送成功
                                    messagePhone = phone;
                                    Toast.makeText(GoRegisterNowActivity.this, "验证码发送成功，请在一分钟内完成注册", Toast.LENGTH_SHORT).show();
                                    sendMessage = entity.getDataMap().getVaildCode();
                                    break;
                            }
                        }
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GoRegisterNowActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    Log.i("geanwen1", failString);
                }
            });
        } else {
//            showToast("请输入11位手机号");
        }
    }

    private void showToast(String message){
        if (toast == null){
            toast = Toast.makeText(GoRegisterNowActivity.this, message, Toast.LENGTH_SHORT);
            toast.show();
        }else {
            toast.setText(message);
            toast.show();
        }
    }


    //清空状态  并改变点击的控件状态
    private void initStatue(View v) {
        register_phoneET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        register_messageET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        register_inputPswET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        register_putPswTwoET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        register_inputNameET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        register_phoneET.clearFocus();
        register_messageET.clearFocus();
        register_inputPswET.clearFocus();
        register_putPswTwoET.clearFocus();
        register_inputNameET.clearFocus();
        v.setBackgroundResource(R.drawable.shape_forget_psw_edit);
        v.requestFocus();
        textWatcherLisener();//监听四个edittext输入情况
    }

    private void textWatcherLisener() {
        phone = register_phoneET.getText().toString().trim();
        message = register_messageET.getText().toString().trim();
        psw = register_inputPswET.getText().toString().trim();
        pswTwo = register_putPswTwoET.getText().toString().trim();
        nickName = register_inputNameET.getText().toString().trim();
        if ((!phone.equals("")) && (!message.equals("")) && (!psw.equals("")) && (!pswTwo.equals("")) && (!nickName.equals(""))) {
            if (psw.length() > 5) {//密码不得少于六位
                register_nextBtn.setBackgroundResource(R.mipmap.bluebutton);
                register_nextBtn.setClickable(true);
            }
        } else {
            register_nextBtn.setClickable(false);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        initStatue(v);
        return false;
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
            if (editText == register_inputNameET){
                String editable = editText.getText().toString();
                String str = JrUtils.stringFilter(editable);
                if (!editable.equals(str)) {
                    editText.setText(str);
                    showToast("不能输入特殊字符");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            textWatcherLisener();
            if (register_phoneET == editText) {
                if (register_phoneET.getText().length() == 11) {
                    register_sendMessageBtn.setBackgroundResource(R.mipmap.backtohome);
                    register_sendMessageBtn.setClickable(true);
                }else {
                    register_sendMessageBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
                    register_sendMessageBtn.setClickable(false);
                }
            }
            int index = editText.getSelectionStart() - 1;
            if (index > 0) {
                if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                    Editable edit = editText.getText();
                    edit.delete(index, index + 1);
                    showToast("输入内容不能包含表情符号");
                }
            }
        }
    }

    /**
     * 强制隐藏输入法键盘
     */
    private void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
