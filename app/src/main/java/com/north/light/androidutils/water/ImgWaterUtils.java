package com.north.light.androidutils.water;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/1/27 8:51
 * @Description:图片水印工具类
 */
public class ImgWaterUtils implements Serializable {


    private static class SingleHolder implements Serializable {
        static ImgWaterUtils mInstance = new ImgWaterUtils();
    }

    public static ImgWaterUtils getInstance() {
        return SingleHolder.mInstance;
    }


    /**
     * 生成水印图片
     */
    public Bitmap createWaterBitmap(Context context, String path, int resId) throws Exception {
        Bitmap watermark = BitmapFactory.decodeResource(context.getApplicationContext().getResources(), resId);
        Bitmap src = BitmapFactory.decodeFile(path);
        int w = src.getWidth();
        int h = src.getHeight();
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        Canvas cv = new Canvas(newb);
        //draw src into
        cv.drawBitmap(src, 0, 0, p);//在 0，0坐标开始画入src
        //draw watermark into
        //右下角
//        cv.drawBitmap(watermark, w - ww - 10, h - wh - 10, p);
        //右上角
        Bitmap trainRes;
        if (w > h) {
            //宽大于高
            trainRes = imageScale(watermark, w / 10, w / 30);
            int ww = trainRes.getWidth();
            int wh = trainRes.getHeight();
            cv.drawBitmap(trainRes, w - ww - 10, wh + 10, p);
        } else {
            //高大于等于宽
            trainRes = imageScale(watermark, h / 10, h / 30);
            int ww = trainRes.getWidth();
            int wh = trainRes.getHeight();
            cv.drawBitmap(trainRes, w - ww - 10, wh + 10, p);
        }
        //save all clip
        cv.save();
        cv.restore();
        if (src != null && !src.isRecycled()) {
            src.recycle();
        }
        if (watermark != null && !watermark.isRecycled()) {
            watermark.recycle();
        }
        if (trainRes != null && !trainRes.isRecycled()) {
            trainRes.recycle();
        }
        return newb;
    }





    /**
     * 调整图片大小
     *
     * @param bitmap 源
     * @param dst_w  输出宽度
     * @param dst_h  输出高度
     * @return
     */
    private Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        try {
            int src_w = bitmap.getWidth();
            int src_h = bitmap.getHeight();
            float scale_w = ((float) dst_w) / src_w;
            float scale_h = ((float) dst_h) / src_h;
            Matrix matrix = new Matrix();
            matrix.postScale(scale_w, scale_h);
            Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                    true);
            return dstbmp;
        } catch (Exception e) {
            return bitmap;
        }
    }
}
