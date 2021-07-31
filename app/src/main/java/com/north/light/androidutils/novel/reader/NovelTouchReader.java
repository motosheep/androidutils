package com.north.light.androidutils.novel.reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.north.light.androidutils.novel.utils.LogUtils;
import com.north.light.androidutils.novel.utils.NovelViewShotUtils;

/**
 * FileName: NovelReader
 * Author: lizhengting
 * Date: 2021/7/31 12:51
 * Description:小说reader
 */
public abstract class NovelTouchReader extends RelativeLayout {
    private static final String TAG = NovelTouchReader.class.getSimpleName();
    /**
     * 监听
     */
    private StataChangeListener mListener;
    /**
     * 浏览模式
     */
    private NovelReaderMode mMode = NovelReaderMode.MODE_HORIZONTAL;
    /**
     * 触摸时的位置
     */
    private static float DATA_DOWN_X = 0;
    private static float DATA_DOWN_Y = 0;
    private static float DATA_MOVE_X = 0;
    private static float DATA_MOVE_Y = 0;
    private static float DATA_UP_X = 0;
    private static float DATA_UP_Y = 0;
    /**
     * 开始绘制时的位置
     */
    private static float DATA_HORIZONTAL_START_X = 0;
    private static float DATA_HORIZONTAL_START_Y = 0;
    private static float DATA_HORIZONTAL_RIGHT_INTERVAL_X = 0;
    /**
     * 是否绘制中的标识
     */
    private boolean isDrawing = false;
    /**
     * 绘制bitmap paint
     */
    private static Paint mBitmapPaint;
    /**
     * 滑动方向标识：1左滑 2右滑
     */
    private int mSlideTAG = 1;
    /**
     * 绘制时，当前bitmap
     */
    private static Bitmap mCurBitmap;
    /**
     * 绘制时，底部bitmap
     */
    private static Bitmap mBottomBitmap;
    /**
     * 水平绘制rect
     */
    private static Rect mSrcRect;
    private static Rect mDestRect;
    /**
     * 是否绘制动画中--绘制动画中停止接收触摸事件
     */
    private boolean isDrawAnim = false;

    public NovelTouchReader(Context context) {
        super(context);
        init();
    }

    public NovelTouchReader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NovelTouchReader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public abstract RelativeLayout getCurTextView();

    public abstract RelativeLayout getNextTextView();

    public abstract RelativeLayout getPreTextView();

    /**
     * 设置绘制标识
     */
    private void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    /**
     * 重置变量
     */
    private void resetValue() {
        mSlideTAG = 0;
        DATA_HORIZONTAL_START_X = 0;
        DATA_HORIZONTAL_START_Y = 0;
    }

    /**
     * 开始绘制翻页动画
     *
     * @param canvas
     */
    public void drawing(Canvas canvas) {
        if (!isDrawing) {
            //判断当前是否进行上下页的切换
            LogUtils.d(TAG, "x滑动距离：" + (Math.abs(DATA_UP_X - DATA_DOWN_X)));
            drawAfterFinish(canvas);
            return;
        }
        //判断滑动方向
        if (mSlideTAG == 0) {
            if (DATA_DOWN_X >= DATA_MOVE_X) {
                mSlideTAG = 1;
            } else {
                mSlideTAG = 2;
            }
        }
        if (DATA_HORIZONTAL_START_X == 0) {
            DATA_HORIZONTAL_START_X = DATA_MOVE_X;
            DATA_HORIZONTAL_RIGHT_INTERVAL_X = getWidth() - DATA_HORIZONTAL_START_X;
        }
        if (DATA_HORIZONTAL_START_Y == 0) {
            DATA_HORIZONTAL_START_Y = DATA_MOVE_Y;
        }
        switch (mMode) {
            case MODE_REAL:
                drawReal(canvas);
                break;
            case MODE_VERTICAL:
                drawVertical(canvas);
                break;
            case MODE_HORIZONTAL:
                drawHorizontal(canvas);
                break;
        }
    }


    /**
     * 手指离开后绘制逻辑
     *
     * @param canvas
     */
    private void drawAfterFinish(Canvas canvas) {
        switch (mMode) {
            case MODE_REAL:
                break;
            case MODE_VERTICAL:
                break;
            case MODE_HORIZONTAL:
                drawHorizontalFinish(canvas);
                break;
        }
    }

