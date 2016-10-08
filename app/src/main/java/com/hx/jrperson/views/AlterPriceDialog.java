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
 * 订单价格修改dialog
 * Created by ge on 2016/3/3.
 */
public class AlterPriceDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Button alter_price_cancleBtn, alter_price_sureBtn;
    private TextView alterGutTV;
    private static int default_width = 300;
    private static int default_heigh = 200;
    private String str;

    public AlterPriceDialog(Context context, String str) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        this.str = str;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_alter_price);
        alter_price_cancleBtn = (Button) findViewById(R.id.alter_price_cancleBtn);
        alter_price_sureBtn = (Button) findViewById(R.id.alter_price_sureBtn);
        alterGutTV = (TextView) findViewById(R.id.alterGutTV);
        alterGutTV.setText(str);
        alter_price_cancleBtn.setOnClickListener(this);
        alter_price_sureBtn.setOnClickListener(this);

        Window window = getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) JrUtils.dip2px(context, default_width);
        params.height = (int) JrUtils.dip2px(context, default_heigh);
        window.setAttributes(params);

    }


    @Override
    public void onClick(View v) {
        onClickAlterPriceListener.onClickAlterPrice(v);
    }

    private OnClickAlterPriceListener onClickAlterPriceListener;

    public void setOnClickAlterPriceListener(OnClickAlterPriceListener onClickAlterPriceListener){
        this.onClickAlterPriceListener = onClickAlterPriceListener;
    }

    public interface OnClickAlterPriceListener{
        void onClickAlterPrice(View view);
    }


}
