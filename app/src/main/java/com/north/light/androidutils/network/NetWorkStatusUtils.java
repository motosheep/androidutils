package com.north.light.androidutils.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:li
 * date:2021/3/7
 * desc:
 */
public class NetWorkStatusUtils {
    //没有网络连接
    private static final int NETWORN_NONE = 0;
    //wifi连接
    private static final int NETWORN_WIFI = 1;
    //手机网络数据连接类型
    private static final int NETWORN_2G = 2;
    private static final int NETWORN_3G = 3;
    private static final int NETWORN_4G = 4;
    private static final int NETWORN_5G = 5;
    private static final int NETWORN_MOBILE = 6;
    //全局context
    private Context mContext;
    //网路状态改变广播
    private NetStatusReceiver mNetStatusReceiver;
    private IntentFilter mNetStatusFilter;
    //tag
    private static final String TAG = NetWorkStatusUtils.class.getSimpleName();
    private CopyOnWriteArrayList<NetStatusListener> mListener = new CopyOnWriteArrayList<>();

    private static final class SingleHolder {
        static NetWorkStatusUtils mInstance = new NetWorkStatusUtils();
    }

    public static NetWorkStatusUtils getInstance() {
        return SingleHolder.mInstance;
    }


    /**
     * 初始化--必须调用
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        if (mNetStatusReceiver == null) {
            mNetStatusReceiver = new NetStatusReceiver();
        }
        if (mNetStatusFilter == null) {
            mNetStatusFilter = new IntentFilter();
            mNetStatusFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        }
        try{
            mContext.unregisterReceiver(mNetStatusReceiver);
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        mContext.registerReceiver(mNetStatusReceiver, mNetStatusFilter);
    }

    /**
     * 释放
     */
    public void release() {
        if (mContext == null) {
            return;
        }
        try {
            mContext.unregisterReceiver(mNetStatusReceiver);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


    /**
     * 网络连接是否可用
     */
    public boolean isNetworkConnect() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取当前网络连接类型
     *
     * @return 无网络(返回0)、WF(返回1)、2G(返回2)、3G(返回3)、4G(返回4)、5G(返回5) 网络
     */
    public int getNetworkState() {
        try {
            //获取系统的网络服务
            ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            //如果当前没有网络
            if (null == connManager)
                return NETWORN_NONE;
            //获取当前网络类型，如果为空，返回无网络
            @SuppressLint("MissingPermission") NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
                return NETWORN_NONE;
            }
            // 判断是不是连接的是不是wifi
            @SuppressLint("MissingPermission") NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != wifiInfo) {
                NetworkInfo.State state = wifiInfo.getState();
                if (null != state)
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        return NETWORN_WIFI;
                    }
            }
            // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != networkInfo) {
                NetworkInfo.State state = networkInfo.getState();
                String strSubTypeName = networkInfo.getSubtypeName();
                if (null != state)
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        switch (activeNetInfo.getSubtype()) {
                            //如果是2g类型
                            case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                            case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                            case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                return NETWORN_2G;
                            //如果是3g类型
                            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                return NETWORN_3G;
                            //如果是4g类型
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                return NETWORN_4G;
                            //TelephonyManager.NETWORK_TYPE_NR
                            case 20: //对应的20 只有依赖为android 10.0才有此属性
                                return NETWORN_5G;
                            default:
                                //中国移动 联通 电信 三种3G制式
                                if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") ||
                                        strSubTypeName.equalsIgnoreCase("WCDMA") ||
                                        strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                    return NETWORN_3G;
                                } else {
                                    return NETWORN_MOBILE;
                                }
                        }
                    }
            }
        } catch (Exception e) {

        }
        return NETWORN_NONE;
    }

    /**
     * 网络状态改变广播
     */
    private class NetStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "网络状态发生变化");
            for (NetStatusListener listener : mListener) {
                listener.status(getNetworkState());
            }
        }
    }

    /**
     * 监听事件
     */
    public interface NetStatusListener {
        //无网络(返回0)、WF(返回1)、2G(返回2)、3G(返回3)、4G(返回4)、5G(返回5) 网络
        void status(int status);
    }

    public void setOnNetStatusListener(NetStatusListener listener) {
        mListener.add(listener);
    }

    public void removeNetStatusListener(NetStatusListener listener) {
        mListener.remove(listener);
    }
}
