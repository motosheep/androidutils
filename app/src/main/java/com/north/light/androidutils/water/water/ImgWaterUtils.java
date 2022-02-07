package com.north.light.androidutils.water.water;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.north.light.androidutils.water.water.function.ImgWaterEngine;
import com.north.light.androidutils.water.water.function.ImgWaterStreamProvider;
import com.north.light.androidutils.water.water.params.ImgWaterOrg;
import com.north.light.androidutils.water.water.params.ImgWaterSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/1/27 8:51
 * @Description:图片水印工具类
 */
public class ImgWaterUtils implements Serializable {

    /**
     * builder
     * 构建数据
     */
    public static class Builder implements Serializable {
        private Context context;
        private List<ImgWaterStreamProvider> streamProvider = new ArrayList<>();
        //引擎
        private ImgWaterEngine waterEngine;

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public List<ImgWaterStreamProvider> getStreamProvider() {
            return streamProvider;
        }

        public Context getContext() {
            return context;
        }

        //数据源采集-start----------------------------------------------------------------------------
        public Builder source(List<ImgWaterStreamProvider> streamProvider) {
            this.streamProvider.addAll(streamProvider);
            return this;
        }

        public <T> Builder source(String source, T tar, ImgWaterOrg imgWaterOrg) {
            Bitmap sourceBitmap = BitmapFactory.decodeFile(source);
            return source(sourceBitmap, tar, new ImgWaterSize(), imgWaterOrg);
        }

        public <T> Builder source(Bitmap source, T tar, ImgWaterSize sizeParams,
                                  ImgWaterOrg org) {
            Bitmap targetBitmap = targetPath(tar);
            ImgWaterStreamProvider imgWaterStreamProvider = new ImgWaterStreamProvider() {
                @Override
                public Bitmap targetBitmap() {
                    return targetBitmap;
                }

                @Override
                public Bitmap inputBitmap() {
                    return source;
                }

                @Override
                public ImgWaterOrg org() {
                    return org;
                }

                @Override
                public ImgWaterSize size() {

                    return sizeParams;
                }

                @Override
                public String outputPath() {
                    return null;
                }
            };
            List<ImgWaterStreamProvider> streamProviderList = new ArrayList<>();
            streamProviderList.add(imgWaterStreamProvider);
            return source(streamProviderList);
        }

        private <T> Bitmap targetPath(T t) {
            if (t instanceof Integer) {
                Integer targetInt = (Integer) t;
                return BitmapFactory.decodeResource(
                        this.context.getApplicationContext().getResources(),
                        targetInt);
            } else if (t instanceof String) {
                String targetPath = (String) t;
                return BitmapFactory.decodeFile(targetPath);
            } else {
                return null;
            }
        }

        //数据源采集-end----------------------------------------------------------------------------


        //启动水印-end----------------------------------------------------------------------------
        public List<String> launch() throws Exception {
            //先检查条件是否符合
            if (this.context == null || this.streamProvider == null || this.streamProvider.size() == 0) {
                return new ArrayList<>();
            }
            waterEngine = new ImgWaterEngine();
            return waterEngine.start(this);
        }

        //停止水印
        public void close() {
            if (this.context == null || this.streamProvider == null || this.streamProvider.size() == 0) {
                return;
            }
            if (this.waterEngine == null) {
                return;
            }
            waterEngine.close();
        }

    }

}
