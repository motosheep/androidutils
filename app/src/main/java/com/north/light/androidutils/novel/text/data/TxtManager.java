package com.north.light.androidutils.novel.text.data;

import android.content.Context;

import com.north.light.androidutils.novel.text.data.function.StreamReader;
import com.north.light.androidutils.novel.text.data.function.TxtIOStreamReader;
import com.north.light.androidutils.novel.text.data.function.TxtLoadingListener;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/2/10 9:57
 * @Description:txt管理类
 */
public class TxtManager implements Serializable {
    private StreamReader streamReader;

    public static class SingleHolder implements Serializable {
        static TxtManager mInstance = new TxtManager();
    }

    public static TxtManager getInstance() {
        return TxtManager.SingleHolder.mInstance;
    }

    public TxtManager() {
        streamReader = new TxtIOStreamReader();
    }

    public void loadTxt(Context context, String path) throws Exception {
        streamReader.load(context, path);
    }

    public void cancel(Context context, String path) throws Exception {
        streamReader.cancel(context, path);
    }

    public void setOnLoadingListener(TxtLoadingListener loadingListener) {
        if (loadingListener == null) {
            return;
        }
        streamReader.setOnLoadingListener(loadingListener);
    }

    public void removeLoadingListener(TxtLoadingListener loadingListener) {
        if (loadingListener == null) {
            return;
        }
        streamReader.removeLoadingListener(loadingListener);
    }

}
