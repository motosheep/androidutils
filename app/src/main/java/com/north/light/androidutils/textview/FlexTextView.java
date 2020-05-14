package com.north.light.androidutils.textview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.north.light.androidutils.R;


/**
 * Created by lzt
 * on 2020/2/27
 * 可伸缩的文字显示控件
 */

public class FlexTextView extends LinearLayout {
    private static final String TAG = FlexTextView.class.getName();
    private TextView mContentView;//内容的textView
    private TextView mFlexView;//伸缩的按钮
    private boolean isFlex;//是否折叠标记
    private int FLEX_LINE = 3;//显示伸缩按钮的行数限制

    public FlexTextView(Context context) {
        this(context, null);
    }

    public FlexTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关属性
     */
    private void init() {
        //竖直方向
        setOrientation(LinearLayout.VERTICAL);
        //初始化view
        //内容
        mContentView = new TextView(getContext());
        mContentView.setTextSize(12);
        mContentView.setTextColor(Color.BLACK);
        //flex按钮
        mFlexView = new TextView(getContext());
        mFlexView.setTextSize(12);
        mFlexView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
        //大小设置
        addView(mContentView);
        addView(mFlexView);

        LayoutParams contentParams = (LayoutParams) mContentView.getLayoutParams();
        contentParams.width = LayoutParams.MATCH_PARENT;
        contentParams.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(contentParams);
        mContentView.setGravity(Gravity.CENTER | Gravity.START);

        LayoutParams flexParams = (LayoutParams) mFlexView.getLayoutParams();
        flexParams.width = LayoutParams.MATCH_PARENT;
        flexParams.height = LayoutParams.WRAP_CONTENT;
        mFlexView.setLayoutParams(flexParams);
        mFlexView.setGravity(Gravity.CENTER);

        flex(true);
        //默认伸缩按钮不显示
        mFlexView.setVisibility(GONE);
        //监听事件
        mFlexView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flex(!isFlex);
            }
        });
    }

    /**
     * 设置显示伸缩按钮的行数设置
     */
    public void setFlexSwtich(int count) {
        this.FLEX_LINE = count;
    }

    /**
     * 设置显示字体的大小
     */
    public void setTextSize(int size) {
        if (mContentView != null) {
            mContentView.setTextSize(size);
        }
    }

    /**
     * 设置显示字体的颜色
     */
    public void setTextColor(int color) {
        if (mContentView != null) {
            mContentView.setTextColor(color);
        }
    }

    /**
     * 设置按钮字体的大小
     */
    public void setFlexTextSize(int size) {
        if (mFlexView != null) {
            mFlexView.setTextSize(size);
        }
    }

    /**
     * 设置按钮字体的颜色
     */
    public void setFlexTextColor(int color) {
        if (mFlexView != null) {
            mFlexView.setTextColor(color);
        }
    }

    /**
     * 设置要显示的文字
     * 若文字为空，显示按钮隐藏
     * 若文字不为空，显示按钮出现
     */
    public void setText(String content) {
        if (TextUtils.isEmpty(content)) {
            mFlexView.setVisibility(GONE);
            mContentView.setText("");
        } else {
            mContentView.setText(content);
            mFlexView.setVisibility(VISIBLE);
            mFlexView.setText("全文");
            //统计行数，用来确定是否显示伸缩按钮
            mContentView.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "内容行数: " + mContentView.getLineCount());
                    if (mContentView.getLineCount() < FLEX_LINE) {
                        mFlexView.setVisibility(GONE);
                    } else {
                        mFlexView.setVisibility(VISIBLE);
                    }
                }
            });
        }
    }

    /**
     * 伸缩，折叠函数
     */
    private void flex(boolean isFlex) {
        this.isFlex = isFlex;
        if (isFlex) {
            mContentView.setMaxLines(FLEX_LINE);
            mContentView.requestLayout();
            mFlexView.setText("全文");
        } else {
            mContentView.setMaxLines(Integer.MAX_VALUE);
            mContentView.requestLayout();
            mFlexView.setText("收起");
        }
    }
}
