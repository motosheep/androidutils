package com.north.light.androidutils.canvas.base;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/1/24 15:04
 * @Description:图表builder
 */
public class BaseChartBuilder implements Serializable {

    //控件宽度
    private int width = -1;
    //控件高度
    private int height = -1;
    //是否自适应宽高
    private boolean autoFix = true;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAutoFix() {
        return autoFix;
    }

    public void setAutoFix(boolean autoFix) {
        this.autoFix = autoFix;
    }
}
