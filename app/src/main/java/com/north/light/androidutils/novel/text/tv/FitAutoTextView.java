package com.north.light.androidutils.novel.text.tv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.north.light.androidutils.R;

/**
 * @Author: lzt
 * @Date: 2022/2/7 14:07
 * @Description:自适应textview
 */
public class FitAutoTextView extends View {
    //画笔
    private Paint mTextPaint;
    //测量的字体
    private String mSPText = "啊";
    //默认的间距
    private int mWidthInterval = 10;
    private int mHeightInterval = 10;
    //显示的字体
    private String mContent;
    //宽--一行绘制多少个字体
    private int mWidthSize;
    //高--一列绘制多少个字体
    private int mHeightSize;
    //宽--一个字体的宽度
    private int mFontWidth;
    private int mFontHeight;
    //默认字体大小
    private int mDefaultSize = 64;
    //padding
    private int padLeft;
    private int padRight;
    private int padTop;
    private int padBottom;
    //监听
    private FitAutoTextListener mListener;
    //剩余宽度填充
    private int mRestWidth = 0;

    public FitAutoTextView(Context context) {
        super(context);
        init();
    }

    public FitAutoTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FitAutoTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(getResources().getColor(R.color.color_F06091));
        mTextPaint.setTextSize(mDefaultSize);
        setTextSize(mDefaultSize, mWidthInterval, mHeightInterval);
    }

    /**
     * 获取字体高度
     */
    private int[] getTextWH(String temp) {
        if (mTextPaint == null) {
            return new int[]{0, 0};
        }
        Rect rect = new Rect();
        mTextPaint.getTextBounds(temp, 0, temp.length(), rect);
        int height = rect.height();
        int width = rect.width();
        return new int[]{width, height};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mContent)) {
            //内容不为空，绘制文字
            drawContent(canvas, mContent);
        }
    }

    /**
     * 绘制文字内容
     */
    private void drawContent(Canvas canvas, String mContent) {
        int length = mContent.length();
        if (length <= 0) {
            return;
        }
        if (mWidthSize <= 0) {
            return;
        }
        //中间变量-----------------------
        int widthCacheSize = mWidthSize;
        int heightCacheSize = mHeightSize;
        int fontCacheWidth = mFontWidth;
        int padLeftCache = padLeft;
        int padTopCache = padTop;
        int fontCacheHeight = mFontHeight;
        int heightCacheInterval = mHeightInterval;
        //开始绘制------------------
        canvas.save();
        int txDrawCounter = 0;
        for (int h = 0; h < heightCacheSize; h++) {
            if (txDrawCounter > mContent.length() - 1) {
                break;
            }
            for (int w = 0; w < widthCacheSize; w++) {
                if (txDrawCounter > mContent.length() - 1) {
                    break;
                }
                //绘制
                String txInfo = mContent.substring(txDrawCounter, txDrawCounter + 1);
                txDrawCounter++;
                if (txInfo.equals("\n")) {
                    //换行
                    break;
                } else {
                    //绘制
                    int startX = w * fontCacheWidth + fontCacheWidth / 2 + padLeftCache + mRestWidth / 2;
                    int startY = (h + 1) * fontCacheHeight + padTopCache - (heightCacheInterval / 2);
                    canvas.drawText(txInfo, startX, startY, mTextPaint);
                }
            }
        }
        if (mListener != null) {
            mListener.trueDrawCount(txDrawCounter);
        }
        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mListener = null;
    }

    //外部调用---------------------------------------------------------------------------------------

    /**
     * 设置监听
     */
    public void setOnTextListener(FitAutoTextListener listener) {
        mListener = listener;
    }

    /**
     * 设置字体颜色
     */
    public void setTextColor(int color) {
        if (mTextPaint == null) {
            return;
        }
        mTextPaint.setColor(getResources().getColor(color));
        postInvalidate();
    }

    /**
     * 设置字体大小--返回满屏可绘制多少字数
     */
    public void setTextSize(int size, int widthInterval, int heightInterval) {
        if (mTextPaint == null || size <= 0 || widthInterval <= 0) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                mWidthInterval = widthInterval;
                mHeightInterval = heightInterval;
                mDefaultSize = size;
                mTextPaint.setTextSize(mDefaultSize);
                int[] textSize = getTextWH(mSPText);
                mFontWidth = textSize[0] + mWidthInterval;
                mFontHeight = textSize[1] + mHeightInterval;
                //计算出能绘制多少个字体
                padLeft = getPaddingLeft();
                padRight = getPaddingRight();
                padTop = getPaddingTop();
                padBottom = getPaddingBottom();
                int measureWidth = getMeasuredWidth() - padLeft - padRight;
                int measureHeight = getMeasuredHeight() - padTop - padBottom;
                mWidthSize = measureWidth / mFontWidth;
                mRestWidth = measureWidth % mFontWidth;
                mHeightSize = measureHeight / mFontHeight;
                if (mListener != null) {
                    mListener.maxDrawCount(mWidthSize * mHeightSize);
                }
                postInvalidate();
            }
        });
    }

    /**
     * 设置宽间距
     */
    public void setTextSize(int size) {
        setTextSize(size, mWidthInterval, mHeightInterval);
    }

    /**
     * 设置宽间距
     */
    public void setWidthInterval(int widthInterval) {
        setTextSize(mDefaultSize, widthInterval, mHeightInterval);
    }

    /**
     * 设置列间距
     */
    public void setHeightInterval(int heightInterval) {
        setTextSize(mDefaultSize, mWidthInterval, heightInterval);
    }


    /**
     * 设置字体
     */
    public void setTextView(String content) {
        mContent = content;
        postInvalidate();
    }

}
