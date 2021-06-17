package com.north.light.androidutils;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.imageview.FlowLikeView;
import com.north.light.androidutils.imageview.RotateImageView;

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
        final RotateImageView like = findViewById(R.id.activity_main_rotate);
       like.startAnim();
    }

}
