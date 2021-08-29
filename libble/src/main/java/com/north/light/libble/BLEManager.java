package com.north.light.libble;

import com.north.light.libble.bean.BLEInfo;
import com.north.light.libble.listener.BLEDataBackListener;
import com.north.light.libble.listener.BLEScanResultListener;
import com.north.light.libble.listener.BLEStatusListener;
import com.north.light.libble.model.BLENormalImpl;
import com.north.light.libble.model.api.BLEModelApi;
import com.north.light.libble.thread.BLEThreadManager;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙管理类
 */
public class BLEManager implements BLEModelApi {

    private static class SingleHolder {
        static BLEManager mInstance = new BLEManager();
        static BLENormalImpl ble = new BLENormalImpl();
    }

    public static BLEManager getInstance() {
        return SingleHolder.mInstance;
    }

    public BLEModelApi getBLEObj() {
        return SingleHolder.ble;
    }

    /**
     * 全局初始化时调用
     */
    public void init() {
        BLEThreadManager.getInstance().init();
        SingleHolder.ble.init();
    }

    /**
     * 全局释放时调用
     */
    public void release() {
        SingleHolder.ble.release();
    }


    @Override
    public boolean scanDevice() {
        return getBLEObj().scanDevice();
    }

    @Override
    public boolean stopScan() {
        return getBLEObj().stopScan();
    }

    @Override
    public void connect(BLEInfo info, String uuid) {
        getBLEObj().connect(info, uuid);
    }

    @Override
    public void disConnect() {
        getBLEObj().disConnect();
    }

    @Override
    public void receive(String uuid) {
        getBLEObj().receive(uuid);
    }

    @Override
    public void disReceive() {
        getBLEObj().disReceive();
    }

    @Override
    public void sendData(String data) {
        getBLEObj().sendData(data);
    }

    @Override
    public void releaseAll() {
        getBLEObj().releaseAll();
    }

    @Override
    public void clientAutoConnect(boolean auto) {
        getBLEObj().clientAutoConnect(auto);
    }

    @Override
    public void serverAutoAccept(boolean auto) {
        getBLEObj().serverAutoAccept(auto);
    }

    @Override
    public void setOnResultListener(BLEScanResultListener listener) {
        getBLEObj().setOnResultListener(listener);
    }

    @Override
    public void removeResultListener(BLEScanResultListener listener) {
        getBLEObj().removeResultListener(listener);
    }

    @Override
    public void setOnStatusListener(BLEStatusListener listener) {
        getBLEObj().setOnStatusListener(listener);
    }

    @Override
    public void removeOnStatusListener(BLEStatusListener listener) {
        getBLEObj().removeOnStatusListener(listener);
    }

    @Override
    public void setOnDataListener(BLEDataBackListener listener) {
        getBLEObj().setOnDataListener(listener);
    }

    @Override
    public void removeOnDataListener(BLEDataBackListener listener) {
        getBLEObj().removeOnDataListener(listener);
    }

    @Override
    public boolean isOpenBLE() {
        return getBLEObj().isOpenBLE();
    }

    @Override
    public boolean openBLE() {
        return getBLEObj().openBLE();
    }

    @Override
    public boolean closeBLE() {
        return getBLEObj().closeBLE();
    }
}
