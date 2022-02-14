package com.north.light.androidutils.novel.text.data;

import android.content.Context;
import android.text.TextUtils;

import com.north.light.androidutils.gson.GsonUtils;
import com.north.light.androidutils.log.LogUtil;
import com.north.light.androidutils.novel.text.data.bean.TxtInfo;
import com.north.light.androidutils.novel.text.data.bean.TxtReadInfo;
import com.north.light.androidutils.novel.text.data.function.StreamReader;
import com.north.light.androidutils.novel.text.data.function.TxtIOStreamReader;
import com.north.light.androidutils.novel.text.data.function.TxtLoadingListener;
import com.north.light.androidutils.novel.text.data.function.TxtMemoryManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: lzt
 * @Date: 2022/2/10 9:57
 * @Description:txt管理类--汇总内存，读取等逻辑
 */
public class TxtManager implements TxtManagerApi {
    //监听集合
    private CopyOnWriteArrayList<TxtManagerListener> mListener = new CopyOnWriteArrayList<>();
    private StreamReader streamReader;
    //当前文件的path
    private String mCurrentPath;
    //全局context
    private Context mContext;
    //当前阅读位置--------
    //某章阅读游标
    private int mDetailPos = 0;
    //第几章
    private int mSectionPos = 0;
    //标识-------
    private AtomicBoolean mLoading = new AtomicBoolean();
    //每页最大的数量
    private int mPageMaxSize = 0;
    //当前页面显示的字体数量
    private int mPageShowSize = 0;
    //当前章节内容
    private String mContent;
    //当前阅读游标
    private AtomicInteger mCachePos = new AtomicInteger(0);
    private HashMap<Integer, Integer> mCachePosMap = new HashMap<>();

    public static class SingleHolder implements Serializable {
        static TxtManager mInstance = new TxtManager();
    }

    public static TxtManager getInstance() {
        return TxtManager.SingleHolder.mInstance;
    }

    public TxtManager() {
        streamReader = new TxtIOStreamReader();
    }

    /**
     * 设置loading
     */
    private void setLoading(boolean load) {
        mLoading.set(load);
    }

    /**
     * 通知外部数据错误
     */
    private void notifyError(Exception e) {
        for (TxtManagerListener listener : mListener) {
            listener.readBookError(e);
        }
    }

    /**
     * 读取完数据了
     */
    private void notifyReady(String path, String previewContent) {
        for (TxtManagerListener listener : mListener) {
            listener.ready(path, previewContent);
        }
    }


    /**
     * 处理读取结果
     */
    private void dealReadInfo(TxtReadInfo info, String key) {
        if (info == null || TextUtils.isEmpty(key)) {
            notifyError(new Exception("read read info failed because params not exist"));
            setLoading(false);
            return;
        }
        mContent = info.getContent();
        String content = mContent;
        if (!TextUtils.isEmpty(content)) {
            if (mDetailPos >= content.length() || (mDetailPos + mPageMaxSize) >= content.length()) {
                notifyError(new Exception("read detail pos error"));
                setLoading(false);
                return;
            }
            String tx = content.substring(mDetailPos, mDetailPos + mPageMaxSize);
            notifyReady(info.getOrgPath(), tx);
        }
        //处理数据
        setLoading(false);
    }

    /**
     * 读取章节信息
     */
    private void readSection(Map<Integer, TxtInfo> infoMap) {
        //判断本书是否有数据
        if (infoMap == null || infoMap.size() == 0) {
            //错误的数据
            notifyError(new Exception("read book failed because size not map"));
            setLoading(false);
            return;
        }
        TxtMemoryManager.getInstance().setSum(mCurrentPath, infoMap);
        //判断传入的position是否符合条件
        int bookSectionSize = infoMap.size();
        if (mSectionPos > bookSectionSize) {
            //错误--传入参数错误--从头开始
            mSectionPos = 1;
        }
        TxtInfo txInfo = TxtMemoryManager.getInstance().getSum(mCurrentPath, mSectionPos);
        if (txInfo == null) {
            notifyError(new Exception("read book failed because info is not exist"));
            setLoading(false);
            return;
        }
        //读取该章节的数据
        try {
            streamReader.read(mContext, txInfo, txInfo.getTrainPath());
        } catch (Exception e) {
            e.printStackTrace();
            notifyError(e);
            setLoading(false);
        }
    }

