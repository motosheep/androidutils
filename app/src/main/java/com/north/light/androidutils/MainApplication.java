package com.north.light.androidutils;

import android.app.Application;
import android.content.Context;

import com.north.light.androidutils.activitycounter.ActivityCounter;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/30 15:45
 * @Version: 1.0
 * @Description:
 */
public class MainApplication extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ActivityCounter.getInstance().init(this);
    }
}
