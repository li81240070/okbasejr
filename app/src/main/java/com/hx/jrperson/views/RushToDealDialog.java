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
 * 已登录状态点击抢险抢修弹出框
 * Created by ge on 2016/3/1.
 */
public class RushToDealDialog extends Dialog implements View.OnClickListener {

    private Button cancleBtn, sureBtn;
    private Context context;
    private static int default_width = 280;
    private static int default_heigh = 190;


    public RushToDealDialog(Context context) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_rush_to_deal);
        cancleBtn = (Button) findViewById(R.id.cancleBtn);
        sureBtn = (Button) findViewById(R.id.sureBtn);
        cancleBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);

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
