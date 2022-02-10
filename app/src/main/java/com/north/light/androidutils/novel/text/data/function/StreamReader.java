package com.north.light.androidutils.novel.text.data.function;

import android.content.Context;

/**
 * @Author: lzt
 * @Date: 2022/2/8 14:40
 * @Description:流读取
 */
public interface StreamReader {

    void load(Context context, String path) throws Exception;

    void cancel(Context context, String path) throws Exception;

    void setOnLoadingListener(TxtLoadingListener loadingListener);

    void removeLoadingListener(TxtLoadingListener loadingListener);
}
