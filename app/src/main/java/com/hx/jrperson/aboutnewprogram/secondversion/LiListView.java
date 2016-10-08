package com.hx.jrperson.aboutnewprogram.secondversion;

import android.content.Context;
import android.util.AttributeSet;

import com.hx.jrperson.views.PersonalListView;

/**
 * Created by Administrator on 2016/8/31.
 * 原本用于二次封装在全部展开listview情况下扩展拉伸操作的封装类,已废除
 */
public class LiListView extends PersonalListView{
    public LiListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LiListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
