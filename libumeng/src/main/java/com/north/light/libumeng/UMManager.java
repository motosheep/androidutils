package com.north.light.libumeng;

import android.content.Context;

import com.north.light.libumeng.api.UMActivityApi;
import com.north.light.libumeng.api.UMInitApi;
import com.north.light.libumeng.api.UMManagerApi;
import com.north.light.libumeng.api.UMNoActivityApi;
import com.north.light.libumeng.constant.CollectMode;
import com.north.light.libumeng.impl.UMActivityImpl;
import com.north.light.libumeng.impl.UMInitImpl;
import com.north.light.libumeng.impl.UMNoActivityImpl;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 16:07
 * @Version: 1.0
 * @Description:友盟管理类
 * 61011d8026e9627944aac7c7
 */
public class UMManager implements UMManagerApi {
    /**
     * 是否初始化sdk标识
     */
    private boolean isInit = false;
    /**
     * 初始化实现obj
     */
    private UMInitApi umInit;
    /**
     * activity实现obj
     */
    private UMActivityApi umActivityApi;
    /**
     * 非activity实现obj
     */
    private UMNoActivityApi umNoActivityApi;


    public UMManager() {
        umInit = new UMInitImpl();
        umActivityApi = new UMActivityImpl();
        umNoActivityApi = new UMNoActivityImpl();
    }

    /**
     * 判断当前模式
     */
    @Override
    public void onActivityResume(Context context) {
        if (!isInit) {
            return;
        }
        switch (getCollectMode()) {
            case MODE_AUTO:
                break;
            case MODE_MANUAL:
            case MODE_LEGAVCY_AUTO:
            case MODE_LEGAVCY_MANUAL:
                umActivityApi.onActivityResume(context);
                break;
        }
    }

    @Override
    public void onActivityPause(Context context) {
        if (!isInit) {
            return;
        }
        switch (getCollectMode()) {
            case MODE_AUTO:
                break;
            case MODE_MANUAL:
            case MODE_LEGAVCY_AUTO:
            case MODE_LEGAVCY_MANUAL:
                umActivityApi.onActivityPause(context);
                break;
        }
    }

    @Override
    public void onPageStart(String viewName) {
        if (!isInit) {
            return;
        }
        switch (getCollectMode()) {
            case MODE_LEGAVCY_AUTO:
                break;
            case MODE_MANUAL:
            case MODE_LEGAVCY_MANUAL:
            case MODE_AUTO:
                umNoActivityApi.onPageStart(viewName);
                break;
        }
    }

    @Override
    public void onPageEnd(String viewName) {
        if (!isInit) {
            return;
        }
        switch (getCollectMode()) {
            case MODE_LEGAVCY_AUTO:
                break;
            case MODE_MANUAL:
            case MODE_LEGAVCY_MANUAL:
            case MODE_AUTO:
                umNoActivityApi.onPageEnd(viewName);
                break;
        }
    }

    private static final class SingleHolder {
        static UMManager mInstance = new UMManager();
    }

    public static UMManager getInstance() {
        return SingleHolder.mInstance;
    }


    @Override
    public void init(Context context, String appkey, String channel, int deviceType, String pushSecret, int mode) {
        if (isInit) {
            return;
        }
        umInit.init(context, appkey, channel, deviceType, pushSecret, mode);
        isInit = true;
    }

    @Override
    public void init(Context context, int deviceType, String pushSecret, int mode) {
        if (isInit) {
            return;
        }
        umInit.init(context, deviceType, pushSecret, mode);
        isInit = true;
    }

    @Override
    public CollectMode getCollectMode() {
        return umInit.getCollectMode();
    }


}
