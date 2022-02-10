package com.north.light.androidutils.novel.text.data.function;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * author:li
 * date:2020/12/13
 * desc:
 */
class TxtExecutorsManager implements Serializable {
    private static final String DEFAULT = "TxtExecutorsManager_DEFAULT";
    //线程池map
    private final ConcurrentMap<String, ExecutorService> mPoolMap = new ConcurrentHashMap<>();

    private static final class SingleHolder {
        static TxtExecutorsManager mInstance = new TxtExecutorsManager();
    }

    public static TxtExecutorsManager getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 获取cache executor
     */
    public ExecutorService getCacheExecutors(String key) {
        String name = key;
        if (TextUtils.isEmpty(key)) {
            name = DEFAULT + "CACHE";
        }
        ExecutorService pool = mPoolMap.get(name);
        if (pool == null) {
            mPoolMap.put(name, Executors.newCachedThreadPool());
        }
        return mPoolMap.get(name);
    }

    /**
     * 关闭cache executor
     */
    public boolean closeCacheExecutors(String key) {
        String name = key;
        if (TextUtils.isEmpty(key)) {
            name = DEFAULT + "CACHE";
        }
        ExecutorService pool = mPoolMap.get(name);
        if (pool == null) {
            return false;
        }
        try {
            // 向学生传达“问题解答完毕后请举手示意！”
            pool.shutdown();
            // 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题
            // (所有的任务都结束的时候，返回TRUE)
            if (!pool.awaitTermination(1, TimeUnit.MILLISECONDS)) {
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            System.out.println("awaitTermination interrupted: " + e);
            pool.shutdownNow();
        }
        mPoolMap.remove(name);
        mPoolMap.put(name, Executors.newCachedThreadPool());
        return true;
    }
}
