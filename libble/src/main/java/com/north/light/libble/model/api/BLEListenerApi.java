package com.north.light.libble.model.api;

import com.north.light.libble.listener.BLEDataBackListener;
import com.north.light.libble.listener.BLEScanResultListener;
import com.north.light.libble.listener.BLEStatusListener;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙监听api
 */
public interface BLEListenerApi {
    /**
     * 设置结果监听
     */
    void setOnResultListener(BLEScanResultListener listener);

    /**
     * 移除结果监听
     */
    void removeResultListener(BLEScanResultListener listener);

    /**
     * 设置状态监听
     * */
    void setOnStatusListener(BLEStatusListener listener);

    /**
     * 移除状态监听
     * */
    void removeOnStatusListener(BLEStatusListener listener);

    /**
     * 设置数据监听
     * */
    void setOnDataListener(BLEDataBackListener listener);

    /**
     * 移除数据监听
     * */
    void removeOnDataListener(BLEDataBackListener listener);

}
