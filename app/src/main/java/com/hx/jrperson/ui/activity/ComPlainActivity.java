package com.hx.jrperson.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.controller.JrController;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.views.WaittingDiaolog;
import com.hx.jrperson.views.baseView.BaseActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 投诉页面
 * Created by ge on 2016/4/15.
 */
public class ComPlainActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText complainET;
    private Button sendComplainBtn;
    private boolean isclick = false;
    private String order_id;
    private WaittingDiaolog diaolog;
    private RelativeLayout complainRL;//输入框布局
    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Handler handler;
    private Toast toast;
    //////////////////////////////////////////
    private RelativeLayout backButtonInComplain;
    private ImageView backbuttonInComplain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        showToolBar("投诉", true, ComPlainActivity.this, false);
        initView();
        initData();
        setListener();
        backButtonInComplain= (RelativeLayout) findViewById(R.id.backButtonInComplain);
        backButtonInComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComPlainActivity.this.finish();
            }
        });
        backbuttonInComplain= (ImageView) findViewById(R.id.backbuttonInComplain);
        backbuttonInComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComPlainActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        complainET = (EditText) findViewById(R.id.complainET);
        sendComplainBtn = (Button) findViewById(R.id.sendComplainBtn);
        complainRL = (RelativeLayout) findViewById(R.id.complainRL);
    }

    @Override
    protected void initData() {
        handler = new Handler();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        order_id = bundle.getString("order_id");
    }

    @Override
    protected void setListener() {
        complainET.addTextChangedListener(this);
        sendComplainBtn.setOnClickListener(this);
        complainRL.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendComplainBtn://发送
                String text = complainET.getText().toString().trim();
                if (isclick){
                    if (!"".equals(text)){
                        diaolog = new WaittingDiaolog(ComPlainActivity.this);
                        diaolog.show();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                clickSendBtn();
                            }
                        }.start();
                    } else {
                        showToast("不能全是空格");
                    }
                }
                break;
            case R.id.complainRL://总布局
                sendComplainBtn.requestFocus();
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    private void showToast(String str){
        if (null == toast){
            toast = Toast.makeText(ComPlainActivity.this, str, Toast.LENGTH_SHORT);
            toast.show();
        }else {
            toast.setText(str);
            toast.show();
        }
    }

    //发送
    private void clickSendBtn() {
        if (isclick && order_id != null && !order_id.equals("")){
            String text = complainET.getText().toString().trim();
            String url = API.COMPLAIN;
            JSONObject object = new JSONObject();
            try {
                object.put(Consts.ORDERID, order_id);
                object.put(Consts.CONTENT, text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient okHttpClient = new OkHttpClient();
            try {
                JrController.setCertificates(ComPlainActivity.this, okHttpClient, ComPlainActivity.this.getAssets().open("zhenjren.cer"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String token = PreferencesUtils.getString(ComPlainActivity.this, Consts.TOKEN);
            RequestBody body = RequestBody.create(JSON, object.toString());
            Request request = new Request.Builder().post(body).url(url).addHeader(Consts.TOKEN, token).build();
            okHttpClient.setConnectTimeout(40, TimeUnit.SECONDS);
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    diaolog.dismiss();
                    if (200 == response.code()){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ComPlainActivity.this, "投诉成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        ComPlainActivity.this.finish();
                    }else if (response.code() == 401){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ComPlainActivity.this, "此账号已在别处登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else {
                    diaolog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int index = complainET.getSelectionStart() - 1;
        if (index > 0) {
            if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                Editable edit = complainET.getText();
                edit.delete(index, index + 1);
                showToast("输入内容不能包含表情符号");
            }
        }
        if (s.length() > 0){
            sendComplainBtn.setBackgroundResource(R.mipmap.send_btn);
            isclick = true;
        }else {
            sendComplainBtn.setBackgroundResource(R.drawable.shape_forget_psw_btn);
            isclick = false;
        }
    }
}
