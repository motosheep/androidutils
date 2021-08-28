package com.north.light.libble.listener;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * author:li
 * date:2021/8/28
 * desc:蓝牙广播监听
 */
public interface BLEBroadcastListener {

    //搜索类---------------------------------------------------------------------------

    /**
     * 开始搜索
     */
    void startDiscovery();

    /**
     * 停止搜索
     */
    void stopDiscovery();

    /**
     * 搜索得到的设备
     */
    void discoveryDevice(List<BluetoothDevice> deviceList);


    //状态类---------------------------------------------------------------------------

    /**
     * 打开中
     */
    void opening();

    /**
     * 已打开
     */
    void opened();

    /**
     * 关闭中
     */
    void closing();

    /**
     * 已关闭
     */
    void closed();

    /**
     * 远程设备连接到本机
     */
    void remoteConnected(BluetoothDevice device);

    /**
     * 远程设备与本机断开连接
     */
    void removeDisconnected(BluetoothDevice device);

    /**
     * 蓝牙连接中
     */
    void connecting(BluetoothDevice device);

    /**
     * 蓝牙已连接
     * */
    void connected(BluetoothDevice device);

    /**
     * 断开中
     * */
    void disConnecting(BluetoothDevice device);

    /**
     * 已断开
     * */
    void disconnected(BluetoothDevice device);

}
