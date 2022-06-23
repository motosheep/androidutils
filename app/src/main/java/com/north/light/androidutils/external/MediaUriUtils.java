package com.north.light.androidutils.external;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.Serializable;

/**
 * FileName: MediaUriUtils
 * Author: lzt
 * Date: 2022/6/21 14:38
 */
public class MediaUriUtils implements Serializable {

    /**
     * uri 转 path
     *
     * @return
     */
    public static String getFilePathFromContentUri(Context context, Uri selectedVideoUri) {
        try {
            String filePath;
            String[] filePathColumn = {MediaStore.MediaColumns.DATA};
            Cursor cursor = context.getContentResolver().query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * path转uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        try {
            String filePath = imageFile.getAbsolutePath();
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (imageFile.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }

    }

}
