package com.north.light.androidutils.brand;

import android.os.Build;
import android.text.TextUtils;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/30 14:59
 * @Version: 1.0
 * @Description: 手机品牌工具类
 */
public class BrandUtils {
    private final String XIAOMI = "xiaomi";
    private final String HUAWEI = "huawei";
    private final String OPPO = "oppo";
    private final String VIVO = "vivo";
    private final String MEIZU = "meizu";
    private final String MEIZU2 = "22c4185e";

    private static class SingleHolder {
        static BrandUtils mInstance = new BrandUtils();
    }

    public static BrandUtils getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 获取手机的品牌
     */
    public String getBrand() {
        if (!TextUtils.isEmpty(Build.BRAND)) {
            return Build.BRAND;
        } else if (!TextUtils.isEmpty(Build.MANUFACTURER)) {
            return Build.MANUFACTURER;
        }
        return "";
    }

    public boolean isBrandXiaoMi() {
        return getBrand().toLowerCase().equals(XIAOMI);
    }

    public boolean isBrandHuawei() {
        return getBrand().toLowerCase().equals(HUAWEI);
    }

    public boolean isBrandMeizu() {
        return getBrand().toLowerCase().equals(MEIZU) || getBrand().toLowerCase().equals(MEIZU2);
    }

    public boolean isBrandOppo() {
        return getBrand().toLowerCase().equals(OPPO);
    }

    public boolean isBrandVivo() {
        return getBrand().toLowerCase().equals(VIVO);
    }

}
