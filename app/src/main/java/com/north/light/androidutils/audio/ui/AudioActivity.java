package com.north.light.androidutils.audio.ui;

import android.media.AudioManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.audio.focus.AudioFocusListener;
import com.north.light.androidutils.audio.focus.AudioFocusManager;
import com.north.light.androidutils.log.LogUtil;

/**
 * 音频焦点测试类
 */
public class AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("AudioActivity");
        setContentView(R.layout.activity_audio);
        AudioFocusManager.getInstance().setAudioFocusListener(audioFocusListener);
        AudioFocusManager.getInstance().requestFocus(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onDestroy() {
        AudioFocusManager.getInstance().removeAudioFocusListener(audioFocusListener);
        super.onDestroy();
    }

    /**
     * 音频焦点监听
     * */
    private AudioFocusListener audioFocusListener = new AudioFocusListener() {
        @Override
        public void noInit() {
            LogUtil.d("noInit");
        }

        @Override
        public void resume() {
            LogUtil.d("resume");
        }

        @Override
        public void pause() {
            LogUtil.d("pause");
        }

        @Override
        public void stop() {
            LogUtil.d("stop");
        }
    };
}