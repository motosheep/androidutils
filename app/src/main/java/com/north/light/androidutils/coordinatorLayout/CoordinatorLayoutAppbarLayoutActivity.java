package com.north.light.androidutils.coordinatorLayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;

/**
 * appbar layout 基础实现
 */
public class CoordinatorLayoutAppbarLayoutActivity extends AppCompatActivity {
    private CoorStringRecyclerView mRecy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enterAlwaysCollapsed--滑动到顶部，再次下拉，才会出现
        //enterAlways--随时滑动，都会出现
        //exitUntilCollapsed--根据view的min height进行显示
        //注意，结合recyclerview无数据时，也能滑动recyclerview的情况，只需要把recyclerview的高度修改为自适应即可
        setContentView(R.layout.activity_coordinator_layout_appbar);
        mRecy = findViewById(R.id.activity_coordinator_layout_content);
        mRecy.initData();

    }
}