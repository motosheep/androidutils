package com.north.light.libumeng.impl;

import com.north.light.libumeng.api.UMNoActivityApi;
import com.umeng.analytics.MobclickAgent;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 16:57
 * @Version: 1.0
 * @Description:非activity实现
 */
public class UMNoActivityImpl implements UMNoActivityApi {


    @Override
    public void onPageStart(String viewName) {
        MobclickAgent.onPageStart(viewName);
    }

    @Override
    public void onPageEnd(String viewName) {
        MobclickAgent.onPageEnd(viewName);
    }
}
