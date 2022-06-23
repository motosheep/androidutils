package com.north.light.androidutils.canvas.ability;

import android.graphics.Color;

import com.north.light.androidutils.R;

import java.io.Serializable;

/**
 * FileName: AbilityTxInfo
 * Author: lzt
 * Date: 2022/6/22 10:35
 * 能力图谱信息
 */
public class AbilityTxInfo implements Serializable {
    //标题
    private String title;
    //标题文字大小
    private float titleTxSize = 38;
    //标题文字颜色
    private int titleColor = R.color.color_000000;
    //进度：0-100
    private int progress;
    //进度显示阈值
    private int progressSwitch = 0;
    //进度描述
    private String progressDesc = "未达到做题数量";
    //进度字体大小
    private float progressSize = 30;
    //进度文字颜色
    private int progressTxColor = R.color.color_a8a8a8;
    //开始颜色
    private int startColor = R.color.colorPrimary;
    //结束颜色
    private int endColor = R.color.colorPrimaryDark;

    public int getProgressTxColor() {
        return progressTxColor;
    }

    public void setProgressTxColor(int progressTxColor) {
        this.progressTxColor = progressTxColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public float getProgressSize() {
        return progressSize;
    }

    public void setProgressSize(float progressSize) {
        this.progressSize = progressSize;
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    public float getTitleTxSize() {
        return titleTxSize;
    }

    public void setTitleTxSize(float titleTxSize) {
        this.titleTxSize = titleTxSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgressSwitch() {
        return progressSwitch;
    }

    public void setProgressSwitch(int progressSwitch) {
        this.progressSwitch = progressSwitch;
    }

    public String getProgressDesc() {
        return progressDesc;
    }

    public void setProgressDesc(String progressDesc) {
        this.progressDesc = progressDesc;
    }
}
