package com.north.light.androidutils.viewshot;

import android.graphics.Bitmap;
import android.view.View;

import java.io.Serializable;

/**
 * Created by lzt
 * time 2020/10/27
 * 描述：控件工具类
 */
public class ViewShotUtils implements Serializable {

    /**
     * 对View进行截图
     */
    public interface ViewSnapListener {
        void success(Bitmap bitmap);

        void failed();
    }

    public static void viewSnapshot(View view, ViewSnapListener listener) {
        //使控件可以进行缓存
        view.setDrawingCacheEnabled(true);
        //获取缓存的 Bitmap
        Bitmap drawingCache = view.getDrawingCache();
        //复制获取的 Bitmap
        drawingCache = Bitmap.createBitmap(drawingCache);
        //关闭视图的缓存
        view.setDrawingCacheEnabled(false);
        if (drawingCache != null) {
            if (listener != null) {
                listener.success(drawingCache);
            }
        } else {
            if (listener != null) {
                listener.failed();
            }
        }
    }
}
