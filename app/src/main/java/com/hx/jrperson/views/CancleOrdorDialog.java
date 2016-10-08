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
 * 订单取消确认dialog
 * Created by ge on 2016/3/3.
 */
public class CancleOrdorDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private int count, type;
    private TextView cancle_ordor_cancleTV, cancle_ordor_sureTV, gutTV;
    private static int default_width = 300;
    private static int default_heigh = 180;

    public CancleOrdorDialog(Context context, int count, int type) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        this.count = count;
        this.type = type;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_cancle_ordor);
        cancle_ordor_cancleTV = (TextView) findViewById(R.id.cancle_ordor_cancleTV);
        cancle_ordor_sureTV = (TextView) findViewById(R.id.cancle_ordor_sureTV);
        gutTV = (TextView) findViewById(R.id.gutTV);
        if (type == 1){//订单进行中得取消
            gutTV.setText("确定取消订单么？每天最多取消2次订单。您当前已取消" + count + "次");
        }else {
            gutTV.setText("确定取消订单么？");
        }
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
