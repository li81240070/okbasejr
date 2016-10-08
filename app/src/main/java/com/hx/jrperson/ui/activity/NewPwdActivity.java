package com.hx.jrperson.ui.activity;

import android.content.Intent;
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

import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.DigestUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
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
 * 忘记密码界面的新密码输入界面
 */
public class NewPwdActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {
    private EditText inputNewPswET, inputTwoNewPswET, inputOldPswET;
    private Button sureNewPswBtn;
    private String show, psw, pswTwo, pswOld;
    private boolean isNext = false;
    private String codes;
    private Toast toast;
    private Handler handler;
    ////////////////////////////////////
    private RelativeLayout backButtonInMyPassword;
    private ImageView backbuttonInMyPassword;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Integer staute) {
        if (staute == 10004) {
            if (codes.equals("200")) {
                if (show.equals("1")) {
                    showToast("修改成功");
                    //修改密码
                    NewPwdActivity.this.finish();
                } else if (show.equals("2")) {
                    // 忘记密码
                    ForgetPwdActivity.inance.finish();
                    Intent intent = new Intent(NewPwdActivity.this, LoginActivity.class);
                    startActivity(intent);
                    NewPwdActivity.this.finish();
                }
            } else if (codes.equals("1000")) {
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pwd);
        EventBus.getDefault().register(this);
        initToolBar();
        initView();
        initData();
        setListener();
        backButtonInMyPassword= (RelativeLayout) findViewById(R.id.backButtonInMyPassword);
        backButtonInMyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPwdActivity.this.finish();
            }
        });
        backbuttonInMyPassword= (ImageView) findViewById(R.id.backbuttonInMyPassword);
        backbuttonInMyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPwdActivity.this.finish();
            }
        });
    }

    //toolbar显示
    private void initToolBar() {
        Intent intent = getIntent();
        show = intent.getStringExtra("newPwd");
        if (show.equals("1")) {
            showToolBar("修改密码", true, this, false);
        } else if (show.equals("2")) {
            showToolBar("找回密码", true, this, false);
        }

    }

    @Override
    protected void initView() {
        inputOldPswET = (EditText) findViewById(R.id.inputOldPswET);
        inputNewPswET = (EditText) findViewById(R.id.inputNewPswET);
        inputTwoNewPswET = (EditText) findViewById(R.id.inputTwoNewPswET);
        sureNewPswBtn = (Button) findViewById(R.id.sureNewPswBtn);
        if (show.equals("1")) {
            findViewById(R.id.intputOldPswLL).setVisibility(View.VISIBLE);
//            inputOldPswET.setBackgroundResource(R.drawable.shape_forget_psw_edit); // ZY 2016年5月17日23:38:40 保持样式与IOS一致
        } else if (show.equals("2")) {
//            inputNewPswET.setBackgroundResource(R.drawable.shape_forget_psw_edit);
        }
    }

    @Override
    protected void initData() {
        handler = new Handler();
    }

    @Override
    protected void setListener() {
        inputOldPswET.setOnTouchListener(this);
        inputNewPswET.setOnTouchListener(this);
        inputTwoNewPswET.setOnTouchListener(this);
        inputOldPswET.setOnClickListener(this);
        inputNewPswET.setOnClickListener(this);
        inputTwoNewPswET.setOnClickListener(this);
        inputOldPswET.addTextChangedListener(new MyTextWathch(inputOldPswET));
        inputNewPswET.addTextChangedListener(new MyTextWathch(inputNewPswET));
        inputTwoNewPswET.addTextChangedListener(new MyTextWathch(inputTwoNewPswET));
        sureNewPswBtn.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        initStatue(v);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sureNewPswBtn://确定按钮
                if (isNext) {
                    if (show.equals("2")) {
                        clickSureBtn();//忘记密码找回密码
                    } else if (show.equals("1")) {
                        clickSureModifyBtn();//修改密码
                    }
                }
                break;
            case R.id.inputNewPswET://输入新密码
                initStatue(v);
                break;
            case R.id.inputTwoNewPswET://再次输入新密码
                initStatue(v);
                break;
            case R.id.inputOldPswET://输入旧密码
                initStatue(v);
                break;
        }
    }

    //点击确定按钮
    private void clickSureBtn() {
        psw = inputNewPswET.getText().toString().trim();
        pswTwo = inputTwoNewPswET.getText().toString().trim();
        if (psw.length() < 6 || pswTwo.length() < 6) {
            Toast.makeText(this, "密码长度小于6位", Toast.LENGTH_SHORT).show();
        } else if (!pswTwo.equals(psw)) {
            Toast.makeText(this, "请输入相同的密码", Toast.LENGTH_SHORT).show();
        } else if (pswTwo.equals(psw)) {
            String pwd = DigestUtils.md5(psw);
            String url = API.FORGETPWD;
            String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USER_ID, phone);
            map.put(Consts.NEWPWD, pwd);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(this).loadOrdinaryPostData(NewPwdActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200) {
                        try {
                            JSONObject object = new JSONObject(resultString);
                            codes = object.getString("code");
                            showToast("修改成功");
                            NewPwdActivity.this.finish();
                            EventBus.getDefault().post(10004);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewPwdActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    Log.i("geanwen", failString);
                }
            }, null);
        }

    }

    private void clickSureModifyBtn() {
        pswOld = inputOldPswET.getText().toString();
        psw = inputNewPswET.getText().toString();
        pswTwo = inputTwoNewPswET.getText().toString();
        if (pswOld.length() < 6 || psw.length() < 6 || pswTwo.length() < 6) {
            Toast.makeText(this, "密码长度小于6位", Toast.LENGTH_SHORT).show();
        } else if (!pswTwo.equals(psw)) {
            Toast.makeText(this, "请输入相同的密码", Toast.LENGTH_SHORT).show();
        } else if (pswTwo.equals(psw)) {
            String pwd = DigestUtils.md5(psw);
            String oldpwd = DigestUtils.md5(pswOld);
            String url = API.MODIFYPWD;
            String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USERID, phone);
            map.put(Consts.PWD, pwd);
            map.put(Consts.OLDPWD, oldpwd);
            url = JrUtils.appendParams(url, map);

            NetLoader.getInstance(this).loadGetData(NewPwdActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200) {
                        try {
                            JSONObject object = new JSONObject(resultString);
                            codes = object.getString("code");
                            EventBus.getDefault().post(10004);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewPwdActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    Log.i("geanwen", "将人位置列表 : " + failString);
                }
            });
        }
    }


    //清空状态  并改变点击的控件状态
    private void initStatue(View v) {
        inputOldPswET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        inputNewPswET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        inputTwoNewPswET.setBackgroundResource(R.drawable.shape_forget_not_psw_edit);
        inputOldPswET.clearFocus();
        inputNewPswET.clearFocus();
        inputTwoNewPswET.clearFocus();
        v.setBackgroundResource(R.drawable.shape_forget_psw_edit);
        v.requestFocus();
        textWatcherLisener();//监听edittext输入情况
    }

    private void textWatcherLisener() {
        pswOld = inputOldPswET.getText().toString();
        psw = inputNewPswET.getText().toString();
        pswTwo = inputTwoNewPswET.getText().toString();
        if (show.equals("2")) {
            if ((psw.length() >= 6) && (pswTwo.length() >= 6)) {
                isNext = true;
                sureNewPswBtn.setBackgroundResource(R.mipmap.bluebutton);
                sureNewPswBtn.setTextColor(getResources().getColor(R.color.material_black));
            } else {
                isNext = false;
                sureNewPswBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
                sureNewPswBtn.setTextColor(getResources().getColor(R.color.material_white));
            }
        } else if (show.equals("1")) {
            if ((pswOld.length() >= 6) && (psw.length() >= 6) && (pswTwo.length() >= 6)) {
                isNext = true;
                sureNewPswBtn.setBackgroundResource(R.mipmap.bluebutton);
                sureNewPswBtn.setTextColor(getResources().getColor(R.color.material_black));
            } else {
                isNext = false;
                sureNewPswBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
                sureNewPswBtn.setTextColor(getResources().getColor(R.color.material_white));
            }
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
            if (editText == inputOldPswET || editText == inputNewPswET || editText == inputTwoNewPswET) {
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

    private void showToast(String str) {
        if (toast != null) {
            toast.setText(str);
            toast.show();
        } else {
            toast = Toast.makeText(NewPwdActivity.this, str, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
