package com.north.light.androidutils.novel.reader;

import android.text.TextUtils;

import com.north.light.androidutils.MainApplication;
import com.north.light.androidutils.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: ReadDataManager
 * Author: lizhengting
 * Date: 2021/7/31 8:27
 * Description:阅读数据管理类
 * 阅读，则一定需要一个页数，和对应每一页显示的字数
 * 本类功能，用于把数据来源，划分好页数，与页数对应的字数
 */
public class ReadDataManager implements Serializable {

    /**
     * 当前阅读的位置
     */
    private int mCurReadPos = 0;

    /**
     * 当前阅读数据集合
     */
    private List<ReadData> mCurReadList = new ArrayList<>();

    /**
     * 原数据
     */
    private StringBuilder original = new StringBuilder();

    /**
     * 更新集合
     *
     * @param unitSize 每一页容纳的最大字符数量
     */
    public List<ReadData> updateList(int unitSize) throws Exception {
        if (TextUtils.isEmpty(original.toString())) {
            original.append(MainApplication.getContext().getResources().getString(R.string.novel));
//
//            for (int i = 0; i < 1000; i++) {
////                original.append(MainApplication.getContext().getResources().getString(R.string.messure_txt));
//                if (i % 2== 0) {
//                    original.append("\n");
//                    original.append("\t");
//                }
//                original.append("i just want to fucking you" + i + "个");
//            }
        }
        mCurReadList.clear();
        int rest = original.toString().length() % unitSize;
        int size = (original.toString().length() / unitSize) + ((rest == 0) ? 0 : 1);
        for (int i = 0; i < size; i++) {
            if (i != size - 1) {
                String detail = original.toString().substring(i * unitSize, (i + 1) * unitSize);
                mCurReadList.add(i, new ReadData(detail));
            } else {
                if (size == 1) {
                    String detail = original.toString();
                    mCurReadList.add(i, new ReadData(detail));
                } else {
                    String detail = original.toString().substring(i * unitSize);
                    mCurReadList.add(i, new ReadData(detail));
                }
            }
        }
        return mCurReadList;
    }


    private static final class SingleHolder {
        static ReadDataManager mInstance = new ReadDataManager();
    }

    public static ReadDataManager getInstance() {
        return SingleHolder.mInstance;
    }

    public ReadDataManager() {

    }

    /**
     * 获取上一个
     */
    public List<ReadData> getPre() throws Exception {
        return getNextOrPre(0);
    }

    /**
     * 获取下一个
     */
    public List<ReadData> getNext() throws Exception {
        return getNextOrPre(1);
    }

    /**
     * 能否获取下一个
     */
    public boolean hasNext() {
        return !((mCurReadPos == mCurReadList.size() - 1) || (mCurReadList.size() == 0));
    }

    /**
     * 能否获取上一个
     */
    public boolean hasPre() {
        return !((mCurReadList.size() == 0) || mCurReadPos == 0);
    }

    /**
     * 获取当前位置的数据
     */
    public List<ReadData> getCur() throws Exception {
        if (mCurReadList.size() == 0) {
            throw new Exception("暂无更多数据");
        } else if (mCurReadList.size() == 1) {
            if (mCurReadPos == 0) {
                ReadData nextD = new ReadData();
                ReadData curD = mCurReadList.get(mCurReadPos);
                ReadData preD = new ReadData();
                List<ReadData> result = new ArrayList<>();
                result.add(0, preD);
                result.add(1, curD);
                result.add(2, nextD);
                return result;
            } else {
                throw new Exception("暂无更多数据");
            }
        } else {
            if (mCurReadPos < 0 || mCurReadPos >= mCurReadList.size()) {
                throw new Exception("暂无更多数据");
            }
            ReadData nextD = new ReadData();
            ReadData curD = mCurReadList.get(mCurReadPos);
            ReadData preD = new ReadData();
            List<ReadData> result = new ArrayList<>();
            if (mCurReadPos - 1 >= 0) {
                preD = mCurReadList.get(mCurReadPos - 1);
            }
            if (mCurReadPos + 1 < mCurReadList.size()) {
                nextD = mCurReadList.get(mCurReadPos + 1);
            }
            result.add(0, preD);
            result.add(1, curD);
            result.add(2, nextD);
            return result;
        }
    }


    private List<ReadData> getNextOrPre(int type) throws Exception {
        if (type == 1) {
            //下一个
            if (mCurReadPos + 1 >= mCurReadList.size()) {
                throw new Exception("暂无更多数据");
            }
            mCurReadPos = mCurReadPos + 1;
            int pre = mCurReadPos - 1;
            int cur = mCurReadPos;
            int next = mCurReadPos + 1;
            ReadData preD = mCurReadList.get(pre);
            ReadData curD = mCurReadList.get(cur);
            ReadData nextD = new ReadData();
            if (next < mCurReadList.size()) {
                nextD = mCurReadList.get(next);
            }
            List<ReadData> result = new ArrayList<>();
            result.add(0, preD);
            result.add(1, curD);
            result.add(2, nextD);
            return result;
        } else {
            //上一个
            if (mCurReadPos - 1 < 0) {
                throw new Exception("暂无更多数据");
            }
            mCurReadPos = mCurReadPos - 1;
            int pre = mCurReadPos - 1;
            int cur = mCurReadPos;
            int next = mCurReadPos + 1;
            ReadData nextD = mCurReadList.get(next);
            ReadData curD = mCurReadList.get(cur);
            ReadData preD = new ReadData();
            if (pre >= 0) {
                preD = mCurReadList.get(pre);
            }
            List<ReadData> result = new ArrayList<>();
            result.add(0, preD);
            result.add(1, curD);
            result.add(2, nextD);
            return result;
        }
    }


    public static class ReadData implements Serializable {
        private String data;

        public ReadData(String data) {
            this.data = data;
        }

        public ReadData() {
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
