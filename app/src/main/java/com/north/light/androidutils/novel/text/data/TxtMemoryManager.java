package com.north.light.androidutils.novel.text.data;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: lzt
 * @Date: 2022/2/9 14:08
 * @Description:文本内存管理
 */
public class TxtMemoryManager implements Serializable {
    /**
     * 小说map集合
     */
    private ConcurrentMap<String, List<String>> txtMap = new ConcurrentHashMap<>();


    public static class SingleHolder implements Serializable {
        static TxtMemoryManager mInstance = new TxtMemoryManager();
    }

    public static TxtMemoryManager getInstance() {
        return SingleHolder.mInstance;
    }

    public void set(String path, List<String> strList) {
        txtMap.put(path, strList);
    }

}
