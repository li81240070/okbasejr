package com.hx.jrperson.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.jrperson.R;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.views.baseView.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 填写地址界面
 * **/
public class InputAddressActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView  saveAddressTV;
    private EditText inputGutAddressTV,changeAddressTV;
    private String address = "", addressChange = "", addressGut = "";
    private Map<String, String> map = new HashMap<>();
    private Toast toast;
    //////////////////////////////////////
    private ImageView backButtonInChoose;
    private RelativeLayout backButtonInChooses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_input_address);
        showToolBar("填写地址", true, this, false);
        initView();
        initData();
        setListener();

    }

    @Override
    protected void initView() {
        changeAddressTV = (EditText) findViewById(R.id.changeAddressTV);

        changeAddressTV.setFocusable(true);
        inputGutAddressTV = (EditText) findViewById(R.id.inputGutAddressTV);
        saveAddressTV = (TextView) findViewById(R.id.saveAddressTV);
        /////////////////////////////////
        backButtonInChoose= (ImageView) findViewById(R.id.backButtonInChoose);
        backButtonInChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputAddressActivity.this.finish();
            }
        });
        backButtonInChooses= (RelativeLayout) findViewById(R.id.backButtonInChooses);
        backButtonInChooses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputAddressActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    public static String[] split(String str, String splitsign) {
        int index;
        if (str == null || splitsign == null)
            return null;
        List<String> al = new ArrayList<>();
        while ((index = str.indexOf(splitsign)) != -1) {
            al.add(str.substring(0, index));
            str = str.substring(index + splitsign.length());
        }
        al.add(str);
        return al.toArray(new String[0]);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(Map<String, String> map) {
        if (map != null){
            address = map.get("address");
            changeAddressTV.setText(address);
            addressChange = address;
            this.map = map;
        }
    }


    @Override
    protected void setListener() {
        changeAddressTV.setOnClickListener(this);
        saveAddressTV.setOnClickListener(this);
        inputGutAddressTV.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changeAddressTV://选择地址
                Intent intent = new Intent(this, ChangeProvinceActivity.class);
                startActivity(intent);
                break;
            case R.id.saveAddressTV://保存地址
                addressGut = inputGutAddressTV.getText().toString().trim();
                address = address + inputGutAddressTV.getText().toString().trim();
                if (!addressChange.equals("")){
                    if (!addressGut.equals("")){
                        if (!address.equals("")){
                            if (addressGut.length() >= 2 && addressGut.length() <= 60){
                                map.remove("address");
                                map.put("address", address);
                                map.put("addressGut", addressGut);
                                map.put("key", "2");
                                EventBus.getDefault().post(map);
                                InputAddressActivity.this.finish();
                            }else {
                                showToast(getString(R.string.toast_input_address));
                            }
                        }
                    }else {
                        showToast(getString(R.string.toast_input_address_gut));
                    }
                }else {
                    showToast(getString(R.string.toast_input_address_change));
                }
                break;
        }
    }

    /**
     * 保证本界面toast不会持续弹出
     * **/
    private void showToast(String s) {
        if (toast == null){
            toast = Toast.makeText(InputAddressActivity.this, s, Toast.LENGTH_SHORT);
            toast.show();
        }else {
            toast.setText(s);
            toast.show();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = inputGutAddressTV.getText().toString();
        String str = JrUtils.stringFilter(editable);
        if (!editable.equals(str)) {
            inputGutAddressTV.setText(str);
            showToast("不能输入特殊字符");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        int index = inputGutAddressTV.getSelectionStart() - 1;
        if (index > 0) {
            if (JrUtils.isEmojiCharacter(s.charAt(index))) {
                Editable edit = inputGutAddressTV.getText();
                edit.delete(index, index + 1);
                showToast("输入内容不能包含表情符号");
            }
        }
    }

}
