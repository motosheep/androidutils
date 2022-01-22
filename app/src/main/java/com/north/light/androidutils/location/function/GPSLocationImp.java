package com.north.light.androidutils.location.function;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @Author: lzt
 * @Date: 2022/1/22 11:04
 * @Description:
 */
public class GPSLocationImp implements LocationApi {
    private static final String TAG = GPSLocationImp.class.getName();
    private Context mContext;
    private LocationManager mGPSLocation;
    //默认定位时间间隔为5s一次
    private long mInterval = LocationConstant.TIME_LOCATION_INTERVAL;
    //默认定位再次触发为0米
    private long mDistance = 0;
    //是否init标识
    private volatile boolean isInit = false;
    //handler
    private Handler mHandler;
    //监听集合
    private CopyOnWriteArrayList<LocationStatusCallBack> mListener = new CopyOnWriteArrayList<>();
    private List<GpsSatellite> numSatelliteList = new ArrayList<>();

    @Override
    public void init(Context context) {
        if (context == null) return;
        if (isInit) return;
        mContext = context.getApplicationContext();
        mGPSLocation = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler.removeCallbacksAndMessages(null);
        }
        isInit = true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void start() {
        //只使用gps进行定位
        if (!isInit) {
            for (LocationStatusCallBack callBack : mListener) {
                callBack.NoInit();
            }
            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            for (LocationStatusCallBack callBack : mListener) {
                callBack.NoPermission();
            }
            return;
        }
//        mGPSLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        boolean isOpenGPS = mGPSLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOpenGPS) {
            for (LocationStatusCallBack callBack : mListener) {
                callBack.GPSClose();
            }
            return;
        }
        mGPSLocation.addGpsStatusListener(new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                Log.d(TAG, "onGpsStatusChanged:" + event);
                GpsStatus status = mGPSLocation.getGpsStatus(null); // 取当前状态
                updateGpsStatus(event, status);
                Log.d(TAG, "onGpsStatusChanged count:" + numSatelliteList.size());
            }
        });
        mGPSLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, mInterval, mDistance, mLocationListener);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void stop() {
        if (mGPSLocation != null) {
            mGPSLocation.removeUpdates(mLocationListener);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void release() {
        stop();
        mGPSLocation = null;
        isInit = false;
    }

    @Override
    public void setLocListener(LocationStatusCallBack callBack) {
        if (callBack != null) {
            mListener.add(callBack);
        }
    }

    @Override
    public void removeLocListener(LocationStatusCallBack callBack) {
        if (callBack != null) {
            mListener.remove(callBack);
        }
    }

    /**
     * 更新gps状态
     * */
    private void updateGpsStatus(int event, GpsStatus status) {
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            numSatelliteList.clear();
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = it.next();
                if (s.getSnr() != 0)//只有信躁比不为0的时候才算搜到了星
                {
                    numSatelliteList.add(s);
                    count++;
                }
            }
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //得到纬度
            double latitude = location.getLatitude();
            //得到经度
            double longitude = location.getLongitude();
            //原生gps84转gcj02
            double[] pos = GPSUtils.gps84_To_Gcj02(latitude, longitude);
            for (LocationStatusCallBack callBack : mListener) {
                callBack.Pos(pos[0], pos[1]);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged status: " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");
        }
    };

}

