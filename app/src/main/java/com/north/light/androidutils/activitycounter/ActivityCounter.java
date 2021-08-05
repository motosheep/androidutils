package com.north.light.androidutils.activitycounter;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lzt
 * time 2020/11/27
 * 描述：activity生命周期管理类--count为0时，代表activity在后台
 */
public class ActivityCounter implements Serializable {
    //用于判断应用是否最小化的数量标识
    private AtomicInteger mBackHomeCounter = new AtomicInteger(0);
    //用于判断当前有多少个activity
    private AtomicInteger mActivityCounter = new AtomicInteger(0);

    private static class SingleHolder {
        static ActivityCounter mInstance = new ActivityCounter();
    }

    public static ActivityCounter getInstance() {
        return ActivityCounter.SingleHolder.mInstance;
    }

    //初始化--必须在application中调用
    public void init(Application context) {
        context.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksImpl());
    }

    public int getHomeCounter() {
        return mBackHomeCounter.intValue();
    }

    public int getActivityCounter() {
        return mActivityCounter.intValue();
    }

    private class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivityCounter.incrementAndGet();
            ActivityStack.getInstance().put(activity.getClass().getSimpleName(), activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            mBackHomeCounter.incrementAndGet();
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            mBackHomeCounter.decrementAndGet();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivityCounter.decrementAndGet();
            ActivityStack.getInstance().out(activity.getClass().getSimpleName());
            Log.d("stack", "当前栈顶activity2:" + ActivityStack.getInstance().getTopActivity());
        }
    }
}
