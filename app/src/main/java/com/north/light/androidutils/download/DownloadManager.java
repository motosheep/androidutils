package com.north.light.androidutils.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;


/**
 * 文件下载管理器
 *
 * @author liuyazhuang
 */
public class DownloadManager {
    //下载线程的数量
    private static final int TREAD_SIZE = 3;
    private File file;

    /**
     * 下载文件的方法
     *
     * @param path：下载文件的路径
     * @param listener：自定义的下载文件监听接口
     * @throws Exception
     */
    public void download(String path, ProgressBarListener listener) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            int filesize = conn.getContentLength();
            //设置进度条的最大长度
            listener.getMax(filesize);
            //创建一个和服务器大小一样的文件
            file = new File(Environment.getExternalStorageDirectory(), this.getFileName(path));
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(filesize);
            //要关闭RandomAccessFile对象
            accessFile.close();

            //计算出每条线程下载的数据量
            int block = filesize % TREAD_SIZE == 0 ? (filesize / TREAD_SIZE) : (filesize / TREAD_SIZE + 1);

            //开启线程下载
            for (int i = 0; i < TREAD_SIZE; i++) {
                new DownloadThread(i, path, file, listener, block).start();
            }
        }
    }

    /**
     * 截取路径中的文件名称
     *
     * @param path:要截取文件名称的路径
     * @return:截取到的文件名称
     */
    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
