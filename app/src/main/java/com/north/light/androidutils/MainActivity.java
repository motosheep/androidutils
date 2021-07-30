package com.north.light.androidutils;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.activitycounter.ActivityCounter;
import com.north.light.androidutils.brand.BrandUtils;
import com.north.light.androidutils.coordinatorLayout.CoordinatorCollapsingActivity;
import com.north.light.androidutils.coordinatorLayout.CoordinatorLayoutAppbarLayoutActivity;
import com.north.light.androidutils.coordinatorLayout.CoordinatorLayoutBehaviorFirstActivity;

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
    }

}
