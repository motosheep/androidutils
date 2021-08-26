package com.north.light.libble.content;

import android.content.Context;

/**
 * author:li
 * date:2021/8/26
 * desc:ble context
 */
public class BLEContext {

    private static class SingleHolder {
        static BLEContext mInstance = new BLEContext();
    }

    public static BLEContext getInstance() {
        return SingleHolder.mInstance;
    }

    public Context getAppContext() {
        return BLEContentProvider.getAppContext();
    }
}
