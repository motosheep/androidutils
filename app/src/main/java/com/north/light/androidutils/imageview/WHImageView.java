package com.north.light.androidutils.imageview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by lzt
 * time 2020/9/18
 * 描述：宽高相等的image view
 */
public class WHImageView extends androidx.appcompat.widget.AppCompatImageView {

    public WHImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置View宽高的测量值
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        // 只有setMeasuredDimension调用之后，才能使用getMeasuredWidth()和getMeasuredHeight()来获取视图测量出的宽高，以此之前调用这两个方法得到的值都会是0
        int childWidthSize = getMeasuredWidth();

        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}