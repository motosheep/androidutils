package com.north.light.androidutils.novel.text.data.function;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/2/9 14:48
 * @Description:文件constant
 */
public class TxtConstant implements Serializable {

    /**
     * 文件切割大小
     */
    public static final int SPLIT_SIZE = 1 * 1024 * 1024 * 5;

    /**
     * 文件切割路径
     */
    public static String getDefaultOutputPath(Context context) {
        File externalCacheFile = context.getApplicationContext().getExternalCacheDir();
        if (externalCacheFile != null) {
            return externalCacheFile.getAbsolutePath();
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

}
