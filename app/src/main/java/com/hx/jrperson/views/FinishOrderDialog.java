package com.hx.jrperson.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.utils.JrUtils;

/**
 * 订单完工dialog
 * Created by ge on 2016/3/3.
 */
public class FinishOrderDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Button finish_order_sureBtn;
    private TextView finish_orderGutTV;
    private static int default_width = 300;
    private static int default_heigh = 200;
    private String str;

    public FinishOrderDialog(Context context, String str) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        this.str = str;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_finish_order);
        finish_order_sureBtn = (Button) findViewById(R.id.finish_order_sureBtn);
        finish_orderGutTV = (TextView) findViewById(R.id.finish_orderGutTV);
        finish_orderGutTV.setText(str);
        finish_order_sureBtn.setOnClickListener(this);

        Window window = getWindow();
           window.setType(WindowManager.LayoutParams.TYPE_TOAST);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) JrUtils.dip2px(context, default_width);
        params.height = (int) JrUtils.dip2px(context, default_heigh);
        window.setAttributes(params);

    }


    @Override
    public void onClick(View v) {
        onClickFinishListener.onClickFinish(v);
    }

    private OnClickFinishListener onClickFinishListener;

    public void setOnClickFinishListener(OnClickFinishListener onClickFinishListener){
        this.onClickFinishListener = onClickFinishListener;
    }

    public interface OnClickFinishListener{
        void onClickFinish(View view);
    }



}
