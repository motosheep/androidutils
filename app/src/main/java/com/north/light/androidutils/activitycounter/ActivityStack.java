package com.north.light.androidutils.activitycounter;


import android.app.Activity;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by lzt
 * on 2019/12/11
 * <p>
 * activity 栈
 * singleton
 */

public class ActivityStack {
    //保存activity对象
    private ConcurrentMap<String, Activity> mActivityMap = new ConcurrentHashMap<>();
    //栈顶activity存储类__由于权限等各方面问题，直接新建一个集合用于保存activity栈信息，需要在onCreate,onDestroy的地方同时调用
    //key activity类名 value插入的时间
    private ConcurrentMap<String, String> mStackInfo = new ConcurrentHashMap<>();

    private static class SingleHolder {
        static final ActivityStack mInstance = new ActivityStack();
    }

    public static ActivityStack getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 插入--与移除成对调用
     * 传入activity simple name 和 activity object
     */
    public void put(String name, Activity activity) {
        if (TextUtils.isEmpty(name) || activity == null) return;
        mActivityMap.put(name, activity);
        mStackInfo.put(name, String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 移除--与插入成对调用
     * 传入activity simple name
     */
    public void out(String name) {
        if (TextUtils.isEmpty(name)) return;
        mActivityMap.remove(name);
        mStackInfo.remove(name);
    }

    /**
     * 查询栈中的activity
     * 传入activity simple name
     */
    public Activity get(String name) {
        if (TextUtils.isEmpty(name)) return null;
        return mActivityMap.get(name);
    }

    /**
     * 清空所有__finish all
     */
    public void finishAll() {
        for (ConcurrentMap.Entry<String, Activity> cache : mActivityMap.entrySet()) {
            if (cache.getValue() != null) {
                cache.getValue().finish();
            } else {
                mActivityMap.remove(cache.getKey());
            }
        }
    }

    /**
     * 获取当前栈顶activity的simple name
     */
    public synchronized String getTopActivity() {
        if (mStackInfo.size() == 0) return "";
        List<StackInfo> result = new ArrayList<>();
        for (ConcurrentMap.Entry<String, String> cache : mStackInfo.entrySet()) {
            StackInfo info = new StackInfo(cache.getKey(), cache.getValue());
            result.add(info);
        }
        Collections.sort(result, new Comparator<StackInfo>() {
            @Override
            public int compare(StackInfo o1, StackInfo o2) {
                return o1.mTime.compareTo(o2.mTime);
            }
        });
        return result.get(result.size() - 1).mActivityName;
    }

    //转化缓存类
    private class StackInfo {
        private String mActivityName;
        private String mTime;

        public StackInfo(String mActivityName, String mTime) {
            this.mActivityName = mActivityName;
            this.mTime = mTime;
        }
    }

}
