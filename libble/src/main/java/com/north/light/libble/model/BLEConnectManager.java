package com.north.light.libble.model;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.text.TextUtils;

import com.north.light.libble.bean.BLEInfo;
import com.north.light.libble.listener.inner.BLEDataListener;
import com.north.light.libble.thread.BLEThreadManager;
import com.north.light.libble.utils.BLELog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:li
 * date:2021/8/28
 * desc:蓝牙连接管理类
 */
public class BLEConnectManager {
    private final static String TAG = BLEConnectManager.class.getSimpleName();
    //当前连接的蓝牙对象
    private BLEInfo mCurrentBLEInfo;
    //当前连接的uuid
    private String mCurrentSendUUID;
    private String mCurrentReceiveUUID;
    //是否主动连接的标识
    private boolean mIsConnectRemote = false;
    //是否被连接标识
    private boolean mIsBeConnectEd = false;
    //蓝牙连接对象
    private static BluetoothSocket mSendSocket;
    private static BluetoothSocket mReceiveSocket;
    private static BluetoothServerSocket mServerSocket;
    private static BufferedReader mSendBufferedReader;
    private static BufferedWriter mSendBufferedWriter;
    private static InputStream mReceiveBufferedReader;
    //读取数据中间缓存变量
    private static String readTxt;
    private static byte[] cacheByte;
    //监听数据集合
    private CopyOnWriteArrayList<BLEDataListener> mListener = new CopyOnWriteArrayList<>();
    //自动重连标识
    private static boolean mClientAutoConnect = false;
    private final long TIME_CLIENT_RECONNECT = 1200;
    private static boolean mServerAutoAccept = false;
    private final long TIME_SERVER_ACCEPT = 1200;
    //是否释放中的标识
    private static volatile boolean isReleaseClient = false;
    private static volatile boolean isReleaseServer = false;

    public void clientAutoConnect(boolean auto) {
        mClientAutoConnect = auto;
        if (!auto) {
            BLEThreadManager.getInstance().getHandler().removeCallbacks(mAutoConnectClientRunnable);
        }
    }

    public void serverAutoAccept(boolean auto) {
        mServerAutoAccept = auto;
        if (!auto) {
            BLEThreadManager.getInstance().getHandler().removeCallbacks(mAutoServerAccept);
        }
    }

    private static class SingleHolder {
        static BLEConnectManager mInstance = new BLEConnectManager();
    }

    public static BLEConnectManager getInstance() {
        return SingleHolder.mInstance;
    }

    public BLEConnectManager() {
    }

