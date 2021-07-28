package com.north.light.androidutils.pic;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzt
 * time 2020/5/29
 * 描述：竖直方向图片控件
 */
public class VerticalMulPicView extends CardView {
    private static final String TAG = VerticalMulPicView.class.getName();
    //装载的图片集合
    private List<ImageView> mImageList = new ArrayList<>();
    //根布局linear layout
    private LinearLayout mRootLinearLayout;

    public VerticalMulPicView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalMulPicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalMulPicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //先载入一个linear layout
        mRootLinearLayout = new LinearLayout(getContext());
        addView(mRootLinearLayout);
        CardView.LayoutParams linearParams = (FrameLayout.LayoutParams) mRootLinearLayout.getLayoutParams();
        linearParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        linearParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        mRootLinearLayout.setLayoutParams(linearParams);
        mRootLinearLayout.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 设置图片
     */
    public void setPicWithBitmap(List<Bitmap> picList) {
        if (mRootLinearLayout != null) {
            for (ImageView img : mImageList) {
                recyBitmap(img);
            }
            mImageList.clear();
            mRootLinearLayout.removeAllViews();
            ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            double mRootHeight = 0;
            if (picList == null || picList.size() == 0) {
                //设置控件高度
                layoutParams.height = (int) mRootHeight;
                setLayoutParams(layoutParams);
                return;
            }
            //根据图片个数，确定圆角的情况，图片一张，没有圆角，图片两张以上首尾圆角
            if (picList.size() >= 2) {
                this.setRadius(50);
            } else {
                this.setRadius(0);
            }
            //计算图片个数，并动态添加bitmap到ImageView中，ImageView个数也动态添加
            for (int i = 0; i < picList.size(); i++) {
                ImageView img = new ImageView(getContext());
                mRootLinearLayout.addView(img);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                //动态设置img的宽高
                Bitmap cacheBitmap = picList.get(i);
                double cacheWidth = cacheBitmap.getWidth() / 2;
                double cacheHeight = cacheBitmap.getHeight() / 2;
                //情况1，图片宽度比控件宽度小__增加height
                double realHeight;
                double viewWidth = getWidth();
                if (cacheWidth <= viewWidth) {
                    realHeight = (viewWidth / cacheWidth) * cacheHeight;
                } else {
                    realHeight = (viewWidth / cacheWidth) * cacheHeight;
                }
                LinearLayout.LayoutParams imgParams = (LinearLayout.LayoutParams) img.getLayoutParams();
                imgParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                imgParams.height = (int) realHeight;
                img.setLayoutParams(imgParams);
                //设置bitmap到图片中
                img.setImageBitmap(cacheBitmap);
                mImageList.add(img);
                //修改父控件高度
                mRootHeight = mRootHeight + realHeight;
            }
            //设置控件高度
            layoutParams.height = (int) mRootHeight;
            setLayoutParams(layoutParams);
            CardView.LayoutParams linearParams = (FrameLayout.LayoutParams) mRootLinearLayout.getLayoutParams();
            linearParams.height = (int) mRootHeight;
            mRootLinearLayout.setLayoutParams(linearParams);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        //回收bitmap
        for (ImageView img : mImageList) {
            recyBitmap(img);
        }
        mImageList.clear();
        super.onDetachedFromWindow();
    }

    private void recyBitmap(ImageView iv) {
        try {
            iv.setImageDrawable(null);
//            if (iv != null && iv.getDrawable() != null) {
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
//                if (bitmapDrawable != null) {
//                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    if (bitmap != null) {
//                        bitmap.recycle();
//                    }
//
//                }
//            }
        } catch (Exception e) {
            Log.d(TAG, "强制回收出错");
        }
    }
}
