package com.hx.jrperson.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.hx.jrperson.R;
import com.hx.jrperson.aboutnewprogram.NewsActivity;
import com.hx.jrperson.views.baseView.BaseActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 闪屏页面
 * by ge
 * **/
public class SplachActivity extends BaseActivity {

    private ImageView splachImg;
    private Handler handler;
    private SharedPreferences sp;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ///////////////////////
//            SharedPreferences getSp=getSharedPreferences("ok",MODE_PRIVATE);
////取出的数据存储名称和未取到数据时显示的内容
//            String name1=getSp.getString("isfirst","默认");
//            if (name1.equals("默认")){
//                sp=getSharedPreferences("ok",MODE_PRIVATE);
//                SharedPreferences.Editor editor=sp.edit();
//                editor.putString("isfirst","中华小当家");
//                editor.commit();
                Intent intent = new Intent(SplachActivity.this, NewsActivity.class);
                startActivity(intent);
                SplachActivity.this.finish();
//            }else{
//
//                Intent intent = new Intent(SplachActivity.this,MainActivity.class);
//                startActivity(intent);
//                SplachActivity.this.finish();
//            }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        splachImg = (ImageView) findViewById(R.id.splachImg);
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
        splachImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////////////////////
                ///////////////////////
//                SharedPreferences getSp=getSharedPreferences("ok",MODE_PRIVATE);
////取出的数据存储名称和未取到数据时显示的内容
//                String name1=getSp.getString("isfirst","默认");
//                if (name1.equals("默认")){
//                    sp=getSharedPreferences("ok",MODE_PRIVATE);
//                    SharedPreferences.Editor editor=sp.edit();
//                    editor.putString("isfirst","中华小当家");
//                    editor.commit();
                    Intent intent = new Intent(SplachActivity.this, NewsActivity.class);
                    startActivity(intent);
                    handler.removeCallbacks(runnable);
                    SplachActivity.this.finish();
//                }else{
//                    Intent intent = new Intent(SplachActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    handler.removeCallbacks(runnable);
//                    SplachActivity.this.finish();
//                }




//                Intent intent = new Intent(SplachActivity.this, NewsActivity.class);
//                startActivity(intent);
//                handler.removeCallbacks(runnable);
//                SplachActivity.this.finish();
            }
        });

//        sp=getSharedPreferences("ok",MODE_PRIVATE);
//
////向硬盘中存储,需要获得editor对象
//        SharedPreferences.Editor editor=sp.edit();
//
////放数据
//
//        editor.putString("name1","中华小当家");
//
//
////提交数据
//        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}
