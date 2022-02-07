package com.north.light.androidutils.novel;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.novel.text.FitTextListener;
import com.north.light.androidutils.novel.text.FitTextView;

public class NovelActivity extends AppCompatActivity {

    private static final String TAG = NovelActivity.class.getSimpleName();
    private FitTextView fitTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        fitTextView = findViewById(R.id.activity_novel);
        fitTextView.setOnTextListener(new FitTextListener() {
            @Override
            public void drawCount(int count) {
                Log.d(TAG, "可显示字体数量" + count);
            }
        });
        fitTextView.setTextSize(64, 20, 20);
        fitTextView.setTextView("\t\t今天是个好日子，然后我出去放牛了，路上了隔壁村的寡妇。我穿着黑色毛衣，" +
                "一路西区，发现很多钞票随风飘逸.今天是个好日子，然后我出去放牛了，路上了隔壁村的寡妇。我穿着黑色毛衣," +
                "一路西区，发现很多钞票随风飘逸.今天是个好日子，然后我出去放牛了，路上了隔壁村的寡妇。我穿着黑色毛衣," +
                "一路西区，发现很多钞票随风飘逸.今天是个好日子，然后我出去放牛了，路上了隔壁村的寡妇。我穿着黑色毛衣," +
                "一路西区，发现很多钞票随风飘逸.今天是个好日子，然后我出去放牛了，路上了隔壁村的寡妇。我穿着黑色毛衣," +
                "一路西区，发现很多钞票随风飘逸.今天是个好日子，然后我出去放牛了，路上了隔壁村的寡妇。我穿着黑色毛衣," +
                "一路西区，发现很多钞票随风飘逸.");

        SeekBar fontSize = findViewById(R.id.novel_size);
        SeekBar colSize = findViewById(R.id.novel_col_size);
        SeekBar rowSize = findViewById(R.id.novel_row_size);
        fontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fitTextView.setTextSize(seekBar.getProgress());
            }
        });
        colSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fitTextView.setWidthInterval(seekBar.getProgress());

            }
        });
        rowSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fitTextView.setHeightInterval(seekBar.getProgress());

            }
        });
    }
}