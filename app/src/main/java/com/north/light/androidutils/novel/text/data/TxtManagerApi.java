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
     *
     * @param sectionPos 章节的页码--第几章
     * @param detailPos  章节的页数--某章第几页
     */
    void loadData(Context context, String path, int sectionPos, int detailPos) throws Exception;

 /**
     * 加载数据
     */
    void loadData(Context context, String path) throws Exception;


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
     * 是否加载中
     */
    boolean isLoading();

    /**
     * 设置监听
     */
    void setOnTxtManagerListener(TxtManagerListener listener);

    /**
     * 移除监听
     */
    void removeTxtManagerListener(TxtManagerListener listener);


    /**
     * 设置每页数据最大读取的字体数量
     */
    void setPageMaxSize(int size);

    /**
     * 当前显示字体数量
     * */
    void setPageShowSize(int size);

}
