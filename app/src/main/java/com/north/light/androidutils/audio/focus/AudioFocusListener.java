package com.north.light.androidutils.audio.focus;

/**
 * @Author: lzt
 * @CreateDate: 2021/8/4 17:46
 * @Version: 1.0
 * @Description:音频焦点Listener
 */
public interface AudioFocusListener {

    /**
     * 没有初始化
     */
    void noInit();

    /**
     * 恢复了焦点
     */
    void resume();

    /**
     * 暂时失去了焦点
     */
    void pause();

    /**
     * 失去了焦点
     */
    void stop();

}
