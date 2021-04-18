package com.north.light.androidutils.imageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.north.light.androidutils.R;

/**
 * 旋转image view
 */
public class RotateImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Animation mAnim;
    private AnimationListener mListener;

    public RotateImageView(Context context) {
        this(context, null);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 开始旋转
     */
    public void startAnim() {
        if (mAnim == null) {
            mAnim = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.rotate_imageview);
        }
        LinearInterpolator lir = new LinearInterpolator();
        mAnim.setInterpolator(lir);
        clearAnimation();
        startAnimation(mAnim);
        mAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mListener != null) {
                    mListener.start();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mListener != null) {
                    mListener.stop();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mListener != null) {
                    mListener.repeat();
                }
            }
        });
    }

    /**
     * 停止旋转
     */
    public void stopAnim() {
        clearAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        mAnim = null;
        stopAnim();
        removeAnimationListener();
        super.onDetachedFromWindow();
    }


    /**
     * 设置监听
     */
    public void setOnAnimationListener(AnimationListener listener) {
        this.mListener = listener;
    }

    /**
     * 移除监听
     */
    public void removeAnimationListener() {
        this.mListener = null;
    }

    /**
     * 动画监听
     */
    public interface AnimationListener {
        void start();

        void stop();

        void repeat();
    }
}
