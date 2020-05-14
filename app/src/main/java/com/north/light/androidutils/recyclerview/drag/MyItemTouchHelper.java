package com.north.light.androidutils.recyclerview.drag;

import android.support.annotation.NonNull;

/**
 * Created by lzt
 * time 2020/5/14
 * 描述：拖拽调用类
 * 传入recyclerview只在原生测试下没有问题，自定义recyclerview不建议
 */

public class MyItemTouchHelper extends BaseItemTouchHelper {

    private MyItemTouchHelpCallback myItemTouchHelpCallback;

    public MyItemTouchHelper(MyItemTouchHelpCallback.OnItemTouchCallbackListener callback) {
        super(new MyItemTouchHelpCallback(callback));
        myItemTouchHelpCallback = (MyItemTouchHelpCallback) getCallback();
    }

    /**
     * 设置是否可以拖动
     *
     * @param canDrag
     */
    public void setDragEnable(boolean canDrag) {
        myItemTouchHelpCallback.setDragEnable(canDrag);
    }

    public void setSwipeEnable(boolean canSwipe) {
        myItemTouchHelpCallback.setSwipeEnable(canSwipe);
    }
}


/*
*  //拖拽设置
        MyItemTouchHelper itemTouchHelper;
        itemTouchHelper = new MyItemTouchHelper(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        itemTouchHelper.setSwipeEnable(true);
        itemTouchHelper.setDragEnable(true);
*
* */


/*//拖拽回调
    private MyItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener = new MyItemTouchHelpCallback.OnItemTouchCallbackListener() {

        @Override
        public void onSwiped(int position) {
            if (contentList != null) {
                contentList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        }

        @Override
        public boolean onMove(int srcPosition, int targerPostion) {
            if (contentList != null) {
                Collections.swap(contentList, srcPosition, targerPostion);
                adapter.notifyItemMoved(srcPosition, targerPostion);
            }
            return true;
        }
    };
*
* */


