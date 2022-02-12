package com.north.light.androidutils.novel.text.data.function;

import com.north.light.androidutils.novel.text.data.bean.TxtInfo;
import com.north.light.androidutils.novel.text.data.bean.TxtReadInfo;

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
     * @param info
     */
    void splitPart(String path, String name, TxtInfo info);

    /**
     * @param path    原始文件路径
     * @param name    原始文件名字
     * @param infoMap 切割后的map集合
     */
    void splitFinish(String path, String name, Map<Integer, TxtInfo> infoMap);


    /**
     * @param e 异常exception
     */
    void splitFailed(Exception e);

    /**
     * 读取文件
     * */
    void read(TxtReadInfo info,String key);

    /**
     * 读取失败
     * */
    void readFailed(Exception e);

    /**
     * 分割进度
     * */
    void splitProgress(String path,String trainPath,long progress,long total);

}
