package com.north.light.androidutils.mulchannel;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 17:43
 * @Version: 1.0
 * @Description:多渠道工具
 */
public class ChannelUtils {

    public static String getChannel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            //key为<meta-data>标签中的name
            String channel = appInfo.metaData.getString("UMENG_CHANNEL");
            if (!TextUtils.isEmpty(channel)) {
                return channel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
