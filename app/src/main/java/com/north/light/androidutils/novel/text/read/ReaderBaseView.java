package com.north.light.androidutils.novel.text.read;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.north.light.androidutils.novel.text.tv.FitTextListener;
import com.north.light.androidutils.novel.text.tv.FitTextView;

/**
 * @Author: lzt
 * @Date: 2022/2/8 9:46
 * @Description:阅读view
 */
public abstract class ReaderBaseView extends RelativeLayout implements ReaderViewApi {
    protected FitTextView preTxView;
    protected FitTextView curTxView;
    protected FitTextView nextTxView;
    protected FitTextView showTxView;
    private TextStatusListener mListener;
    //count缓存
    private int mDrawCountCache = -1;

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

    public abstract FitTextView getPreTxView();

    public abstract FitTextView getCurTxView();

    public abstract FitTextView getNextTxView();

    public abstract FitTextView getShowTxView();


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

        preTxView.setOnTextListener(new FitTextListener() {
            @Override
            public void drawCount(int count) {

            }
        });
        curTxView.setOnTextListener(new FitTextListener() {
            @Override
            public void drawCount(int count) {
                if (mListener != null && mDrawCountCache != count) {
                    //防止重复回调
                    mDrawCountCache = count;
                    mListener.count(count);
                }
            }
        });
        nextTxView.setOnTextListener(new FitTextListener() {
            @Override
            public void drawCount(int count) {

            }
        });
        showTxView.setOnTextListener(new FitTextListener() {
            @Override
            public void drawCount(int count) {

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
        void count(int count);
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
