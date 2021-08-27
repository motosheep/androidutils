package com.north.light.libble.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.north.light.libble.BLEManager;

/**
 * author:li
 * date:2021/8/26
 * desc:ble content provider
 */
public class BLEContentProvider extends ContentProvider {

    /**
     * content
     */
    private static Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext().getApplicationContext();
        BLEManager.getInstance().init();
        return false;
    }

    public static Context getAppContext() {
        return mContext;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
