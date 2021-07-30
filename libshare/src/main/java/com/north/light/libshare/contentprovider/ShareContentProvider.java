package com.north.light.libshare.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/30 16:42
 * @Version: 1.0
 * @Description:share lib content provider
 * 通过此类，实现全局content获取
 */
public class ShareContentProvider extends ContentProvider {
    /**
     * 全局content
     */
    private static volatile Context mAppContent;

    public static Context getAppContent() {
        return mAppContent;
    }

    @Override
    public boolean onCreate() {
        mAppContent = getContext().getApplicationContext();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
