package com.north.light.androidutils.vibrator;

import android.content.Context;
import android.os.Vibrator;
import androidx.annotation.RequiresPermission;
import android.util.Log;

/**
 * 震动工具类
 * */
public class VibratorUtils {
    private static final String TAG = VibratorUtils.class.getName();
    //震动间隔
    private static Vibrator mVibrator;

    //普通震动
    public static void vibrator(Context context, int mill) {
        try {
            if (context == null) {
                return;
            }
            if (mVibrator == null) {
                mVibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            }
            if (mVibrator != null) {
                mVibrator.vibrate(mill);
            }
        } catch (Exception e) {
            Log.d(TAG, "vibrator error: " + e.getMessage());
        }
    }

    //取消震动
    public static void cancel() {
        if (mVibrator != null) {
            mVibrator.cancel();
        }
    }
}
