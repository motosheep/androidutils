package com.north.light.libble.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:li
 * date:2021/8/28
 * desc:蓝牙线程管理
 */
public class BLEThreadManager {
    private ExecutorService mCacheThread = Executors.newCachedThreadPool();

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

    }

    public ExecutorService getCacheHandler() {
        return mCacheThread;
    }


}
