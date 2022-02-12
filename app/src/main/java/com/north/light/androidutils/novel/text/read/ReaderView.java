package com.north.light.androidutils.novel.text.read;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.north.light.androidutils.novel.text.tv.FitAutoTextView;

/**
 * @Author: lzt
 * @Date: 2022/2/8 10:05
 * @Description:阅读view
 */
public class ReaderView extends ReaderBaseView {
    private static final String TAG = ReaderView.class.getSimpleName();
    //阅读模式
    private ReaderAnimMode mReadMode = ReaderAnimMode.ANIM_HORIZONTAL;
    //文字显示控件
    private FitAutoTextView mCurView;
    private FitAutoTextView mShowView;
    //点击的x,y
    private float mClickPosX, mClickPosY;
    //监听
    private ReadEventListener mListener;

    public ReaderView(Context context) {
        super(context);
    }

    public ReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        mCurView = new FitAutoTextView(getContext());
        mShowView = new FitAutoTextView(getContext());
        super.init();
        initEvent();
    }

    private void initEvent() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mClickPosX = event.getX();
                        mClickPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (event.getX() - mClickPosX < 10 &&
                                event.getY() - mClickPosY < 10) {
                            //视为点击
                            dealClick(mClickPosX, mClickPosY);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return true;
            }
        });
    }

    private void dealClick(float mClickPosX, float mClickPosY) {
        if (getMeasuredHeight() / 2 > mClickPosY) {
            return;
        }
        boolean next = getMeasuredWidth() / 2 < mClickPosX;
        if (next) {
            nextPageData();
        } else {
            prePageData();
        }
    }

    /**
     * 上一页
     */
    public void prePageData() {
        Log.d(TAG, "prePageData");
        if (mListener != null) {
            mListener.change(-1);
        }
    }

    /**
     * 下一页
     */
    public void nextPageData() {
        Log.d(TAG, "nextPageData");
        if (mListener != null) {
            mListener.change(1);
        }
    }

    /**
     * 初始化数据
     */
    public void initData(String content) {
        if (mListener != null) {
            setTxContent(content);
        }
    }

    @Override
    public FitAutoTextView getCurTxView() {
        return mCurView;
    }

    @Override
    public FitAutoTextView getShowTxView() {
        return mShowView;
    }

    @Override
    public void setMode(ReaderAnimMode animMode) {
        mReadMode = animMode;
    }

    @Override
    public ReaderAnimMode getMode() {
        return mReadMode;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    //监听事件---------------------
    public void setReadEventListener(ReadEventListener listener) {
        mListener = listener;
    }

    public interface ReadEventListener {
        void change(int type);
    }
}
