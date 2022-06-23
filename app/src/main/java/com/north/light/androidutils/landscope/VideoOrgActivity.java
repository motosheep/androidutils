package com.north.light.androidutils.landscope;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 视频播放方向旋转
 */
public class VideoOrgActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView surfaceView;

    SurfaceHolder surfaceHolder;

    MediaPlayer mediaPlayer;

    //视频控件原先的高度
    private int portraitSurfaceHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_org);

        surfaceView = findViewById(R.id.surface_view);

        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);

        mediaPlayer = new MediaPlayer();

        surfaceView.post(new Runnable() {
            @Override
            public void run() {
                portraitSurfaceHeight = surfaceView.getMeasuredHeight();
            }
        });

        try {
            mediaPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                LogUtil.d("onPrepared");
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                LogUtil.d("onCompletion");
            }
        });
        findViewById(R.id.changeBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换
                updateScreenMode();
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Surface surface = holder.getSurface();
        mediaPlayer.setSurface(surface);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //方向改变的时候会调用
        updatePlayView();
    }

    private void updatePlayView() {
        int orientation = getResources().getConfiguration().orientation;
        RelativeLayout.LayoutParams mSurParams = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LogUtil.d("ORIENTATION_PORTRAIT");
            mSurParams.height = portraitSurfaceHeight;

            mSurParams.setMargins(0, 0, 0, 0);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtil.d("ORIENTATION_LANDSCAPE");
            mSurParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;

            int videoWidth = mediaPlayer.getVideoWidth();
            int videoHeight = mediaPlayer.getVideoHeight();
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int mSurfaceViewWidth = dm.widthPixels;
            int mSurfaceViewHeight = dm.heightPixels;
            int w = mSurfaceViewHeight * videoWidth / videoHeight;
            int margin = (mSurfaceViewWidth - w) / 2;
            mSurParams.setMargins(margin, 0, margin, 0);
        }




        surfaceView.setLayoutParams(mSurParams);
    }

    private void updateScreenMode() {
        if (isPortrait()) {
            //竖屏
            LogUtil.d("------------------orientation == Configuration.ORIENTATION_PORTRAIT--------");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            LogUtil.d("------------------orientation == Configuration.ORIENTATION_LANDSCAPE--------");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 判断当前屏幕是否为竖屏
     */
    private boolean isPortrait() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}