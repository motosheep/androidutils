package com.north.light.androidutils.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Environment;


/**
 * 文件下载管理器
 *
 * @author liuyazhuang
 */
public class DownloadManager {
    //下载线程的数量
    private static final int TREAD_SIZE = 5;
    private File file;

    private static final class SingleHolder {
        static DownloadManager mInstance = new DownloadManager();
    }

    public static DownloadManager getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 初始化方法
     * */
    public void init(Context context){
        DownloadCacheManager.getInstance().init(context);
    }

    /**
     * 下载文件的方法
     *
     * @param path：下载文件的路径
     * @param listener：自定义的下载文件监听接口
     * @throws Exception
     */
    public void download(String path, ProgressBarListener listener) {
        try {
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
                ExecutorsManager.getInstance().closeCacheExecutors(path);
                for (int i = 0; i < TREAD_SIZE; i++) {
                    ExecutorsManager.getInstance().getCacheExecutors(path).execute(new DownloadThread(i, path, file, listener, block));
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.error(e.getMessage());
            }
        }
    }

    /**
     * 关闭下载
     */
    public Boolean stopDownLoad(String path) {
        return ExecutorsManager.getInstance().closeCacheExecutors(path);
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
