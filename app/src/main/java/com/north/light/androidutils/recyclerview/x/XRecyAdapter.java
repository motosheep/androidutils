package com.north.light.androidutils.recyclerview.x;


import androidx.recyclerview.widget.RecyclerView;

import com.north.light.androidutils.R;

public class XRecyAdapter extends XRecyclerAdapter {

    public XRecyAdapter(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public int getHeaderViewLayout() {
        return R.layout.recy_item_test_head;
    }

    @Override
    public int getFooterViewLayout() {
        return R.layout.recy_item_test_foot;
    }
}
