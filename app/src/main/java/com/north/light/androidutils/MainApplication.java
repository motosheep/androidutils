package com.north.light.androidutils;

import android.app.Application;
import android.content.Context;

import com.north.light.androidutils.activitycounter.ActivityCounter;
import com.north.light.androidutils.audio.focus.AudioFocusManager;
import com.north.light.androidutils.location.main.LocManager;
import com.north.light.androidutils.novel.text.data.TxtManager;
import com.squareup.leakcanary.LeakCanary;

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
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
        mContext = this;
        AudioFocusManager.getInstance().init(this);
        ActivityCounter.getInstance().init(this);
        LocManager.getInstance().getLocation().init(this);
        TxtManager.getInstance().init(this);
    }
}
