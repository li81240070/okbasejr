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
 * 抢险抢修Dialog(已经登陆后弹出样式)
 * Created by ge on 2016/3/1.
 */
public class SurePayDialog extends Dialog implements View.OnClickListener {

    private Button payCancleBtn, paySureBtn;
    private Context context;
    private static int default_width = 300;
    private static int default_heigh = 200;


    public SurePayDialog(Context context) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_pay_sure);
        payCancleBtn = (Button) findViewById(R.id.payCancleBtn);
        paySureBtn = (Button) findViewById(R.id.paySureBtn);
        payCancleBtn.setOnClickListener(this);
        paySureBtn.setOnClickListener(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) JrUtils.dip2px(context, default_width);
        params.height = (int) JrUtils.dip2px(context, default_heigh);
        window.setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        onRushClickBtnLinsener.onRushClickBtn(v);
    }

    //按钮回调接口
    private OnRushClickBtnLinsener onRushClickBtnLinsener;

    public void setOnRushClickBtnLinsener(OnRushClickBtnLinsener onRushClickBtnLinsener){
        this.onRushClickBtnLinsener = onRushClickBtnLinsener;
    }

    public interface OnRushClickBtnLinsener{
        void onRushClickBtn(View v);
    }

}
