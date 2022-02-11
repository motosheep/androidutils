package com.north.light.androidutils.novel.text.data.function;

import android.text.TextUtils;

import com.north.light.androidutils.novel.text.data.bean.TxtInfo;
import com.north.light.androidutils.novel.text.data.bean.TxtReadInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: lzt
 * @Date: 2022/2/9 14:08
 * @Description:文本内存管理
 */
public class TxtMemoryManager implements Serializable {
    /**
     * txt map集合--内容
     */
    private ConcurrentMap<String, Map<Integer, TxtReadInfo>> txtMap = new ConcurrentHashMap<>();
    /**
     * txt map集合--摘要
     */
    private ConcurrentMap<String, Map<Integer, TxtInfo>> txtSumMap = new ConcurrentHashMap<>();
    /**
     * 当前阅读的txt集合
     */
    private CopyOnWriteArrayList<String> currentListStr = new CopyOnWriteArrayList<>();

    public static class SingleHolder implements Serializable {
        static TxtMemoryManager mInstance = new TxtMemoryManager();
    }

    public static TxtMemoryManager getInstance() {
        return SingleHolder.mInstance;
    }


    //当前txt------------------------------------------------------------------------------------

    /**
     * 调整当前阅读的数据集合
     */
    public void resetCurList(List<String> org) {
        clearCur();
        currentListStr.addAll(org);
    }

    /**
     * 当前阅读的数据集合
     */
    public List<String> getCurList() {
        return currentListStr;
    }
    /**
     * 当前阅读的数据总长度
     */
    public long getCurLength() {
        long total = 0;
        for(String s :currentListStr){
            total = total+s.length();
        }
        return total;
    }

    /**
     * 清空
     */
    public void clearCur() {
        currentListStr.clear();
    }

    /**
     * 添加
     */
    public void addCur(List<String> str) {
        currentListStr.addAll(str);
    }


    //摘要---------------------------------------------------------------------------------------

    /**
     * 获取摘要数量
     * */
    public int getSumSize() {
        return txtSumMap.size();
    }

    /**
     * 清空某个txt的内存缓存
     */
    public void clearSum(String path) {
        txtSumMap.remove(path);
    }

    /**
     * 增加某个txt的缓存
     */
    public void addSum(String path, TxtInfo info) {
        //检查数据
        if (info.getTotal() == 0 || info.getSize() == 0 || info.getPos() == 0 ||
                TextUtils.isEmpty(info.getOrgPath()) ||
                TextUtils.isEmpty(info.getTrainPath())) {
            return;
        }
        //写入数据
        Map<Integer, TxtInfo> map = txtSumMap.get(path);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(info.getPos(), info);
        txtSumMap.put(path, map);
    }

    /**
     * 获取某个位置的数据
     */
    public TxtInfo getSum(String path, int pos) {
        Map<Integer, TxtInfo> map = txtSumMap.get(path);
        if (map == null) {
            return null;
        }
        return map.get(pos);
    }
    //内容---------------------------------------------------------------------------------------

    /**
     * 清空某个txt的内存缓存
     */
    public void clearContent(String path) {
        txtMap.remove(path);
    }

    /**
     * 增加某个txt的缓存
     */
    public void addContent(String path, TxtReadInfo info) {
        //检查数据
        if (info.getTotal() == 0 || info.getSize() == 0 || info.getPos() == 0 ||
                TextUtils.isEmpty(info.getContent()) ||
                TextUtils.isEmpty(info.getOrgPath()) ||
                TextUtils.isEmpty(info.getTrainPath())) {
            return;
        }
        //写入数据
        Map<Integer, TxtReadInfo> map = txtMap.get(path);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(info.getPos(), info);
        txtMap.put(path, map);
    }

    /**
     * 获取某个位置的数据
     */
    public TxtReadInfo getContent(String path, int pos) {
        Map<Integer, TxtReadInfo> map = txtMap.get(path);
        if (map == null) {
            return null;
        }
        return map.get(pos);
    }
}
