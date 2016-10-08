package com.hx.jrperson.aboutnewprogram;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/22.
 * 第三版中对二维码数据进行处理生成的相关代码,暂时由html5接手相关功能
 * 但功能已经完善,可以随时启用,暂时放置
 */
public class CodeActivity extends Activity {
    private TextView mTv_Ruslet;
    private EditText mInput;
    private ImageView mImg;
    private CheckBox isLogo;
    //二维码取出本地值相关
    private SharedPreferences getSp;
    private RelativeLayout backButtonInCode;
    private ImageView backbuttonInCode, saveYourCode;
    private Bitmap bitmap, bitmap2;
    //变色计时器
    private CountDownTimer timer;
    private int colorNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codeactivitypage);

        timer = new CountDownTimer(Integer.MAX_VALUE, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                mImg.setImageBitmap(null);
                getSp = getSharedPreferences("twocode", MODE_PRIVATE);
                String tokenValue = getSp.getString("token", "默认");
                if (!tokenValue.equals("默认")) {
                    bitmap = EncodingUtils.createQRCode(tokenValue, 800, 800,
                            //是否出现商标
                            BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.iconlogo), colorNum
                    );

                    mImg.setImageBitmap(bitmap);
                    colorNum++;
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();


//        mTv_Ruslet= (TextView) findViewById(R.id.tv_ruselt);
//        mInput= (EditText) findViewById(R.id.et_text);
        mImg = (ImageView) findViewById(R.id.img);
//        isLogo= (CheckBox) findViewById(R.id.is_logo);
        //取出本地存储的token
        getSp = getSharedPreferences("twocode", MODE_PRIVATE);
        String tokenValue = getSp.getString("token", "默认");
        if (!tokenValue.equals("默认")) {
            bitmap = EncodingUtils.createQRCode(tokenValue, 800, 800,
                    //是否出现商标
                    BitmapFactory.decodeResource(getResources(),
                            R.mipmap.iconlogo), colorNum
            );

            mImg.setImageBitmap(bitmap);

        }
        //返回按钮
        backbuttonInCode = (ImageView) findViewById(R.id.backbuttonInCode);
        backbuttonInCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeActivity.this.finish();
            }
        });
        backButtonInCode = (RelativeLayout) findViewById(R.id.backButtonInCode);
        backButtonInCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeActivity.this.finish();
            }
        });

        //保存当前二维码当手机相册中
        saveYourCode = (ImageView) findViewById(R.id.saveYourCode);
        saveYourCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToGallery(CodeActivity.this, bitmap);
            }
        });

    }
    /**
     *生成二维码
     */
//    public void make(View view){
//        String input=mInput.getText().toString().trim();
//        //生成二维码，然后为二维码增加logo
//        Bitmap bitmap= EncodingUtils.createQRCode(input,500,500,
//                isLogo.isChecked()? BitmapFactory.decodeResource(getResources(),
//                        R.mipmap.logoicon):null
//        );
//        mImg.setImageBitmap(bitmap);
//    }

    /**
     * 扫描二维码
     */
//    public void scan(View view){
//        startActivityForResult(new Intent(CodeActivity.this, CaptureActivity.class),0);
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //Log.i("TAG","resultCode: "+resultCode+" result_ok: "+RESULT_OK);
//        if (resultCode==RESULT_OK){
//            Bundle bundle=data.getExtras();
//            String result= bundle.getString("result");
//            mTv_Ruslet.setText(result);
//        } if(resultCode == RESULT_CANCELED) {
//            mTv_Ruslet.setText("扫描出错");
//        }
//    }

    //将图片保存到手机相册中
    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/zhenjiangren/image.jpg")));

        //弹出提示下载图片成功的对话框
        AlertDialog.Builder alert1 = new AlertDialog.Builder(CodeActivity.this);
        //设置图标
        alert1.setIcon(R.mipmap.iconlogo);
        //设置标题
        alert1.setTitle("保存成功");
        //设置主体信息
        alert1.setMessage("二维码已经保存到您的相册中");
        //设置积极按钮 参数一是按钮上的文字 之后是点击事件
        alert1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        //开始,显示
        alert1.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
