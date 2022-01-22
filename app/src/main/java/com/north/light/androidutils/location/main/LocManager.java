package com.north.light.androidutils.location.main;

import com.north.light.androidutils.location.function.GPSLocationImp;
import com.north.light.androidutils.location.function.LocationApi;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/1/22 11:08
 * @Description:定位管理类
 */
public class LocManager implements Serializable {

    private LocationApi locationApi;

    private static class SingleHolder {
        static LocManager mInstance = new LocManager();
    }

    public static LocManager getInstance() {
        return SingleHolder.mInstance;
    }

    public LocationApi getLocation() {
        if (locationApi == null) {
            locationApi = new GPSLocationImp();
        }
        return locationApi;
    }

}
