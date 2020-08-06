package com.north.light.androidutils.viewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lzt
 * time 2020/8/3
 * 描述：banner viewpager
 */
public class BannerViewPager extends RelativeLayout {
    private static final String TAG = BannerViewPager.class.getSimpleName();
    //自动更新的looper
    private Handler mRefreshHandler;
    //view list
    private List<ImageView> mPicList = new ArrayList<>();
    //view pager
    private ViewPager mViewPager;
    //自动轮播标识
    private volatile boolean mAotoRun = true;
    private final int TAG_RUN = 0x0001;
    private int mAutoRunDelay = 2000;//滚动的时间间隔(毫秒)

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
        RelativeLayout.LayoutParams params = (LayoutParams) mViewPager.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(params);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                switchRun(false);
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switchRun(true);
            }
        });
        //handler初始化
        mRefreshHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case TAG_RUN:
                        //启动
                        if (mAotoRun) {
                            //viewpager自动滚动
                            viewpagerScroll(true);
                            switchRun(true);
                        } else {
                            //停止
                            switchRun(false);
                        }
                        break;
                }
            }
        };
    }

    /**
     * viewpager滚动
     */
    private void viewpagerScroll(boolean tag) {
        if (mViewPager == null) return;
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
            mRefreshHandler.sendEmptyMessageDelayed(TAG_RUN, 10);
        }
    }

    /**
     * 暂停or启动自动轮播
     */
    public void switchRun(boolean run) {
        this.mAotoRun = run;
        if (mRefreshHandler != null) {
            if (mAotoRun) {
                mRefreshHandler.sendEmptyMessageDelayed(TAG_RUN, mAutoRunDelay);
            } else {
                mRefreshHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    /**
     * 设置结果监听
     */
    public interface LoadImageListener {
        void LoadImage(String url, ImageView pic);
    }

    /**
     * 设置图片，传入String url,返回给外部一个view,一个
     */
    public void setImageView(final List<String> picList, final LoadImageListener loadImageListener) {
        mPicList.clear();
        for (String string : picList) {
            mPicList.add(new ImageView(getContext()));
        }
        if (mViewPager != null) {
            mViewPager.setAdapter(null);
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
                            loadImageListener.LoadImage(picList.get(position), mPicList.get(position));
                        }
                        return mPicList.get(position);
                    } catch (Exception e) {
                        Log.d(TAG, "view pager instantiateItem e: " + e.getMessage());
                    }
                    return new ImageView(getContext());
                }
            });
            switchRun(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRefreshHandler != null) {
            mRefreshHandler.removeCallbacksAndMessages(null);
            mRefreshHandler = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRefreshHandler == null) {
            mRefreshHandler = new Handler();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        switchRun(hasWindowFocus);
    }
}
