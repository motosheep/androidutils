package com.north.light.androidutils.novel.text.data.util;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/2/10 10:07
 * @Description:文件管理类
 */
public class TxFileUtils implements Serializable {

    /**
     * 创建文件
     */
    public static void createFile(File file, boolean isFile) {// 创建文件
        try {
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
        } catch (Exception e) {

        }
    }

    /**
     * 删除文件
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) { //目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) { //如果是文件，删除
                        file.delete();
                    } else { //目录
                        if (file.listFiles().length == 0) { //目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
