package com.north.light.androidutils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.north.light.androidutils.recyclerview.test.XRecyActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void xrecyclerview(View view){
        //xrecyclerview
        startActivity(new Intent(this, XRecyActivity.class));
    }
}
