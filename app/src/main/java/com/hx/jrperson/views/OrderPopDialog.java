package com.hx.jrperson.views;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.bean.entity.OrderEntity;
import com.hx.jrperson.controller.JrController;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * 当发布的订单被抢的时候弹出的dailog
 * Created by ge on 2016/4/2.
 */
public class OrderPopDialog extends Dialog {

    private Context context;
    private int wid, hei;
    private RoundAngleImageView workerHeadImg;
    private TextView workerNameTV, workerNumberTV, workerPhoneTV;//匠人名字， 匠人工号, 手机号
    private RatingBar workerStar;//星星
    private OrderEntity entity;
    private LinearLayout popupOrderLL;
    private Handler handler;
    private ImageView call_jr_phoneIV;//打电话

    public OrderPopDialog(Context context, int  wid, int hei, OrderEntity entity) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        this.wid = wid;
        this.hei = hei;
        this.entity = entity;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_order_pop);
        workerHeadImg = (RoundAngleImageView) findViewById(R.id.workerHeadImg);
        workerNameTV = (TextView) findViewById(R.id.workerNameTV);
        workerNumberTV = (TextView) findViewById(R.id.workerNumberTV);
        workerStar = (RatingBar) findViewById(R.id.workerStar);
        workerPhoneTV = (TextView) findViewById(R.id.workerPhoneTV);
        popupOrderLL = (LinearLayout) findViewById(R.id.popupOrderLL);
        call_jr_phoneIV = (ImageView) findViewById(R.id.call_jr_phoneIV);
        call_jr_phoneIV.setFocusable(true);
        call_jr_phoneIV.setFocusableInTouchMode(true);
        workerStar.setClickable(false);
        handler = new Handler();
        String str = entity.getDataMap().getWorker_avatar();
        final String path = API.AVATER + str + "_640.jpg";
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    getAvater(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        workerNameTV.setText(entity.getDataMap().getWorker_name());//匠人名
        workerNumberTV.setText(entity.getDataMap().getWorker_no());//匠人工号
        workerPhoneTV.setText(entity.getDataMap().getWorker_mobile());//手机号
        workerStar.setRating(entity.getDataMap().getWorker_star());//星星
        workerStar.setIsIndicator(true);//不能滑动
        call_jr_phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickWorkerHeadListener != null) {
                    onClickWorkerHeadListener.onClickWorkerHead(v, entity);
                }
            }
        });
        popupOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickWorkerHeadListener != null) {
                    onClickWorkerHeadListener.onClickWorkerHead(v, entity);
                }
            }
        });

        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_TOAST);
        WindowManager.LayoutParams params = window.getAttributes();
        wid = wid * 2 / 3;
        hei = hei / 2;
        params.width = wid;
        params.height = hei;
        window.setAttributes(params);

        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        OrderPopDialog.this.dismiss();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    public void getAvater(String avatarUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JrController.setCertificates(context, client, context.getAssets().open("zhenjren.cer"));
        try {
            Request request = new Request.Builder().url(avatarUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                InputStream is = response.body().byteStream();
                final Bitmap bm = BitmapFactory.decodeStream(is);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        workerHeadImg.setImageBitmap(bm);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private OnClickWorkerHeadListener onClickWorkerHeadListener;

    public void setOnClickWorkerHeadListener(OnClickWorkerHeadListener onClickWorkerHeadListener){
        this.onClickWorkerHeadListener = onClickWorkerHeadListener;
    }

    public interface OnClickWorkerHeadListener{
        void onClickWorkerHead(View view, OrderEntity entity);
    }




}
