package com.north.light.androidutils.viewpagervideo;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * author:li
 * date:2022/3/24
 * desc:自定义视频播放控件--只提供动态创建入口
 */
public class CusVideoView extends RelativeLayout {
    private VideoView mPlayView;
    //播放图标资源
    private int mPlayRes, mPauseRes;
    //播放图标
    private ImageView mPlayBt;
    //当前播放进度
    private int mPlayProgress = 0;

    public CusVideoView(Context context) {
        super(context);
        initView();
        initEvent();
        initData();
    }

    private void initView() {

    }

    private void initEvent() {

    }

    private void initData() {

    }

    @Override
    protected void onDetachedFromWindow() {
        VideoBuilder.getInstance().releaseVideoView(mPlayView);
        super.onDetachedFromWindow();
    }

    /**
     * add view
     */
    public void addToParent(LinearLayout root) {
        root.removeAllViews();
        root.addView(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        setLayoutParams(params);
        //在把video view添加到该view
        mPlayView = VideoBuilder.getInstance().Builder(getContext(), this);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mPlayView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPlayView.setLayoutParams(layoutParams);
        //把播放按钮设置到view
        mPlayBt = new ImageView(getContext());
        addView(mPlayBt);
        RelativeLayout.LayoutParams btLayoutParams = (LayoutParams) mPlayBt.getLayoutParams();
        btLayoutParams.width = 80;
        btLayoutParams.height = 80;
        btLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPlayBt.setLayoutParams(btLayoutParams);
        //设置默认图片
        mPlayBt.setImageResource(mPlayRes);

        //监听事件---------------------------------------------------------------------------------
        mPlayView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mPlayProgress = 0;
                updatePlayUI(false);
                return true;
            }
        });
        mPlayView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayProgress = 0;
                updatePlayUI(false);
            }
        });
        mPlayBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayView == null) {
                    return;
                }
                if (mPlayView.isPlaying()) {
                    mPlayView.pause();
                    mPlayProgress = mPlayView.getCurrentPosition();
                    updatePlayUI(false);
                }else{
                    mPlayView.seekTo(mPlayProgress);
                    mPlayView.start();
                    updatePlayUI(true);
                }
            }
        });
    }

    /**
     * 更新播放图片
     * */
    private void updatePlayUI(boolean isPlay){
        if(mPlayBt == null){
            return;
        }
        if(isPlay){
            mPlayBt.setImageResource(mPauseRes);
        }else{
            mPlayBt.setImageResource(mPlayRes);
        }
    }

    /**
     * 设置播放,暂停按钮
     */
    public void setPlayRes(int playRes, int pauseRes) {
        mPlayRes = playRes;
        mPauseRes = pauseRes;
    }

    /**
     * 播放
     */
    public void play(String path) {
        if (mPlayView == null) {
            return;
        }
        mPlayView.setVideoPath(path);
        mPlayView.start();
        updatePlayUI(true);
    }

}
