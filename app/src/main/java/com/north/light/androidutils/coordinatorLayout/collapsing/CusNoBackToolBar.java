package com.north.light.androidutils.coordinatorLayout.collapsing;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Field;

/**
 * author:li
 * date:2021/2/21
 * desc:
 */
public class CusNoBackToolBar extends Toolbar {
    public CusNoBackToolBar(Context context) {
        super(context);
    }

    public CusNoBackToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CusNoBackToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            try {
                Class toolbar = getClass().getSuperclass();
                Field field = toolbar.getDeclaredField("mNavButtonView");
                field.setAccessible(true);
                ((ImageButton)field.get(this)).setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG","改变属性错误: " + e);
            }
        }
    }
}
