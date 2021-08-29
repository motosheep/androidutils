package com.north.light.libble.listener;

/**
 * author:li
 * date:2021/8/29
 * desc:数据发送接收监听
 */
public interface BLEDataBackListener {

    /**
     * 发送数据
     */
    void sendCallBack(boolean success, String data);

    /**
     * 接收数据
     */
    void receiveCallBack(String data);
}
