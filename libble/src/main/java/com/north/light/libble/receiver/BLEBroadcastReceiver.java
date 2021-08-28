package com.north.light.libble.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.north.light.libble.bean.BLEInfo;
import com.north.light.libble.content.BLEContext;
import com.north.light.libble.listener.BLEBroadcastListener;
import com.north.light.libble.utils.BLELog;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:li
 * date:2021/8/28
 * desc:蓝牙状态监听
 */
public class BLEBroadcastReceiver {
    private CopyOnWriteArrayList<BLEBroadcastListener> mListener = new CopyOnWriteArrayList<>();

    private static class SingleHolder {
        static BLEBroadcastReceiver mInstance = new BLEBroadcastReceiver();
    }

    public static BLEBroadcastReceiver getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 初始化--全局只能调用一次
     */
    public void init() {
        release();
        //扫描广播接收------------------------------------------------------------------
        IntentFilter scanFilter = new IntentFilter();
        scanFilter.addAction(BluetoothDevice.ACTION_FOUND);
        scanFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        scanFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        BLEContext.getInstance().getAppContext().registerReceiver(scanReceiver, scanFilter);
        //状态广播接收------------------------------------------------------------------
        IntentFilter statusFilter = new IntentFilter();
        statusFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //本机的蓝牙连接状态发生变化（连接第一个远程设备与断开最后一个远程设备才触发）
        statusFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        //有远程设备成功连接至本机(每个远程设备都会触发)
        statusFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        //有远程设备断开连接(每个远程设备都会触发)
        statusFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        BLEContext.getInstance().getAppContext().registerReceiver(statusReceiver, statusFilter);
    }


    /**
     * 结束--与init对应使用
     */
    public void release() {
        try {
            BLEContext.getInstance().getAppContext().unregisterReceiver(scanReceiver);
        } catch (Exception e) {
            BLELog.d(getClass().getSimpleName(), "BLEImpl release scanReceiver error:" + e.getMessage());
        }
        try {
            BLEContext.getInstance().getAppContext().unregisterReceiver(statusReceiver);
        } catch (Exception e) {
            BLELog.d(getClass().getSimpleName(), "BLEImpl release statusReceiver error:" + e.getMessage());
        }
    }


    /**
     * 蓝牙搜索发现广播
     */
    private final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    // 从 Intent 中获取发现的 BluetoothDevice
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MAX_VALUE);
                    // 将名字和地址放入要显示的适配器中
                    ArrayList<BLEInfo> result = new ArrayList<>();
                    result.add(new BLEInfo(device, rssi));
                    for (BLEBroadcastListener listener : mListener) {
                        listener.discoveryDevice(result);
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    for (BLEBroadcastListener listener : mListener) {
                        listener.startDiscovery();
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    for (BLEBroadcastListener listener : mListener) {
                        listener.stopDiscovery();
                    }
                    break;
            }
        }
    };

    /**
     * 蓝牙状态监听广播
     */
    private final BroadcastReceiver statusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                //蓝牙开关改变
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    //获取蓝牙广播中的蓝牙新状态
                    int blueNewState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    //获取蓝牙广播中的蓝牙旧状态
                    int blueOldState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, 0);
                    switch (blueNewState) {
                        case BluetoothAdapter.STATE_TURNING_ON: {
                            //正在打开蓝牙
                            for (BLEBroadcastListener listener : mListener) {
                                listener.opening();
                            }
                            break;
                        }
                        case BluetoothAdapter.STATE_ON: {
                            //蓝牙已打开
                            for (BLEBroadcastListener listener : mListener) {
                                listener.opened();
                            }
                            break;
                        }
                        case BluetoothAdapter.STATE_TURNING_OFF: {
                            //正在关闭蓝牙
                            for (BLEBroadcastListener listener : mListener) {
                                listener.closing();
                            }
                            break;
                        }
                        case BluetoothAdapter.STATE_OFF: {
                            //蓝牙已关闭
                            for (BLEBroadcastListener listener : mListener) {
                                listener.closed();
                            }
                            break;
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    /*
                     * 本机的蓝牙连接状态发生变化
                     * 特指“无任何连接”→“连接任意远程设备”，以及“连接任一或多个远程设备”→“无任何连接”的状态变化，
                     * 即“连接第一个远程设备”与“断开最后一个远程设备”时才会触发该Action
                     */
                    //获取蓝牙广播中的蓝牙连接新状态
                    int newConnState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);
                    //获取蓝牙广播中的蓝牙连接旧状态
                    int oldConnState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, 0);
                    // 当前远程蓝牙设备
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (newConnState) {
                        //蓝牙连接中
                        case BluetoothAdapter.STATE_CONNECTING: {
                            for (BLEBroadcastListener listener : mListener) {
                                listener.connecting(new BLEInfo(device));
                            }
                            break;
                        }
                        //蓝牙已连接
                        case BluetoothAdapter.STATE_CONNECTED: {
                            for (BLEBroadcastListener listener : mListener) {
                                listener.connected(new BLEInfo(device));
                            }
                            break;
                        }
                        //蓝牙断开连接中
                        case BluetoothAdapter.STATE_DISCONNECTING: {
                            for (BLEBroadcastListener listener : mListener) {
                                listener.disConnecting(new BLEInfo(device));
                            }
                            break;
                        }
                        //蓝牙已断开连接
                        case BluetoothAdapter.STATE_DISCONNECTED: {
                            for (BLEBroadcastListener listener : mListener) {
                                listener.disconnected(new BLEInfo(device));
                            }
                            break;
                        }
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    //有远程设备成功连接至本机
                    BluetoothDevice connectDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    for (BLEBroadcastListener listener : mListener) {
                        listener.remoteConnected(new BLEInfo(connectDevice));
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    //有远程设备断开连接
                    BluetoothDevice disConnectDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    for (BLEBroadcastListener listener : mListener) {
                        listener.removeDisconnected(new BLEInfo(disConnectDevice));
                    }
                    break;
            }
        }
    };

    public void setOnListener(BLEBroadcastListener listener) {
        mListener.add(listener);
    }

    public void removeListener(BLEBroadcastListener listener) {
        mListener.remove(listener);
    }
}
