package com.hx.jrperson.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.AlearSignEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.bean.entity.PersonalInforEntity;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 修改昵称 + 修改签名界面
 * Created by ge on 2016/4/1.
 */
public class AlterNameAndSignActivity extends BaseActivity implements View.OnClickListener{

    private TextView saveAlterTV;
    private EditText alterNickNameET, alterSignET;
    private LinearLayout signLL, nickNameLL;
    private int staute;//页面状态 1:修改昵称 2:修改签名
    private WaittingDiaolog diaolog;
    private Toast toast;
    private String alStr, alNameStr;
    private Handler handler;
    private PersonalInforEntity entity;
    ////////////////////////////////////////
    private RelativeLayout backButtonInWord;
    private ImageView backbuttonInWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_name_sign);
        initView();
        initData();
        setListener();
        backButtonInWord= (RelativeLayout) findViewById(R.id.backButtonInWord);
        backButtonInWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlterNameAndSignActivity.this.finish();
            }
        });
        backbuttonInWord= (ImageView) findViewById(R.id.backbuttonInWord);
        backbuttonInWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlterNameAndSignActivity.this.finish();
            }
        });
    }


    @Override
    protected void initView() {
        saveAlterTV = (TextView) findViewById(R.id.saveAlterTV);
        alterNickNameET = (EditText) findViewById(R.id.alterNickNameET);
        alterSignET = (EditText) findViewById(R.id.alterSignET);
        signLL = (LinearLayout) findViewById(R.id.signLL);
        nickNameLL = (LinearLayout) findViewById(R.id.nickNameLL);
    }

    @Override
    protected void initData() {
        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        staute = bundle.getInt("staute");
        entity = (PersonalInforEntity) bundle.get("infor");
        toolBar(staute);
        if (!TextUtils.isEmpty(entity.getDataMap().getCustom_sign())){
            alterSignET.setText(entity.getDataMap().getCustom_sign());
        }
        if (!TextUtils.isEmpty(entity.getDataMap().getNick_name())){
            alterNickNameET.setText(entity.getDataMap().getNick_name());
        }
    }

    private void toolBar(int staute) {
        if (staute == 1){//昵称
            showToolBar("昵称修改", true, this, false);
            nickNameLL.setVisibility(View.VISIBLE);
        }else if (staute == 2){//签名
            showToolBar("签名修改", true, this, false);
            signLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setListener() {
        saveAlterTV.setOnClickListener(this);
        signLL.setOnClickListener(this);
        alterSignET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = alterSignET.getText().toString();
                String str = JrUtils.stringFilter(editable);
                if (!editable.equals(str)) {
                    alterSignET.setText(str);
                    showToast("不能输入特殊字符");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = alterSignET.getSelectionStart() - 1;
                if (index > 0) {
                    if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                        Editable edit = alterSignET.getText();
                        edit.delete(index, index + 1);
                        showToast("输入内容不能包含表情符号");
                    }
                }
            }
        });
        alterNickNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = alterNickNameET.getText().toString();
                String str = JrUtils.stringFilter(editable);
                if (!editable.equals(str)) {
                    alterNickNameET.setText(str);
                    showToast("不能输入特殊字符");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = alterNickNameET.getSelectionStart() - 1;
                if (index > 0) {
                    if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                        Editable edit = alterNickNameET.getText();
                        edit.delete(index, index + 1);
                        showToast("输入内容不能包含表情符号");
                    }
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveAlterTV://保存
                clickSaveBtn();
                break;
            case R.id.signLL://修改签名行布局
//                clickSignLL();
                break;
        }
    }

    //修改签名行布局
    private void clickSignLL() {
        alterSignET.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void clickSaveBtn() {
        diaolog = new WaittingDiaolog(this);
        diaolog.show();
        if (staute == 1){
            alNameStr = alterNickNameET.getText().toString().trim();
            if (!alNameStr.equals("")){
                alterNickName(alNameStr);
            }else {
                showToast("修改的信息不能为空");
                diaolog.dismiss();
            }
        }else if (staute == 2){
            alStr = alterSignET.getText().toString().trim();
            if (!alStr.equals("")){
                alterSign(alStr);
            }else {
                showToast("修改的信息不能为空");
                diaolog.dismiss();
            }
        }
    }

    //修改签名
    private void alterSign(String str) {
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        String token = PreferencesUtils.getString(this, Consts.TOKEN);
        String url = API.ALTERINFOR;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.SIGN, str);
        map.put(Consts.USER_ID, phone);
        map.put(Consts.NICKNAME, "");
        map.put(Consts.TOKEN, token);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(AlterNameAndSignActivity.this, url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200){
                    Gson gson = new Gson();
                    AlearSignEntity entity = gson.fromJson(resultString, AlearSignEntity.class);
                    diaolog.dismiss();
                        if (entity.getCode() == 200) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("修改成功");
                                }
                            });
                            AlterNameAndSignActivity.this.finish();
                        } else {
                            showToast("签名保存失败，请检查您的输入");
                        }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlterNameAndSignActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen", failString);
                diaolog.dismiss();
            }
        });
    }

    //修改昵称
    private void alterNickName(String nameStr) {
        String phone = PreferencesUtils.getString(this, Consts.PHONE_PF);
        String token = PreferencesUtils.getString(this, Consts.TOKEN);
        String url = API.ALTERINFOR;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.SIGN, "");
        map.put(Consts.USER_ID, phone);
        map.put(Consts.NICKNAME, nameStr);
        map.put(Consts.TOKEN, token);
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(this).loadGetData(AlterNameAndSignActivity.this, url, new NetLoader.NetResponseListener() {
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
                                showToast("修改成功");
                            }
                        });
                        AlterNameAndSignActivity.this.finish();
                    }else {
                        showToast("昵称修改失败，请检查您的输入");
                    }
                }else if (code == 401){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlterNameAndSignActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void fail(String failString, Exception e) {
                Log.i("geanwen", failString);
                diaolog.dismiss();
            }
        });
    }


    private void showToast(String str){
        if (toast != null){
            toast.setText(str);
            toast.show();
        }else {
            toast = Toast.makeText(AlterNameAndSignActivity.this, str, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
