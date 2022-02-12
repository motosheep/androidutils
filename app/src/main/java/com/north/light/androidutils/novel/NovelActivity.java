package com.north.light.androidutils.novel;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;
import com.north.light.androidutils.novel.text.data.TxtManager;
import com.north.light.androidutils.novel.text.data.TxtManagerListener;
import com.north.light.androidutils.novel.text.read.ReaderBaseView;
import com.north.light.androidutils.novel.text.read.ReaderView;

public class NovelActivity extends AppCompatActivity {
    private static final String TAG = NovelActivity.class.getSimpleName();
    private ReaderView readView;
    //是否已经读取
    private boolean isReadBook = false;

    @Override
    protected void onDestroy() {
        try {
            TxtManager.getInstance().cancel(NovelActivity.this, Environment.getExternalStorageDirectory().getPath() + "/novel.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TxtManager.getInstance().removeTxtManagerListener(txtManagerListener);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        readView = findViewById(R.id.activity_novel);
        readView.setTextStatusListener(new ReaderBaseView.TextStatusListener() {
            @Override
            public void maxDraw(int count) {
                try {
                    TxtManager.getInstance().setPageMaxSize(count);
                    if (isReadBook) {
                        return;
                    }
                    isReadBook = true;
                    TxtManager.getInstance().loadData(NovelActivity.this,
                            Environment.getExternalStorageDirectory().getPath() + "/novel.txt", 1, 0);
                    LogUtil.d("maxDraw" + count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void curDraw(int count) {
                //当前绘制数据
                LogUtil.d("curDraw" + count);
                TxtManager.getInstance().setPageShowSize(count);
            }

        });
        readView.setReadEventListener(new ReaderView.ReadEventListener() {
            @Override
            public void change(int type) {
                //切换数据
                LogUtil.d("change" + type);
                TxtManager.getInstance().change(type);
            }
        });
        TxtManager.getInstance().setOnTxtManagerListener(txtManagerListener);
    }


    private TxtManagerListener txtManagerListener = new TxtManagerListener() {

        @Override
        public void readBookError(Exception e) {

        }

        @Override
        public void ready(String path, String preViewContent) {
            readView.initData(preViewContent);
        }

        @Override
        public void progress(String path, String trainPath, long curPos, long totalPos) {

        }
    };
}