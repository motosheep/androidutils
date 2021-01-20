package com.north.light.androidutils.colordraw;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by lzt
 * time 2021/1/20
 * 描述：
 */
public class SadDrawUtils {
    private static class SingleHolder {
        static SadDrawUtils mInstance = new SadDrawUtils();
    }

    public static SadDrawUtils getInstance() {
        return SadDrawUtils.SingleHolder.mInstance;
    }

    /**
     * 设置哀悼主题
     */
    public void setSadTheme(Activity activity) {
        try {
            WeakReference<Activity> weakReference = new WeakReference<>(activity);
            View view = weakReference.get().getWindow().getDecorView();
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            view.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        } catch (Exception e) {

        }
    }
}
