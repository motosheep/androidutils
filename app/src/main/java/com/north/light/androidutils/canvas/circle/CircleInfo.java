package com.north.light.androidutils.canvas.circle;

import com.north.light.androidutils.R;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/1/24 14:55
 * @Description:饼状图信息
 */
public class CircleInfo implements Serializable {

    /**
     * 百分比
     */
    private float percent = 0;
    /**
     * 绘制的颜色
     */
    private int color = R.color.color_000000;

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        if (percent < 0) {
            return;
        }
        this.percent = percent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
