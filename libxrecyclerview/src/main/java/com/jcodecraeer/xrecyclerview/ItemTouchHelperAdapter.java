package com.jcodecraeer.xrecyclerview;


/**
 * Created by jianghejie on 16/6/20.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