    /**
     * 水平滑动完成动画
     */
    private void drawHorizontalFinish(Canvas canvas) {
        int interval = (int) Math.abs(DATA_DOWN_X - DATA_UP_X);
        //如果滑动距离大于宽度的1/3,才进行回调等操作
        if (interval < getWidth() / 3) {
            return;
        }
        if (mSlideTAG == 2) {
            if (mListener != null) {
                mListener.pre();
            }
            resetValue();
        } else if (mSlideTAG == 1) {
            isDrawAnim = true;
            if (DATA_UP_X + DATA_HORIZONTAL_RIGHT_INTERVAL_X < 0) {
                if (mListener != null) {
                    mListener.next();
                }
                isDrawAnim = false;
                resetValue();
                return;
            }
            DATA_UP_X = DATA_UP_X - 30;
            canvas.save();
            createHorizontalBitmap(false);
            canvas.drawBitmap(mBottomBitmap, 0, 0, mBitmapPaint);
            //根据差值，绘制x轴偏移的上传bitmap
            mSrcRect = new Rect((int) (getWidth() - DATA_UP_X + DATA_HORIZONTAL_RIGHT_INTERVAL_X),
                    0, mCurBitmap.getWidth(), mCurBitmap.getHeight());
            mDestRect = new Rect(0, 0, (int) (DATA_UP_X + DATA_HORIZONTAL_RIGHT_INTERVAL_X), mCurBitmap.getHeight());
            canvas.drawBitmap(mCurBitmap, mSrcRect, mDestRect, mBitmapPaint);
            canvas.restore();
            postInvalidateDelayed(2);
        }
    }

    /**
     * 绘制水平
     */
    private void drawVertical(Canvas canvas) {
    }

    /**
     * 仿真
     */
    private void drawReal(Canvas canvas) {
    }

    /**
     * 绘制水平动画
     * 判断左滑还是右滑，左滑才做动画
     */
    private void drawHorizontal(Canvas canvas) {
        try {
            if (mSlideTAG == 2) {
                return;
            }
            if (getCurTextView() == null) {
                return;
            }
            if (getNextTextView() == null) {
                return;
            }
            if (DATA_HORIZONTAL_START_X - DATA_MOVE_X > 0) {
                float interval = DATA_HORIZONTAL_START_X - DATA_MOVE_X;
                //左滑中
                canvas.save();
                createHorizontalBitmap(false);
                canvas.drawBitmap(mBottomBitmap, 0, 0, mBitmapPaint);
                //根据差值，绘制x轴偏移的上传bitmap
                mSrcRect = new Rect((int) interval, 0, mCurBitmap.getWidth(), mCurBitmap.getHeight());
                mDestRect = new Rect(0, 0, (int) (mCurBitmap.getWidth() - interval), mCurBitmap.getHeight());
                canvas.drawBitmap(mCurBitmap, mSrcRect, mDestRect, mBitmapPaint);
                canvas.restore();
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "drawing error:" + e.getMessage());
        }
    }

    /**
     * 创建水平方向的bitmap
     *
     * @param refreshTag 刷新标识
     */
    public void createHorizontalBitmap(boolean refreshTag) {
        if (refreshTag) {
            releaseBitmap();
        }
        if (mCurBitmap == null) {
            mCurBitmap = NovelViewShotUtils.viewSnapshot(getCurTextView());
        }
        if (mBottomBitmap == null) {
            mBottomBitmap = NovelViewShotUtils.viewSnapshot(getNextTextView());
        }
    }

    /**
     * 释放bitmap
     */
    private void releaseBitmap() {
        if (mCurBitmap != null && !mCurBitmap.isRecycled()) {
            mCurBitmap.recycle();
        }
        if (mBottomBitmap != null && !mBottomBitmap.isRecycled()) {
            mBottomBitmap.recycle();
        }
        mCurBitmap = null;
        mBottomBitmap = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        removeStataChangeListener();
        releaseBitmap();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawing(canvas);
    }

    private void init() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        //监听事件
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isDrawAnim) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DATA_DOWN_X = event.getX();
                        DATA_DOWN_Y = event.getY();
                        Log.d("touch", "down X:" + DATA_DOWN_X + "\tdown Y:" + DATA_DOWN_Y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        DATA_MOVE_X = event.getX();
                        DATA_MOVE_Y = event.getY();
                        if (Math.abs(DATA_DOWN_X - DATA_MOVE_X) > 10 || Math.abs(DATA_DOWN_Y - DATA_MOVE_Y) > 10) {
                            Log.d("touch", "move X:" + DATA_MOVE_X + "\tmove Y:" + DATA_MOVE_Y);
                            setDrawing(true);
                            invalidate();
                        } else {
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        DATA_UP_X = event.getX();
                        DATA_UP_Y = event.getY();
                        setDrawing(false);
                        invalidate();
                        Log.d("touch", "up X:" + DATA_UP_X + "\tup Y:" + DATA_UP_Y);
                        break;
                }
                return true;
            }
        });
    }


    //回调------------------------------------------------------------------------------------------
    public interface StataChangeListener {
        public void pre();

        public void next();
    }

    public void setStataChangeListener(StataChangeListener listener) {
        mListener = listener;
    }

    public void removeStataChangeListener() {
        mListener = null;
    }
}
