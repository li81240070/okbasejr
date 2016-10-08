package com.hx.jrperson.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.AlearSignEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 意见反馈界面
 * Created by ge on 2016/3/15.
 */
public class IdaerBackActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, TextWatcher {

    private EditText idearET;
    private Button idear_sendBtn;
    private WaittingDiaolog diaolog;
    private Handler handler;
    private boolean isSend = false;
    private Toast toast;
    ////////////////////////////
    private RelativeLayout backButtonInIdea;
    private ImageView backbuttonInIdea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_back);
        showToolBar("意见反馈", true, this, false);
        initView();
        initData();
        setListener();
        backButtonInIdea= (RelativeLayout) findViewById(R.id.backButtonInIdea);
        backButtonInIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdaerBackActivity.this.finish();
            }
        });
        backbuttonInIdea= (ImageView) findViewById(R.id.backbuttonInIdea);
        backbuttonInIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdaerBackActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        idearET = (EditText) findViewById(R.id.idearET);
        idear_sendBtn = (Button) findViewById(R.id.idear_sendBtn);
        handler = new Handler();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        idear_sendBtn.setOnClickListener(this);
        idearET.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idear_sendBtn://发送按钮
                if (isSend){
                    diaolog = new WaittingDiaolog(this);
                    diaolog.show();
                    onClickSendBtn();
                } else {
//                    showToast("请输入您的意见"); // ZY 2016年5月17日23:50:12 为了跟IOS保持一致
                }
                break;
        }
    }


    private void showToast(String s){
        if (toast == null){
            toast = Toast.makeText(IdaerBackActivity.this, s, Toast.LENGTH_SHORT);
            toast.show();
        }else {
            toast.setText(s);
            toast.show();
        }
    }

    private void onClickSendBtn() {

        String url = API.FEEDBACK;
        String user_id = PreferencesUtils.getString(this, Consts.PHONE_PF);
        String token = PreferencesUtils.getString(this, Consts.TOKEN);
        String idaer = idearET.getText().toString().trim();
        if (!TextUtils.isEmpty(idaer) && !TextUtils.isEmpty(user_id) && !TextUtils.isEmpty(token)) {

            Map<String, String> map = new HashMap<>();
            map.put(Consts.USER_ID, user_id);
            map.put(Consts.TOKEN, token);
            map.put(Consts.CONTENT, idaer);
            url = JrUtils.appendParams(url, map);
            NetLoader.getInstance(this).loadGetData(IdaerBackActivity.this, url, new NetLoader.NetResponseListener() {
                @Override
                public void success(String resultString, int code) {
                    diaolog.dismiss();
                    if (code == 200){
                        Gson gson = new Gson();
                        AlearSignEntity entity = gson.fromJson(resultString, AlearSignEntity.class);
                        if (entity.getCode() == 200){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(IdaerBackActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.i("geanwen", resultString);
                            IdaerBackActivity.this.finish();
                        }else {
                            showToast("意见反馈失败，请检查您的输入");
                        }
                    }else if (code == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(IdaerBackActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void fail(String failString, Exception e) {
                    diaolog.dismiss();
                    showToast("内部服务错误");
                    Log.i("geanwen", failString);
                }
            });
        } else {
            diaolog.dismiss();
            showToast("内部服务错误");
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = idearET.getText().toString();
        String str = JrUtils.stringFilter(editable);
        if (!editable.equals(str)) {
            idearET.setText(str);
            showToast("不能输入特殊字符");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        int index = idearET.getSelectionStart() - 1;
        if (index > 0) {
            if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                Editable edit = idearET.getText();
                edit.delete(index, index + 1);
                showToast("输入内容不能包含表情符号");
            }
        }
        if (s.toString().length() > 0) {
            idear_sendBtn.setBackgroundResource(R.mipmap.bluebutton);
            isSend = true;
        } else {
            idear_sendBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
            isSend = false;
        }

    }
}
