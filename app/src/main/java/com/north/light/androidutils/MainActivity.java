package com.north.light.androidutils;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.audio.ui.AudioActivity;
import com.north.light.androidutils.brand.BrandUtils;
import com.north.light.androidutils.coordinatorLayout.CoordinatorCollapsingActivity;
import com.north.light.androidutils.coordinatorLayout.CoordinatorLayoutAppbarLayoutActivity;
import com.north.light.androidutils.coordinatorLayout.CoordinatorLayoutBehaviorFirstActivity;
import com.north.light.androidutils.dagger.DaggerActivity;
import com.north.light.androidutils.drawer.DrawerActivity;
import com.north.light.androidutils.network.NetStatusActivity;
import com.north.light.androidutils.novel.NovelActivity;
import com.north.light.androidutils.recyclerview.custom.test.XRecyActivity;
import com.north.light.androidutils.recyclerview.sdk.SDKXrecyclerviewActivity;
import com.north.light.androidutils.recyclerview.sdk.SDkSmartActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Log.d("brand", "brand:" + BrandUtils.getInstance().getBrand());
    }

    public void initView() {
        findViewById(R.id.activity_main_coor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入CoordinatorLayout act
                Intent intent = new Intent(MainActivity.this, CoordinatorLayoutAppbarLayoutActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_coor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入CoordinatorLayout act
                Intent intent = new Intent(MainActivity.this, CoordinatorLayoutBehaviorFirstActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_coor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入CoordinatorLayout act
                Intent intent = new Intent(MainActivity.this, CoordinatorCollapsingActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_novel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入novel act
                Intent intent = new Intent(MainActivity.this, NovelActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入novel act
                Intent intent = new Intent(MainActivity.this, AudioActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_recy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, XRecyActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_recy2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SDKXrecyclerviewActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_recy3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SDkSmartActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_netstatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetStatusActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_main_dagger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DaggerActivity.class);
                startActivity(intent);
            }
        });
    }

}
