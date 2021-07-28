package com.north.light.libumeng.api;

import android.content.Context;

import com.north.light.libumeng.constant.CollectMode;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 16:17
 * @Version: 1.0
 * @Description:友盟初始化api
 */
public interface UMInitApi {

    /**
     * 初始化一
     * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
     * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
     * UMConfigure.init调用中appkey和channel参数请置为null）。
     * mode:1--auto   2--manual  3--LEGACY_auto  4--LEGACY_MANUAL
     */
    public void init(Context context, String appkey, String channel, int deviceType, String pushSecret, int mode);

    /**
     * 初始化二
     * 注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
     * mode:1--auto   2--manual  3--LEGACY_auto  4--LEGACY_MANUAL
     */
    public void init(Context context, int deviceType, String pushSecret, int mode);


    /**
     * 获取当前数据收集模式
     */
    public CollectMode getCollectMode();
}
