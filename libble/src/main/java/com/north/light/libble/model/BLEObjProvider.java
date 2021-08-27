package com.north.light.libble.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import com.north.light.libble.content.BLEContext;

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
        BluetoothManager manager = (BluetoothManager) BLEContext.getInstance().getAppContext()
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }
}
