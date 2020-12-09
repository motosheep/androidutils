package com.north.light.androidutils.viewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.north.light.androidutils.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lzt
 * time 2020/8/3
 * 描述：banner viewpager
 */
public class BannerViewPager extends RelativeLayout  {
    private static final String TAG = BannerViewPager.class.getSimpleName();
    //自动更新的looper
    private Handler mRefreshHandler;
    //view list
    private List<ImageView> mPicList = new ArrayList<>();
    //view pager
    private ViewPager mViewPager;
    private final int TAG_RUN = 0x0001;
    private int mAutoRunDelay = 5000;//滚动的时间间隔(毫秒)
    //指示器
    private LinearLayout mIndicateLayout;//指示器父布局
    private List<ImageView> mIndicateImgList = new ArrayList<>();//指示器ImageView List
    //监听事件
    private onClickListener mListener;
    //是否触摸的标识
    private boolean isTouch = false;
    private boolean isResetData = false;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //初始化插入viewPager布局
        mViewPager = new ViewPager(getContext());
        addView(mViewPager);
        LayoutParams params = (LayoutParams) mViewPager.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(params);
        //初始化指示器父布局
        mIndicateLayout = new LinearLayout(getContext());
        addView(mIndicateLayout);
        LayoutParams params2 = (LayoutParams) mIndicateLayout.getLayoutParams();
        params2.width = LayoutParams.MATCH_PARENT;
        params2.height = LayoutParams.WRAP_CONTENT;
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mIndicateLayout.setLayoutParams(params2);
        mIndicateLayout.setOrientation(LinearLayout.HORIZONTAL);
        mIndicateLayout.setGravity(Gravity.CENTER);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (!isResetData) {
                    isTouch = true;
                } else {
                    isResetData = false;
                }
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                isTouch = false;
                updateIndicate(mViewPager.getCurrentItem());
            }
        });
        //handler初始化
        mRefreshHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case TAG_RUN:
                        //viewpager自动滚动
                        if (!isTouch) {
                            viewpagerScroll(true);
                        }
                        start();
                        break;
                }
            }
        };
    }


    /**
     * 是否显示指示器
     */
    public void showIndicate(boolean isShow) {
        try {
            if (isShow) {
                mIndicateLayout.setVisibility(View.VISIBLE);
            } else {
                mIndicateLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d(TAG, "updateIndicate: " + e);
        }
    }

    /**
     * viewpager滚动
     */
    private void viewpagerScroll(boolean tag) {
        if (mViewPager == null) {
            return;
        }
        try {
            if (tag) {
                int curPos = mViewPager.getCurrentItem();
                int tolPos = mPicList.size();
                if (curPos < tolPos - 1) {
                    //继续向右滑动
                    mViewPager.setCurrentItem(++curPos, true);
                } else {
                    //回到第一个
                    mViewPager.setCurrentItem(0);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "viewpagerScroll: " + e);
        }
    }

    /**
     * 设置滚动的时间间隔
     */
    public void setRunDelay(int time) {
        this.mAutoRunDelay = time;
        if (mRefreshHandler != null) {
            mRefreshHandler.removeCallbacksAndMessages(null);
            mRefreshHandler.sendEmptyMessageDelayed(TAG_RUN, 10);
        }
    }

    /**
     * 暂停or启动自动轮播
     */
    public void start() {
        if (mRefreshHandler != null) {
            mRefreshHandler.removeCallbacksAndMessages(null);
            mRefreshHandler.sendEmptyMessageDelayed(TAG_RUN, mAutoRunDelay);
        }
    }

    /**
     * 设置结果监听
     */
    public interface LoadImageListener {
        void loadImage(String url, ImageView pic);
    }

    /**
     * 设置图片，传入String url,返回给外部一个view,一个
     */
    public void setImageView(final List<String> picList, final LoadImageListener loadImageListener) {
        isResetData = true;
        mPicList.clear();
        for (int i = 0; i < picList.size(); i++) {
            ImageView view = new ImageView(getContext());
            mPicList.add(view);
            final int finalI = i;
            final int finalI1 = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.click(finalI, picList.get(finalI1), picList);
                    }
                }
            });
        }
        if (mViewPager != null) {
            mViewPager.removeAllViews();
            mViewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return picList.size();
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                    return view == o;
                }

                @Override
                public void destroyItem(ViewGroup container, int position,
                                        Object object) {
                    try {
                        container.removeView(mPicList.get(position));
                    } catch (Exception e) {
                        Log.d(TAG, "view pager destroyItem e: " + e.getMessage());
                    }
                }

                @Override
                public int getItemPosition(Object object) {
                    return super.getItemPosition(object);
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return "title";
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    try {
                        container.addView(mPicList.get(position));
                        if (loadImageListener != null) {
                            loadImageListener.loadImage(picList.get(position), mPicList.get(position));
                        }
                        return mPicList.get(position);
                    } catch (Exception e) {
                        Log.d(TAG, "view pager instantiateItem e: " + e.getMessage());
                    }
                    return new ImageView(getContext());
                }
            });
            setIndicate(mPicList.size());
            start();
        }
    }



    /**
     * 指示器的圆点创建
     */
    private void setIndicate(int size) {
        if (mIndicateLayout != null) {
            //插入指示器布局
            mIndicateLayout.removeAllViews();
            mIndicateImgList.clear();
            for (int i = 0; i < size; i++) {
                ImageView point = new ImageView(mIndicateLayout.getContext());
                mIndicateLayout.addView(point);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) point.getLayoutParams();
                params.width = 12;
                params.height = 12;
                params.leftMargin = 2;
                params.rightMargin = 2;
                params.bottomMargin = 20;
                params.topMargin = 20;
                point.setLayoutParams(params);
                if (i == 0) {
                    point.setImageResource(R.drawable.shape_banner_sel_banner);
                } else {
                    point.setImageResource(R.drawable.shape_banner_unsel_banner);
                }
                mIndicateImgList.add(point);
            }
        }
    }

    /**
     * 设置指示器的颜色
     */
    private void updateIndicate(int pos) {
        if (pos > mIndicateImgList.size() - 1) {
            return;
        }
        try {
            for (int i = 0; i < mIndicateImgList.size(); i++) {
                if (i == pos) {
                    mIndicateImgList.get(i).setImageResource(R.drawable.shape_banner_sel_banner);
                } else {
                    mIndicateImgList.get(i).setImageResource(R.drawable.shape_banner_unsel_banner);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "updateIndicate: " + e);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRefreshHandler != null) {
            mRefreshHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    //监听事件------------------------------------------------
    public void setOnClickListener(onClickListener listener) {
        this.mListener = listener;
    }

    public void removeOnClickListener() {
        this.mListener = null;
    }

    public interface onClickListener {
        void click(int pos, String url, List<String> urlList);
    }
}
