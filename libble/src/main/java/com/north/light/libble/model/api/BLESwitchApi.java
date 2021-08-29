package com.north.light.libble.model.api;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙开关api
 */
public interface BLESwitchApi {

    /**
     * 是否打开蓝牙
     */
    boolean isOpenBLE();

    /**
     * 打开蓝牙
     */
    boolean openBLE();

    /**
     * 关闭蓝牙
     */
    boolean closeBLE();


}
