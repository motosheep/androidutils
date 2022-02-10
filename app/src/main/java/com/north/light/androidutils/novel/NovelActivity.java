package com.north.light.androidutils.novel;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.novel.text.data.TxtManager;
import com.north.light.androidutils.novel.text.data.TxtManagerListener;
import com.north.light.androidutils.novel.text.data.bean.TxtInfo;
import com.north.light.androidutils.novel.text.read.ReaderBaseView;
import com.north.light.androidutils.novel.text.read.ReaderView;

public class NovelActivity extends AppCompatActivity {
    private static final String TAG = NovelActivity.class.getSimpleName();
    private ReaderView readView;

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
            public void count(int count) {
                try {
                    TxtManager.getInstance().loadData(NovelActivity.this,
                            Environment.getExternalStorageDirectory().getPath() + "/novel.txt", count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        readView.setReadEventListener(new ReaderView.ReadEventListener() {
            @Override
            public void change(int type) {
                TxtManager.getInstance().change(type);
            }

            @Override
            public String preData() {
                return TxtManager.getInstance().getShowContent(-1);
            }

            @Override
            public String curData() {
                return TxtManager.getInstance().getShowContent(0);
            }

            @Override
            public String nextData() {
                return TxtManager.getInstance().getShowContent(1);
            }
        });
        TxtManager.getInstance().setOnTxtManagerListener(txtManagerListener);
    }

    private TxtManagerListener txtManagerListener = new TxtManagerListener() {
        @Override
        public void info(TxtInfo info) {

        }

        @Override
        public void init() {
            readView.initCurPage();
        }

        @Override
        public void autoNext() {
            readView.nextPageData();
        }
    };
}