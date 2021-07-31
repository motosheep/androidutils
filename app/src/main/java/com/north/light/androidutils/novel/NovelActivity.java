package com.north.light.androidutils.novel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.novel.reader.NovelReader;
import com.north.light.androidutils.novel.reader.NovelTouchReader;
import com.north.light.androidutils.novel.reader.ReaderTextView;
import com.north.light.androidutils.novel.utils.LogUtils;

public class NovelActivity extends AppCompatActivity {

    private static final String TAG = NovelActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
//        ReaderTextView textView = findViewById(R.id.ddd);
//        textView.setOnTextChangeListener(new ReaderTextView.TextChangeListener() {
//            @Override
//            public void textSize(int count) {
//                LogUtils.d(TAG,count+"多少个字");
//            }
//        });
//        textView.setText(getResources().getString(R.string.messure_txt));
    }
}