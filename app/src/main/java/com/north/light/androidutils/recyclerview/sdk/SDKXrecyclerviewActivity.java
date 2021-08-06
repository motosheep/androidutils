package com.north.light.androidutils.recyclerview.sdk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 三方recyclerview
 */
public class SDKXrecyclerviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdkxrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        XRecyclerView mRecyclerView = findViewById(R.id.activity_sdk_recy);
        mRecyclerView.setLayoutManager(layoutManager);
        SDKTestAdapter mAdapter = new SDKTestAdapter();
        List<String> mData = new ArrayList();
        for (int i = 0; i < 30; i++) {
            mData.add(i + "");
        }
        mAdapter.add(mData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.d("下拉刷新");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.refreshComplete();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                LogUtil.d("加载更多");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.loadMoreComplete();
                            }
                        });
                    }
                }).start();
            }
        });
    }
}