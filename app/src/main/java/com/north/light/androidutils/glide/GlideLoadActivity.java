package com.north.light.androidutils.glide;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.north.light.androidutils.R;
import com.north.light.androidutils.glide.func.GlideApp;
import com.north.light.androidutils.glide.func.progress.callback.GlideProgressListener;
import com.north.light.androidutils.glide.func.progress.callback.GlideProgressManager;

import org.jetbrains.annotations.NotNull;


/**
 * glide进度条加载显示--详情见博客：
 * https://www.jianshu.com/p/dc6ab2343428
 */
public class GlideLoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_load);


        String imgUrl = "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";

        GlideProgressManager.getInstance().setGlideProgressListener(new GlideProgressListener() {
            @Override
            public void onProgress(String url, int progress, boolean isFinish) {
                Log.d("glide", "glide onProgress：" + progress);
            }
        });
        GlideApp.with(this).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                Log.d("glide", "glide onResourceReady");
            }
        });
    }
}