package com.north.light.androidutils.water.compress.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.north.light.androidutils.water.compress.Compress;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/1/27 8:59
 * @Description:图片压缩
 */
public class ImgCompress implements Serializable {
    private final String MAP_WIDTH = "MAP_WIDTH";
    private final String MAP_HEIGHT = "MAP_HEIGHT";

    private static class SingleHolder implements Serializable {
        static ImgCompress mInstance = new ImgCompress();
    }

    public static ImgCompress getInstance() {
        return SingleHolder.mInstance;
    }

    //内部方法---------------------------------------------------------------------------------------

    /**
     * 获取数据源的宽高
     */
    private <T> HashMap<String, Integer> getSourceWH(Context context, T t) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int orgWidth = 0;
        int orgHeight = 0;
        if (t instanceof Bitmap) {
            orgWidth = ((Bitmap) t).getWidth();
            orgHeight = ((Bitmap) t).getHeight();
        } else if (t instanceof Integer) {
            BitmapFactory.decodeResource(context.getApplicationContext().getResources(), (Integer) t, options);
            orgWidth = options.outWidth;
            orgHeight = options.outHeight;
        } else if (t instanceof String) {
            BitmapFactory.decodeFile((String) t, options);
            orgWidth = options.outWidth;
            orgHeight = options.outHeight;
        }
        HashMap<String, Integer> map = new HashMap();
        map.put(MAP_HEIGHT, orgHeight);
        map.put(MAP_WIDTH, orgWidth);
        return map;
    }

    /**
     * 图片压缩--通用内部调用函数
     */
    private String qualityMain(Context context, Bitmap source, int percent, String root) throws Exception {
        //质量压缩
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.JPEG, percent, out);
        String rootPath;
        if (!TextUtils.isEmpty(root)) {
            rootPath = root;
        } else {
            rootPath = ImgConstant.getDefaultOutputPath(context.getApplicationContext());
        }
        //写入文件
        String fileName = System.currentTimeMillis() + ".jpg";
        String finalPath = rootPath + "/" + fileName;
        FileOutputStream fos = new FileOutputStream(finalPath);
        fos.write(out.toByteArray());
        fos.flush();
        fos.close();
        out.close();
        if (!source.isRecycled()) {
            source.recycle();
        }
        return finalPath;
    }

    /**
     * 检查bitmap是否需要压缩
     */
    private <T> Bitmap checkSrcAndScale(Context context, T t, int width, int height) throws Exception {
        boolean isEmptyWidthHeight = (width == 0) || (height == 0);
        if (t instanceof Bitmap) {
            if ((((Bitmap) t).getWidth() <= width && ((Bitmap) t).getHeight() <= height) || isEmptyWidthHeight) {
                return (Bitmap) t;
            }
        } else if (t instanceof Integer) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getApplicationContext().getResources(), (Integer) t, options);
            if ((options.outWidth <= width && options.outHeight <= height) || isEmptyWidthHeight) {
                return BitmapFactory.decodeResource(context.getApplicationContext().getResources(), (Integer) t);
            }
        } else if (t instanceof String) {
            String orgStr = (String) t;
            if (orgStr.startsWith("http")) {
                throw new Exception("not support type");
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(orgStr, options);
            if ((options.outWidth <= width && options.outHeight <= height) || isEmptyWidthHeight) {
                return BitmapFactory.decodeFile(orgStr);
            }
        } else {
            throw new Exception("not support type");
        }
        //至此，是需要处理剪裁的--------------------------------
        HashMap<String, Integer> whMap = getSourceWH(context, t);
        Integer orgWidth = whMap.get(MAP_WIDTH);
        Integer orgHeight = whMap.get(MAP_HEIGHT);
        if (orgWidth == null || orgHeight == null || orgHeight == 0 || orgWidth == 0) {
            //参数不符合
            throw new Exception("params error");
        }
        //压缩后返回一个bitmap--------
        return scaleBitmap(context, t, width, height);
    }

    /**
     * 缩放bitmap起到压缩作用
     */
    private <T> Bitmap scaleBitmap(Context context, T t, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int orgWidth = 0;
        int orgHeight = 0;
        if (t instanceof Bitmap) {
            orgWidth = ((Bitmap) t).getWidth();
            orgHeight = ((Bitmap) t).getHeight();
            if (width >= orgWidth && height >= orgHeight) {
                return (Bitmap) t;
            }
        } else if (t instanceof Integer) {
            BitmapFactory.decodeResource(context.getApplicationContext().getResources(), (Integer) t, options);
            orgWidth = options.outWidth;
            orgHeight = options.outHeight;
            if (width >= orgWidth && height >= orgHeight) {
                return BitmapFactory.decodeResource(context.getApplicationContext().getResources(), (Integer) t);
            }
        } else if (t instanceof String) {
            BitmapFactory.decodeFile((String) t, options);
            orgWidth = options.outWidth;
            orgHeight = options.outHeight;
            if (width >= orgWidth && height >= orgHeight) {
                return BitmapFactory.decodeFile((String) t);
            }
        }
        //源宽高，目标宽高比对--------------------------------------------------
        //计算缩放比率
        if (orgWidth >= width || orgHeight >= height) {
            int rate = 2;
            if (orgWidth >= width) {
                //实际宽比要求宽大
                rate = orgWidth / width;
            } else if (orgHeight >= height) {
                //实际高比要求高大
                rate = orgHeight / height;
            }
            //需要压缩------------------------
            BitmapFactory.Options targetOptions = new BitmapFactory.Options();
            targetOptions.inSampleSize = rate;
            Bitmap trainBitmap;
            if (t instanceof Bitmap) {
                Bitmap cacheBitmap = (Bitmap) t;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                cacheBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                trainBitmap = ImgTrainUtils.getPicFromBytes(baos.toByteArray(), targetOptions);
                if (!cacheBitmap.isRecycled()) {
                    cacheBitmap.recycle();
                }
            } else if (t instanceof Integer) {
                trainBitmap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(), (Integer) t, targetOptions);
            } else {
                trainBitmap = BitmapFactory.decodeFile((String) t, targetOptions);
            }
            if (rate == 1) {
                //不需要压缩
                return trainBitmap;
            }
            return scaleBitmap(context, trainBitmap, width, height);
        } else {
            return scaleBitmap(context, t, width, height);
        }
    }


    /**
     * 图片压缩--bitmap
     */
    private String compress(Context context, Bitmap bitmap, int width, int height, int percent, String path) throws Exception {
        Bitmap scaleBitmap = checkSrcAndScale(context, bitmap, width, height);
        return qualityMain(context, scaleBitmap, percent, path);
    }


    //外部调用---------------------------------------------------------------------------------------

    /**
     * 传入builder并开始压缩
     */
    public List<String> compress(Compress.Builder builder) throws Exception {
        List<PicStreamProvider> adapters = builder.getStreamProviderList();
        if (adapters == null || adapters.size() == 0) {
            return new ArrayList<>();
        }
        //开始压缩
        List<String> result = new ArrayList<>();
        for (PicStreamProvider provider : adapters) {
            InputStream inputStream = provider.getStream();
            InputStream checkStream = provider.getStream();
            boolean isJpg = ImgFormatChecker.SINGLE.isJPG(checkStream);
            if (!isJpg) {
                //如果不是jpg，则不压缩
                result.add(provider.outputPath());
                continue;
            }
            Bitmap bitmap = ImgTrainUtils.getPicFromBytes(ImgTrainUtils.readStream(inputStream), null);
            int width = provider.width();
            int height = provider.height();
            int rate = provider.qualityRate();
            String outputPath = provider.outputPath();
            if (width < 0 || height < 0 || rate < 0) {
                return new ArrayList<>();
            }
            String compressPath = compress(builder.getContext(), bitmap, width, height, rate, outputPath);
            result.add(compressPath);
            provider.close();
        }
        return result;
    }


}
