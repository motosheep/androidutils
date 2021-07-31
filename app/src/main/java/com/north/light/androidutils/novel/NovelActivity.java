package com.north.light.androidutils.novel;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;

public class NovelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        ReaderTextView canvas = findViewById(R.id.activity_novel_canvas);
        canvas.setOnTextChangeListener(new ReaderTextView.TextChangeListener() {
            @Override
            public void textSize(int count) {
                Log.d("Novel", "字数数量:" + count);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < 100; i++) {
                    str.append("第").append(i);
                }
                canvas.setText(str);
            }
        }, 200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < 122; i++) {
                    str.append("第").append(i);
                }
                canvas.setText(str);
            }
        }, 300);
    }
}