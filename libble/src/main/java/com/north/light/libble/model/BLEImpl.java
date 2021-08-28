package com.north.light.libble.model;

import android.bluetooth.BluetoothAdapter;

import com.north.light.libble.api.BLEModelApi;
import com.north.light.libble.bean.BLEInfo;
import com.north.light.libble.listener.BLEBroadcastListener;
import com.north.light.libble.listener.BLEScanResultListener;
import com.north.light.libble.receiver.BLEBroadcastReceiver;
import com.north.light.libble.utils.BLELog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙功能集成类
 */
public class BLEImpl implements BLEModelApi {
    public final String TAG = BLEImpl.class.getSimpleName();
    /**
     * 监听集合
     */
    private CopyOnWriteArrayList<BLEScanResultListener> mListener = new CopyOnWriteArrayList<>();


    public BLEImpl() {
        BLELog.d(TAG, "BLEImpl 构造函数");
    }


    /**
     * 初始化--全局只能调用一次
     */
    public void init() {
        BLELog.d(TAG, "BLEImpl init");
        BLEBroadcastReceiver.getInstance().setOnListener(bleBroadcastListener);
        BLEBroadcastReceiver.getInstance().init();
    }

    /**
     * 结束--与init对应使用
     */
    public void release() {
        BLEBroadcastReceiver.getInstance().removeListener(bleBroadcastListener);
        BLEBroadcastReceiver.getInstance().release();
    }

    private BLEBroadcastListener bleBroadcastListener = new BLEBroadcastListener() {
        @Override
        public void startDiscovery() {
            BLELog.d(TAG, "startDiscovery");
        }

        @Override
        public void stopDiscovery() {
            BLELog.d(TAG, "stopDiscovery");
        }

        @Override
        public void discoveryDevice(List<BLEInfo> deviceList) {
            BLELog.d(TAG, "discoveryDevice");
            for (BLEScanResultListener listener : mListener) {
                listener.result(deviceList);
            }
        }

        @Override
        public void opening() {
            BLELog.d(TAG, "opening");
        }

        @Override
        public void opened() {
            BLELog.d(TAG, "opened");
        }

        @Override
        public void closing() {
            BLELog.d(TAG, "closing");
        }

        @Override
        public void closed() {
            BLELog.d(TAG, "closed");
        }

        @Override
        public void remoteConnected(BLEInfo device) {
            BLELog.d(TAG, "remoteConnected");
        }

        @Override
        public void removeDisconnected(BLEInfo device) {
            BLELog.d(TAG, "removeDisconnected");
        }

        @Override
        public void connecting(BLEInfo device) {
            BLELog.d(TAG, "connecting");
        }

        @Override
        public void connected(BLEInfo device) {
            BLELog.d(TAG, "connected");
        }

        @Override
        public void disConnecting(BLEInfo device) {
            BLELog.d(TAG, "disConnecting");
        }

        @Override
        public void disconnected(BLEInfo device) {
            BLELog.d(TAG, "disconnected");
        }
    };


    @Override
    public boolean scanDevice() {
        try {
            BluetoothAdapter adapter = BLEObjProvider.getInstance().getBluetoothAdapter();
            adapter.cancelDiscovery();
            return adapter.startDiscovery();
        } catch (Exception e) {
            BLELog.d(TAG + "scanDevice error:", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean stopScan() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().cancelDiscovery();
        } catch (Exception e) {
            BLELog.d(TAG + "stopScan error:", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isOpenBLE() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().isEnabled();
        } catch (Exception e) {
            BLELog.d(TAG + "isOpen error:", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean openBLE() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().enable();
        } catch (Exception e) {
            BLELog.d(TAG + "open error:", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean closeBLE() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().disable();
        } catch (Exception e) {
            BLELog.d(TAG + "close error:", e.getMessage());
            return false;
        }
    }

    @Override
    public void setOnResultListener(BLEScanResultListener listener) {
        mListener.add(listener);
    }

    @Override
    public void removeResultListener(BLEScanResultListener listener) {
        mListener.remove(listener);
    }


    @Override
    public void connect(BLEInfo info, String uuid) {
        stopScan();
        BLEConnectManager.getInstance().connect(info, uuid);
    }

    @Override
    public void disConnect() {
        BLEConnectManager.getInstance().disconnect();
    }

    @Override
    public void receive(String uuid) {
        BLEConnectManager.getInstance().receive(uuid);
    }

    @Override
    public void disReceive() {
        BLEConnectManager.getInstance().disReceive();
    }

    @Override
    public void sendData(String data) {
        BLEConnectManager.getInstance().sendData(data);
    }

    @Override
    public void releaseAll() {
        BLEConnectManager.getInstance().releaseAll();
    }
}
