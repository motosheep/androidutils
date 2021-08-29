package com.north.light.libble.listener.inner;

/**
 * author:li
 * date:2021/8/28
 * desc:蓝牙数据监听
 */
public interface BLEDataListener {

    /**
     * 发送数据
     */
    void sendCallBack(boolean success, String data);

    /**
     * 接收数据
     */
    void receiveCallBack(String data);

    //状态类---------------------------------------------------------------------
    /**
     * 连接状态
     */
    void connecting();

    void connectSuccess();

    void connectFailed();

    /**
     * 接收状态
     */
    void receiveAccept();

    void receivingSuccess();

    void receiveFailed();
}
