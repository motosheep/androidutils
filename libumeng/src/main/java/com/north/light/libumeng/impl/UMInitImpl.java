package com.north.light.libumeng.impl;

import android.content.Context;

import com.north.light.libumeng.api.UMInitApi;
import com.north.light.libumeng.constant.CollectMode;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 16:18
 * @Version: 1.0
 * @Description:初始化实现类
 */
public class UMInitImpl implements UMInitApi {

    /**
     * 当前数据手机模式
     */
    private CollectMode mCollectMode = CollectMode.MODE_AUTO;


    /**
     * 初始化一
     * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
     * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
     * UMConfigure.init调用中appkey和channel参数请置为null）。
     * mode:1--auto   2--manual  3--LEGACY_auto  4--LEGACY_MANUAL
     */
    @Override
    public void init(Context context, String appkey, String channel, int deviceType, String pushSecret, int mode) {
        UMConfigure.init(context, appkey, channel, deviceType, pushSecret);
        initCollectMode(mode);
    }

    /**
     * 初始化二
     * 注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
     * mode:1--auto   2--manual  3--LEGACY_auto  4--LEGACY_MANUAL
     */
    @Override
    public void init(Context context, int deviceType, String pushSecret, int mode) {
        UMConfigure.init(context, deviceType, pushSecret);
        initCollectMode(mode);
    }

    /**
     * 获取当前数据收集模式
     */
    @Override
    public CollectMode getCollectMode() {
        return mCollectMode;
    }

    /**
     * 初始化数据收集模式
     * mode:1--auto   2--manual  3--LEGACY_auto  4--LEGACY_MANUAL
     */
    public void initCollectMode(int mode) {
        switch (mode) {
            case 1:
                mCollectMode = CollectMode.MODE_AUTO;
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
                break;
            case 2:
                mCollectMode = CollectMode.MODE_MANUAL;
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
                break;
            case 3:
                mCollectMode = CollectMode.MODE_LEGAVCY_AUTO;
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);
                break;
            case 4:
                mCollectMode = CollectMode.MODE_LEGAVCY_MANUAL;
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
                break;
        }
    }
}
