package com.north.light.libble.model;

import android.bluetooth.BluetoothAdapter;

import com.north.light.libble.bean.BLEInfo;
import com.north.light.libble.listener.BLEDataBackListener;
import com.north.light.libble.listener.BLEScanResultListener;
import com.north.light.libble.listener.BLEStatusListener;
import com.north.light.libble.listener.inner.BLEBroadcastListener;
import com.north.light.libble.listener.inner.BLEDataListener;
import com.north.light.libble.model.api.BLEModelApi;
import com.north.light.libble.receiver.BLEBroadcastReceiver;
import com.north.light.libble.utils.BLELog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙功能集成类--基础方法
 */
public class BLEImpl implements BLEModelApi {
    public final String TAG = BLEImpl.class.getSimpleName();
    /**
     * 结果监听集合
     */
    private CopyOnWriteArrayList<BLEScanResultListener> mResultListener = new CopyOnWriteArrayList<>();
    /**
     * 状态监听集合
     */
    private CopyOnWriteArrayList<BLEStatusListener> mStatusListener = new CopyOnWriteArrayList<>();
    /**
     * 数据监听集合
     */
    private CopyOnWriteArrayList<BLEDataBackListener> mDataListener = new CopyOnWriteArrayList<>();


    public BLEImpl() {
        BLELog.d(TAG, "BLEImpl 构造函数");
    }

    /**
     * 初始化--全局只能调用一次
     */
    public void init() {
        BLELog.d(TAG, "BLEImpl init");
        BLEBroadcastReceiver.getInstance().setOnListener(bleBroadcastListener);
        BLEConnectManager.getInstance().setBLEDataListener(dataListener);
        BLEBroadcastReceiver.getInstance().init();
    }

    /**
     * 结束--与init对应使用
     */
    public void release() {
        BLEBroadcastReceiver.getInstance().removeListener(bleBroadcastListener);
        BLEConnectManager.getInstance().removeBLEDataListener(dataListener);
        BLEBroadcastReceiver.getInstance().release();
    }

    /**
     * 蓝牙数据监听
     */
    private BLEDataListener dataListener = new BLEDataListener() {
        @Override
        public void sendCallBack(boolean success, String data) {
            BLELog.d(TAG, "sendCallBack success: " + success + "\tdata: " + data);
            for (BLEDataBackListener listener : mDataListener) {
                listener.sendCallBack(success, data);
            }
        }

        @Override
        public void receiveCallBack(String data) {
            BLELog.d(TAG, "receiveCallBack: " + data);
            for (BLEDataBackListener listener : mDataListener) {
                listener.receiveCallBack(data);
            }
        }

        @Override
        public void connecting() {
            BLELog.d(TAG, "connecting");
            for (BLEStatusListener listener : mStatusListener) {
                listener.connecting();
            }
        }

        @Override
        public void connectSuccess() {
            BLELog.d(TAG, "connectSuccess");
            for (BLEStatusListener listener : mStatusListener) {
                listener.connectSuccess();
            }
        }

        @Override
        public void connectFailed() {
            BLELog.d(TAG, "connectFailed");
            for (BLEStatusListener listener : mStatusListener) {
                listener.connectFailed();
            }
        }

        @Override
        public void receiveAccept() {
            BLELog.d(TAG, "receiveAccept");
        }

        @Override
        public void receivingSuccess() {
            BLELog.d(TAG, "receivingSuccess");
        }

        @Override
        public void receiveFailed() {
            BLELog.d(TAG, "receiveFailed");
        }
    };

    /**
     * 蓝牙广播监听
     */
    private BLEBroadcastListener bleBroadcastListener = new BLEBroadcastListener() {
        @Override
        public void startDiscovery() {
            BLELog.d(TAG, "startDiscovery");
            for (BLEScanResultListener listener : mResultListener) {
                listener.startScan();
            }
        }

        @Override
        public void stopDiscovery() {
            BLELog.d(TAG, "stopDiscovery");
            for (BLEScanResultListener listener : mResultListener) {
                listener.stopScan();
            }
        }

        @Override
        public void discoveryDevice(List<BLEInfo> deviceList) {
            BLELog.d(TAG, "discoveryDevice");
            for (BLEScanResultListener listener : mResultListener) {
                listener.result(deviceList);
            }
        }

        @Override
        public void opening() {
            BLELog.d(TAG, "opening");
            for (BLEStatusListener listener : mStatusListener) {
                listener.opening();
            }
        }

        @Override
        public void opened() {
            BLELog.d(TAG, "opened");
            for (BLEStatusListener listener : mStatusListener) {
                listener.opened();
            }
        }

        @Override
        public void closing() {
            BLELog.d(TAG, "closing");
            for (BLEStatusListener listener : mStatusListener) {
                listener.closing();
            }
        }

        @Override
        public void closed() {
            BLELog.d(TAG, "closed");
            for (BLEStatusListener listener : mStatusListener) {
                listener.closed();
            }
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
            //取消重连机制
            BLEConnectManager.getInstance().releaseRetryHandler();
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
            //取消重连机制
            BLEConnectManager.getInstance().releaseRetryHandler();
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
            //取消重连机制
            BLEConnectManager.getInstance().releaseRetryHandler();
            return BLEObjProvider.getInstance().getBluetoothAdapter().enable();
        } catch (Exception e) {
            BLELog.d(TAG + "open error:", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean closeBLE() {
        try {
            //取消重连机制
            BLEConnectManager.getInstance().releaseRetryHandler();
            return BLEObjProvider.getInstance().getBluetoothAdapter().disable();
        } catch (Exception e) {
            BLELog.d(TAG + "close error:", e.getMessage());
            return false;
        }
    }

    @Override
    public void setOnResultListener(BLEScanResultListener listener) {
        mResultListener.add(listener);
    }

    @Override
    public void removeResultListener(BLEScanResultListener listener) {
        mResultListener.remove(listener);
    }

    @Override
    public void setOnStatusListener(BLEStatusListener listener) {
        mStatusListener.add(listener);
    }

    @Override
    public void removeOnStatusListener(BLEStatusListener listener) {
        mStatusListener.remove(listener);
    }

    @Override
    public void setOnDataListener(BLEDataBackListener listener) {
        mDataListener.add(listener);
    }

    @Override
    public void removeOnDataListener(BLEDataBackListener listener) {
        mDataListener.remove(listener);
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

    @Override
    public void clientAutoConnect(boolean auto) {
        BLEConnectManager.getInstance().clientAutoConnect(auto);
    }

    @Override
    public void serverAutoAccept(boolean auto) {
        BLEConnectManager.getInstance().serverAutoAccept(auto);
    }
}
