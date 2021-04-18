package com.north.light.androidutils;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.imageview.FlowLikeView;

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
        final TextView like = findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FlowLikeView) findViewById(R.id.activity_main_like)).addLikeView();
            }
        });
    }

}
