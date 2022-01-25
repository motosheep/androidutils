package com.north.light.androidutils.canvas.builder;

import com.north.light.androidutils.R;
import com.north.light.androidutils.canvas.bean.CircleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/1/25 8:24
 * @Description:饼状图builder
 */
public class CircleBuilder extends BaseChartBuilder {

    /**
     * 数据
     */
    private List<CircleInfo> dataList = new ArrayList<>();
    /**
     * 底圆颜色
     */
    private int bottomColor = R.color.color_F06091;

    public List<CircleInfo> getDataList() {
        return dataList;
    }

    public void setDataList(List<CircleInfo> dataList) {
        this.dataList = dataList;
    }

    public int getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(int bottomColor) {
        this.bottomColor = bottomColor;
    }
}
