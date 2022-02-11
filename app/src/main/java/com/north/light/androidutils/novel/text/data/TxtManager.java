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
import com.north.light.androidutils.novel.text.data.util.TxStringSplitUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: lzt
 * @Date: 2022/2/10 9:57
 * @Description:txt管理类
 */
public class TxtManager implements TxtManagerApi {
    private StreamReader streamReader;
    //当前文件的path
    private String mCurrentPath;
    //每页显示字体大小
    private int mPageSize;
    //监听集合
    private CopyOnWriteArrayList<TxtManagerListener> mListener = new CopyOnWriteArrayList<>();
    //全局context
    private Context mContext;
    //首次读取，初始化相关信息
    private AtomicBoolean mFirstLoad = new AtomicBoolean(true);
    private AtomicBoolean mFirstRead = new AtomicBoolean(true);
    private AtomicBoolean mNextRead = new AtomicBoolean(false);
    private AtomicBoolean mIsLoading = new AtomicBoolean(true);
    private AtomicBoolean mIsLoadFinish = new AtomicBoolean(false);
    //当前阅读位置
    private int mCurPos = 0;
    //当前分页位置
    private int mCurMapInfoPos = 1;

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
     * 读取分割文件数据
     */
    private void loadSplitFile(TxtInfo info) throws Exception {
        streamReader.read(mContext, info);
    }

    /**
     * 通知初始化
     */
    private void notifyInit() {
        for (TxtManagerListener listener : mListener) {
            listener.init();
        }
    }

    /**
     * 通知自动下一页
     */
    private void notifyAutoNext() {
        for (TxtManagerListener listener : mListener) {
            listener.autoNext();
        }
    }

    /**
     * 加载下一页数据
     */
    private void loadNextPage() {
        try {
            TxtInfo txtInfo = TxtMemoryManager.getInstance().getSum(mCurrentPath, mCurMapInfoPos);
            loadSplitFile(txtInfo);
            mIsLoading.set(true);
        } catch (Exception e) {
            e.printStackTrace();
            mIsLoading.set(false);
        }
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
                TxtMemoryManager.getInstance().addSum(mCurrentPath, info);
                //不断分割不断读取
                try {
                    if (mFirstLoad.get()) {
                        mFirstLoad.set(false);
                        loadSplitFile(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void splitFinish(String path, String name, Map<Integer, TxtInfo> infoMap) {
                LogUtil.d("加载loadingFinish: path" + path + "\tname:" + name);
                mIsLoadFinish.set(true);
            }

            @Override
            public void splitFailed(Exception e) {
                LogUtil.d("加载loadFailed e:" + e.getMessage());
                mIsLoading.set(false);
            }

            @Override
            public void read(TxtReadInfo info) {
                mCurMapInfoPos = mCurMapInfoPos + 1;
                TxtMemoryManager.getInstance().addContent(mCurrentPath, info);
                List<String> txList = TxStringSplitUtils.splitStrToList(info.getContent(), mPageSize);
                TxtMemoryManager.getInstance().addCur(txList);
                mIsLoading.set(false);
                if (mFirstRead.get()) {
                    mFirstRead.set(false);
                    long memorySize = TxtMemoryManager.getInstance().getCurLength();
                    int totalPage = (int) (memorySize / mPageSize);
                    mCurPos = Math.min(totalPage, mCurPos);
                    notifyInit();
                } else if (mNextRead.get()) {
                    mNextRead.set(false);
                    notifyAutoNext();
                }
            }

            @Override
            public void readFailed(Exception e) {
                LogUtil.d("读取e:" + e.getMessage());
                mIsLoading.set(false);
            }
        });
    }

    @Override
    public void loadData(Context context, String path, int pageSize, int readPos) throws Exception {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        mCurPos = readPos;
        mCurrentPath = path;
        mPageSize = pageSize;
        mIsLoadFinish.set(false);
        mIsLoading.set(true);
        mFirstRead.set(true);
        mFirstLoad.set(true);
        mNextRead.set(false);
        cancel(context, path);
        TxtMemoryManager.getInstance().clearCur();
        TxtMemoryManager.getInstance().clearSum(path);
        TxtMemoryManager.getInstance().clearContent(path);
        streamReader.split(context, path);
    }

    @Override
    public void cancel(Context context, String path) throws Exception {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        TxtMemoryManager.getInstance().clearCur();
        TxtMemoryManager.getInstance().clearSum(path);
        TxtMemoryManager.getInstance().clearContent(path);
        streamReader.cancel(context, path);
    }

    /**
     * 修改了数据
     *
     * @param type -1上一页 0当前页 1下一页
     */
    @Override
    public void change(int type) {
        if (isLoading()) {
            return;
        }
        List<String> data = TxtMemoryManager.getInstance().getCurList();
        if (data == null || data.size() == 0) {
            return;
        }
        if (data.size() == 1) {
            if (type == 1) {
                //自动加载下一个数据集合
                mNextRead.set(true);
                loadNextPage();
                return;
            }
        }
        if (mCurPos == 0) {
            //第一个
            if (type == 1) {
                mCurPos = mCurPos + 1;
            }
        } else if (mCurPos == TxtMemoryManager.getInstance().getCurList().size() - 1) {
            //最后一个
            if (type == -1) {
                mCurPos = mCurPos - 1;
            }
            //自动加载下一个数据集合
            mNextRead.set(true);
            loadNextPage();
        } else {
            //中间
            if (type == -1) {
                mCurPos = mCurPos - 1;
            } else if (type == 1) {
                mCurPos = mCurPos + 1;
            }
        }
    }

    /**
     * 获取不同的数据
     */
    @Override
    public String getShowContent(int type) {
        if (isLoading()) {
            return "";
        }
        List<String> data = TxtMemoryManager.getInstance().getCurList();
        if (mCurPos >= data.size()) {
            return "";
        }
        if (data == null || data.size() == 0) {
            return "";
        }
        switch (type) {
            case -1:
                if (mCurPos == 0) {
                    return data.get(mCurPos);
                }
                return data.get(mCurPos - 1);
            case 0:
                if (mCurPos == 0) {
                    return data.get(mCurPos);
                }
                return data.get(mCurPos);
            case 1:
                if (mCurPos == data.size() - 1) {
                    return data.get(mCurPos);
                }
                return data.get(mCurPos + 1);
        }
        return "";
    }

    @Override
    public boolean isLoading() {
        return mIsLoading.get();
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

}
