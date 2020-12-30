package com.north.light.androidutils.textview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.math.BigDecimal;

/**
 * Created by lzt
 * time 2020/12/30
 * 描述：数字动画text view 只能输入数字
 */
public class NumAnimTextView extends androidx.appcompat.widget.AppCompatTextView {
    private ValueAnimator mValueAnimator;

    public NumAnimTextView(Context context) {
        this(context, null);
    }

    public NumAnimTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumAnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }
        mValueAnimator = null;
        super.onDetachedFromWindow();
    }

    /**
     * 初始化属性
     */
    private void initAttr() {
        //数字和小数点
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    /**
     * 设置字体
     */
    public void setAnimText(String target) {
        try {
            if (!judgeType(target)) {
                setText("0");
                return;
            }
            //转换为数字
            String originalTx = getText().toString();
            if (TextUtils.isEmpty(originalTx)) {
                originalTx = "0";
            }
            //计算差值
            BigDecimal startTx = new BigDecimal(originalTx);
            BigDecimal endTx = new BigDecimal(target);
            //动画
            if (mValueAnimator != null) {
                mValueAnimator.end();
            }
            mValueAnimator = ValueAnimator.ofFloat(startTx.floatValue(), endTx.floatValue());
            mValueAnimator.setDuration(500);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    try {
                        BigDecimal cache = new BigDecimal(valueAnimator.getAnimatedValue().toString());
                        setText(cache.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } catch (Exception e) {

                    }
                }
            });
            mValueAnimator.start();
        } catch (Exception e) {
            setText("");
        }

    }

    /**
     * 判断输入字符是否为数字
     */
    private boolean judgeType(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }
        try {
            Double.valueOf(target);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
