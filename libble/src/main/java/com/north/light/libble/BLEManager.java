package com.north.light.libble;

import com.north.light.libble.api.BLEModelApi;
import com.north.light.libble.model.BLEImpl;
import com.north.light.libble.thread.BLEThreadManager;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙管理类
 */
public class BLEManager {

    private static class SingleHolder {
        static BLEManager mInstance = new BLEManager();
        static BLEImpl ble = new BLEImpl();
    }

    public static BLEManager getInstance() {
        return SingleHolder.mInstance;
    }

    public static BLEModelApi getBLEObj() {
        return SingleHolder.ble;
    }

    public void init() {
        BLEThreadManager.getInstance().init();
        SingleHolder.ble.init();
    }

    public void release() {
        SingleHolder.ble.release();
    }
}
