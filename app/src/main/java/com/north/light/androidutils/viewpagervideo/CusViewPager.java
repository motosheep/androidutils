package com.north.light.androidutils.viewpagervideo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

/**
 * author:li
 * date:2022/3/24
 * desc:自定义viewpager
 */
public class CusViewPager extends ViewPager {
    //当前位置
    private int mCurPos = -1;
    private PageListener pageListener;

    public CusViewPager(@NonNull @NotNull Context context) {
        super(context);
        init();
    }


    public CusViewPager(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mCurPos == -1) {
                    mCurPos = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (pageListener != null) {
                    pageListener.pageChange(mCurPos, position);
                }
                mCurPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    public interface PageListener {
        void pageChange(int oldPos, int newPos);
    }
}
