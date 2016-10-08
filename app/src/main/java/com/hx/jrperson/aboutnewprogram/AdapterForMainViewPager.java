package com.hx.jrperson.aboutnewprogram;

import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hx.jrperson.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/5.
 * 第二版主页面适配器文件,已被第三版内容替代,置放即可
 */
public class AdapterForMainViewPager extends PagerAdapter {
    private ArrayList imageViewList;
    private CountDownTimer timer, timer2;
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public void setImageViewList(ArrayList imageViewList) {
        this.imageViewList = imageViewList;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//取出指定位置的图片ImageView
        final ImageView imageView = (ImageView) imageViewList.get(position % imageViewList.size());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        try {
            container.addView(imageView);
        } catch (IllegalStateException e) {
            container.removeView(imageView);
            container.addView(imageView);
        }
        //对轮播图增加刷油漆彩蛋
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                imageView.setImageResource(0);
                imageView.setImageResource(R.mipmap.brush);
                timer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        imageView.setImageResource(0);
                        timer.cancel();
                    }
                }.start();

                return false;
            }
        });
        return imageView;
    }

    //销毁指定位置的ImageView回收内存
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container.getChildAt(position % imageViewList.size()) == object) {
            // container.removeViewAt(position % imageViewList.size());
        }
    }
}
