package com.north.light.androidutils.coordinatorLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.north.light.androidutils.R;
import com.north.light.androidutils.coordinatorLayout.collapsing.CollapsingToolbarLayoutState;

/**
 * coordinator Collapsing activity
 */
public class CoordinatorCollapsingActivity extends AppCompatActivity {
    private CoorStringRecyclerView mRecy;
    private CollapsingToolbarLayoutState state = CollapsingToolbarLayoutState.EXPANDED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_collapsing);
        mRecy = findViewById(R.id.activity_collapsing_content);
        mRecy.initData();
        AppBarLayout toolbar = findViewById(R.id.barlayout);
        TextView toolbartx = findViewById(R.id.toolbartx);
        toolbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        toolbartx.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= toolbar.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        toolbartx.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }
}