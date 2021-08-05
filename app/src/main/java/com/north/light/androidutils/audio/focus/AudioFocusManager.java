package com.north.light.androidutils.audio.focus;

import android.content.Context;
import android.media.AudioManager;

import com.north.light.androidutils.log.LogUtil;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: lzt
 * @CreateDate: 2021/8/4 17:42
 * @Version: 1.0
 * @Description:音频焦点管理类
 */
public class AudioFocusManager implements AudioFocusApi {
    private boolean isInit = false;
    private AudioManager audioManager;
    private Context mContext;
    private CopyOnWriteArrayList<AudioFocusListener> mListener = new CopyOnWriteArrayList<>();


    private static class SingleHolder implements Serializable {
        static AudioFocusManager mInstance = new AudioFocusManager();
    }

    public static AudioFocusManager getInstance() {
        return SingleHolder.mInstance;
    }

    public AudioFocusManager() {

    }

    /**
     * 判断是否初始化
     */
    private boolean isInit() {
        return isInit;
    }

    /**
     * 初始化方法
     */
    @Override
    public void init(Context context) {
        if (isInit()) {
            return;
        }
        isInit = true;
        mContext = context.getApplicationContext();
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }


    /**
     * 请求焦点
     *
     * @param type AudioManager.STREAM_MUSIC
     */
    @Override
    public void requestFocus(int type) {
        if (!isInit()) {
            notifyNoInit();
            return;
        }
        audioManager.requestAudioFocus(mFocusChangeListener,
                // Use the music stream.
                type,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }


    /**
     * 释放音频焦点
     */
    @Override
    public void releaseFocus() {
        if (!isInit()) {
            notifyNoInit();
            return;
        }
        audioManager.abandonAudioFocus(mFocusChangeListener);
    }

    /**
     * 音频焦点监听
     */
    private AudioManager.OnAudioFocusChangeListener mFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (!isInit()) {
                notifyNoInit();
                return;
            }
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                LogUtil.d("AUDIOFOCUS_LOSS_TRANSIENT");
                notifyPause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                LogUtil.d("AUDIOFOCUS_GAIN");
                notifyResume();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                LogUtil.d("AUDIOFOCUS_LOSS");
                audioManager.abandonAudioFocus(mFocusChangeListener);
                notifyStop();
            }
        }
    };


    @Override
    public void setAudioFocusListener(AudioFocusListener listener) {
        if (listener == null) {
            return;
        }
        mListener.add(listener);
    }

    @Override
    public void removeAudioFocusListener(AudioFocusListener listener) {
        if (listener == null) {
            return;
        }
        mListener.remove(listener);
    }

    //监听回调------------------------------------------------------------------------------------

    /**
     * 没有初始化
     */
    private void notifyNoInit() {
        for (AudioFocusListener listener : mListener) {
            if (listener != null) {
                listener.noInit();
            }
        }
    }

    /**
     * 暂停
     */
    private void notifyPause() {
        for (AudioFocusListener listener : mListener) {
            if (listener != null) {
                listener.pause();
            }
        }
    }

    /**
     * 恢复
     */
    private void notifyResume() {
        for (AudioFocusListener listener : mListener) {
            if (listener != null) {
                listener.resume();
            }
        }
    }

    /**
     * 停止
     */
    private void notifyStop() {
        for (AudioFocusListener listener : mListener) {
            if (listener != null) {
                listener.stop();
            }
        }
    }

}
