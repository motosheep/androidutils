package com.north.light.androidutils.tablayout;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzt
 * time 2020/8/20
 * 描述：自定义tab layout
 * change by lzt 20201031 增加是否设置默认选中的标识
 */
public class CusTabLayout extends TabLayout {
    private List<TextView> mTextViewList = new ArrayList<>();
    //原本大小
    private float orgSize = 12f;
    //修改后的大小
    private float targetSize = 18f;
    //是否选中字体
    private boolean mSelDefaultFont = true;


    public CusTabLayout(Context context) {
        this(context, null);
    }

    public CusTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                //选中状态
                changeTextSize(1, tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {
                //没选中状态
                changeTextSize(2, tab.getPosition());
            }

            @Override
            public void onTabReselected(Tab tab) {
                changeTextSize(1, tab.getPosition());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            try {
                if (mSelDefaultFont) {
                    if (getSelectedTabPosition() == 0) {
                        //默认选中了第一个__则修改颜色
                        changeTextSize(1, 0);
                    }
                } else {
                    mTextViewList.get(0).setTextSize(orgSize);
                }
            } catch (Exception e) {
                Log.d("CusTabLayout", "onWindowFocusChanged failed: " + e.getMessage());
            }
        }
    }

    public void setSelDefaultFont(boolean mSelDefaultFont) {
        this.mSelDefaultFont = mSelDefaultFont;
    }

    /**
     * 选中or取消选中
     *
     * @param type 1选中 2取消选中
     * @param pos  位置
     */
    private void changeTextSize(int type, int pos) {
        try {
            if (type == 1) {
                mTextViewList.get(pos).setTextSize(targetSize);
            } else if (type == 2) {
                mTextViewList.get(pos).setTextSize(orgSize);
            }
        } catch (Exception e) {
            Log.d("CusTabLayout", "changeTextSize failed: " + e.getMessage());
        }
    }

    /**
     * 添加textview集合
     */
    public void addTxView(TextView textView, float orgSize, float targetSize) {
        this.orgSize = orgSize;
        this.targetSize = targetSize;
        mTextViewList.add(textView);
    }

    /**
     * 清空所有数据
     */
    public void clearView() {
        removeAllTabs();
        this.mTextViewList.clear();
    }

    /**
     * 获取textview集合
     */
    public List<TextView> getTextViewList() {
        return mTextViewList;
    }

    @Override
    protected void onDetachedFromWindow() {
        mTextViewList.clear();
        super.onDetachedFromWindow();
    }
}
