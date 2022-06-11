package com.north.light.androidutils.glide.func.progress.callback;

/**
 * FileName: GlideProgressListener
 * Author: lzt
 * Date: 2022/6/8 16:32
 */
public interface GlideProgressListener {

    void onProgress(String url, int progress, boolean isFinish);

}