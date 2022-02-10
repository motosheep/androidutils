package com.north.light.androidutils.novel.text.data.function;

import java.util.Map;

/**
 * @Author: lzt
 * @Date: 2022/2/10 8:50
 * @Description:txt加载监听
 */
public interface TxtLoadingListener {

    /**
     * @param path  切割文件的路径
     * @param name  切割文件的名字
     * @param pos   排序位置
     * @param total 总数量
     */
    void loadingPart(String path, String name, int pos, int total);

    /**
     * @param path    原始文件路径
     * @param name    原始文件名字
     * @param infoMap 切割后的map集合
     */
    void loadingFinish(String path, String name, Map<Integer, TxtInfo> infoMap);


    /**
     * @param e 异常exception
     */
    void loadFailed(Exception e);
}
