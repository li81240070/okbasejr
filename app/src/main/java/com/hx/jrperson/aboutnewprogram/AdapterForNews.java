package com.hx.jrperson.aboutnewprogram;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/5.
 * 第二版内容中的内容,已被第三版内容覆盖,放置即可
 */
public class AdapterForNews extends FragmentPagerAdapter {
    private ArrayList fragments;
    public void setFragments(ArrayList fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public AdapterForNews(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments != null && fragments.size() > 0 ? (Fragment) fragments.get(position) : null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
