package com.hx.jrperson.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.LoginEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.DigestUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 登录界面
 * by ge
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private Button loginBtn;
    private TextView gotoRegisterTV, forgetPswTV;
    private ImageView backMainIVBtn;
    private LinearLayout login_area_LL, accountLL;
    private EditText loginInputPhoneET, loginInputPswET;//手机号密码
    private Toast toast;//本类使用这一个toast 不会持续弹出 只会显示最后点击的toast
    public static LoginActivity intace = null;
    private WaittingDiaolog loginDialog;
    private Handler handler;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intace = this;
        initView();
        initData();
        setListener();
        //为二维码服务的数据持久化操作
         sp=getSharedPreferences("twocode",MODE_PRIVATE);

    }

    @Override
    protected void initView() {
        loginBtn = (Button) findViewById(R.id.loginBtn);
        gotoRegisterTV = (TextView) findViewById(R.id.gotoRegisterTV);
        forgetPswTV = (TextView) findViewById(R.id.forgetPswTV);
        backMainIVBtn = (ImageView) findViewById(R.id.backMainIVBtn);
        login_area_LL = (LinearLayout) findViewById(R.id.login_area_LL);
        accountLL = (LinearLayout) findViewById(R.id.accountLL);
        loginInputPhoneET = (EditText) findViewById(R.id.loginInputPhoneET);
//        loginInputPhoneET.setText("13664266902");
        loginInputPswET = (EditText) findViewById(R.id.loginInputPswET);
//        loginInputPswET.setText("qqqqqq");
    }

    @Override
    protected void initData() {
        handler = new Handler();
    }

    @Override
    protected void setListener() {
        loginBtn.setOnClickListener(this);
        gotoRegisterTV.setOnClickListener(this);
        forgetPswTV.setOnClickListener(this);
        backMainIVBtn.setOnClickListener(this);
        loginInputPswET.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn://登陆
                clicLoginBtn();
                break;
            case R.id.forgetPswTV://忘记密码
                Intent forgetIntent = new Intent(this, ForgetPwdActivity.class);
                startActivity(forgetIntent);
                break;
            case R.id.gotoRegisterTV://立即注册
                Intent gotoRegoseIntent = new Intent(this, GoRegisterNowActivity.class);
                startActivity(gotoRegoseIntent);
                break;
            case R.id.backMainIVBtn://返回主页
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                LoginActivity.this.finish();
                break;
        }
    }

    /**
     * 点击登陆按钮
     * **/
    private void clicLoginBtn() {
        final String phone = loginInputPhoneET.getText().toString().trim();
        final String psw = loginInputPswET.getText().toString().trim();

        boolean isForm = JrUtils.isMobileNO(phone);
        if(!isForm){
            showToast("手机号格式不正确");
            return;
        }
        if (!phone.equals("") && !psw.equals("")){
            loginDialog = new WaittingDiaolog(LoginActivity.this);
            loginDialog.show();
            String token = PreferencesUtils.getString(LoginActivity.this, Consts.TOKEN);

            String url = API.LOGIN;
            Map<String, String> map = new HashMap<>();
            map.put(Consts.USERNAME, phone);
            map.put(Consts.PASSWORD, DigestUtils.md5(psw));
            map.put(Consts.OS, "2");
            map.put(Consts.TOKEN, token != null ? token : "");
            url = JrUtils.appendParams(url, map);

            NetLoader.getInstance(LoginActivity.this).loadGetData(url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    if (code == 200){
                        Gson gson = new Gson();
                        try {
                            final LoginEntity entity = gson.fromJson(resultString, LoginEntity.class);
                            switch (entity.getCode()){
                                case 500:
                                    loginDialog.dismiss();
                                    showToast("用户不存在");
                                    break;
                                case 200:
                                    loginDialog.dismiss();
                                    showToast("登录成功");
                                    //登录成功后向所有页面发送刷新数据的命令
                                    Intent intent=new Intent("com.example.dllo.broadcast.reflushYourOrder");
                                    //往广播里面放内容
                                    intent.putExtra("order","刷新当前菜单");
                                    //进入广播启动项
                                    sendBroadcast(intent);

                                    JPushInterface.resumePush(getApplicationContext());
                                    JPushInterface.setAlias(getApplicationContext(), phone, new TagAliasCallback() {
                                        @Override
                                        public void gotResult(int i, String s, Set<String> set) {
                                            //                                        Log.i("geanwen", "i : " + i + "s : " + s + "set : " + set.toArray().toString());
                                        }
                                    });
                                    PreferencesUtils.putBoolean(LoginActivity.this, Consts.ISLOGIN, true);//是否登陆
                                    PreferencesUtils.putString(LoginActivity.this, Consts.PHONE_PF, phone);
                                    PreferencesUtils.putString(LoginActivity.this, Consts.PSW, psw);//密码
                                    PreferencesUtils.putString(LoginActivity.this, Consts.TOKEN, entity.getDataMap().getToken());//token
                                    //二维码相关token存储
                                    SharedPreferences.Editor editor=sp.edit();
                                    editor.putString("token",entity.getDataMap().getToken());
                                    editor.commit();

                                    PreferencesUtils.putInt(LoginActivity.this, Consts.USER_ID, entity.getDataMap().getUserId());//userid
                                    EventBus.getDefault().post(true);//登陆成功 发送给mainctivity

//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    LoginActivity.this.startActivity(intent);
                                    LoginActivity.this.finish();
                                    break;
                                case 1007:
                                    loginDialog.dismiss();
                                    showToast("已登录");
                                    break;
                                case 1000:
                                    loginDialog.dismiss();
                                    showToast("用户名或密码错误");
                                    break;
                                case 1001:
                                    loginDialog.dismiss();
                                    showToast("用户数据异常");
                                    break;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    loginDialog.dismiss();
                    Log.i("geanwen", failString);
                }
            });
        }else {
            showToast("用户名或密码不能为空");
        }
    }

    private void showToast(String message){
        if (toast == null){
            toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
            toast.show();
        }else {
            toast.setText(message);
            toast.show();
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE){
//            Log.i("geanwen回车登陆 ；", "----");
            clicLoginBtn();
            //隐藏软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        intace = null;
        super.onDestroy();
    }
}
