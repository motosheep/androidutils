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
    protected FitAutoTextView preTxView;
    protected FitAutoTextView curTxView;
    protected FitAutoTextView nextTxView;
    protected FitAutoTextView showTxView;
    private TextStatusListener mListener;
    //count缓存
    private int mMaxDrawCountCache = -1;
    private int mTrueDrawCountCache = -1;

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

    public abstract FitAutoTextView getPreTxView();

    public abstract FitAutoTextView getCurTxView();

    public abstract FitAutoTextView getNextTxView();

    public abstract FitAutoTextView getShowTxView();


    protected void init() {
        //初始化view
        preTxView = getPreTxView();
        curTxView = getCurTxView();
        nextTxView = getNextTxView();
        showTxView = getShowTxView();
        addView(preTxView);
        addView(curTxView);
        addView(nextTxView);
        addView(showTxView);
        RelativeLayout.LayoutParams preParams = (LayoutParams) preTxView.getLayoutParams();
        RelativeLayout.LayoutParams curParams = (LayoutParams) curTxView.getLayoutParams();
        RelativeLayout.LayoutParams nextParams = (LayoutParams) nextTxView.getLayoutParams();
        RelativeLayout.LayoutParams showParams = (LayoutParams) showTxView.getLayoutParams();
        preParams.width = LayoutParams.MATCH_PARENT;
        preParams.height = LayoutParams.MATCH_PARENT;
        preTxView.setLayoutParams(preParams);

        curParams.width = LayoutParams.MATCH_PARENT;
        curParams.height = LayoutParams.MATCH_PARENT;
        curTxView.setLayoutParams(curParams);

        nextParams.width = LayoutParams.MATCH_PARENT;
        nextParams.height = LayoutParams.MATCH_PARENT;
        nextTxView.setLayoutParams(nextParams);

        showParams.width = LayoutParams.MATCH_PARENT;
        showParams.height = LayoutParams.MATCH_PARENT;
        showTxView.setLayoutParams(showParams);

        preTxView.setVisibility(View.INVISIBLE);
        curTxView.setVisibility(View.INVISIBLE);
        nextTxView.setVisibility(View.INVISIBLE);


        curTxView.setOnTextListener(new FitAutoTextListener() {
            @Override
            public void trueDrawCount(int count) {
                //实际绘制数量
                if (mListener != null && mTrueDrawCountCache != count) {
                    //防止重复回调
                    mTrueDrawCountCache = count;
                    mListener.trueDraw(count);
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
     * 获取页面的bitmap
     *
     * @param pos -1上一页 0当前页 1下一页
     */
    public void getViewBitmap(int pos, ReaderViewShotUtils.ViewSnapListener listener) {
        switch (pos) {
            case -1:
                ReaderViewShotUtils.viewSnapshot(preTxView, listener);
                break;
            case 0:
                ReaderViewShotUtils.viewSnapshot(curTxView, listener);
                break;
            case 1:
                ReaderViewShotUtils.viewSnapshot(nextTxView, listener);
                break;
        }
    }

    /**
     * 设置显示数据
     *
     * @param type -1上一页 0当前页 1下一页
     */
    public void setTxContent(int type, String content) {
        switch (type) {
            case -1:
                preTxView.setTextView(content);
                break;
            case 0:
                curTxView.setTextView(content);
                showTxView.setTextView(content);
                break;
            case 1:
                nextTxView.setTextView(content);
                break;
        }
    }


    //监听
    public interface TextStatusListener {
        void maxDraw(int count);

        void trueDraw(int count);
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
