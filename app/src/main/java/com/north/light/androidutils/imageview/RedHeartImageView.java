package com.north.light.androidutils.imageview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.north.light.androidutils.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 红心点赞ImageView
 * 宽度和高度必须一样
 **/
public class RedHeartImageView extends androidx.appcompat.widget.AppCompatImageView {
    //默认的图片
    private int mDefaultRes = 0;
    //点击后改变的图片
    private int mTargetRes = 0;
    //缩小的时间
    private int TIME_REDUCE = 200;
    //放大的时间
    private int TIME_ADD = 200;


    public RedHeartImageView(Context context) {
        this(context, null);
    }

    public RedHeartImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedHeartImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }

    private void initParams() {

    }

    /**
     * 设置图片资源--必须调用
     *
     * @param defaultRes 默认的图片
     * @param targetRes  点击后改变的图片
     */
    public void setRes(int defaultRes, int targetRes) {
        this.mDefaultRes = defaultRes;
        this.mTargetRes = targetRes;
        //加载默认图片
        setImageResource(defaultRes);
    }


    /**
     * 根据状态，修改图片
     *
     * @param showStatus 0原图 1变图
     */
    public void changeRes(final int showStatus) {
        if (!initRes()) {
            return;
        }
        if (showStatus == 1) {
            setImageResource(mTargetRes);
        } else if (showStatus == 0) {
            setImageResource(mDefaultRes);
        }
        animate().scaleX(0.5f).scaleY(0.5f).setDuration(TIME_REDUCE).alpha(0.5f).withEndAction(new Runnable() {
            @Override
            public void run() {
                animate().scaleX(1f).scaleY(1f).setDuration(TIME_ADD).alpha(1f).start();
            }
        }).start();
    }


    /**
     * 是否初始化了资源判断函数
     */
    private Boolean initRes() {
        return (mDefaultRes != 0 && mTargetRes != 0);
    }

}
