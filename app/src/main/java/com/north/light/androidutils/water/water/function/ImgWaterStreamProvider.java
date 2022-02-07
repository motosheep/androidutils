package com.north.light.androidutils.water.water.function;

import android.graphics.Bitmap;

import com.north.light.androidutils.water.water.params.ImgWaterOrg;
import com.north.light.androidutils.water.water.params.ImgWaterSize;

/**
 * @Author: lzt
 * @Date: 2022/1/28 9:10
 * @Description:图片流provider--输入时转换
 */
public interface ImgWaterStreamProvider {
    Bitmap targetBitmap();

    Bitmap inputBitmap();

    ImgWaterOrg org();

    ImgWaterSize size();

    String outputPath();
}
