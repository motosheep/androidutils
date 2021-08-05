package com.north.light.androidutils.audio.focus;

import android.content.Context;

/**
 * @Author: lzt
 * @CreateDate: 2021/8/4 17:46
 * @Version: 1.0
 * @Description:音频焦点定义api
 */
public interface AudioFocusApi {
    /**
     * 初始花
     */
    public void init(Context context);

    /**
     * 请求音频焦点
     * @param type
     */
    public void requestFocus(int type);

    /**
     * 释放音频焦点
     */
    public void releaseFocus();

    /**
     * 设置监听对象
     */
    public void setAudioFocusListener(AudioFocusListener listener);

    /**
     * 释放监听对象
     */
    public void removeAudioFocusListener(AudioFocusListener listener);

}
