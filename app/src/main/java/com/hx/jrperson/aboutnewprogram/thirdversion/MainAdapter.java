package com.hx.jrperson.aboutnewprogram.thirdversion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 * 主页面中最外层视图碎片的适配器
 */
public class MainAdapter extends FragmentPagerAdapter {
    //建立集合组,控制mainactivity中流入的数据
    private ArrayList fragments;
    //设置标题相关的字符组
    private String[] titles;

    //从外部设置标题
    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public ArrayList getFragments() {
        return fragments;

    }


    public void setFragments(ArrayList fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
        //当数据发生变化的时候,通知adapter中的对象,刷新视图
    }


    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    //通过集合的下标得到fragment
    @Override
    public Fragment getItem(int position) {
        return fragments != null && fragments.size() > 0 ? (Fragment) fragments.get(position) : null;
    }

    //得到集合的个数
    @Override
    public int getCount() {
        return fragments.size();

    }

    //写标题
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
