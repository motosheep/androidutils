package com.north.light.androidutils.water;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author: lzt
 * @Date: 2022/1/27 8:59
 * @Description:图片压缩
 */
public class ImgCompressUtils implements Serializable {
    private final String MAP_WIDTH = "MAP_WIDTH";
    private final String MAP_HEIGHT = "MAP_HEIGHT";

    private static class SingleHolder implements Serializable {
        static ImgCompressUtils mInstance = new ImgCompressUtils();
    }

    public static ImgCompressUtils getInstance() {
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
    private String qualityMain(Context context, Bitmap source) throws Exception {
        //质量压缩
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.JPEG, 50, out);
        String rootPath = context.getApplicationContext().getExternalCacheDir().getPath();
        //写入文件
        String fileName = System.currentTimeMillis() + ".jpg";
        String path = rootPath + "/" + fileName;
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(out.toByteArray());
        fos.flush();
        fos.close();
        out.close();
        if (!source.isRecycled()) {
            source.recycle();
        }
        return path;
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
                trainBitmap = (Bitmap) t;
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

//    private int computeSize(int srcWidth, int srcHeight) {
//        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
//        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
//        int longSide = Math.max(srcWidth, srcHeight);
//        int shortSide = Math.min(srcWidth, srcHeight);
//        float scale = ((float) shortSide / longSide);
//        if (scale <= 1 && scale > 0.5625) {
//            if (longSide < 1664) {
//                return 1;
//            } else if (longSide < 4990) {
//                return 2;
//            } else if (longSide > 4990 && longSide < 10240) {
//                return 4;
//            } else {
//                return longSide / 1280 == 0 ? 1 : longSide / 1280;
//            }
//        } else if (scale <= 0.5625 && scale > 0.5) {
//            return longSide / 1280 == 0 ? 1 : longSide / 1280;
//        } else {
//            return (int) Math.ceil(longSide / (1280.0 / scale));
//        }
//    }


    //外部调用---------------------------------------------------------------------------------------
    /**
     * 备注：
     * Bitmap.compress方法确实可以压缩图片，但压缩的是存储大小，即你放到disk上的大小
     * 但是其内存占用是没有变化的
     * */

    /**
     * 图片压缩--路径
     */
    public String compress(Context context, String picPath) throws Exception {
        return compress(context, picPath, 0, 0);
    }

    /**
     * 图片压缩--路径
     */
    public String compress(Context context, String picPath, int width, int height) throws Exception {
        //判断是否需要剪裁压缩--指定大小
        Bitmap scaleBitmap = checkSrcAndScale(context, picPath, width, height);
        return qualityMain(context, scaleBitmap);
    }

    /**
     * 图片压缩--res id
     */
    public String compress(Context context, Integer resId) throws Exception {
        return compress(context, resId, 0, 0);
    }

    /**
     * 图片压缩--res id
     */
    public String compress(Context context, Integer resId, int width, int height) throws Exception {
        Bitmap scaleBitmap = checkSrcAndScale(context, resId, width, height);
        return qualityMain(context, scaleBitmap);
    }

    /**
     * 图片压缩--bitmap
     */
    public String compress(Context context, Bitmap bitmap) throws Exception {
        return compress(context, bitmap, 0, 0);
    }

    /**
     * 图片压缩--bitmap
     */
    public String compress(Context context, Bitmap bitmap, int width, int height) throws Exception {
        Bitmap scaleBitmap = checkSrcAndScale(context, bitmap, width, height);
        return qualityMain(context, scaleBitmap);
    }


}
