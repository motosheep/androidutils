package com.north.light.androidutils.novel.text.read;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.north.light.androidutils.novel.text.tv.FitAutoTextListener;
import com.north.light.androidutils.novel.text.tv.FitAutoTextView;

/**
 * @Author: lzt
 * @Date: 2022/2/8 9:46
 * @Description:阅读view
 */
public abstract class ReaderBaseView extends RelativeLayout implements ReaderViewApi {
    protected FitAutoTextView curTxView;
    protected FitAutoTextView showTxView;
    private TextStatusListener mListener;
    //count缓存
    private int mMaxDrawCountCache = -1;
    private int mTrueDrawCountCurCache = -1;

    public ReaderBaseView(Context context) {
        super(context);
        init();
    }

    public ReaderBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReaderBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public abstract FitAutoTextView getCurTxView();

    public abstract FitAutoTextView getShowTxView();


    protected void init() {
        //初始化view
        curTxView = getCurTxView();
        showTxView = getShowTxView();
        addView(curTxView);
        addView(showTxView);
        RelativeLayout.LayoutParams curParams = (LayoutParams) curTxView.getLayoutParams();
        curParams.width = LayoutParams.MATCH_PARENT;
        curParams.height = LayoutParams.MATCH_PARENT;
        curTxView.setLayoutParams(curParams);
        curTxView.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams showParams = (LayoutParams) showTxView.getLayoutParams();
        showParams.width = LayoutParams.MATCH_PARENT;
        showParams.height = LayoutParams.MATCH_PARENT;
        showTxView.setLayoutParams(curParams);
        showTxView.setVisibility(View.INVISIBLE);


        curTxView.setOnTextListener(new FitAutoTextListener() {
            @Override
            public void trueDrawCount(int count) {
                //实际绘制数量
                if (mListener != null && mTrueDrawCountCurCache != count) {
                    //防止重复回调
                    mTrueDrawCountCurCache = count;
                    mListener.curDraw(count);
                }
            }

            @Override
            public void maxDrawCount(int count) {
                //最大绘制数量
                if (mListener != null && mMaxDrawCountCache != count) {
                    //防止重复回调
                    mMaxDrawCountCache = count;
                    mListener.maxDraw(count);
                }
            }
        });

    }

    /**
     * 设置显示数据
     */
    public void setTxContent(String content) {
        curTxView.setTextView(content);
        showTxView.setTextView(content);
    }


    //监听
    public interface TextStatusListener {
        void maxDraw(int count);

        void curDraw(int count);
    }

    public void setTextStatusListener(TextStatusListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        mListener = null;
        super.onDetachedFromWindow();
    }
}
