package com.north.light.androidutils.novel.text.data.function;

import android.content.Context;
import android.text.TextUtils;

import com.north.light.androidutils.novel.text.data.bean.TxtInfo;
import com.north.light.androidutils.novel.text.data.bean.TxtReadInfo;
import com.north.light.androidutils.novel.text.data.util.TxFileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: lzt
 * @Date: 2022/2/8 14:40
 * @Description:本地txt文件读取reader
 */
public class TxtIOStreamReader implements StreamReader {

    private final String FORMAT_UTF8 = "utf-8";

    private CopyOnWriteArrayList<TxtLoadingListener> mListener = new CopyOnWriteArrayList<>();

    private AtomicBoolean mOut = new AtomicBoolean(false);

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
    private void notifyProgress(String path, String name, TxtInfo info) {
        if (mListener == null || mListener.size() == 0) {
            return;
        }
        for (TxtLoadingListener listener : mListener) {
            listener.splitPart(path, name, info);
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
            listener.splitFinish(path, name, infoMap);
        }
    }

    /**
     * 通知错误
     *
     * @param type 1分割错误 2读取错误
     */
    private void notifyError(int type, Exception e) {
        if (mListener == null || mListener.size() == 0) {
            return;
        }
        for (TxtLoadingListener listener : mListener) {
            if (type == 1) {
                listener.splitFailed(e);
            } else if (type == 2) {
                listener.readFailed(e);
            }
        }
    }

    /**
     * 通知读取结果
     */
    private void notifyRead(TxtReadInfo info) {
        if (mListener == null || mListener.size() == 0) {
            return;
        }
        for (TxtLoadingListener listener : mListener) {
            listener.read(info);
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
    public void split(Context context, String path) throws Exception {
        mOut.set(false);
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
                        int spaceCount = 0;
                        while ((cacheStr = bre.readLine()) != null && !mOut.get()) {
                            stringBuilder.append(cacheStr).append("\n");
                            if (TextUtils.isEmpty(cacheStr)) {
                                spaceCount = spaceCount + 1;
                            }
                            cacheCount = cacheCount + cacheStr.getBytes().length;
                            if (cacheCount > TxtConstant.SPLIT_SIZE || spaceCount >= 3) {
                                String fileName = getSplitName(path, String.valueOf(count));
                                String fileSplitName = splitParentPath + "/" + fileName;
                                writerToFile(fileSplitName, stringBuilder.toString());
                                //通知进度-------------
                                TxtInfo txInfo = setMapInfo(count, totalCount, cacheCount, path, fileSplitName);
                                notifyProgress(splitParentPath, fileSplitName, txInfo);
                                mapInfo.put(count, txInfo);
                                //同步中间变量----------------------------
                                count = count + 1;
                                cacheCount = 0;
                                spaceCount = 0;
                                stringBuilder = new StringBuilder();
                            }
                        }
                        //兜底--判断是否还剩下数据没有写入
                        if (!TextUtils.isEmpty(stringBuilder.toString())) {
                            String fileName = getSplitName(path, String.valueOf(count));
                            String fileSplitName = splitParentPath + "/" + fileName;
                            writerToFile(fileSplitName, stringBuilder.toString());
                            //通知进度-------------
                            TxtInfo txInfo = setMapInfo(count, totalCount, cacheCount, path, fileSplitName);
                            notifyProgress(splitParentPath, fileSplitName, txInfo);
                            mapInfo.put(count, txInfo);
                        }
                        //至此，全部分割完成------------------
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
                        TxtInfo txInfo = setMapInfo(1, 1, fileLength, path, fileSplitName);
                        mapInfo.put(1, txInfo);
                        //通知外部----
                        notifyProgress(splitParentPath, fileSplitName, txInfo);
                        notifyFinish(path, orgFileName, mapInfo);
                    }
                } catch (Exception e) {
                    notifyError(1, e);
                }
            }
        });
    }

    @Override
    public void read(Context context, TxtInfo txtInfo) throws Exception {
        TxtExecutorsManager.getInstance().getCacheExecutors(txtInfo.getOrgPath()).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File cacheFileName = new File(txtInfo.getTrainPath());
                    int cacheFileSize = (int) cacheFileName.length();
                    byte[] buff = new byte[cacheFileSize];
                    FileInputStream cacheInputStream = new FileInputStream(cacheFileName);
                    cacheInputStream.read(buff, 0, cacheFileSize);
                    cacheInputStream.close();
                    String result = new String(buff, FORMAT_UTF8);
                    //赋值-----------------------
                    TxtReadInfo info = new TxtReadInfo();
                    info.setContent(result);
                    info.setPos(txtInfo.getPos());
                    info.setOrgPath(txtInfo.getOrgPath());
                    info.setSize(txtInfo.getSize());
                    info.setTotal(txtInfo.getTotal());
                    info.setTrainPath(txtInfo.getTrainPath());
                    //通知外部-----------------------
                    notifyRead(info);
                } catch (Exception e) {
                    notifyError(2, e);
                }
            }
        });

    }

    @Override
    public void cancel(Context context, String path) throws Exception {
        mOut.set(true);
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


