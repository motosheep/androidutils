package com.north.light.androidutils.novel.text.data.util;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/2/10 15:06
 * @Description:字符分割工具类
 */
public class TxStringSplitUtils implements Serializable {

    /**
     * 传入字符串，分割为数组
     */
    public static List<String> splitStrToList(String content, int listSize) {
        if (TextUtils.isEmpty(content) || listSize == 0) {
            return new ArrayList<>();
        }
        int count = 0;
        if (content.length() % listSize == 0) {
            count = content.length() / listSize;
        } else {
            count = content.length() / listSize + 1;
        }
        List<String> trainResult = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (i == count - 1) {
                //最后一个
                trainResult.add(content.substring(i * listSize));
            } else {
                //普通
                trainResult.add(content.substring(i * listSize, (i + 1) * listSize));
            }
        }
        return trainResult;
    }
}
