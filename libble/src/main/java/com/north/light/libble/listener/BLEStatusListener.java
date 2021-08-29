package com.north.light.libble.listener;

/**
 * author:li
 * date:2021/8/29
 * desc:蓝牙状态监听
 */
public interface BLEStatusListener {

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
     * 连接中
     */
    void connecting();

    /**
     * 连接成功
     */
    void connectSuccess();

    /**
     * 连接失败
     */
    void connectFailed();
}
