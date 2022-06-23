package com.north.light.androidutils.canvas.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.north.light.androidutils.R;
import com.north.light.androidutils.canvas.circle.CircleInfo;
import com.north.light.androidutils.canvas.circle.CircleBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/1/24 14:47
 * @Description:饼状绘制图
 */
public class CircleChart extends View {
    /**
     * 显示的数据
     */
    private List<CircleInfo> mData = new ArrayList<>();
    /**
     * 外部设置控件大小
     */
    private int mWidth = -1, mHeight = -1;
    private final int wrapSize = -1;
    /**
     * 是否自适应
     */
    private boolean mAutoFit = true;

    /**
     * 画布相关----------------------------------------------------------------------------
     */
    private Paint mCanvasPaint;
    private int mBottomColor = 0;

    public CircleChart(Context context) {
        super(context);
        initParams();
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }

    /**
     * 设置参数
     */
    public void initParams() {
        mCanvasPaint = new Paint();
        mCanvasPaint.setStyle(Paint.Style.FILL);
        mCanvasPaint.setStrokeWidth(1);
        mCanvasPaint.setAntiAlias(true);
        mCanvasPaint.setDither(true);
        mCanvasPaint.setTextAlign(Paint.Align.CENTER);
        mCanvasPaint.setColor(getResources().getColor(R.color.color_F06091));
    }

    /**
     * 设置绘制的数据
     */
    public void setData(CircleBuilder circleBuilder) {
        post(new Runnable() {
            @Override
            public void run() {
                if (circleBuilder != null) {
                    mWidth = circleBuilder.getWidth();
                    mHeight = circleBuilder.getHeight();
                    if (circleBuilder.getDataList() == null ||
                            circleBuilder.getDataList().size() == 0) {
                        mData = new ArrayList<>();
                    } else {
                        mData.clear();
                        mData.addAll(circleBuilder.getDataList());
                    }
                    mAutoFit = circleBuilder.isAutoFix();
                    //画布颜色
                    mBottomColor = circleBuilder.getBottomColor();
                } else {
                    mWidth = wrapSize;
                    mHeight = wrapSize;
                    mData = new ArrayList<>();
                    mAutoFit = true;
                }
                //开始刷新
                postInvalidate();
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算绘制的参数
        try {
            messageParams();
            //绘制底图
            drawBottomCircle(canvas);
            //绘制数据
            drawContentCircle(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void messageParams() throws Exception {
        if (mAutoFit) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
        } else {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = mWidth;
            layoutParams.height = mHeight;
            setLayoutParams(layoutParams);
        }
        //设置颜色
        mCanvasPaint.setColor(getResources().getColor(mBottomColor));
    }

    private void drawBottomCircle(Canvas canvas) throws Exception {
        if (!canDraw()) {
            return;
        }
        canvas.save();
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 3, mCanvasPaint);
        canvas.restore();
    }

    private void drawContentCircle(Canvas canvas) {
        if (!canDraw()) {
            return;
        }
        if (mData == null || mData.size() == 0) {
            return;
        }
        //重新计算百分比--防止超过100导致失效的情况
        float totalPercent = 0;
        for (int i = 0; i < mData.size(); i++) {
            totalPercent = totalPercent + mData.get(i).getPercent();
        }
        if (totalPercent >= 100) {
            return;
        }
        int totalCount = mData.size();
        float[] percentArray = new float[totalCount];
        for (int i = 0; i < mData.size(); i++) {
            percentArray[i] = (mData.get(i).getPercent() / 100f) * 360f;
        }
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        for (int i = 0; i < mData.size(); i++) {
            CircleInfo info = mData.get(i);
            mCanvasPaint.setColor(getResources().getColor(info.getColor()));
            int radius = mWidth / 3;
            RectF rectF = new RectF(-radius, -radius, radius, radius);
            if (i == 0) {
                canvas.drawArc(rectF, 0, percentArray[i], true, mCanvasPaint);
            } else {
                canvas.drawArc(rectF, percentArray[i - 1], percentArray[i], true, mCanvasPaint);
            }
        }
        canvas.restore();
    }


    //是否可以绘制
    private boolean canDraw() {
        return mWidth >= 0 && mHeight >= 0;
    }

}
