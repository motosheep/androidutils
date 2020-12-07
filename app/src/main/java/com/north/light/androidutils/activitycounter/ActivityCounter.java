package com.north.light.androidutils.activitycounter;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lzt
 * time 2020/11/27
 * 描述：activity生命周期管理类--count为0时，代表activity在后台
 */
public class ActivityCounter implements Serializable {
    //activity计数器
    private AtomicInteger mActivityCounter = new AtomicInteger(0);

    private static class SingleHolder {
        static ActivityCounter mInstance = new ActivityCounter();
    }

    public static ActivityCounter getInstance() {
        return ActivityCounter.SingleHolder.mInstance;
    }

    //初始化
    public void init(Application context){
        context.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksImpl());
    }

    public int getActivityCounter() {
        return mActivityCounter.intValue();
    }

    private class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mActivityCounter.incrementAndGet();
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            mActivityCounter.decrementAndGet();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
