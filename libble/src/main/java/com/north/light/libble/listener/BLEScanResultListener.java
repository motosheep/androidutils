package com.north.light.libble.listener;

import android.bluetooth.BluetoothDevice;

import com.north.light.libble.bean.BLEInfo;

import java.util.List;

/**
 * author:li
 * date:2021/8/26
 * desc:扫描结果监听
 */
public interface BLEScanResultListener {

    /**
     * 扫描结果
     */
    void result(List<BLEInfo> result);

    /**
     * 错误
     */
    void error(String tips);
}
