package com.north.light.androidutils.persistence;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mmkv.MMKV;

import java.io.Serializable;

/**
 * Created by lzt
 * time 2020/11/27
 * 描述：MMKV manager
 */
public class MMKVManager implements Serializable {
    private static final String DATA_PATH = "YSP_MMKV_DATA";
    private static final String TAG = MMKVManager.class.getSimpleName();
    private Context mContext;
    private MMKV mMMKVObj;

    private static final class SingleHolder {
        static MMKVManager mInstance = new MMKVManager();
    }

    public static MMKVManager getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 初始化--只需要调用一次
     */
    public void init(Context context) {
        try {
            mContext = context.getApplicationContext();
            MMKV.initialize(mContext);
            mMMKVObj = MMKV.mmkvWithID(DATA_PATH);
        } catch (Exception e) {
            Log.d(TAG, "init失败:" + e.getMessage());
        }
    }

    /**
     * 放入数据
     */
    public synchronized <T> void put(String key, T t) {
        if (TextUtils.isEmpty(key)) return;
        try {
            if (t instanceof Integer) {
                mMMKVObj.encode(key, (Integer) t);
            } else if (t instanceof Long) {
                mMMKVObj.encode(key, (Long) t);
            } else if (t instanceof Boolean) {
                mMMKVObj.encode(key, (Boolean) t);
            } else if (t instanceof Float) {
                mMMKVObj.encode(key, (Float) t);
            } else if (t instanceof String) {
                mMMKVObj.encode(key, (String) t);
            }
        } catch (Exception e) {
            Log.d(TAG, "插入失败:" + e.getMessage());
        }
    }

    /**
     * 删除数据
     */
    public synchronized void clear(String key) {
        if (TextUtils.isEmpty(key)) return;
        try {
            mMMKVObj.removeValueForKey(key);
        } catch (Exception e) {
            Log.d(TAG, "clear失败:" + e.getMessage());
        }
    }

    public synchronized String getString(String key) {
        if (TextUtils.isEmpty(key)) return null;
        try {
            return mMMKVObj.decodeString(key, "");
        } catch (Exception e) {
            Log.d(TAG, "getString失败:" + e.getMessage());
            return "";
        }
    }

    public synchronized int getInt(String key) {
        if (TextUtils.isEmpty(key)) return -1;
        try {
            return mMMKVObj.decodeInt(key, -1);
        } catch (Exception e) {
            Log.d(TAG, "getInt失败:" + e.getMessage());
            return -1;
        }
    }

    public synchronized long getLong(String key) {
        if (TextUtils.isEmpty(key)) return -1L;
        try {
            return mMMKVObj.decodeLong(key, -1);
        } catch (Exception e) {
            Log.d(TAG, "getLong失败:" + e.getMessage());
            return -1;
        }
    }


    public synchronized float getFloat(String key) {
        if (TextUtils.isEmpty(key)) return -1f;
        try {
            return mMMKVObj.decodeFloat(key, -1);
        } catch (Exception e) {
            Log.d(TAG, "getFloat失败:" + e.getMessage());
            return -1;
        }
    }

    public synchronized boolean getBoolean(String key) {
        if (TextUtils.isEmpty(key)) return false;
        try {
            return mMMKVObj.getBoolean(key, false);
        } catch (Exception e) {
            Log.d(TAG, "getBoolean失败:" + e.getMessage());
            return false;
        }
    }
}
