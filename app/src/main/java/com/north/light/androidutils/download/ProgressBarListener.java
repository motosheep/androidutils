package com.north.light.androidutils.download;

/**
 * author:li
 * date:2020/12/13
 * desc:下载监听接口
 */
public interface ProgressBarListener {
    /**
     * 获取文件的长度
     * @param length
     */
    void getMax(int length);
    /**
     * 获取每次下载的长度
     * @param length
     */
    void getDownload(int length);
}