    /**
     * 连接
     */
    public void connect(BLEInfo info, String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            return;
        }
        mCurrentSendUUID = uuid;
        mCurrentBLEInfo = info;
        if (!checkBLE(mCurrentBLEInfo)) {
            return;
        }
        BLEThreadManager.getInstance().getHandler().removeCallbacks(mAutoConnectClientRunnable);
        BLEThreadManager.getInstance().getCachePool().execute(mConnectRunnable);
    }

    /**
     * 连接runnable
     */
    private Runnable mConnectRunnable = new Runnable() {
        @Override
        public void run() {
            releaseSend();
            try {
                notifyStatus(1);
                //开始连接
                mSendSocket = mCurrentBLEInfo.getDevice().createRfcommSocketToServiceRecord(UUID.fromString(mCurrentSendUUID));
                mSendSocket.connect();
                mIsConnectRemote = true;
                notifyStatus(2);
            } catch (Exception e) {
                e.printStackTrace();
                releaseSend();
                mIsConnectRemote = false;
                notifyStatus(3);
                if (mClientAutoConnect) {
                    if (isReleaseClient) {
                        isReleaseClient = false;
                        return;
                    }
                    BLEThreadManager.getInstance().getHandler().removeCallbacks(mAutoConnectClientRunnable);
                    BLEThreadManager.getInstance().getHandler().postDelayed(mAutoConnectClientRunnable, TIME_CLIENT_RECONNECT);
                }
            }
        }
    };

    /**
     * 自动重连客户端runnable
     */
    private Runnable mAutoConnectClientRunnable = new Runnable() {
        @Override
        public void run() {
            BLELog.d(TAG, "客户端自动重连");
            connect(mCurrentBLEInfo, mCurrentSendUUID);
        }
    };

    /**
     * 监听
     */
    public void receive(String uuid) {
        releaseReceive();
        mCurrentReceiveUUID = uuid;
        if (TextUtils.isEmpty(mCurrentReceiveUUID)) {
            return;
        }
        BLEThreadManager.getInstance().getHandler().removeCallbacks(mAutoServerAccept);
        BLEThreadManager.getInstance().getCachePool().execute(mReceiveRunnable);
    }

    /**
     * 接收runnable
     */
    private Runnable mReceiveRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                notifyStatus(4);
                mServerSocket = BLEObjProvider.getInstance().getBluetoothAdapter()
                        .listenUsingInsecureRfcommWithServiceRecord(
                                "SERVER", UUID.fromString(mCurrentReceiveUUID));
                BLELog.d(TAG, "等待连接1");
                mReceiveSocket = mServerSocket.accept();
                mReceiveBufferedReader = mReceiveSocket.getInputStream();
                mServerSocket.close();
                mIsBeConnectEd = true;
                BLELog.d(TAG, "等待连接2");
                notifyStatus(5);
                //被连接后，监听连接
                while (mIsBeConnectEd) {
                    //使用byte固定长度进行读取，不使用read line原因是其会导致阻塞
                    cacheByte = new byte[2048];
                    int length = mReceiveBufferedReader.read(cacheByte, 0, cacheByte.length);
                    readTxt = new String(cacheByte, 0, length);
                    if (!TextUtils.isEmpty(readTxt)) {
                        BLELog.d(TAG, "接收信息：" + readTxt);
                        for (BLEDataListener listener : mListener) {
                            listener.receiveCallBack(readTxt);
                        }
                    }
                }
                //已经取消了监听
                releaseReceive();
            } catch (Exception e) {
                BLELog.d(TAG, "接收信息 Exception：" + e.getMessage());
                e.printStackTrace();
                releaseReceive();
                notifyStatus(6);
                if (mServerAutoAccept) {
                    if (isReleaseServer) {
                        isReleaseServer = false;
                        return;
                    }
                    BLEThreadManager.getInstance().getHandler().removeCallbacks(mAutoServerAccept);
                    BLEThreadManager.getInstance().getHandler().postDelayed(mAutoServerAccept, TIME_SERVER_ACCEPT);
                }
            }
        }
    };

    /**
     * 自动重试服务端接收
     */
    private Runnable mAutoServerAccept = new Runnable() {
        @Override
        public void run() {
            BLELog.d(TAG, "服务端自动重连");
            receive(mCurrentReceiveUUID);
        }
    };

    /**
     * 发送数据
     */
    public void sendData(String data) {
        if (!mIsConnectRemote) {
            for (BLEDataListener listener : mListener) {
                listener.sendCallBack(false, data);
            }
            return;
        }
        BLEThreadManager.getInstance().getCachePool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mSendBufferedReader = new BufferedReader(new InputStreamReader(
                            new ByteArrayInputStream(data.getBytes())));
                    mSendBufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(mSendSocket.getOutputStream()));
                    String txt;
                    while ((txt = mSendBufferedReader.readLine()) != null) {
                        mSendBufferedWriter.write(txt + "\n");
                    }
                    mSendBufferedWriter.flush();
                    for (BLEDataListener listener : mListener) {
                        listener.sendCallBack(true, data);
                    }
                } catch (Exception e) {
                    BLELog.d(TAG, "发送错误");
                    for (BLEDataListener listener : mListener) {
                        listener.sendCallBack(false, data);
                    }
                }
            }
        });

    }

    public void disconnect() {
        releaseSend();
    }

    public void disReceive() {
        releaseReceive();
    }

    public void releaseAll() {
        isReleaseClient = true;
        isReleaseServer = true;
        disReceive();
        disconnect();
        releaseRetryHandler();
    }

    /**
     * 停止自动重连
     */
    public void releaseRetryHandler() {
        BLEThreadManager.getInstance().releaseHandler();
    }

    /**
     * 释放发送的监听
     */
    private void releaseSend() {
        mIsConnectRemote = false;
        try {
            if (mSendBufferedReader != null) {
                mSendBufferedReader.close();
            }
            mSendBufferedReader = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (mSendBufferedWriter != null) {
                mSendBufferedWriter.close();
            }
            mSendBufferedWriter = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (mSendSocket != null) {
                mSendSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放接收监听
     */
    private void releaseReceive() {
        mIsBeConnectEd = false;
        try {
            if (mReceiveBufferedReader != null) {
                mReceiveBufferedReader.close();
            }
            mReceiveBufferedReader = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (Exception e) {

        }
        try {
            if (mReceiveSocket != null) {
                mReceiveSocket.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 检查蓝牙对象是否符合
     */
    private boolean checkBLE(BLEInfo info) {
        if (info == null) {
            return false;
        }
        if (info.getRssi() == 0 || info.getDevice() == null) {
            return false;
        }
        return true;
    }

    /**
     * 监听---------------------------------------------------------------------------------------
     */
    public void setBLEDataListener(BLEDataListener listener) {
        mListener.add(listener);
    }

    public void removeBLEDataListener(BLEDataListener listener) {
        mListener.remove(listener);
    }

    private void notifyStatus(int type) {
        for (BLEDataListener listener : mListener) {
            switch (type) {
                case 1:
                    listener.connecting();
                    break;
                case 2:
                    listener.connectSuccess();
                    break;
                case 3:
                    listener.connectFailed();
                    break;
                case 4:
                    listener.receiveAccept();
                    break;
                case 5:
                    listener.receivingSuccess();
                    break;
                case 6:
                    listener.receiveFailed();
                    break;
            }
        }
    }
}
