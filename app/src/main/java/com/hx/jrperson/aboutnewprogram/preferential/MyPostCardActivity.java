package com.hx.jrperson.aboutnewprogram.preferential;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hx.jrperson.R;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyPostCardActivity extends BaseActivity{
    private TabLayout postTablayout;
    private ViewPager postViewpager;
    private PostCardAdapter adapter;
    private ArrayList fragmentArrayList;
    private ImageView backbuttonInPostcard;
    private RelativeLayout backButtonInPostcard;
    private CheckBox checkBox;
    ///////////////////////////
    private Button makeSureThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypostcardpage);
        //点击确认按钮返回订单页
        makeSureThis= (Button) findViewById(R.id.makeSureThis);
        makeSureThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostCardActivity.this.finish();
            }
        });


        //当选中按钮生效,发出广播取消当前所有的选中优惠
        checkBox= (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent=new Intent("com.example.dllo.broadcast.desAll");
                    //往广播里面放内容
                    intent.putExtra("des","取消");
                    //进入广播启动项
                    sendBroadcast(intent);
                }
                if (!isChecked){
                    Intent intent=new Intent("com.example.dllo.broadcast.desAll");
                    //往广播里面放内容
                    intent.putExtra("des","选中");
                    //进入广播启动项
                    sendBroadcast(intent);
                }


            }
        });

        //返回按钮
        backbuttonInPostcard= (ImageView) findViewById(R.id.backbuttonInPostcard);
        backbuttonInPostcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostCardActivity.this.finish();
            }
        });
        backButtonInPostcard= (RelativeLayout) findViewById(R.id.backButtonInPostcard);
        backButtonInPostcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostCardActivity.this.finish();
            }
        });


        postTablayout= (TabLayout) findViewById(R.id.postTablayout);
        postViewpager= (ViewPager) findViewById(R.id.postViewpager);
        adapter=new PostCardAdapter(getSupportFragmentManager());
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new DiscountPostFragment());
        fragmentArrayList.add(new ValuePostFragment());
        adapter.setFragments(fragmentArrayList);
      postViewpager.setAdapter(adapter);
        //给tabLayout设置viewpager
        postTablayout.setupWithViewPager(postViewpager);
        //设置引导线颜色
        postTablayout.setSelectedTabIndicatorColor(0xff3399ff);
//改变选中字体和未选中字体颜色
        postTablayout.setTabTextColors(0xff969082,0xff3399ff);

    }
}
