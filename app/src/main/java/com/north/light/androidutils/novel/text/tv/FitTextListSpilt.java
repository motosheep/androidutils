package com.north.light.androidutils.novel.text.tv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzt
 * time 2021/2/4
 *
 * @author lizhengting
 * 描述：list分割
 */
public class FitTextListSpilt implements Serializable {


    public static List<String> splitStr(String str) {
        List<String> contentList = new ArrayList<>();
        for (int split = 0; split < str.length(); split++) {
            contentList.add(str.substring(split, split + 1));
        }
        return contentList;
    }

    public static <T> List<List<T>> splitList(List<T> list, int groupSize) {
        int length = list.size();
        // 计算可以分成多少组
        int num = (length + groupSize - 1) / groupSize;
        List<List<T>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
            newList.add(list.subList(fromIndex, toIndex));
        }
        return newList;
    }
}
