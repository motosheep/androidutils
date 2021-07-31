package com.north.light.androidutils.novel.utils;

import android.graphics.Bitmap;
import android.view.View;

import java.io.Serializable;

/**
 * Created by lzt
 * time 2020/10/27
 * 描述：控件工具类
 */
public class NovelViewShotUtils implements Serializable {


    public static Bitmap viewSnapshot(View view) {
        //使控件可以进行缓存
        view.setDrawingCacheEnabled(true);
        //获取缓存的 Bitmap
        Bitmap drawingCache = view.getDrawingCache();
        //复制获取的 Bitmap
        drawingCache = Bitmap.createBitmap(drawingCache);
        //关闭视图的缓存
        view.setDrawingCacheEnabled(false);
        return drawingCache;
    }
}
