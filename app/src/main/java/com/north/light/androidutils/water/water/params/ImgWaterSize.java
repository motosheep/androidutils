package com.north.light.androidutils.water.water.params;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/2/7 9:33
 * @Description:水印图片大小宽高参数
 */
public class ImgWaterSize implements Serializable {
    private float heightPercent = 0.1f;
    private float widthPercent= 0.1f;
    private float padLeftPercent = 0.1f;
    private float padRightPercent = 0.1f;
    private float padTopPercent = 0.1f;
    private float padBottomPercent = 0.1f;
    private int compressRate = 20;

    public int getCompressRate() {
        return compressRate;
    }

    public void setCompressRate(int compressRate) {
        this.compressRate = compressRate;
    }

    public float getHeightPercent() {
        return heightPercent;
    }

    public void setHeightPercent(float heightPercent) {
        this.heightPercent = heightPercent;
    }

    public float getWidthPercent() {
        return widthPercent;
    }

    public void setWidthPercent(float widthPercent) {
        this.widthPercent = widthPercent;
    }

    public float getPadLeftPercent() {
        return padLeftPercent;
    }

    public void setPadLeftPercent(float padLeftPercent) {
        this.padLeftPercent = padLeftPercent;
    }

    public float getPadRightPercent() {
        return padRightPercent;
    }

    public void setPadRightPercent(float padRightPercent) {
        this.padRightPercent = padRightPercent;
    }

    public float getPadTopPercent() {
        return padTopPercent;
    }

    public void setPadTopPercent(float padTopPercent) {
        this.padTopPercent = padTopPercent;
    }

    public float getPadBottomPercent() {
        return padBottomPercent;
    }

    public void setPadBottomPercent(float padBottomPercent) {
        this.padBottomPercent = padBottomPercent;
    }
}
