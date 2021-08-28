package com.north.light.libble.api;

import com.north.light.libble.bean.BLEInfo;

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

    /**
     * 连接蓝牙
     */
    void connect(BLEInfo info, String uuid);

    /**
     * 断开连接
     */
    void disConnect();

    /**
     * 设置接收
     */
    void receive(String uuid);

    /**
     * 断开接收
     */
    void disReceive();

    /**
     * 发送数据
     */
    void sendData(String data);

    /**
     * 释放所有
     */
    void releaseAll();

    /**
     * 客户端是否自动重连--每次使用都要重新设置
     */
    void clientAutoConnect(boolean auto);

    /**
     * 服务端自动监听--每次使用都要重新设置
     */
    void serverAutoAccept(boolean auto);

}
