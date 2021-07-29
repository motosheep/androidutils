
package com.north.light.androidutils.wechat;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/29 11:35
 * @Version: 1.0
 * @Description:检测微信是否安装的工具
 */
public class WeChatInstallUtils implements Serializable {

    private static class SingleHolder implements Serializable {
        static WeChatInstallUtils mInstance = new WeChatInstallUtils();
    }

    public static WeChatInstallUtils getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 检查微信是否安装
     */
    public boolean isInstallWeChat(Context context, String wxAppId) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, wxAppId);
        boolean bIsWXAppInstalledAndSupported = (wxApi.isWXAppInstalled() && wxApi.getWXAppSupportAPI() != 0);
        if (!bIsWXAppInstalledAndSupported) {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

}
