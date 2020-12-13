package com.north.light.androidutils.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
     */
    public void init(Context context) {
        DownloadCacheManager.getInstance().init(context);
    }


    /**
     * 下载文件的方法
     *
     * @param path：下载文件的路径
     * @param listener：自定义的下载文件监听接口
     * @throws Exception
     */
    private void download(String path, ProgressBarListener listener) {
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
     * 截取路径中的文件名称
     *
     * @param path:要截取文件名称的路径
     * @return:截取到的文件名称
     */
    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }


    //外部调用方法---------------------------------------------------------------------------------


    /**
     * 开启
     */
    public void start(final String path, final DataBackInfo listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //总进度
                final AtomicInteger total = new AtomicInteger();
                final AtomicInteger current = new AtomicInteger();
                download(path, new ProgressBarListener() {
                    @Override
                    public void getMax(int length) {
                        total.set(length);
                        if (listener != null) {
                            listener.data(total.get(), 0, 0, false);
                        }
                    }

                    @Override
                    public void getDownload(int length) {
                        if (listener != null) {
                            Float tol = total.get() / 1f;
                            Float cur = current.addAndGet(length) / 1f;
                            listener.data(tol.longValue(), cur.longValue(), cur / tol, (tol.equals(cur)));
                        }
                    }


                    @Override
                    public void error(String msg) {
                        if (listener != null) {
                            listener.error(msg);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 关闭
     */
    public Boolean stop(String path) {
        return ExecutorsManager.getInstance().closeCacheExecutors(path);
    }

    /**
     * 数据回调监听
     */
    public interface DataBackInfo {
        void data(long total, long current, float percent, boolean isFinish);

        void error(String message);
    }
}
