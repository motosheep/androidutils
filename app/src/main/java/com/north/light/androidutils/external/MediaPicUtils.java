package com.north.light.androidutils.external;

import android.Manifest;
import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.north.light.androidutils.log.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;


/**
 * FileName: MediaPicUtils
 * Author: lzt
 * Date: 2022/6/21 13:57
 */
public class MediaPicUtils implements Serializable {

    //请求code
    public static final int CODE_REQ = 10086;


    //内部实现方法，只能内部调用---------------------------------------------------------------------

    /**
     * 保存图片到系统
     *
     * @return 保存图片路径
     */
    private static String saveMediaToSys(Context context, Bitmap bitmap, String dirType, String relativeDir,
                                         String filename, String mimeType, String description) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            //首先保存
            File saveDir = Environment.getExternalStoragePublicDirectory(dirType);
            saveDir = new File(saveDir, relativeDir);
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                try {
                    throw new Exception("create directory fail!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            File outputFile = new File(saveDir, filename);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            //最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
            return outputFile.getPath();
        } else {
            String path = (!TextUtils.isEmpty(relativeDir)) ?
                    (Environment.DIRECTORY_PICTURES + File.separator + relativeDir) :
                    Environment.DIRECTORY_PICTURES;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, description);
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, path);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            //contentValues.put(MediaStore.Images.Media.IS_PENDING,1)
            Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri insertUri = context.getContentResolver().insert(external, contentValues);
            OutputStream fos = (OutputStream) null;
            if (insertUri != null) {
                try {
                    fos = context.getContentResolver().openOutputStream(insertUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
            return MediaUriUtils.getFilePathFromContentUri(context, insertUri);
        }
    }

    /**
     * 保存图片到应用目录
     */
    private static String saveImageToApp(Context context, Bitmap source, String relPath, String fileName) {
        String rootPath = MediaPathManager.getInstance().getAppInnerRootPath(context);
        String savePath = rootPath + File.separator + relPath + File.separator;
        File fileDir = new File(savePath);
        if (!fileDir.exists()) {
            createFile(fileDir, false);
        }
        File f = new File(savePath + fileName);
        if (!fileDir.exists()) {
            createFile(f, true);
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            source.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            LogUtil.d("save app dir: " + (savePath + fileName));
            return savePath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void createFile(File file, boolean isFile) {// 创建文件
        try {
            if (file != null && !file.exists()) {// 如果文件不存在
                if (file.getParentFile() != null && !file.getParentFile().exists()) {// 如果文件父目录不存在
                    createFile(file.getParentFile(), false);
                } else {// 存在文件父目录
                    if (isFile) {// 创建文件
                        try {
                            boolean createResult = file.createNewFile();// 创建新文件
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        boolean mkResult = file.mkdir();// 创建目录
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //外部实现方法，只能外部调用---------------------------------------------------------------------

    /**
     * 保存图片到相册
     * 安卓29以前需要申请权限，但是这里直接强制申请
     * 保存到系统相册--picture
     */
    public static String saveImagesToSys(Activity activity, Bitmap bitmap) {
        String defaultName = System.currentTimeMillis() + ".jpg";
        String defaultPath = "DefaultPath";
        String defaultDsc = "";
        return saveImagesToSys(activity, defaultPath, defaultName, bitmap, defaultDsc);
    }

    /**
     * 保存图片到相册
     * 安卓29以前需要申请权限，但是这里直接强制申请
     * 保存到系统相册--picture
     *
     * @param relPath      Picture目录的自定义目录名字--例如  defaultpic
     * @param targetName   保存的文件名字--例如 abc.jpg
     * @param sourceBitmap 保存的bitmap源
     * @param desc         描述
     */
    public static String saveImagesToSys(Activity activity, String relPath, String targetName,
                                         Bitmap sourceBitmap, String desc) {
        if (TextUtils.isEmpty(relPath) || TextUtils.isEmpty(targetName)) {
            return null;
        }
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try {
            String mineType = "image/JPEG";
            if (targetName.toLowerCase().contains(".png")) {
                mineType = "image/PNG";
            }
            String savePath = saveMediaToSys(activity, sourceBitmap, Environment.DIRECTORY_PICTURES,
                    relPath, targetName, mineType, desc);
            LogUtil.d("savePath: " + savePath);
            return savePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存图片到应用内部目录
     */
    public static String savePicToApp(Activity activity, Bitmap sourceBitmap) {
        String defaultName = System.currentTimeMillis() + ".jpg";
        String defaultPath = "DefaultPath";
        return savePicToApp(activity, defaultPath, defaultName, sourceBitmap);
    }

    /**
     * 保存图片到应用内部目录
     *
     * @param relPath      Picture目录的自定义目录名字--例如  defaultpic or default/aba/dafdsf
     * @param targetName   保存的文件名字--例如 abc.jpg
     * @param sourceBitmap 保存的bitmap源
     */
    public static String savePicToApp(Activity activity, String relPath, String targetName,
                                      Bitmap sourceBitmap) {
        return saveImageToApp(activity, sourceBitmap, relPath, targetName);
    }


    /**
     * Android Q以下版本，删除文件需要申请WRITE_EXTERNAL_STORAGE权限。
     * 通过MediaStore的DATA字段获得媒体文件的绝对路径，然后使用File相关API删除
     * <p>
     * Android Q以上版本，应用删除自己创建的媒体文件不需要用户授权。
     * 删除其他应用创建的媒体文件需要申请READ_EXTERNAL_STORAGE权限。
     * 删除其他应用创建的媒体文件，还会抛出RecoverableSecurityException异常，在操作或删除公共目录的文件时，
     * 需要Catch该异常，由MediaProvider弹出弹框给用户选择是否允许应用修改或删除图片/视频/音频文件
     */
    public static void deletePicWithUri(Activity activity, String path) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                new File(path).delete();
            } else {
                try {
                    Uri imageUri = MediaUriUtils.getImageContentUri(activity, new File(path));
                    activity.getContentResolver().delete(imageUri, null, null);
                } catch (RecoverableSecurityException e1) {
                    //捕获 RecoverableSecurityException异常，发起请求
                    try {
                        ActivityCompat.startIntentSenderForResult(activity,
                                e1.getUserAction().getActionIntent().getIntentSender(),
                                CODE_REQ, null, 0, 0, 0, null);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

}
