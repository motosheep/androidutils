package com.north.light.androidutils.download;

import android.content.Context;

import com.north.light.androidutils.persistence.MMKVManager;

import java.io.Serializable;

/**
 * author:li
 * date:2020/12/13
 * desc:下载缓存管理
 */
public class DownloadCacheManager implements Serializable {
    private Context mContext ;

    private static final class SingleHolder {
        static DownloadCacheManager mInstance = new DownloadCacheManager();
    }

    public static DownloadCacheManager getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 初始化
     * */
    public void init(Context context){
        this.mContext = context.getApplicationContext();
        MMKVManager.getInstance().init(mContext);
    }

    /**
     * 设置进度
     * */
    public void setProgress(String key,long pro){
        MMKVManager.getInstance().put(key,pro);
    }

    /**
     * 获取进度
     * */
    public Long getProgress(String key){
        return MMKVManager.getInstance().getLong(key);
    }
}
