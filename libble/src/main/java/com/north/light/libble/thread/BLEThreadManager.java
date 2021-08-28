package com.north.light.libble.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:li
 * date:2021/8/28
 * desc:蓝牙线程管理
 */
public class BLEThreadManager {
    private ExecutorService mCacheThread = Executors.newCachedThreadPool();
    private Handler mHandler;

    private static class SingleHolder {
        static BLEThreadManager mInstance = new BLEThreadManager();
    }

    public static BLEThreadManager getInstance() {
        return SingleHolder.mInstance;
    }

    public BLEThreadManager() {

    }

    /**
     * 必须在application进行初始化
     */
    public void init() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    public ExecutorService getCachePool() {
        return mCacheThread;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void releaseHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
