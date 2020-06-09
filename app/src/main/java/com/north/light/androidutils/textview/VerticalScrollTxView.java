package com.north.light.androidutils.textview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.north.light.androidutils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lzt
 * time 2020/6/9
 * 描述：竖直滚动文字text view
 */
public class VerticalScrollTxView extends TextSwitcher implements TextSwitcher.ViewFactory {
    private Context mContext;
    //handler
    private Handler mUIHandler;
    //文字大小
    private int mTextSize = 12;
    //文字颜色__默认为0
    private int mTextColor = 0;
    //显示文字的集合
    private List<String> mShowList = new ArrayList<>();
    //handler 标识
    private static final int TAG_START = 0x0001;
    private static final int TAG_STOP = 0x0002;
    //当前滚动的位置
    private int mCurPos = 0;//默认为0
    //滚动标识
    private boolean mScrollTag = true;//默认为滚动
    //滚动时间间隔
    private long mScrollInterval = 3000;

    public VerticalScrollTxView(Context context) {
        this(context, null);
    }

    public VerticalScrollTxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        // 设置转换动画，这里引用系统自带动画
        Animation in = AnimationUtils.loadAnimation(mContext, R.anim.scroll_in);
        Animation out = AnimationUtils.loadAnimation(mContext,R.anim.scroll_out);
        this.setInAnimation(in);
        this.setOutAnimation(out);
        //设置ViewSwitcher.ViewFactory
        this.setFactory(this);
        //线程初始化
        if (mUIHandler == null) {
            mUIHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case TAG_START:
                            //开始
                            if (mScrollTag) {
                                if (mCurPos < mShowList.size()) {
                                    setText(mShowList.get(mCurPos));
                                    mCurPos++;
                                    //轮询下一次
                                    this.sendEmptyMessageDelayed(TAG_START, mScrollInterval);
                                } else {
                                    //循环
                                    mCurPos = 0;
                                    this.sendEmptyMessage(TAG_START);
                                }
                            }
                            break;
                        case TAG_STOP:
                            //停止
                            this.sendEmptyMessageDelayed(TAG_START, mScrollInterval);
                            break;
                    }
                }
            };
        }
    }

    /**
     * 从窗口销毁
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mUIHandler != null) {
            mUIHandler.removeCallbacksAndMessages(null);
        }
        super.onDetachedFromWindow();
    }


    @Override
    public View makeView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(mTextSize);
        if (mTextColor != 0) {
            textView.setTextColor(mTextColor);
        }
        return textView;
    }

    //外部调用函数---------------------------------------------------------------

    /**
     * 设置滚动时间间隔
     */
    public void setScrollInterval(long interval) {
        this.mScrollInterval = interval;
    }

    /**
     * 开始滚动
     *
     * @param start true开始滚动  false停止滚动
     */
    public void scroll(boolean start) {
        this.mScrollTag = start;
        if (mUIHandler != null) {
            if (start) {
                mUIHandler.sendEmptyMessage(TAG_START);
            } else {
                mUIHandler.sendEmptyMessage(TAG_STOP);
            }
        }
    }

    /**
     * 文字集合
     */
    public void setTextList(List<String> txList) {
        this.mShowList = (txList == null ? new ArrayList<String>() : txList);
        if (mUIHandler != null) {
            mUIHandler.removeCallbacksAndMessages(null);
            //重置滚动位置标识
            mCurPos = 0;
            mScrollTag = true;
            mUIHandler.sendEmptyMessage(TAG_START);
        }
    }
}
