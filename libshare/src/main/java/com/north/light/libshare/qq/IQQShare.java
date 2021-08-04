package com.north.light.libshare.qq;

import android.app.Activity;
import android.content.Intent;

/**
 * @Author: lzt
 * @CreateDate: 2021/8/4 15:07
 * @Version: 1.0
 * @Description: qq share interface
 */
public interface IQQShare {
    /**
     * 初始化
     *
     * @param appId
     */
    public void init(String appId);

    /**
     * （3） 特别注意：
     * 应用调用Andriod_SDK接口时，如果要成功接收到回调，
     * 需要在调用接口的Activity的onActivityResult方法中增加如下代码：
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * 分享图文信息
     */
    public void shareImageTxt(Activity baseAct, String jumpUrl, String title,
                              String desc, String picUrl, String backTxt, String appName,
                              int extra, QQBaseUIListener listener);

    /**
     * 分享纯图片
     */
    public void shareImage(Activity baseAct, String appName, String picLocalPath,
                           QQBaseUIListener listener);
}
