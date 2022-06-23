package com.north.light.androidutils.external;

import android.content.Context;
import android.os.Environment;

import java.io.Serializable;

/**
 * FileName: StorageManager
 * Author: lzt
 * Date: 2022/4/20 17:07
 * 存储管理工具类
 */
public class MediaPathManager implements Serializable {

    private static class SingleHolder implements Serializable {
        static MediaPathManager mInstance = new MediaPathManager();
    }

    public static MediaPathManager getInstance() {
        return SingleHolder.mInstance;
    }


    /**
     * 获取存储根目录--应用内部
     */
    public String getAppInnerRootPath(Context context) {
        boolean useExternalStorage = false;
        if (context.getApplicationContext().getExternalCacheDir() != null) {
            if (Environment.getExternalStorageState().equals("mounted")) {
                if (Environment.getExternalStorageDirectory().getFreeSpace() > 0) {
                    useExternalStorage = true;
                }
            }
        }
        if (useExternalStorage) {
            return context.getApplicationContext().getExternalCacheDir().getAbsolutePath();
        } else {
            return context.getApplicationContext().getCacheDir().getAbsolutePath();
        }
    }
}
