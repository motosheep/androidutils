package com.north.light.androidutils.water;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/1/27 11:17
 * @Description:图片管理类
 */
public class ImgUtils {

    private static class SingleHolder implements Serializable {
        static ImgUtils mInstance = new ImgUtils();
    }

    public static ImgUtils getInstance() {
        return ImgUtils.SingleHolder.mInstance;
    }

    /**
     * 保存图片
     */
    public String saveBitmap(Context context, Bitmap bm, String path) {
        File f = new File(path);
        if (!f.exists()) {
            createFile(f, true);
        }
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bm.recycle();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void createFile(File file, boolean isFile) {// 创建文件
        if (!file.exists()) {// 如果文件不存在
            if (!file.getParentFile().exists()) {// 如果文件父目录不存在
                createFile(file.getParentFile(), false);
            } else {// 存在文件父目录
                if (isFile) {// 创建文件
                    try {
                        file.createNewFile();// 创建新文件
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    file.mkdir();// 创建目录
                }
            }
        }
    }
}
