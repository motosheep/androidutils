package com.north.light.androidutils.water.compress.function;

import java.io.InputStream;

/**
 * @Author: lzt
 * @Date: 2022/1/28 10:04
 * @Description:图片流处理adpater
 */
public abstract class PicStreamAdapter implements PicStreamProvider {
    private InputStream inputStream;


    @Override
    public InputStream getStream() throws Exception {
        inputStream = getInput();
        return inputStream;
    }

    public abstract InputStream getInput() throws Exception;

    @Override
    public void close() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Override
    public int width() {
        return 0;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public int qualityRate() {
        return ImgConstant.IMG_COMPRESS_QUALITY_RATE;
    }

    @Override
    public String outputPath() {
        return null;
    }

}
