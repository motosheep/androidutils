package com.north.light.libumeng;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * @Author: lzt
 * @CreateDate: 2021/8/17 14:12
 * @Version: 1.0
 * @Description:友盟工具类--增加加载开关控制
 */
public class UMengUtils extends UMManager {
    /**
     * 是否允许初始化标识
     */
    private boolean isAllowInit = false;
    /**
     * 友盟初始化handler
     */
    private Handler mInitHandler;
    /**
     * 延时初始化时间
     */
    private static final long HANDLER_INIT_DELAY_TIME = 10000;

    public void setAllowInit(boolean allowInit) {
        isAllowInit = allowInit;
    }

    private static final class SingleHolder {
        static UMengUtils mInstance = new UMengUtils();
    }

    public static UMengUtils getInstance() {
        return SingleHolder.mInstance;
    }

    @Override
    public void init(Context context, String appkey, String channel, int deviceType, String pushSecret, int mode) {
        initHandler();
        if (isAllowInit) {
            super.init(context, appkey, channel, deviceType, pushSecret, mode);
        } else {
            //延时任务初始化
            mInitHandler.removeCallbacksAndMessages(null);
            mInitHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAllowInit) {
                        init(context, appkey, channel, deviceType, pushSecret, mode);
                    } else {
                        //再次延时
                        mInitHandler.postDelayed(this, HANDLER_INIT_DELAY_TIME);
                    }
                }
            }, HANDLER_INIT_DELAY_TIME);
        }
    }

    @Override
    public void init(Context context, int deviceType, String pushSecret, int mode) {
        initHandler();
        if (isAllowInit) {
            super.init(context, deviceType, pushSecret, mode);
        } else {
            //延时任务初始化
            mInitHandler.removeCallbacksAndMessages(null);
            mInitHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAllowInit) {
                        init(context, deviceType, pushSecret, mode);
                    } else {
                        //再次延时
                        mInitHandler.postDelayed(this, HANDLER_INIT_DELAY_TIME);
                    }
                }
            }, HANDLER_INIT_DELAY_TIME);
        }
    }

    /**
     * 初始化handler
     */
    private void initHandler() {
        if (mInitHandler == null) {
            mInitHandler = new Handler(Looper.getMainLooper());
        }
    }

    @Override
    public void release() {
        super.release();
        if (mInitHandler != null) {
            mInitHandler.removeCallbacksAndMessages(null);
        }
    }
}
