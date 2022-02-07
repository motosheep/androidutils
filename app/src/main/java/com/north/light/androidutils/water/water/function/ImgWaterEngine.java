package com.north.light.androidutils.water.water.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;

import com.north.light.androidutils.water.water.ImgWaterUtils;
import com.north.light.androidutils.water.water.params.ImgWaterOrg;
import com.north.light.androidutils.water.water.params.ImgWaterSize;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: lzt
 * @Date: 2022/2/7 9:09
 * @Description:图片水印引擎
 */
public class ImgWaterEngine {
    //操作对象集合
    private List<ImgWaterStreamProvider> providerList = new ArrayList<>();
    //是否停止操作标识
    private AtomicBoolean stopTag = new AtomicBoolean(false);
    //context
    private Context context;


    //内部调用-----------------------------------------------------------------

    /**
     * 生成水印图片
     */
    private Bitmap createWaterBitmap(Bitmap src, Bitmap watermark, ImgWaterSize imgWaterSize, ImgWaterOrg imgWaterOrg) {
        int w = src.getWidth();
        int h = src.getHeight();
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        Canvas cv = new Canvas(newb);
        //draw src into
        cv.drawBitmap(src, 0, 0, p);//在 0，0坐标开始画入src
        //draw watermark into
        Bitmap trainRes = imageScale(watermark, (int) (w * imgWaterSize.getWidthPercent()), (int) (w * imgWaterSize.getHeightPercent()));
        int ww = trainRes.getWidth();
        int wh = trainRes.getHeight();
        //间距参数
        float padLeft = w * imgWaterSize.getPadLeftPercent();
        float padRight = w * imgWaterSize.getPadRightPercent();
        float padTop = h * imgWaterSize.getPadTopPercent();
        float padBottom = h * imgWaterSize.getPadBottomPercent();
        switch (imgWaterOrg) {
            case LEFT_TOP:
                cv.drawBitmap(trainRes, padLeft, padTop, p);
                break;
            case RIGHT_TOP:
                cv.drawBitmap(trainRes, w - ww - padRight, padTop, p);
                break;
            case LEFT_BOTTOM:
                cv.drawBitmap(trainRes, padLeft, h - padBottom - wh, p);
                break;
            case RIGHT_BOTTOM:
                cv.drawBitmap(trainRes, w - ww - padRight, h - padBottom - wh, p);
                break;
            case CENTER_VERTICAL_HORIZONTAL:
                cv.drawBitmap(trainRes, w / 2 - ww / 2, h / 2 - wh / 2, p);
                break;
        }
        //save all clip
        cv.save();
        cv.restore();
        if (!src.isRecycled()) {
            src.recycle();
        }
        if (watermark != null && !watermark.isRecycled()) {
            watermark.recycle();
        }
        if (!trainRes.isRecycled()) {
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


    //外部调用-----------------------------------------------------------------

    /**
     * 启动水印
     */
    public List<String> start(ImgWaterUtils.Builder builder) throws Exception {
        stopTag.set(false);
        if (builder.getStreamProvider() == null || builder.getStreamProvider().size() == 0
                || builder.getContext() == null) {
            return new ArrayList<>();
        }
        this.context = builder.getContext();
        providerList.clear();
        providerList.addAll(builder.getStreamProvider());
        //开始处理
        List<String> result = new ArrayList();
        for (int i = 0; i < providerList.size(); i++) {
            //有停止标识了
            ImgWaterStreamProvider waterStreamProvider = providerList.get(i);
            Bitmap inputBitmap = waterStreamProvider.inputBitmap();
            Bitmap targetBitmap = waterStreamProvider.targetBitmap();
            if (stopTag.get()) {
                for (int j = i; j < providerList.size(); j++) {
                    inputBitmap.recycle();
                    targetBitmap.recycle();
                }
                //释放所有数据，然后返回空值
                return new ArrayList<>();
            }
            //继续做水印操作
            Bitmap trainBitmap = createWaterBitmap(inputBitmap, targetBitmap,
                    waterStreamProvider.size(), waterStreamProvider.org());
            //保存图片到本地
            String rootPath = waterStreamProvider.outputPath();
            if (TextUtils.isEmpty(rootPath)) {
                rootPath = ImgWaterConstant.getDefaultOutputPath(this.context);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            trainBitmap.compress(Bitmap.CompressFormat.JPEG, waterStreamProvider.size().getCompressRate(), out);
            //写入文件
            String fileName = System.currentTimeMillis() + ".jpg";
            String finalPath = rootPath + "/" + fileName;
            FileOutputStream fos = new FileOutputStream(finalPath);
            fos.write(out.toByteArray());
            fos.flush();
            fos.close();
            out.close();
            //回收bitmap
            if (!trainBitmap.isRecycled()) {
                trainBitmap.recycle();
            }
            //设置路径
            result.add(finalPath);
        }
        return result;
    }


    /**
     * 停止水印
     */
    public void close() {
        stopTag.set(true);
    }
}
