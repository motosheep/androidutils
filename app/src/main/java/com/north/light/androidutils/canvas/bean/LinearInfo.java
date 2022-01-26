package com.north.light.androidutils.canvas.bean;

import com.north.light.androidutils.R;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/1/24 14:55
 * @Description:直方图信息
 */
public class LinearInfo implements Serializable {

    /**
     * y轴数据
     */
    private float yData;
    /**
     * 绘制的颜色
     */
    private int color = R.color.color_000000;

    public float getYData() {
        return yData;
    }

    public void setYData(float yData) {
        this.yData = yData;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
