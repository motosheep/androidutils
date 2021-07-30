package com.north.light.libshare.contentprovider;


import android.content.Context;

import java.io.Serializable;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/30 16:48
 * @Version: 1.0
 * @Description: share lib application context
 */
public class ShareContext implements Serializable {
    private volatile Context mContext;

    private static final class SingleHolder {
        static final ShareContext mInstance = new ShareContext();
    }

    public static ShareContext getInstance() {
        return ShareContext.SingleHolder.mInstance;
    }

    /**
     * 获取content
     */
    public Context getContext() {
        if (mContext == null) {
            mContext = ShareContentProvider.getAppContent();
        }
        return mContext;
    }
}
