package com.north.light.androidutils.novel.text.data.function;

import android.text.TextUtils;

import com.north.light.androidutils.novel.text.data.bean.TxtInfo;
import com.north.light.androidutils.novel.text.data.bean.TxtReadInfo;

import java.io.Serializable;
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

    public static class SingleHolder implements Serializable {
        static TxtMemoryManager mInstance = new TxtMemoryManager();
    }

    public static TxtMemoryManager getInstance() {
        return SingleHolder.mInstance;
    }

    //摘要---------------------------------------------------------------------------------------

    /**
     * 获取摘要数量
     */
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
     * 增加某个txt的缓存
     */
    public void setSum(String path, Map<Integer, TxtInfo> info) {
        if (info == null) {
            return;
        }
        //写入数据
        txtSumMap.put(path, info);
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
