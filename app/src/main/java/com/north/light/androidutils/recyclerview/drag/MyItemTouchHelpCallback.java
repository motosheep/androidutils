package com.north.light.androidutils.recyclerview.drag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by lzt
 * time 2020/5/14
 * 描述：
 */
public class MyItemTouchHelpCallback extends ItemTouchHelper.Callback {

    /**Item操作的回调*/
    private OnItemTouchCallbackListener onItemTouchCallbackListener;
    /**是否可以拖拽**/
    private boolean isCanDrag = false;
    /***是否可以滑动*/
    private boolean isCanSwipe = false;

    public MyItemTouchHelpCallback(OnItemTouchCallbackListener onItemTouchCallbackListener) {
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    public void setOnItemTouchCallbackListener(OnItemTouchCallbackListener onItemTouchCallbackListener){
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    /**
     * 设置是否可以被拖拽
     * */
    public void setDragEnable(boolean CanDrag){
        this.isCanDrag = CanDrag;
    }

    /**
     * 设置是否可以滑动
     * @param canSwipe
     */
    public void setSwipeEnable(boolean canSwipe){
        this.isCanSwipe = canSwipe;
    }

    /**
     * 当Item被长安的时候是否可以拖动
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return isCanDrag;
    }

    /**
     * Item是否可以被滑动(H：左右滑动，V：上下滑动)
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return isCanSwipe;
    }


    /**
     * 当用户拖拽或者滑动Item的时候需要我们告诉系统滑动或者拖拽的方向
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();

            int dragFlag = 0;
            int swipFlag = 0;

            if(orientation== LinearLayoutManager.HORIZONTAL){
                swipFlag = ItemTouchHelper.UP |ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT;
            }else if(orientation== LinearLayoutManager.VERTICAL){
                dragFlag = ItemTouchHelper.UP |ItemTouchHelper.DOWN;
                swipFlag = ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag,swipFlag);
        }
        return 0;
    }


    /**
     * 当Item被拖拽的时候回调
     * @param recyclerView
     * @param viewHolder 拖拽的vieholder
     * @param target 目的拖拽viewHolder
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(onItemTouchCallbackListener!=null)
            return onItemTouchCallbackListener.onMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }

    /**
     * 当Item被删除的时候回调
     * @param viewHolder 要删除的item
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(onItemTouchCallbackListener!=null){
            onItemTouchCallbackListener.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    public interface OnItemTouchCallbackListener{
        /**
         * 当某个item被滑动删除的时候
         * @param position
         */
        void onSwiped(int position);

        /**
         * 当连个Item位置互相的时候
         * @param srcPosition
         * @param targerPostion
         * @return
         */
        boolean onMove(int srcPosition,int targerPostion);
    }
}