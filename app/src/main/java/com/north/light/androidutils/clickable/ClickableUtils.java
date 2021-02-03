package com.north.light.androidutils.clickable;

import android.view.View;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * author:li
 * date:2021/2/3
 * desc:点击事件工具类
 */
public class ClickableUtils {
    //点击实现保存map
    private ConcurrentMap<String, Long> mViewMap = new ConcurrentHashMap<String, Long>();
    //默认点击间隔--2秒
    private long mInterval = 2000;

    private static class SingleHolder {
        static ClickableUtils mInstance = new ClickableUtils();
    }

    public static ClickableUtils getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 判断view是否可以点击
     */
    public <T extends View> boolean canViewClick(T t) {
        return canViewClick(t, mInterval);
    }

    /**
     * 判断view是否可以点击
     */
    public <T extends View> boolean canViewClick(T t, long time) {
        try{
            if (t == null) {
                return false;
            }
            //判断是否超过期限
            Long lastTime = mViewMap.get(t.getClass().getSimpleName());
            if (lastTime == null) {
                //首次
                mViewMap.put(t.getClass().getSimpleName(), System.currentTimeMillis());
                return true;
            } else {
                //非首次--判断上次时间
                if (lastTime + time < System.currentTimeMillis()) {
                    mViewMap.put(t.getClass().getSimpleName(), System.currentTimeMillis());
                    //可以点击
                    return true;
                } else {
                    return false;
                }
            }
        }catch (Exception e){
            return false;
        }
    }
}
