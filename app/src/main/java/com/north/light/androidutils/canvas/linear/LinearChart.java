package com.north.light.androidutils.canvas.linear;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.north.light.androidutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/1/24 14:47
 * @Description:线状绘制图
 */
public class LinearChart extends View {
    //画笔
    private Paint mXYPaint;
    private int mXColor = R.color.color_000000;
    private int mYColor = R.color.color_000000;
    private int mBgColor = R.color.color_F06091;
    private Paint mContentPaint;
    //控件宽高相关------------------------------------------
    private int mWidth = -1, mHeight = -1;
    private boolean mAutoFit = true;
    //padding
    private int pLeft, pRight, pTop, pBottom;


    private List<LinearInfo> dataList = new ArrayList<>();

    public LinearChart(Context context) {
        super(context);
        initParams();
    }

    public LinearChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public LinearChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }

    private void initParams() {
        mXYPaint = new Paint();
        mXYPaint.setStyle(Paint.Style.FILL);
        mXYPaint.setStrokeWidth(1);
        mXYPaint.setAntiAlias(true);
        mXYPaint.setDither(true);
        mXYPaint.setTextAlign(Paint.Align.CENTER);

        mContentPaint = new Paint();
        mContentPaint.setStyle(Paint.Style.FILL);
        mContentPaint.setStrokeWidth(1);
        mContentPaint.setAntiAlias(true);
        mContentPaint.setDither(true);
        mContentPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(LinearBuilder builder) {
        if (builder == null) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                mXColor = builder.getXColor();
                mYColor = builder.getYColor();
                mBgColor = builder.getBgColor();
                mBgColor = builder.getBgColor();
                dataList = new ArrayList<>();
                if (builder.getDataList() != null) {
                    dataList.addAll(builder.getDataList());
                }
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        measureInfo();
        drawBg(canvas);
        drawXY(canvas);
        drawContent(canvas);
    }

    private void drawBg(Canvas canvas) {
        if (!canDraw()) {
            return;
        }
        mContentPaint.setColor(getResources().getColor(mBgColor));
        canvas.save();
        RectF rectF = new RectF(pLeft, pTop, mWidth - pRight, mHeight - pBottom);
        canvas.drawRect(rectF, mContentPaint);
        canvas.restore();
    }

    /**
     * 绘制内容
     */
    private void drawContent(Canvas canvas) {
        if (!canDraw()) {
            return;
        }
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        int count = dataList.size();
        //绘制的区域--长度
        int drawWidth = mWidth - pLeft - pRight;
        int drawHeight = mHeight - pTop - pBottom;
        int drawInterval = getContentInterval(drawWidth, count);
        //根据数量，计算出各个数据的宽高
        canvas.save();
        for (int i = 0; i < dataList.size(); i++) {
            LinearInfo info = dataList.get(i);
            float yData = info.getYData();
            float trueHeight = yData / 100f * (drawHeight);
            int startX = (2 * (i + 1) - 1) * drawInterval;
            int entX = startX + drawInterval;
            RectF rectF = new RectF(startX, mHeight - trueHeight, entX, mHeight);
            mContentPaint.setColor(getResources().getColor(info.getColor()));
            canvas.drawRect(rectF, mContentPaint);
        }
        canvas.restore();

    }

    /**
     * 获取内容绘制的间距
     */
    private int getContentInterval(int width, int count) {
        return width / (3 + 2 * (count - 1));
    }

    /**
     * 绘制XY轴
     */
    private void drawXY(Canvas canvas) {
        if (!canDraw()) {
            return;
        }
        canvas.save();
        //绘制X轴
        mXYPaint.setColor(getResources().getColor(mXColor));
        canvas.drawLine(pLeft, mHeight - pBottom, pLeft + mWidth - pRight, mHeight - pBottom, mXYPaint);
        //绘制Y轴
        mXYPaint.setColor(getResources().getColor(mYColor));
        canvas.drawLine(pLeft, pTop, pLeft, pTop + mHeight - pBottom, mXYPaint);
        canvas.restore();
    }

    /**
     * 能否绘制
     */
    private boolean canDraw() {
        return mWidth >= 0 && mHeight >= 0;
    }

    /**
     * 绘制，测量
     */
    private void measureInfo() {
        pLeft = getPaddingLeft();
        pRight = getPaddingRight();
        pTop = getPaddingTop();
        pBottom = getPaddingBottom();
        if (mAutoFit) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
        } else {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = mWidth;
            layoutParams.height = mHeight;
            setLayoutParams(layoutParams);
        }
    }
}
