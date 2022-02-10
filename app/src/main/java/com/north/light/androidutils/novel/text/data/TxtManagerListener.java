package com.north.light.androidutils.novel.text.data;

import com.north.light.androidutils.novel.text.data.bean.TxtInfo;

/**
 * @Author: lzt
 * @Date: 2022/2/10 14:43
 * @Description:txt manager api
 */
public interface TxtManagerListener {

    void info(TxtInfo info);

    void init();

    void autoNext();
}
