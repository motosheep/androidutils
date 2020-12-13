package com.north.light.androidutils.permission;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.io.Serializable;

/**
 * author:li
 * date:2020/12/13
 * desc:权限管理
 */
public class PermissionManager implements Serializable {

    private static final class SingleHolder {
        static PermissionManager mInstance = new PermissionManager();
    }

    public static PermissionManager getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 检查权限
     */
    public Boolean checkPermission(Context context, String... permission) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        for (String per : permission) {
            if (pm.checkPermission(per, context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
