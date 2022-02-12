package com.north.light.androidutils.novel.text.data;

/**
 * @Author: lzt
 * @Date: 2022/2/10 14:43
 * @Description:txt manager api
 */
public interface TxtManagerListener {

    /**
     * 读取书本错误
     */
    void readBookError(Exception e);

    /**
     * 准备完毕，可以进行初始化
     */
    void ready(String path,String preViewContent);

    /**
     * 读取进度
     */
    void progress(String path, String trainPath, long curPos, long totalPos);
}
