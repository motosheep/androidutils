package com.north.light.libble.api;

import com.north.light.libble.listener.BLEScanResultListener;

/**
 * author:li
 * date:2021/8/26
 * desc:结果监听
 */
public interface BLEResultApi {
    /**
     * 设置结果监听
     * */
    void setOnResultListener(BLEScanResultListener listener);

    /**
     * 移除结果监听
     * */
    void removeResultListener(BLEScanResultListener listener);

}
