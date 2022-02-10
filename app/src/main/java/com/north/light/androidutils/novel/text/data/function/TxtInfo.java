package com.north.light.androidutils.novel.text.data.function;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/2/10 8:53
 * @Description:读取信息
 */
public class TxtInfo implements Serializable {
    private int pos = 0;
    private int total = 0;
    private long size = 0;
    private String orgPath;
    private String trainPath;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public String getTrainPath() {
        return trainPath;
    }

    public void setTrainPath(String trainPath) {
        this.trainPath = trainPath;
    }
}
