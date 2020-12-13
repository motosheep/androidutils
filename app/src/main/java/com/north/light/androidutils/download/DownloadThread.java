package com.north.light.androidutils.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 自定义线程类
 *
 * @author liuyazhuang
 */
public class DownloadThread extends Thread {
    //下载的线程id
    private int threadId;
    //下载的文件路径
    private String path;
    //保存的文件
    private File file;
    //下载的进度条更新的监听器
    private ProgressBarListener listener;
    //每条线程下载的数据量
    private int block;
    //下载的开始位置
    private int startPosition;
    //下载的结束位置
    private int endPosition;

    public DownloadThread(int threadId, String path, File file, ProgressBarListener listener, int block) {
        this.threadId = threadId;
        this.path = path;
        this.file = file;
        this.listener = listener;
        this.block = block;

        this.startPosition = threadId * block;
        this.endPosition = (threadId + 1) * block - 1;
    }

    @Override
    public void run() {
        super.run();
        try {
            //创建RandomAccessFile对象
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            //跳转到开始位置
            accessFile.seek(startPosition);
            URL url = new URL(path);
            //打开http链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时时间
            conn.setConnectTimeout(5000);
            //指定请求方式为GET方式
            conn.setRequestMethod("GET");
            //指定下载的位置
            conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
            //不用再去判断状态码是否为200
            InputStream in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                accessFile.write(buffer, 0, len);
                //更新下载进度
                listener.getDownload(len);
            }
            accessFile.close();
            in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
