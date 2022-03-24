package com.north.light.androidutils.viewpagervideo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.Serializable;

/**
 * author:li
 * date:2022/3/24
 * desc:Video View builder
 */
public class VideoBuilder implements Serializable {


    private static class SingleHolder {
        static VideoBuilder mInstance = new VideoBuilder();
    }

    public static VideoBuilder getInstance() {
        return VideoBuilder.SingleHolder.mInstance;
    }

    /**
     * 创建video view
     */
    public VideoView Builder(Context context, ViewGroup mPlayerRoot) {
        Context applicationContext = context.getApplicationContext();
        VideoView mPlayView = new VideoView(applicationContext);
        mPlayerRoot.addView(mPlayView);
        ViewGroup.LayoutParams params = mPlayView.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        mPlayView.setLayoutParams(params);
        MediaController mediaController = new MediaController(context);
        mediaController.setVisibility(View.INVISIBLE);
        mPlayView.setMediaController(mediaController);
        return mPlayView;
    }

    /**
     * 释放video view
     */
    public void releaseVideoView(VideoView mPlayView) {
        try {
            mPlayView.stopPlayback();
            mPlayView.suspend();
            mPlayView.setOnErrorListener(null);
            mPlayView.setOnPreparedListener(null);
            mPlayView.setOnCompletionListener(null);
            mPlayView.setMediaController(null);
        } catch (Exception e) {

        }
    }

}
