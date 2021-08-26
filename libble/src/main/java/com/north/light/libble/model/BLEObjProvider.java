package com.north.light.libble.model;

import android.bluetooth.BluetoothAdapter;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙对象提供类
 */
public class BLEObjProvider {
    //获取系统蓝牙适配器管理类
    private BluetoothAdapter mBluetoothAdapter;

    private static class SingleHolder {
        static BLEObjProvider mInstance = new BLEObjProvider();
    }

    public static BLEObjProvider getInstance() {
        return SingleHolder.mInstance;
    }

    public BLEObjProvider() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }
}
