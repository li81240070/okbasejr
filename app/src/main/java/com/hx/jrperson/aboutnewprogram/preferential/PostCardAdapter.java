package com.hx.jrperson.aboutnewprogram.preferential;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 */
public class PostCardAdapter extends FragmentPagerAdapter {
    private ArrayList fragments;
    //设置标题相关的字符组
    private String[] titles = {"抵值券", "折扣卡"};


    public ArrayList getFragments() {
        return fragments;

    }

    public void setFragments(ArrayList fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
//当数据发生变化的时候,通知adapter中的对象,刷新视图
    }


    public PostCardAdapter(FragmentManager fm) {
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
