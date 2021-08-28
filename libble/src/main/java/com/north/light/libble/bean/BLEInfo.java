package com.north.light.libble.bean;

import android.bluetooth.BluetoothDevice;

/**
 * author:li
 * date:2021/8/28
 * desc:自定义蓝牙信息类
 */
public class BLEInfo {
    private BluetoothDevice device;
    private short rssi;

    public BLEInfo() {

    }

    public BLEInfo(BluetoothDevice device) {
        this.device = device;
    }

    public BLEInfo(BluetoothDevice device, short rssi) {
        this.device = device;
        this.rssi = rssi;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public short getRssi() {
        return rssi;
    }

    public void setRssi(short rssi) {
        this.rssi = rssi;
    }
}
