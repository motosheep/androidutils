package com.north.light.libble.model;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.north.light.libble.listener.BLEDataBackListener;
import com.north.light.libble.utils.BLELog;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:li
 * date:2021/8/29
 * desc:继承ble impl，增加续传功能
 */
public class BLENormalImpl extends BLEImpl {
    //发送数据集合--根据下标保证序列
    private CopyOnWriteArrayList<String> mDataList = new CopyOnWriteArrayList<>();
    //消息发送handler
    private Handler mDataHandler;
    //handler标识
    private final static int TYPE_CHECK = 0x0001;
    private final static int TYPE_SEND = 0x0002;
    //是否释放的标识
    private boolean isRelease = false;

    public BLENormalImpl() {
        mDataHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case TYPE_CHECK:
                        if (mDataList.size() == 0) {
                            mDataHandler.sendEmptyMessageDelayed(TYPE_CHECK, 200);
                        } else {
                            //存在数据，马上发送
                            mDataHandler.sendEmptyMessageDelayed(TYPE_SEND, 200);
                        }
                        break;
                    case TYPE_SEND:
                        String data = mDataList.get(0);
                        //发送数据--在发送回调处进行数据处理
                        BLENormalImpl.super.sendData(data);
                        break;
                }
            }
        };
        releaseNormalHandler();
        mDataHandler.sendEmptyMessageDelayed(TYPE_CHECK, 200);
    }

    @Override
    public void init() {
        super.init();
        isRelease = false;
        setOnDataListener(dataBackListener);
    }

    @Override
    public void release() {
        super.release();
        isRelease = true;
        removeOnDataListener(dataBackListener);
        releaseNormalHandler();
    }

    @Override
    public void releaseAll() {
        super.releaseAll();
        isRelease = true;
        releaseNormalHandler();
    }

    private void releaseNormalHandler() {
        if (mDataHandler != null) {
            mDataHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void sendData(String data) {
        if (isRelease) {
            isRelease = false;
            releaseNormalHandler();
            mDataHandler.sendEmptyMessageDelayed(TYPE_CHECK, 200);
        }
        mDataList.add(mDataList.size(), data);
        BLELog.d(TAG, "发送数据:" + data);
    }

    private BLEDataBackListener dataBackListener = new BLEDataBackListener() {
        @Override
        public void sendCallBack(boolean success, String data) {
            if (success) {
                //发送成功，移除数据队列里面的数据，继续检查
                for (int i = mDataList.size() - 1; i >= 0; i--) {
                    if (data.equals(mDataList.get(i))) {
                        mDataList.remove(i);
                    }
                }
                mDataHandler.sendEmptyMessageDelayed(TYPE_CHECK, 200);
            } else {
                //发送失败，继续检查
                mDataHandler.sendEmptyMessageDelayed(TYPE_CHECK, 200);
            }
        }

        @Override
        public void receiveCallBack(String data) {

        }
    };

}
