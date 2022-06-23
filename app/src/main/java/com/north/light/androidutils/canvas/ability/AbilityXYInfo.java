package com.north.light.androidutils.canvas.ability;

import com.north.light.androidutils.R;

import java.io.Serializable;

/**
 * FileName: AbilityXYInfo
 * Author: lzt
 * Date: 2022/6/22 11:06
 */
public class AbilityXYInfo implements Serializable {

    //X轴颜色
    private int xColor = R.color.colorPrimary;
    //X轴粗
    private int xSize = 4;
    //Y轴颜色
    private int yColor = R.color.colorPrimary;
    //Y轴粗
    private int ySize = 4;
    //x轴右侧字体
    private String xEndDesc = "正确率";
    private int xEndColor = R.color.color_a8a8a8;
    private float xEndSize = 38;

    public float getxEndSize() {
        return xEndSize;
    }

    public void setxEndSize(float xEndSize) {
        this.xEndSize = xEndSize;
    }

    public int getxEndColor() {
        return xEndColor;
    }

    public void setxEndColor(int xEndColor) {
        this.xEndColor = xEndColor;
    }

    public String getxEndDesc() {
        return xEndDesc;
    }

    public void setxEndDesc(String xEndDesc) {
        this.xEndDesc = xEndDesc;
    }

    public int getxColor() {
        return xColor;
    }

    public void setxColor(int xColor) {
        this.xColor = xColor;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public int getyColor() {
        return yColor;
    }

    public void setyColor(int yColor) {
        this.yColor = yColor;
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }
}
