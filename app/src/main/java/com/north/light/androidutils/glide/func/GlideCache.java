package com.north.light.androidutils.glide.func;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.north.light.androidutils.glide.func.progress.GlideOkHttpGlideUrlLoader;
import com.north.light.androidutils.glide.func.progress.GlideProgressInterceptor;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * FileName: GlideCache
 * Author: lzt
 * Date: 2022/6/9 14:26
 */
@GlideModule
public final class GlideCache extends AppGlideModule {
    private String appRootPath = null;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //手机app路径--目前该路径是不用权限即可保存的路径
        appRootPath = context.getFilesDir().getPath();
        Log.d("glide", "glide 图片缓存路径：" + appRootPath);
        // 100 MB
        int diskCacheSizeBytes = 1024 * 1024 * 100;
        builder.setDiskCache(new DiskLruCacheFactory(appRootPath + "/GlideDisk", diskCacheSizeBytes));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient.Builder builder = GlideUnsafeOkHttpClient.getUnsafeOkHttpClient();
        builder.addInterceptor(new GlideProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
        registry.replace(GlideUrl.class, InputStream.class, new GlideOkHttpGlideUrlLoader.Factory(okHttpClient));
    }
}
