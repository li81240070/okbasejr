package com.hx.jrperson.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;


/**
 *小球进入和散开的动画
 * Created by ge on 2016/3/1.
 */
public class JrAnimationsHelp {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static AnimatorSet Help(Context context, int wid, int hei, boolean isChange, ImageView waterIv, ImageView eleIv,
                            ImageView houseKeepingIv, ImageView homeTrimIv, ImageView safeIv, ImageView reMoveIv,
                            ImageView setupIv, ImageView upgradleIv) {

        //八个小球的xy移动距离
        float waterDC, eleXDc, eleYDc , houseKeepingXDC, houseKeepingYDC,
                homeTrimXDC, homeTrimYDC, safeXDC, safeYDC, reMoveXDC, reMoveYDC,
                setupXDC, setupYDC, upgradleXDC, upgradleYDC;

        waterDC = (float) (JrUtils.dip2px(context, 200) + hei * 0.4); //水维修 移动的距离
        eleYDc = (float) (JrUtils.dip2px(context, 200) + hei * 0.28);//电维修 纵向移动距离
        eleXDc = (float) (JrUtils.dip2px(context, 100) + wid * 0.11);//电维修 横向移动距离
        houseKeepingYDC = (float) (JrUtils.dip2px(context, 200) + hei * 0.58);//家政力工 纵向
        houseKeepingXDC = (float) (JrUtils.dip2px(context, 100) + wid * 0.2);//家政力工 横向
        homeTrimYDC = (float) (JrUtils.dip2px(context, 200) + hei * 0.47);//居家小修 纵向
        homeTrimXDC = (float) (JrUtils.dip2px(context, 100) + wid * 0.1); //居家小修 横向
        safeYDC = (float) (JrUtils.dip2px(context, 200) + hei * 0.19);//安全检测 纵向
        safeXDC = (float) (JrUtils.dip2px(context, 100) + wid * 0.3);//安全检测 横向
        reMoveYDC = (float) (JrUtils.dip2px(context, 200) + hei * 0.27); //货车搬家 纵向
        reMoveXDC = (float) (JrUtils.dip2px(context, 100) + wid * 0.22); //货车搬家 横向
        setupYDC = (float) (JrUtils.dip2px(context, 200) + hei * 0.40);//居家安装 纵向
        setupXDC = (float) (JrUtils.dip2px(context, 100) + wid * 0.11);//居家安装 横向
        upgradleYDC = (float) (JrUtils.dip2px(context, 200) + hei * 0.5);//我家升级 纵向
        upgradleXDC = (float) (JrUtils.dip2px(context, 100) + wid * 0.05);//我家升级 横向

        /***
         * if进入 else散开
         * **/
        if (isChange == true){
            //水维修
            final ObjectAnimator animator = ObjectAnimator.ofFloat(waterIv, "translationY", 0, -waterDC);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(1000);
            //电维修
            PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationY", 0, -eleYDc);
            PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationX", 0, -eleXDc);
            ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(eleIv, p1, p2);
            animator1.setInterpolator(new AccelerateDecelerateInterpolator());
            animator1.setDuration(1000);
            //家政力工
            PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationY", 0, -houseKeepingYDC);
            PropertyValuesHolder p4 = PropertyValuesHolder.ofFloat("translationX", 0, -houseKeepingXDC);
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(houseKeepingIv, p3, p4);
            animator2.setInterpolator(new AccelerateDecelerateInterpolator());
            animator2.setDuration(1000);
            //居家小修
            PropertyValuesHolder p5 = PropertyValuesHolder.ofFloat("translationY", 0, -homeTrimYDC);
            PropertyValuesHolder p6 = PropertyValuesHolder.ofFloat("translationX", 0, -homeTrimXDC);
            ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(homeTrimIv, p5, p6);
            animator3.setInterpolator(new AccelerateDecelerateInterpolator());
            animator3.setDuration(1000);
            //安全检测
            PropertyValuesHolder p7 = PropertyValuesHolder.ofFloat("translationY", 0, -safeYDC);
            PropertyValuesHolder p8 = PropertyValuesHolder.ofFloat("translationX", 0, -safeXDC);
            ObjectAnimator animator4 = ObjectAnimator.ofPropertyValuesHolder(safeIv, p7, p8);
            animator4.setInterpolator(new AccelerateDecelerateInterpolator());
            animator4.setDuration(1000);
            //货车搬家
            PropertyValuesHolder p9 = PropertyValuesHolder.ofFloat("translationY", 0, -reMoveYDC);
            PropertyValuesHolder p10 = PropertyValuesHolder.ofFloat("translationX", 0, reMoveXDC);
            ObjectAnimator animator5 = ObjectAnimator.ofPropertyValuesHolder(reMoveIv, p9, p10);
            animator5.setInterpolator(new AccelerateDecelerateInterpolator());
            animator5.setDuration(1000);
            //居家安装
            PropertyValuesHolder p11 = PropertyValuesHolder.ofFloat("translationY", 0, -setupYDC);
            PropertyValuesHolder p12 = PropertyValuesHolder.ofFloat("translationX", 0, setupXDC);
            ObjectAnimator animator6 = ObjectAnimator.ofPropertyValuesHolder(setupIv, p11, p12);
            animator6.setInterpolator(new AccelerateDecelerateInterpolator());
            animator6.setDuration(1000);
            //我家升级
            PropertyValuesHolder p13 = PropertyValuesHolder.ofFloat("translationY", 0, -upgradleYDC);
            PropertyValuesHolder p14 = PropertyValuesHolder.ofFloat("translationX", 0, upgradleXDC);
            ObjectAnimator animator7 = ObjectAnimator.ofPropertyValuesHolder(upgradleIv, p13, p14);
            animator7.setInterpolator(new AccelerateDecelerateInterpolator());
            animator7.setDuration(1000);


            final AnimatorSet set = new AnimatorSet();
            set.playTogether(animator, animator1, animator2, animator3, animator4, animator5, animator6, animator7);

            return set;

        }else{
            //水维修
            ObjectAnimator animator = ObjectAnimator.ofFloat(waterIv, "translationY", -waterDC, 0);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(1000);
            //电维修
            PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationY", -eleYDc, 0);
            PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationX", -eleXDc, 0);
            ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(eleIv, p1, p2);
            animator1.setInterpolator(new AccelerateDecelerateInterpolator());
            animator1.setDuration(1000);
            //家政力工
            PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationY", -houseKeepingYDC, 0);
            PropertyValuesHolder p4 = PropertyValuesHolder.ofFloat("translationX", -houseKeepingXDC, 0);
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(houseKeepingIv, p3, p4);
            animator2.setInterpolator(new AccelerateDecelerateInterpolator());
            animator2.setDuration(1000);
            //居家小修
            PropertyValuesHolder p5 = PropertyValuesHolder.ofFloat("translationY", -homeTrimYDC, 0);
            PropertyValuesHolder p6 = PropertyValuesHolder.ofFloat("translationX", -homeTrimXDC, 0);
            ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(homeTrimIv, p5, p6);
            animator3.setInterpolator(new AccelerateDecelerateInterpolator());
            animator3.setDuration(1000);
            //安全检测
            PropertyValuesHolder p7 = PropertyValuesHolder.ofFloat("translationY", -safeYDC, 0);
            PropertyValuesHolder p8 = PropertyValuesHolder.ofFloat("translationX", -safeXDC, 0);
            ObjectAnimator animator4 = ObjectAnimator.ofPropertyValuesHolder(safeIv, p7, p8);
            animator4.setInterpolator(new AccelerateDecelerateInterpolator());
            animator4.setDuration(1000);
            //货车搬家
            PropertyValuesHolder p9 = PropertyValuesHolder.ofFloat("translationY", -reMoveYDC, 0);
            PropertyValuesHolder p10 = PropertyValuesHolder.ofFloat("translationX", reMoveXDC, 0);
            ObjectAnimator animator5 = ObjectAnimator.ofPropertyValuesHolder(reMoveIv, p9, p10);
            animator5.setInterpolator(new AccelerateDecelerateInterpolator());
            animator5.setDuration(1000);
            //居家安装
            PropertyValuesHolder p11 = PropertyValuesHolder.ofFloat("translationY", -setupYDC, 0);
            PropertyValuesHolder p12 = PropertyValuesHolder.ofFloat("translationX", setupXDC, 0);
            ObjectAnimator animator6 = ObjectAnimator.ofPropertyValuesHolder(setupIv, p11, p12);
            animator6.setInterpolator(new AccelerateDecelerateInterpolator());
            animator6.setDuration(1000);
            //我家升级
            PropertyValuesHolder p13 = PropertyValuesHolder.ofFloat("translationY", -upgradleYDC, 0);
            PropertyValuesHolder p14 = PropertyValuesHolder.ofFloat("translationX", upgradleXDC, 0);
            ObjectAnimator animator7 = ObjectAnimator.ofPropertyValuesHolder(upgradleIv, p13, p14);
            animator7.setInterpolator(new AccelerateDecelerateInterpolator());
            animator7.setDuration(1000);

            AnimatorSet sett = new AnimatorSet();
            sett.playTogether(animator, animator1, animator2, animator3, animator4, animator5, animator6, animator7);
            return sett;
        }

    }



}
