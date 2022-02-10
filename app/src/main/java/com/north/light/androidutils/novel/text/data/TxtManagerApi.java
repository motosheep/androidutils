package com.north.light.androidutils.novel.text.data;

import android.content.Context;

/**
 * author:li
 * date:2022/2/10
 * desc:txt manager api
 */
public interface TxtManagerApi {

    /**
     * 初始化
     */
    void init(Context context);

    /**
     * 加载数据
     */
    void loadData(Context context, String path, int pageSize) throws Exception;

    /**
     * 取消
     */
    void cancel(Context context, String path) throws Exception;

    /**
     * 修改了数据
     *
     * @param type -1上一页 0当前页 1下一页
     */
    void change(int type) throws Exception;

    /**
     * 获取不同的数据
     *
     * @param type -1上一页 0当前页 1下一页
     */
    String getShowContent(int type) throws Exception;

    /**
     * 设置监听
     */
    void setOnTxtManagerListener(TxtManagerListener listener);

    /**
     * 移除监听
     */
    void removeTxtManagerListener(TxtManagerListener listener);
}
