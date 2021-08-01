package com.north.light.androidutils.novel.reader;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.north.light.androidutils.R;

import java.util.List;

/**
 * FileName: NovelReader
 * Author: lizhengting
 * Date: 2021/7/31 13:39
 * Description:
 */
public class NovelReader extends RelativeLayout {
    private RelativeLayout mPreTextRV;
    private RelativeLayout mCurTextRV;
    private RelativeLayout mNextTextRV;

    private NovelTouchReader mNovelView;

    /**
     * 具体文字显示控件
     */
    private ReaderTextView mPreTextView;
    private ReaderTextView mCurTextView;
    private ReaderTextView mNextTextView;

    /**
     * 文字测试控件
     */
    private ReaderTextView mTextTestView;


    public NovelReader(Context context) {
        this(context, null);
    }

    public NovelReader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelReader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextTestView = new ReaderTextView(context);
        mPreTextRV = new RelativeLayout(context);
        mCurTextRV = new RelativeLayout(context);
        mNextTextRV = new RelativeLayout(context);
        addView(mTextTestView);
        addView(mNextTextRV);
        addView(mCurTextRV);
        addView(mPreTextRV);
        mNovelView = new NovelTouchReader(context) {
            @Override
            public RelativeLayout getCurTextView() {
                return mCurTextRV;
            }

            @Override
            public RelativeLayout getNextTextView() {
                return mNextTextRV;
            }

            @Override
            public RelativeLayout getPreTextView() {
                return mPreTextRV;
            }
        };
        addView(mNovelView);
        mTextTestView.setVisibility(View.INVISIBLE);
        mPreTextRV.setVisibility(View.INVISIBLE);
        mPreTextRV.setBackgroundColor(Color.WHITE);
        mCurTextRV.setBackgroundColor(Color.WHITE);
        mNextTextRV.setBackgroundColor(Color.WHITE);
        mNovelView.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams parentParams = (LayoutParams) mPreTextRV.getLayoutParams();
        parentParams.width = LayoutParams.MATCH_PARENT;
        parentParams.height = LayoutParams.MATCH_PARENT;
        //match params
        mCurTextRV.setBackgroundColor(Color.YELLOW);
        mPreTextRV.setBackgroundColor(Color.YELLOW);
        mNextTextRV.setBackgroundColor(Color.YELLOW);
        mTextTestView.setLayoutParams(parentParams);
        mPreTextRV.setLayoutParams(parentParams);
        mCurTextRV.setLayoutParams(parentParams);
        mNextTextRV.setLayoutParams(parentParams);
        mNovelView.setLayoutParams(parentParams);
        //add textview
        mPreTextView = new ReaderTextView(context);
        mCurTextView = new ReaderTextView(context);
        mNextTextView = new ReaderTextView(context);
        mPreTextRV.addView(mPreTextView);
        mCurTextRV.addView(mCurTextView);
        mNextTextRV.addView(mNextTextView);
        mTextTestView.setGravity(Gravity.LEFT);
        mPreTextView.setGravity(Gravity.LEFT);
        mCurTextView.setGravity(Gravity.LEFT);
        mNextTextView.setGravity(Gravity.LEFT);
        mPreTextView.setLayoutParams(parentParams);
        mCurTextView.setLayoutParams(parentParams);
        mNextTextView.setLayoutParams(parentParams);
        //事件控制
        mNovelView.setStataChangeListener(new NovelTouchReader.StataChangeListener() {
            @Override
            public void pre() {
                try {
                    updateData(ReadDataManager.getInstance().getPre());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void next() {
                try {
                    updateData(ReadDataManager.getInstance().getNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mTextTestView.setOnTextChangeListener(new ReaderTextView.TextChangeListener() {
            @Override
            public void textSize(int count) {
                try {
                    if (count == 0) {
                        return;
                    }
                    //字体大小--一页能显示多少个字符--初始化数据
                    ReadDataManager.getInstance().updateList(count);
                    updateData(ReadDataManager.getInstance().getCur());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                //设置字体颜色和大小
                setTextSize(30f, Color.BLACK);
            }
        }, 400);
    }

    /**
     * 设置字体大小
     */
    public void setTextSize(float size, int black) {
        mTextTestView.setLineSpacing(20, 1);
        mPreTextView.setLineSpacing(20, 1);
        mCurTextView.setLineSpacing(20, 1);
        mNextTextView.setLineSpacing(20, 1);

        mPreTextView.setTextSize(size);
        mCurTextView.setTextSize(size);
        mNextTextView.setTextSize(size);
        mTextTestView.setTextSize(size);

        mPreTextView.setTextColor(black);
        mCurTextView.setTextColor(black);
        mNextTextView.setTextColor(black);
        mTextTestView.setTextColor(black);

        //在测试布局，测试每页最多显示多少个字符
        mTextTestView.setText(getContext().getResources().getString(R.string.messure_txt_demo));
    }

    /**
     * 更新数据
     */
    private void updateData(List<ReadDataManager.ReadData> data) {
        mPreTextView.setText(data.get(0).getData());
        mCurTextView.setText(data.get(1).getData());
        mNextTextView.setText(data.get(2).getData());
    }
}
