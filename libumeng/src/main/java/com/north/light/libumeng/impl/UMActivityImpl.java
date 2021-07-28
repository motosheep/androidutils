package com.north.light.libumeng.impl;

import android.content.Context;

import com.north.light.libumeng.api.UMActivityApi;
import com.umeng.analytics.MobclickAgent;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 16:46
 * @Version: 1.0
 * @Description:activity页面统计实现
 */
public class UMActivityImpl implements UMActivityApi {


    @Override
    public void onActivityResume(Context context) {
        MobclickAgent.onResume(context);
    }

    @Override
    public void onActivityPause(Context context) {
        MobclickAgent.onPause(context);
    }
}
