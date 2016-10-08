package com.hx.jrperson.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.hx.jrperson.R;
import com.hx.jrperson.utils.JrUtils;

/**
 * 抢险抢修dialog(未登陆)
 * Created by ge on 2016/3/3.
 */
public class RushToDealNotLoginDialog extends Dialog implements View.OnClickListener {

    private RoundUpAngleImageView notLoginSureBtn;
    private EditText dialog_inputPhoneET;
    private Context context;
    private static int default_width = 280;
    private static int default_heigh = 190;

    public RushToDealNotLoginDialog(Context context) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_rush_to_deal_notlogin);
        notLoginSureBtn = (RoundUpAngleImageView) findViewById(R.id.notLoginSureBtn);
        dialog_inputPhoneET = (EditText) findViewById(R.id.dialog_inputPhoneET);
        notLoginSureBtn.setOnClickListener(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) JrUtils.dip2px(context, default_width);
        params.height = (int) JrUtils.dip2px(context, default_heigh);
        window.setAttributes(params);

    }

    @Override
    public void onClick(View v) {
        String phone = "";
        phone = dialog_inputPhoneET.getText().toString();
        onClickNotLoginSureBtn.onClickNotLoginSure(v, phone);
    }

    private OnClickNotLoginSureBtn onClickNotLoginSureBtn;

    public void setOnClickNotLoginSureBtn(OnClickNotLoginSureBtn onClickNotLoginSureBtn){
        this.onClickNotLoginSureBtn = onClickNotLoginSureBtn;
    }

    public interface OnClickNotLoginSureBtn{
        void onClickNotLoginSure(View view, String phone);
    }

}
