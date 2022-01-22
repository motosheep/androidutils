package com.north.light.androidutils.location.function;

/**
 * @Author: lzt
 * @Date: 2022/1/22 14:20
 * @Description:
 */
public interface LocationStatusCallBack {
    void NoPermission();//没有权限

    void GPSClose();//手机gps关闭了

    void NoInit();//没有初始化

    void Pos(double latitude, double longitude);//位置回调
}
