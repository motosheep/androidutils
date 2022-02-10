package com.north.light.androidutils.novel.text.data.function;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: lzt
 * @Date: 2022/2/9 14:08
 * @Description:文本内存管理
 */
public class TxtMemoryManager implements Serializable {
    /**
     * 小说map集合--只保留了分割后路径和大小等，没有保留具体信息
     */
    private ConcurrentMap<String, Map<Integer, TxtInfo>> txtMap = new ConcurrentHashMap<>();

    public static class SingleHolder implements Serializable {
        static TxtMemoryManager mInstance = new TxtMemoryManager();
    }

    public static TxtMemoryManager getInstance() {
        return SingleHolder.mInstance;
    }

    public void set(String path, Map<Integer, TxtInfo> mapInfo) {
        txtMap.put(path, mapInfo);
    }

}
