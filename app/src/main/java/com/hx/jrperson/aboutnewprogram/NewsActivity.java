package com.hx.jrperson.aboutnewprogram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.hx.jrperson.R;
import com.hx.jrperson.aboutnewprogram.homepagefragment.FourFragment;
import com.hx.jrperson.aboutnewprogram.homepagefragment.OneFragment;
import com.hx.jrperson.aboutnewprogram.homepagefragment.ThirdFragment;
import com.hx.jrperson.aboutnewprogram.homepagefragment.SecondFragment;
import com.hx.jrperson.ui.activity.MainActivity;
import com.hx.jrperson.views.baseView.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/5.
 * 第二版中的相关内容,已被第三版内容替代,放置即可
 */
public class NewsActivity extends BaseActivity {
    private ViewPager newsViewPager;
    private ArrayList data;
    private AdapterForNews adapter;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences getSps = getSharedPreferences("ok", MODE_PRIVATE);
        String name0 = getSps.getString("isfirst", "默认");
        if (!name0.equals("默认")) {
            Intent intent = new Intent(NewsActivity.this, MainActivity.class);
            startActivity(intent);
            NewsActivity.this.finish();
        }


        setContentView(R.layout.newsviewpagerpage);
        //设置本地轮播图相关逻辑
        newsViewPager = (ViewPager) findViewById(R.id.newsViewPager);
        adapter = new AdapterForNews(getSupportFragmentManager());
        data = new ArrayList();
        data.add(new OneFragment());
        data.add(new SecondFragment());
        data.add(new ThirdFragment());
        data.add(new FourFragment());

        adapter.setFragments(data);
        newsViewPager.setAdapter(adapter);

    }
}