package com.north.light.androidutils.time;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.annotation.NonNull;


/**
 * create by lzt
 * data 2019/12/21
 * 验证码倒计时
 */
public class CodeTimer extends androidx.appcompat.widget.AppCompatTextView {
    private final int Count = 60;
    private int mCountCul = 0;
    private Handler mCounterHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what <= 60) {
                CodeTimer.this.setText(((60 - mCountCul) < 0 ? 0 : (60 - mCountCul)) + "秒后重发");
                mCounterHandler.sendEmptyMessageDelayed(mCountCul++, 1000);
            } else {
                mCounterHandler.removeCallbacksAndMessages(null);
                CodeTimer.this.setText("发送验证码");
                CodeTimer.this.setEnabled(true);
            }
        }
    };

    public CodeTimer(Context context) {
        this(context, null);
    }

    public CodeTimer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        mCounterHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    private void init() {

    }

    //开始计时
    public void start() {
        this.setEnabled(false);
        mCountCul = 0;
        mCounterHandler.sendEmptyMessage(mCountCul);
    }
}
