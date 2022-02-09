package com.north.light.androidutils.novel.text.data;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/2/8 14:40
 * @Description:本地txt文件读取reader
 */
public class TxtIOStreamReader implements StreamReader {

    private final String FORMAT_UTF8 = "utf-8";

    //内部方法----------------------------------------------------------------------------------


    /**
     * 写入str到文件中
     */
    private void writerToFile(String fileName, String content) throws Exception {
        createFile(new File(fileName), true);
        FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * 获取分割的文件名
     */
    private String getSplitName(String path, String tag) {
        int namePos = path.lastIndexOf("/");
        String trueName = path.substring(namePos + 1);
        int formatName = trueName.lastIndexOf(".");
        String format = trueName.substring(formatName);
        return trueName.replace(format, tag + format);
    }

    /**
     * 获取分割的文件目录
     */
    private String getSplitPathName(String path) {
        int namePos = path.lastIndexOf("/");
        String trueName = path.substring(namePos + 1);
        int formatName = trueName.lastIndexOf(".");
        String format = trueName.substring(formatName);
        return trueName.replace(format, "");
    }


    /**
     * 创建文件
     */
    public void createFile(File file, boolean isFile) {// 创建文件
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
    public void deleteFolderFile(String filePath, boolean deleteThisPath) {
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

    //外部方法----------------------------------------------------------------------------------

    /**
     * 读取文件--先判断是否需要切割，后面再读取
     */
    @Override
    public void load(Context context, String path) throws Exception {
        File file = new File(path);
        int fileLength = (int) file.length();
        if (TxtConstant.SPLIT_SIZE < file.length()) {
            //切割文件所放的目录
            String splitRootPath = TxtConstant.getDefaultOutputPath(context);
            String splitRootName = getSplitPathName(path);
            String splitParentPath = splitRootPath + "/" + splitRootName;
            deleteFolderFile(splitParentPath, true);
            //大于单个文件大小，需要切割
            BufferedReader bre = new BufferedReader(new FileReader(file));
            //没有到结尾，就一直读取
            List<String> filePathList = new ArrayList<>();
            int count = 1;
            int cacheCount = 0;
            String cacheStr;
            StringBuilder stringBuilder = new StringBuilder();
            while ((cacheStr = bre.readLine()) != null) {
                stringBuilder.append(cacheStr);
                cacheCount = cacheCount + cacheStr.getBytes().length;
                if (cacheCount > TxtConstant.SPLIT_SIZE) {
                    String fileName = getSplitName(path, String.valueOf(count));
                    String fileSplitName = splitParentPath + "/" + fileName;
                    writerToFile(fileSplitName, stringBuilder.toString());
                    count = count + 1;
                    cacheCount = 0;
                    stringBuilder = new StringBuilder();
                    filePathList.add(fileSplitName);
                }
            }
            //兜底--判断是否还剩下数据没有写入
            if (!TextUtils.isEmpty(stringBuilder.toString())) {
                String fileName = getSplitName(path, String.valueOf(count));
                String fileSplitName = splitParentPath + "/" + fileName;
                writerToFile(fileSplitName, stringBuilder.toString());
                filePathList.add(fileSplitName);
            }
            //至此，全部分割完成
            List<String> strList = new ArrayList<>();
            for (int i = 0; i < filePathList.size(); i++) {
                File cacheFileName = new File(filePathList.get(i));
                int cacheFileSize = (int) cacheFileName.length();
                byte[] buff = new byte[cacheFileSize];
                FileInputStream cacheInputStream = new FileInputStream(cacheFileName);
                cacheInputStream.read(buff, 0, cacheFileSize);
                cacheInputStream.close();
                String result = new String(buff, FORMAT_UTF8);
                strList.add(result);
            }
            TxtMemoryManager.getInstance().set(path, strList);
        } else {
            //不需要切割
            byte[] buff = new byte[fileLength];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(buff, 0, fileLength);
            fileInputStream.close();
            String result = new String(buff, FORMAT_UTF8);
            List<String> strList = new ArrayList<>();
            strList.add(result);
            TxtMemoryManager.getInstance().set(path, strList);
        }
    }

}


