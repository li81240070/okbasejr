package com.hx.jrperson.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.utils.JrUtils;

/**
 * 拨打电话dialog
 * Created by ge on 2016/3/3.
 */
public class CallPhoneDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView cancle_ordor_cancleTV, cancle_ordor_sureTV, gutTV, title_top_dialog;
    private static int default_width = 300;
    private static int default_heigh = 180;
    private String phone;

    public CallPhoneDialog(Context context, String phone) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        this.phone = phone;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_cancle_ordor);
        cancle_ordor_cancleTV = (TextView) findViewById(R.id.cancle_ordor_cancleTV);
        cancle_ordor_sureTV = (TextView) findViewById(R.id.cancle_ordor_sureTV);
        gutTV = (TextView) findViewById(R.id.gutTV);

        gutTV.setText("您将给客服拨打电话：" + phone);

        cancle_ordor_sureTV.setOnClickListener(this);
        cancle_ordor_cancleTV.setOnClickListener(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) JrUtils.dip2px(context, default_width);
        params.height = (int) JrUtils.dip2px(context, default_heigh);
        window.setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        onClickCancleOrdorListener.onClickCancleOrdor(v);
    }


    private OnClickCancleOrdorListener onClickCancleOrdorListener;

    public void setOnClickCancleOrdorListener(OnClickCancleOrdorListener onClickCancleOrdorListener){
        this.onClickCancleOrdorListener = onClickCancleOrdorListener;
    }

    public interface OnClickCancleOrdorListener{
        void onClickCancleOrdor(View view);
    }



}
