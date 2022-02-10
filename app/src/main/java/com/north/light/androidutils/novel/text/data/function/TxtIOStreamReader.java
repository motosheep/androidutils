package com.north.light.androidutils.novel.text.data.function;

import android.content.Context;
import android.text.TextUtils;

import com.north.light.androidutils.novel.text.data.util.TxFileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: lzt
 * @Date: 2022/2/8 14:40
 * @Description:本地txt文件读取reader
 */
public class TxtIOStreamReader implements StreamReader {

    private final String FORMAT_UTF8 = "utf-8";

    private CopyOnWriteArrayList<TxtLoadingListener> mListener = new CopyOnWriteArrayList<>();

    //内部方法----------------------------------------------------------------------------------

    /**
     * 写入str到文件中
     */
    private void writerToFile(String fileName, String content) throws Exception {
        TxFileUtils.createFile(new File(fileName), true);
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
     * 通知读取进度
     */
    private void notifyProgress(String path, String name, int pos, int total) {
        if (mListener == null || mListener.size() == 0) {
            return;
        }
        for (TxtLoadingListener listener : mListener) {
            listener.loadingPart(path, name, pos, total);
        }
    }

    /**
     * 通知读取完成
     */
    private void notifyFinish(String path, String name, Map<Integer, TxtInfo> infoMap) {
        if (mListener == null || mListener.size() == 0) {
            return;
        }
        for (TxtLoadingListener listener : mListener) {
            listener.loadingFinish(path, name, infoMap);
        }
    }

    /**
     * 通知错误
     */
    private void notifyError(Exception e) {
        if (mListener == null || mListener.size() == 0) {
            return;
        }
        for (TxtLoadingListener listener : mListener) {
            listener.loadFailed(e);
        }
    }

    /**
     * 设置参数
     */
    private TxtInfo setMapInfo(int startPos, int total, int size, String orgPath, String trainPath) {
        TxtInfo info = new TxtInfo();
        info.setOrgPath(orgPath);
        info.setTrainPath(trainPath);
        info.setPos(startPos);
        info.setTotal(total);
        info.setSize(size);
        return info;
    }


    //外部方法----------------------------------------------------------------------------------

    /**
     * 读取文件--先判断是否需要切割，后面再读取
     */
    @Override
    public void load(Context context, String path) throws Exception {
        TxtExecutorsManager.getInstance().closeCacheExecutors(path);
        TxtExecutorsManager.getInstance().getCacheExecutors(path).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    int fileLength = (int) file.length();
                    String splitRootPath = TxtConstant.getDefaultOutputPath(context);
                    String splitRootName = getSplitPathName(path);
                    String splitParentPath = splitRootPath + "/" + splitRootName;
                    TxFileUtils.deleteFolderFile(splitParentPath, true);
                    String orgFileName = getSplitName(path, "");
                    //总信息统计
                    Map<Integer, TxtInfo> mapInfo = new HashMap<>();
                    if (TxtConstant.SPLIT_SIZE < file.length()) {
                        //大于单个文件大小，需要切割
                        BufferedReader bre = new BufferedReader(new FileReader(file));
                        int count = 1;
                        int cacheCount = 0;
                        int totalCount = 0;
                        if (file.length() % TxtConstant.SPLIT_SIZE > 0) {
                            totalCount = (int) (file.length() / TxtConstant.SPLIT_SIZE + 1);
                        } else {
                            totalCount = (int) (file.length() / TxtConstant.SPLIT_SIZE);
                        }
                        String cacheStr;
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((cacheStr = bre.readLine()) != null) {
                            stringBuilder.append(cacheStr);
                            cacheCount = cacheCount + cacheStr.getBytes().length;
                            if (cacheCount > TxtConstant.SPLIT_SIZE) {
                                String fileName = getSplitName(path, String.valueOf(count));
                                String fileSplitName = splitParentPath + "/" + fileName;
                                writerToFile(fileSplitName, stringBuilder.toString());
                                //通知进度-------------
                                notifyProgress(splitParentPath, fileSplitName, count, totalCount);
                                mapInfo.put(count, setMapInfo(count, totalCount, cacheCount, path, fileSplitName));
                                //同步中间变量----------------------------
                                count = count + 1;
                                cacheCount = 0;
                                stringBuilder = new StringBuilder();
                            }
                        }
                        //兜底--判断是否还剩下数据没有写入
                        if (!TextUtils.isEmpty(stringBuilder.toString())) {
                            String fileName = getSplitName(path, String.valueOf(count));
                            String fileSplitName = splitParentPath + "/" + fileName;
                            writerToFile(fileSplitName, stringBuilder.toString());
                            //通知进度-------------
                            notifyProgress(splitParentPath, fileSplitName, count, totalCount);
                            mapInfo.put(count, setMapInfo(count, totalCount, cacheCount, path, fileSplitName));
                        }
                        //至此，全部分割完成,开始读取分割的文件-------------------------------------------------------
//            List<TxtInfo> strList = new ArrayList<>();
//            for (int i = 0; i < filePathList.size(); i++) {
//                File cacheFileName = new File(filePathList.get(i));
//                int cacheFileSize = (int) cacheFileName.length();
//                byte[] buff = new byte[cacheFileSize];
//                FileInputStream cacheInputStream = new FileInputStream(cacheFileName);
//                cacheInputStream.read(buff, 0, cacheFileSize);
//                cacheInputStream.close();
//                String result = new String(buff, FORMAT_UTF8);
//            }
                        //通知完成
                        notifyFinish(path, orgFileName, mapInfo);
                    } else {
                        //不需要切割---------------------------------------------------
                        //直接复制到应用私有目录
                        byte[] buff = new byte[fileLength];
                        FileInputStream fileInputStream = new FileInputStream(file);
                        fileInputStream.read(buff, 0, fileLength);
                        fileInputStream.close();
                        String result = new String(buff, FORMAT_UTF8);
                        String fileName = getSplitName(path, String.valueOf(1));
                        String fileSplitName = splitParentPath + "/" + fileName;
                        writerToFile(fileSplitName, result);
                        //信息转换-----
                        mapInfo.put(1, setMapInfo(1, 1, fileLength, path, fileSplitName));
                        //通知外部----
                        notifyProgress(splitParentPath, fileSplitName, 1, 1);
                        notifyFinish(path, orgFileName, mapInfo);
                    }
                } catch (Exception e) {
                    notifyError(e);
                }
            }
        });
    }

    @Override
    public void cancel(Context context, String path) throws Exception {
        TxtExecutorsManager.getInstance().closeCacheExecutors(path);
    }

    @Override
    public void setOnLoadingListener(TxtLoadingListener loadingListener) {
        if (loadingListener == null) {
            return;
        }
        mListener.add(loadingListener);
    }

    @Override
    public void removeLoadingListener(TxtLoadingListener loadingListener) {
        if (loadingListener == null) {
            return;
        }
        mListener.remove(loadingListener);
    }

}


