package com.north.light.libble.thread;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * author:li
 * date:2021/8/28
 * desc:蓝牙线程管理
 */
public class BLEThreadManager {

    private Handler mCacheHandler;
    private HandlerThread mCacheThread;

    private static class SingleHolder {
        static BLEThreadManager mInstance = new BLEThreadManager();
    }

    public static BLEThreadManager getInstance() {
        return SingleHolder.mInstance;
    }

    public BLEThreadManager() {
        if (mCacheThread == null) {
            mCacheThread = new HandlerThread(BLEThreadManager.class.getSimpleName() + "cache");
            mCacheThread.start();
        }
        if (mCacheHandler == null) {
            mCacheHandler = new Handler(mCacheThread.getLooper());
        }
    }

    /**
     * 必须在application进行初始化
     */
    public void init() {

    }

    public Handler getCacheHandler() {
        return mCacheHandler;
    }

    public void releaseCacheHandler() {
        if (mCacheHandler != null) {
            mCacheHandler.removeCallbacksAndMessages(null);
        }
    }
}
