package com.north.light.libumeng.api;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/28 16:55
 * @Version: 1.0
 * @Description:非activity收集声明
 */
public interface UMNoActivityApi {

    /**
     * on start
     */
    public void onPageStart(String viewName);

    /**
     * on end
     */
    public void onPageEnd(String viewName);
}
