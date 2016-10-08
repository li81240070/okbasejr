package com.hx.jrperson.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.hx.jrperson.R;
import com.hx.jrperson.utils.JrUtils;

/**
 * 支付前提示diaolog
 * Created by ge on 2016/3/4.
 */
public class BeforPayDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Button pay_befor_cancleBtn;
    private static int default_width = 250;
    private static int default_heigh = 180;

    public BeforPayDialog(Context context) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_pay_befor);
        pay_befor_cancleBtn = (Button) findViewById(R.id.pay_befor_cancleBtn);
        pay_befor_cancleBtn.setOnClickListener(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) JrUtils.dip2px(context, default_width);
        params.height = (int) JrUtils.dip2px(context, default_heigh);
        window.setAttributes(params);
    }


    @Override
    public void onClick(View v) {
        onClickPayBeforListener.onClickPayBefor(v);
    }

    private OnClickPayBeforListener onClickPayBeforListener;

    public void setOnClickPayBeforListener(OnClickPayBeforListener onClickPayBeforListener){
        this.onClickPayBeforListener = onClickPayBeforListener;
    }

    public interface OnClickPayBeforListener{
        void onClickPayBefor(View view);
    }


}