    private int getCachePos(int pos) {
        Integer integer = mCachePosMap.get(pos);
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    private void setCachePos(int pos, int count) {
        mCachePosMap.put(pos, count);
    }

    //外部调用---------------------------------------------------------------------------------------

    /**
     * init必须调用--application中
     */
    @Override
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        streamReader.setOnLoadingListener(new TxtLoadingListener() {
            @Override
            public void splitPart(String path, String name, TxtInfo info) {
                LogUtil.d("加载loadingPart: path" + path + "\tname:" + name + "\tinfo:" + GsonUtils.getJsonStr(info));
            }

            @Override
            public void splitFinish(String path, String name, Map<Integer, TxtInfo> infoMap) {
                LogUtil.d("加载loadingFinish: path" + path + "\tname:" + name);
                //开始读取指定章节的数据
                readSection(infoMap);
            }

            @Override
            public void splitFailed(Exception e) {
                LogUtil.d("加载loadFailed e:" + e.getMessage());
                setLoading(false);
            }

            @Override
            public void splitProgress(String path, String trainPath, long progress, long total) {
                LogUtil.d("加载progress: path" + path + "\ttrainPath:" + trainPath + "\tprogress:" + progress + "\ttotal:" + total);
            }

            @Override
            public void read(TxtReadInfo info, String key) {
                LogUtil.d("读取read:" + GsonUtils.getJsonStr(info));
                dealReadInfo(info, key);
            }

            @Override
            public void readFailed(Exception e) {
                LogUtil.d("读取e:" + e.getMessage());
                setLoading(false);
            }
        });
    }


    @Override
    public void loadData(Context context, String path, int sectionPos, int detailPos) throws Exception {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        cancel(context, path);
        mCachePosMap.clear();
        mCachePos.set(0);
        mSectionPos = sectionPos;
        mDetailPos = detailPos;
        mCurrentPath = path;
        setLoading(true);
        //开始分割----
        streamReader.split(context, path);
    }

    @Override
    public void loadData(Context context, String path) throws Exception {
        loadData(context, path, 1, 0);
    }

    @Override
    public void cancel(Context context, String path) throws Exception {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        TxtMemoryManager.getInstance().clearSum(path);
        TxtMemoryManager.getInstance().clearContent(path);
        streamReader.cancel(context, path);
        setLoading(false);
    }

    /**
     * 修改了数据
     *
     * @param type -1上一页 1下一页
     */
    @Override
    public void change(int type) {
        if (isLoading()) {
            return;
        }
        if (type == 1) {
            if (mDetailPos + mPageShowSize >= mContent.length()) {
                //加载下一章
                int sumSize = TxtMemoryManager.getInstance().getSumSize(mCurrentPath);
                if (mSectionPos > sumSize - 1) {
                    return;
                }
                mSectionPos = mSectionPos + 1;
                mDetailPos = 0;
                readSection(TxtMemoryManager.getInstance().getSum(mCurrentPath));
                return;
            }
            //下一页
            mCachePos.incrementAndGet();
            mDetailPos = mDetailPos + mPageShowSize;
            String nextContent = mContent.substring(mDetailPos, Math.min(mDetailPos + mPageMaxSize,mContent.length()));
            notifyReady(mCurrentPath, nextContent);
        } else if (type == -1) {
            //上一页
            if (mDetailPos <= 0) {
                //加载上一章
                if (mSectionPos <= 1) {
                    //没有上一章
                    return;
                }
                mSectionPos = mSectionPos - 1;
                mDetailPos = 0;
                readSection(TxtMemoryManager.getInstance().getSum(mCurrentPath));
                return;
            }
            int posCount = getCachePos(mCachePos.get() - 1);
            if (posCount == 0) {
                return;
            }
            mDetailPos = mDetailPos - posCount;
            mCachePos.decrementAndGet();
            String nextContent = mContent.substring(mDetailPos, mDetailPos + posCount);
            notifyReady(mCurrentPath, nextContent);
        }
    }

    @Override
    public boolean isLoading() {
        return mLoading.get();
    }

    //监听
    @Override
    public void setOnTxtManagerListener(TxtManagerListener listener) {
        if (listener == null) {
            return;
        }
        mListener.add(listener);
    }

    @Override
    public void removeTxtManagerListener(TxtManagerListener listener) {
        if (listener == null) {
            return;
        }
        mListener.remove(listener);
    }

    @Override
    public void setPageMaxSize(int size) {
        mPageMaxSize = size;
    }

    @Override
    public void setPageShowSize(int size) {
        mPageShowSize = size;
        setCachePos(mCachePos.get(), mPageShowSize);
    }

}
