package com.north.light.androidutils.glide.func.progress.callback;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * FileName: GlideProgressManager
 * Author: lzt
 * Date: 2022/6/9 14:55
 * glide进度管理类
 */
public class GlideProgressManager implements Serializable {

    private CopyOnWriteArrayList<GlideProgressListener> mListener = new CopyOnWriteArrayList<>();

    private static final class SingleHolder {
        static GlideProgressManager mInstance = new GlideProgressManager();
    }

    public static GlideProgressManager getInstance() {
        return SingleHolder.mInstance;
    }

    //内部调用
    public void notifyProgress(String url, int progress, boolean isFinish) {
        if (mListener == null || mListener.size() == 0) {
            return;
        }
        //遍历通知
        for (GlideProgressListener listener : mListener) {
            listener.onProgress(url, progress, isFinish);
        }
    }


    //外部调用
    public void setGlideProgressListener(GlideProgressListener listener) {
        if (listener == null) {
            return;
        }
        mListener.add(listener);
    }

    public void removeGlideProgressListener(GlideProgressListener listener) {
        if (listener == null) {
            return;
        }
        mListener.remove(listener);
    }


}
