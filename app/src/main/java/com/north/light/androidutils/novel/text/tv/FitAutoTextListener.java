package com.north.light.androidutils.novel.text.tv;

/**
 * @Author: lzt
 * @Date: 2022/2/12 15:03
 * @Description:
 */
public interface FitAutoTextListener extends FitTextListener{

    /**
     * 实际绘制字数
     * */
    void trueDrawCount(int count);
}
