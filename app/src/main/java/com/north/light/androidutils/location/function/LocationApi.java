package com.north.light.androidutils.location.function;

import android.content.Context;


/**
 * @Author: lzt
 * @Date: 2022/1/22 11:04
 * @Description:
 */
public interface LocationApi {
    void init(Context context);

    void start();//开始定位

    void stop();//停止定位

    void release();//释放所有资源，需要重新初始化

    void setLocListener(LocationStatusCallBack callBack);

    void removeLocListener(LocationStatusCallBack callBack);


}

