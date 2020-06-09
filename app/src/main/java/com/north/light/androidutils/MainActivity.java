package com.north.light.androidutils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.north.light.androidutils.recyclerview.test.XRecyActivity;
import com.north.light.androidutils.textview.VerticalScrollTxView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerticalScrollTxView scrollTxView = findViewById(R.id.scrollText);
        List<String> mShowList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mShowList.add("位置: " + i);
        }
        scrollTxView.setTextList(mShowList);
    }


    public void xrecyclerview(View view) {
        //xrecyclerview
        startActivity(new Intent(this, XRecyActivity.class));
    }
}
