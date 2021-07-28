package com.north.light.libumeng.api;

import android.content.Context;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 16:21
 * @Version: 1.0
 * @Description:activity的接口调用声明
 */
public interface UMActivityApi {


    /**
     * on resume
     */
    public void onActivityResume(Context context);

    /**
     * on pause
     */
    public void onActivityPause(Context context);
}
