package com.hx.jrperson.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 小球动画 呼吸效果帮助类
 * Created by ge on 2016/2/26.
 */
public class AnimatorHelp {

    /**
     * 气泡漂浮动画
     * @param view
     * @param duration  动画运行时间
     * @param offset    动画运行幅度
     * @param repeatCount   动画运行次数
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ObjectAnimator bubbleFloat(final View view, int duration, int offset, int repeatCount) {
        float path = (float) (Math.sqrt(3)/2*offset);
        PropertyValuesHolder translateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(1/12f, offset / 2),
                Keyframe.ofFloat(2/12f, path),
                Keyframe.ofFloat(3/12f, offset),
                Keyframe.ofFloat(4/12f, path),
                Keyframe.ofFloat(5/12f, offset / 2),
                Keyframe.ofFloat(6/12f, 0),
                Keyframe.ofFloat(7/12f, -offset / 2),
                Keyframe.ofFloat(8/12f, -path),
                Keyframe.ofFloat(9/12f, -offset),
                Keyframe.ofFloat(10/12f, -path),
                Keyframe.ofFloat(11/12f, -offset/2),
                Keyframe.ofFloat(1f, 0)
        );

        PropertyValuesHolder translateY = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(1/12f, offset-path),
                Keyframe.ofFloat(2/12f, offset/2),
                Keyframe.ofFloat(3/12f, offset),
                Keyframe.ofFloat(4/12f, offset*3/2),
                Keyframe.ofFloat(5/12f, offset+path),
                Keyframe.ofFloat(6/12f, offset*2),
                Keyframe.ofFloat(7/12f, offset+path),
                Keyframe.ofFloat(8/12f, offset*3/2),
                Keyframe.ofFloat(9/12f, offset),
                Keyframe.ofFloat(10/12f, offset/2),
                Keyframe.ofFloat(11/12f, offset-path),
                Keyframe.ofFloat(1f, 0)
        );

        PropertyValuesHolder rotateX = PropertyValuesHolder.ofKeyframe(View.ROTATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(1/12f, offset / 2),
                Keyframe.ofFloat(2/12f, path),
                Keyframe.ofFloat(3/12f, offset),
                Keyframe.ofFloat(4/12f, path),
                Keyframe.ofFloat(5/12f, offset / 2),
                Keyframe.ofFloat(6/12f, 0),
                Keyframe.ofFloat(7/12f, -offset / 2),
                Keyframe.ofFloat(8/12f, -path),
                Keyframe.ofFloat(9/12f, -offset),
                Keyframe.ofFloat(10/12f, -path),
                Keyframe.ofFloat(11/12f, -offset/2),
                Keyframe.ofFloat(1f, 0)
        );

        PropertyValuesHolder rotateY = PropertyValuesHolder.ofKeyframe(View.ROTATION_Y,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(1/12f, offset / 2),
                Keyframe.ofFloat(2/12f, path),
                Keyframe.ofFloat(3/12f, offset),
                Keyframe.ofFloat(4/12f, path),
                Keyframe.ofFloat(5/12f, offset / 2),
                Keyframe.ofFloat(6/12f, 0),
                Keyframe.ofFloat(7/12f, -offset / 2),
                Keyframe.ofFloat(8/12f, -path),
                Keyframe.ofFloat(9/12f, -offset),
                Keyframe.ofFloat(10/12f, -path),
                Keyframe.ofFloat(11/12f, -offset/2),
                Keyframe.ofFloat(1f, 0)
        );

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, translateX, translateY, rotateX, rotateY).
                setDuration(duration);
        animator.setRepeatCount(repeatCount);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        return animator;
    }

}
