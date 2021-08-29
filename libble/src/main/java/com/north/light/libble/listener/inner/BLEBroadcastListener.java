package com.north.light.libble.listener.inner;


import com.north.light.libble.bean.BLEInfo;

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
    void discoveryDevice(List<BLEInfo> deviceList);


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
    void remoteConnected(BLEInfo device);

    /**
     * 远程设备与本机断开连接
     */
    void removeDisconnected(BLEInfo device);

    /**
     * 蓝牙连接中
     */
    void connecting(BLEInfo device);

    /**
     * 蓝牙已连接
     */
    void connected(BLEInfo device);

    /**
     * 断开中
     */
    void disConnecting(BLEInfo device);

    /**
     * 已断开
     */
    void disconnected(BLEInfo device);

}
