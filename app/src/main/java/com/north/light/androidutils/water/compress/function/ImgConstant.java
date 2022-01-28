package com.north.light.androidutils.water.compress.function;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @Author: lzt
 * @Date: 2022/1/28 9:17
 * @Description:图片压缩常量
 */
public class ImgConstant {

    /**
     * 压缩比率
     */
    public static final int IMG_COMPRESS_QUALITY_RATE = 20;

    /**
     * 获取默认输入目录
     * 默认未应用cache目录
     * 为空则为sd卡跟目录
     */
    public static String getDefaultOutputPath(Context context) {
        File externalCacheFile = context.getApplicationContext().getExternalCacheDir();
        if (externalCacheFile != null) {
            return externalCacheFile.getAbsolutePath();
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
