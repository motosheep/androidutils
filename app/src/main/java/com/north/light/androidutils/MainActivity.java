package com.north.light.androidutils;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.north.light.androidutils.imageview.RedHeartImageView;

public class MainActivity extends AppCompatActivity {
    int type = 1;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final RedHeartImageView rImg = findViewById(R.id.activity_main_r_img);
        rImg.setRes(R.mipmap.ic_heart_unsel,R.mipmap.ic_heart_sel);
        rImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rImg.changeRes(type%2);
                type = type + 1;
            }
        });
    }

}
