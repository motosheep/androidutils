package com.north.light.androidutils.novel.text.tv;

/**
 * @Author: lzt
 * @Date: 2022/2/12 15:03
 * @Description:
 */
public interface FitAutoTextListener {

    /**
     * 实际绘制字数
     */
    void trueDrawCount(int count);

    /**
     * 最多可绘制的字数
     */
    void maxDrawCount(int count);
}
