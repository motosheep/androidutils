package com.north.light.androidutils.recyclerview.custom.x;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义RecyclerView
 */
public class XRecyclerView extends RecyclerView {
    private static final String TAG = XRecyclerView.class.getName();
    // 最后几个完全可见项的位置（瀑布式布局会出现这种情况）
    private int[] lastVisiblePosition;
    // 最后一个完全可见项的位置
    private int lastItemPosition;
    // 第一个位置
    private int firstItemPosition;
    //用户是否滑动到底的标识
    private volatile boolean isScrollBottom = false;
    //用户是否滑动到第一个的标识
    private volatile boolean isScrollTop = false;

    private PullListener mPullListener;//滑动监听
//    private ValueAnimator refreshAnim;//刷新anim
//    private ValueAnimator loadMoreAnim;//加载更多anim


    public XRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(2);
        addOnScrollListener(new AutoLoadScrollListener());
    }

    //下拉的距离
    private float pullPos = 0;
    //上拉的距离
    private float dragPos = 0;
    //触摸时的位置
    private float touchPos = 0;

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchPos = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchPos - e.getY() < 0) {
                    //下拉
                    if (getAdapter() != null && getLayoutManager() != null) {
                        if (((XRecyclerAdapter) getAdapter()).hadHeader() && firstItemPosition == 0) {
                            //有头部__当前位置是第一位__跟随着滑动记录增大而增大
                            if (pullPos == 0) {
                                pullPos = e.getY();
                            }
                            if (e.getY() - pullPos > 20) {
                                float changeY = (e.getY() - pullPos) / 5;
                                View view = getLayoutManager().findViewByPosition(0);
                                if (view != null) {
                                    ViewGroup.LayoutParams params = view.getLayoutParams();
                                    params.height = (int) changeY;
                                    view.setLayoutParams(params);
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    //上拉 判断是否滑动到了底部
                    if (getAdapter() != null && getLayoutManager() != null) {
                        if (((XRecyclerAdapter) getAdapter()).hadFooter() && lastItemPosition == getAdapter().getItemCount() - 1) {
                            //有尾部
                            if (dragPos == 0) {
                                dragPos = e.getY();
                            }
                            if (dragPos - e.getY() > 20) {
                                float changeY = (dragPos - e.getY()) / 5;
                                View view = getLayoutManager().findViewByPosition(getAdapter().getItemCount() - 1);
                                if (view != null) {
                                    view.setVisibility(VISIBLE);
                                    ViewGroup.LayoutParams params = view.getLayoutParams();
                                    params.height = -(int) changeY;
                                    view.setLayoutParams(params);
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //还原所有东西
                if (getAdapter() != null && getLayoutManager() != null) {
                    if (((XRecyclerAdapter) getAdapter()).hadHeader() && firstItemPosition < 1) {
                        //有头部__位置少于前两位__还原控件大小
                        View view = getLayoutManager().findViewByPosition(0);
                        if (view != null) {
                            view.removeCallbacks(refreshRunnable);
                            view.postDelayed(refreshRunnable, 500);
                        }

                    }
                    if (((XRecyclerAdapter) getAdapter()).hadFooter() && getAdapter().getItemCount() - lastItemPosition < 2) {
                        View view = getLayoutManager().findViewByPosition(getAdapter().getItemCount() - 1);
                        if (view != null) {
                            view.removeCallbacks(loadMoreRunnable);
                            view.postDelayed(loadMoreRunnable, 500);
                        }
                    }
                }
                pullPos = 0;
                dragPos = 0;
                break;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void onViewRemoved(View child) {
        if (child != null) {
            child.removeCallbacks(loadMoreRunnable);
        }
        super.onViewRemoved(child);
    }

    //下拉刷新动画runnable
    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                View view = getLayoutManager().findViewByPosition(0);
                if (view != null) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = 0;
                    view.setLayoutParams(params);
                }
                //回调监听事件__下拉刷新
                if (mPullListener != null) {
                    mPullListener.refresh();
                }
            } catch (Exception e) {
                Log.d(TAG, "refresh runnable e: " + e);
            }
        }
    };

    //加载更多动画runnable
    private Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                View view = getLayoutManager().findViewByPosition(getAdapter().getItemCount() - 1);
                if (view != null) {
                    getAdapter().notifyItemChanged(getAdapter().getItemCount() - 1);
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = 0;
                    view.setLayoutParams(params);
                }
                //回调监听事件__加载更多
                if (mPullListener != null) {
                    mPullListener.loadMore();
                }
            } catch (Exception e) {
                Log.d(TAG, "load more runnable e: " + e);
            }
        }
    };

    //------------------------滑动监听
    private int getMaxPosition(int[] positions) {
        int max = positions[0];
        for (int i = 1; i < positions.length; i++) {
            if (positions[i] > max) {
                max = positions[i];
            }
        }
        return max;
    }

    public class AutoLoadScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            // 找到最后一个完全可见项的位置
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                if (lastVisiblePosition == null) {
                    lastVisiblePosition = new int[manager.getSpanCount()];
                }
                manager.findLastCompletelyVisibleItemPositions(lastVisiblePosition);
                manager.findFirstCompletelyVisibleItemPositions(lastVisiblePosition);
                lastItemPosition = getMaxPosition(lastVisiblePosition);
                firstItemPosition = getMaxPosition(lastVisiblePosition);
            } else if (layoutManager instanceof GridLayoutManager) {
                lastItemPosition = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                firstItemPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                firstItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else {
                throw new RuntimeException("Unsupported LayoutManager.");
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case SCROLL_STATE_IDLE:
                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //通过比对 最后完全可见项位置 和 总条目数，来判断是否滑动到底部
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    if (visibleItemCount > 0 && lastItemPosition >= totalItemCount - 1 && !isScrollBottom) {
                        Log.d(TAG, "滑动到底");
                        isScrollBottom = true;
                    } else {
                        Log.d(TAG, "没有滑动到底");
                        isScrollBottom = false;
                    }
                    if (visibleItemCount > 0 && firstItemPosition == 0 && !isScrollTop) {
                        Log.d(TAG, "滑动到顶");
                        isScrollTop = true;
                    } else {
                        Log.d(TAG, "没有滑动到顶");
                        isScrollTop = false;
                    }
                    break;
                case SCROLL_STATE_DRAGGING:
                    break;
                case SCROLL_STATE_SETTLING:
                    break;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        removePullListener();
        super.onDetachedFromWindow();
    }

    //接口回调-------------------------------------------------------------------------------------
    public void setOnPullListener(PullListener pullListener) {
        this.mPullListener = pullListener;
    }

    public void removePullListener() {
        this.mPullListener = null;
    }

    public interface PullListener {
        void refresh();

        void loadMore();
    }
}
