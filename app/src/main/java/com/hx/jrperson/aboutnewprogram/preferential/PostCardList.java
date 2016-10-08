package com.hx.jrperson.aboutnewprogram.preferential;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/9/6.
 * 一次封装的listview类,实现在任何情况下全部展开当前listview全部内容功能
 */
public class PostCardList extends ListView{
    public PostCardList(Context context) {
        super(context);
    }

    public PostCardList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostCardList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //具体实现全部展开功能的相关代码
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
