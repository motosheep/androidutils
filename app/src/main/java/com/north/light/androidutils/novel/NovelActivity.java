package com.north.light.androidutils.novel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.novel.reader.NovelReader;
import com.north.light.androidutils.novel.reader.NovelTouchReader;
import com.north.light.androidutils.novel.utils.LogUtils;

public class NovelActivity extends AppCompatActivity {

    private static final String TAG = NovelActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        NovelReader mReader = findViewById(R.id.activity_novel_reader);
    }
}