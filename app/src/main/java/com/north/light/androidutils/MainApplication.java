package com.north.light.androidutils;

import android.app.Application;

import com.north.light.androidutils.activitycounter.ActivityCounter;
import com.north.light.libshare.ShareMain;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/30 15:45
 * @Version: 1.0
 * @Description:
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityCounter.getInstance().init(this);
    }
}
