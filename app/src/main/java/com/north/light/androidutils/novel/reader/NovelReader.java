package com.north.light.androidutils.novel.reader;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

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


    public NovelReader(Context context) {
        this(context, null);
    }

    public NovelReader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelReader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPreTextRV = new RelativeLayout(context);
        mCurTextRV = new RelativeLayout(context);
        mNextTextRV = new RelativeLayout(context);
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
        addView(mNextTextRV);
        addView(mCurTextRV);
        addView(mPreTextRV);
        addView(mNovelView);
        mPreTextRV.setVisibility(View.INVISIBLE);
        mPreTextRV.setBackgroundColor(Color.WHITE);
        mCurTextRV.setBackgroundColor(Color.WHITE);
        mNextTextRV.setBackgroundColor(Color.WHITE);
        mNovelView.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams parentParams = (LayoutParams) mPreTextRV.getLayoutParams();
        parentParams.width = LayoutParams.MATCH_PARENT;
        parentParams.height = LayoutParams.MATCH_PARENT;
        //match params
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
        mPreTextView.setGravity(Gravity.RIGHT);
        mCurTextView.setGravity(Gravity.RIGHT);
        mNextTextView.setGravity(Gravity.RIGHT);
        mPreTextView.setLayoutParams(parentParams);
        mCurTextView.setLayoutParams(parentParams);
        mNextTextView.setLayoutParams(parentParams);


        try {
            updateData(ReadDataManager.getInstance().getCur());
        } catch (Exception e) {
            e.printStackTrace();
        }


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
    }

    /**
     * 更新数据
     */
    private void updateData(List<ReadDataManager.ReadData> data) {
        mPreTextView.setText(data.get(0).getData());
        mCurTextView.setText(data.get(1).getData());
        mNextTextView.setText(data.get(2).getData());
        mNovelView.createHorizontalBitmap(true);
    }
}
