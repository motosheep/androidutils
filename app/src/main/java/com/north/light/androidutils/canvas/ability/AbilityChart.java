package com.north.light.androidutils.canvas.ability;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: AbilityChart
 * Author: lzt
 * Date: 2022/6/22 10:34
 * 能力图表
 */
public class AbilityChart extends View {
    private List<AbilityTxInfo> mData = new ArrayList<>();
    private AbilityXYInfo mXYInfo = new AbilityXYInfo();
    private int mWidth, mHeight;
    private Paint mPaint;

    public AbilityChart(Context context) {
        super(context);
        initParams();
    }

    public AbilityChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public AbilityChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(22);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        measureInfo();
        drawInfo(canvas);
    }

    private void drawInfo(Canvas canvas) {
        if (cantDraw()) {
            return;
        }
        //标题最大宽度
        int maxTitleWid = 0;
        //一个字体的宽度
        int singleTxWid = 0;

        for (int i = 0; i < mData.size(); i++) {
            AbilityTxInfo cacheInfo = mData.get(i);
            float txSize = cacheInfo.getTitleTxSize();
            mPaint.setTextSize(txSize);
            String title = cacheInfo.getTitle();
            int titleCacheWid = getTextWH(title, mPaint)[0];
            if (titleCacheWid > maxTitleWid) {
                maxTitleWid = titleCacheWid;
            }
            if (i == 0) {
                singleTxWid = getSingleTxWH(mPaint)[0];
            }
        }
        //绘制直方图的x,y轴
        canvas.save();

        int xColor = mXYInfo.getxColor();
        int yColor = mXYInfo.getyColor();
        int xSize = mXYInfo.getxSize();
        int ySize = mXYInfo.getySize();
        int xEndColor = mXYInfo.getxEndColor();
        float xEndSize = mXYInfo.getxEndSize();
        String xEndTx = mXYInfo.getxEndDesc();

        mPaint.setTextSize(xEndSize);
        int[] xEndWH = getTextWH(xEndTx, mPaint);
        int xEndTxWid = xEndWH[0];
        int xEndTxHeight = xEndWH[1];

        //Y轴
        mPaint.setColor(getResources().getColor(yColor));
        canvas.drawRect(singleTxWid + maxTitleWid, 0,
                singleTxWid + maxTitleWid + xSize, mHeight - xEndTxHeight * 2, mPaint);

        //X轴
        mPaint.setColor(getResources().getColor(xColor));
        canvas.drawRect(singleTxWid + maxTitleWid + xSize, mHeight - xEndTxHeight * 2,
                mWidth, mHeight - xEndTxHeight * 2 - ySize, mPaint);

        //X轴字体
        mPaint.setColor(getResources().getColor(xEndColor));
        canvas.drawText(xEndTx, mWidth - xEndTxWid, mHeight - xEndTxHeight / 2, mPaint);

        //回到原点
        canvas.translate(0, 0);

        //绘制文字相关信息
        int txHeightInterval = (mHeight - xEndTxHeight * 2 - ySize) / (mData.size());
        for (int i = 0; i < mData.size(); i++) {
            mPaint.setShader(null);

            AbilityTxInfo txInfo = mData.get(i);
            String title = txInfo.getTitle();
            float titleSize = txInfo.getTitleTxSize();
            mPaint.setColor(getResources().getColor(txInfo.getTitleColor()));
            mPaint.setTextSize(titleSize);

            int[] txWH = getTextWH(title, mPaint);
            int titleCacheWid = txWH[0];
            int titleCacheHeight = txWH[1];

            int drawX = maxTitleWid - titleCacheWid / 2;
            int drawY = i * txHeightInterval + titleCacheHeight * 2;

            //文字
            canvas.drawText(title, drawX - singleTxWid, drawY, mPaint);


            //进度
            int startProX = (maxTitleWid + singleTxWid * 2);



            if (txInfo.getProgressSwitch() < txInfo.getProgress()) {
                //绘制进度条
                int restProX = (mWidth - startProX) * txInfo.getProgress() / 100;
                mPaint.setColor(txInfo.getStartColor());
                int startX = startProX;
                int startY = drawY - titleCacheHeight - titleCacheHeight / 2;
                int stopX = startProX + restProX;
                int stopY = drawY + titleCacheHeight - titleCacheHeight / 2;
                mPaint.setShader(new LinearGradient(startX, startY, stopX, stopY,
                        getResources().getColor(txInfo.getStartColor()),
                        getResources().getColor(txInfo.getEndColor()),
                        Shader.TileMode.CLAMP
                ));
                canvas.drawRect(startX, startY, stopX, stopY, mPaint);
            } else {
                //绘制文字
                mPaint.setColor(txInfo.getProgressTxColor());
                mPaint.setTextSize(txInfo.getProgressSize());
                String progressDesc = txInfo.getProgressDesc();
                canvas.drawText(progressDesc, startProX, drawY, mPaint);
            }

        }

        canvas.restore();


    }

    private void measureInfo() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private boolean cantDraw() {
        return mWidth <= 0 || mHeight <= 0 || mData == null || mData.size() == 0;
    }

    /**
     * 获取字体宽高
     */
    private int[] getTextWH(String temp, Paint paint) {
        if (paint == null) {
            return new int[]{0, 0};
        }
        Rect rect = new Rect();
        paint.getTextBounds(temp, 0, temp.length(), rect);
        int height = rect.height();
        int width = rect.width();
        return new int[]{width, height};
    }

    /**
     * 获取一个字体的宽高
     */
    private int[] getSingleTxWH(Paint paint) {
        return getTextWH("字", paint);
    }

    //外部调用方法-------------------------------------------------------------------------------------

    /**
     * 设置数据
     */
    public void setData(List<AbilityTxInfo> data, AbilityXYInfo xyInfo) {
        this.post(new Runnable() {
            @Override
            public void run() {
                mData.clear();
                if (data == null || data.size() == 0) {
                    mXYInfo = new AbilityXYInfo();
                    postInvalidate();
                    return;
                }
                mData.addAll(data);
                mXYInfo = xyInfo;
                postInvalidate();
            }
        });
    }


}
