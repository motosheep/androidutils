package com.north.light.androidutils;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.north.light.androidutils.colordraw.SadDrawUtils;
import com.north.light.androidutils.download.DownloadManager;
import com.north.light.androidutils.imageview.RotateImageView;
import com.north.light.androidutils.textview.NumAnimTextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

    }

}
