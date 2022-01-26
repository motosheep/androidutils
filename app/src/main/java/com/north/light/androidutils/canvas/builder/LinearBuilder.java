package com.north.light.androidutils.canvas.builder;

import com.north.light.androidutils.R;
import com.north.light.androidutils.canvas.bean.LinearInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/1/25 8:24
 * @Description:直方图builder
 */
public class LinearBuilder extends BaseChartBuilder {

    /**
     * 数据
     */
    private List<LinearInfo> dataList = new ArrayList<>();

    /**
     * 背景颜色
     */
    private int bgColor = R.color.color_ffffff;
    /**
     * x轴颜色
     */
    private int xColor = R.color.color_000000;
    /**
     * y轴颜色
     */
    private int yColor = R.color.color_000000;
    

    public List<LinearInfo> getDataList() {
        return dataList;
    }

    public void setDataList(List<LinearInfo> dataList) {
        this.dataList = dataList;
    }


    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getXColor() {
        return xColor;
    }

    public void setXColor(int xColor) {
        this.xColor = xColor;
    }

    public int getYColor() {
        return yColor;
    }

    public void setYColor(int yColor) {
        this.yColor = yColor;
    }
}
