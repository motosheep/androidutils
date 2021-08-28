package com.north.light.libble.api;

import com.north.light.libble.listener.BLEScanResultListener;

/**
 * author:li
 * date:2021/8/26
 * desc:BLE功能api
 */
public interface BLEFunctionApi {

    /**
     * 搜索设备
     */
    boolean scanDevice();

    /**
     * 停止搜索
     */
    boolean stopScan();

//    /**
//     * 连接蓝牙
//     * */
//    boolean connect();



}
