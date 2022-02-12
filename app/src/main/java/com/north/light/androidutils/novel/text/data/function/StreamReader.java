package com.north.light.androidutils.novel.text.data.function;

import android.content.Context;

import com.north.light.androidutils.novel.text.data.bean.TxtInfo;

/**
 * @Author: lzt
 * @Date: 2022/2/8 14:40
 * @Description:流读取
 */
public interface StreamReader {

    /**
     * 分割文件
     */
    void split(Context context, String path) throws Exception;

    /**
     * 读取文件所有信息
     */
    void read(Context context, TxtInfo txtInfo,String key) throws Exception;

    /**
     * 取消文件一切操作
     */
    void cancel(Context context, String path) throws Exception;

    /**
     * 设置监听
     */
    void setOnLoadingListener(TxtLoadingListener loadingListener);

    /**
     * 移除监听
     */
    void removeLoadingListener(TxtLoadingListener loadingListener);
}
