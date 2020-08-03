package com.north.light.androidutils.viewpager;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
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
    private Handler mRefreshHandler = new Handler();
    //view list
    private List<ImageView> mPicList = new ArrayList<>();
    //view pager
    private ViewPager mViewPager;

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
    }

    /**
     * 设置结果监听
     */
    public interface LoadImageListener {
        void LoadImage(String pic, ImageView url);
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
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRefreshHandler == null) {
            mRefreshHandler = new Handler();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRefreshHandler != null) {
            mRefreshHandler.removeCallbacksAndMessages(null);
            mRefreshHandler = null;
        }
    }

}
