package com.north.light.androidutils.coordinatorLayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;

/**
 * CoordinatorLayout自定义behavior
 */
public class CoordinatorLayoutBehaviorFirstActivity extends AppCompatActivity {
    private CoorStringRecyclerView mRecy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout_behavior_first);
        mRecy = findViewById(R.id.activity_coordinator_layout_content);
        mRecy.initData();
    }
}