package com.north.light.libble.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.north.light.libble.api.BLEModelApi;
import com.north.light.libble.content.BLEContext;
import com.north.light.libble.listener.BLEScanResultListener;
import com.north.light.libble.utils.BLELog;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:li
 * date:2021/8/26
 * desc:蓝牙功能集成类
 */
public class BLEImpl implements BLEModelApi {
    /**
     * 监听集合
     */
    private CopyOnWriteArrayList<BLEScanResultListener> mListener = new CopyOnWriteArrayList<>();


    public BLEImpl() {
        BLELog.d(getClass().getSimpleName(), "BLEImpl 构造函数");
        init();
    }


    /**
     * 初始化--全局只能调用一次
     */
    public void init() {
        IntentFilter scanFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BLEContext.getInstance().getAppContext().registerReceiver(scanReceiver, scanFilter);
    }

    /**
     * 结束--与init对应使用
     */
    public void release() {
        BLEContext.getInstance().getAppContext().unregisterReceiver(scanReceiver);
    }


    @Override
    public boolean scanDevice() {
        try {
            BluetoothAdapter adapter = BLEObjProvider.getInstance().getBluetoothAdapter();
            adapter.cancelDiscovery();
            return adapter.startDiscovery();
        } catch (Exception e) {
            BLELog.d(getClass().getSimpleName() + "scanDevice error:", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean stopScan() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().cancelDiscovery();
        } catch (Exception e) {
            BLELog.d(getClass().getSimpleName() + "stopScan error:", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isOpenBLE() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().isEnabled();
        } catch (Exception e) {
            BLELog.d(getClass().getSimpleName() + "isOpen error:", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean openBLE() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().enable();
        } catch (Exception e) {
            BLELog.d(getClass().getSimpleName() + "open error:", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean closeBLE() {
        try {
            return BLEObjProvider.getInstance().getBluetoothAdapter().disable();
        } catch (Exception e) {
            BLELog.d(getClass().getSimpleName() + "close error:", e.getMessage());
            return false;
        }
    }

    //蓝牙搜索发现广播
    private final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 当 Discovery 发现了一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 从 Intent 中获取发现的 BluetoothDevice
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 将名字和地址放入要显示的适配器中
                ArrayList<BluetoothDevice> result = new ArrayList<>();
                result.add(device);
                for (BLEScanResultListener listener : mListener) {
                    listener.result(result);
                }
            }
        }
    };

    @Override
    public void setOnResultListener(BLEScanResultListener listener) {
        mListener.add(listener);
    }

    @Override
    public void removeResultListener(BLEScanResultListener listener) {
        mListener.remove(listener);
    }
}
