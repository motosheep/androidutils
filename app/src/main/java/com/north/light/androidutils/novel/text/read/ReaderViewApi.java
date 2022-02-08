package com.north.light.androidutils.novel.text.read;

/**
 * @Author: lzt
 * @Date: 2022/2/8 9:47
 * @Description:阅读view interface
 */
public interface ReaderViewApi {

    /**
     * 设置模式
     */
    void setMode(ReaderAnimMode animMode);

    /**
     * 获取当前模式
     */
    ReaderAnimMode getMode();
}
