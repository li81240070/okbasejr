package com.hx.jrperson.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.hx.jrperson.R;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.utils.JrUtils;
import com.squareup.picasso.Picasso;


/**
 * 展示大图片dialog
 * Created by ge on 2016/3/11.
 */
public class ShowBigPhotoDialog extends Dialog {

    private Context context;
    private int wid, hei, num;//dialog宽高
    private String uriStr = API.NEWMESSAGEINFORHEAD;//uri头
    private ImageView cloaseIV, showBigPhotoIV;//大图片
    private Button startUseBtn;
    private RoundAngleImageView newInforIV;

    public ShowBigPhotoDialog(Context context, String uriStr, int wid, int hei, int num) {
        super(context, R.style.RushToDealialog);
        this.context = context;
        if (num != 3){
            this.uriStr = this.uriStr.concat(uriStr).concat(".jpg");
        }else {
            this.uriStr = uriStr;
        }
        this.wid = wid;
        this.hei = hei;
        this.num = num;
        init();
    }

    private void init() {
        if (num == 1) {
            setContentView(R.layout.dialog_new_infor);
            newInforIV = (RoundAngleImageView) findViewById(R.id.newInforIV);
            cloaseIV = (ImageView) findViewById(R.id.cloaseIV);
            Picasso.with(context).load(uriStr).placeholder(R.mipmap.loading_img).into(newInforIV);
            cloaseIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickBigPhotoListener != null) {
                        onClickBigPhotoListener.onClickBigPhotol(v);
                    }
                }
            });
            newInforIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickBigPhotoListener != null) {
                        onClickBigPhotoListener.onClickBigPhotol(v);
                    }
                }
            });
            setCanceledOnTouchOutside(false);
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            JrUtils.getDensity(context);//透明度
            params.width = (wid * 69 / 75);
            params.height = (wid * 69 / 60);
            window.setAttributes(params);
        } else if (num == 2){
            setContentView(R.layout.dialog_big_photo);
            startUseBtn = (Button) findViewById(R.id.startUseBtn);
            startUseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickBigPhotoListener != null) {
                        onClickBigPhotoListener.onClickBigPhotol(v);
                    }
                }
            });
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            JrUtils.getDensity(context);//透明度
            params.width = wid;
            params.height = hei;
            window.setAttributes(params);
        }else if (num == 3){
            setContentView(R.layout.dialog_show_bigphoto);
            showBigPhotoIV = (ImageView) findViewById(R.id.showBigPhotoIV);
            Picasso.with(context).load(uriStr).placeholder(R.mipmap.loading_img).error(R.mipmap.loading_img).into(showBigPhotoIV);
            showBigPhotoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickBigPhotoListener != null) {
                        onClickBigPhotoListener.onClickBigPhotol(v);
                    }
                }
            });
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            JrUtils.getDensity(context);//透明度
            params.width = wid;
            params.height = hei;
            window.setAttributes(params);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    private OnClickBigPhotoListener onClickBigPhotoListener;

    public void setOnClickBigPhotoListener(OnClickBigPhotoListener onClickBigPhotoListener) {
        this.onClickBigPhotoListener = onClickBigPhotoListener;
    }

    public interface OnClickBigPhotoListener {
        void onClickBigPhotol(View view);
    }


}
