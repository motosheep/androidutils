package com.north.light.androidutils.water.compress.function;

import java.io.InputStream;

/**
 * @Author: lzt
 * @Date: 2022/1/28 9:10
 * @Description:图片流provider--输入时转换
 */
public interface PicStreamProvider {
    InputStream getStream() throws Exception;

    void close() throws Exception;

    int width();

    int height();

    int qualityRate();

    String outputPath();

}
