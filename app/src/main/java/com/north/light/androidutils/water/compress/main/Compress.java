package com.north.light.androidutils.water.compress.main;

import android.content.Context;
import android.text.TextUtils;

import com.north.light.androidutils.water.compress.function.ImgCompress;
import com.north.light.androidutils.water.compress.function.ImgConstant;
import com.north.light.androidutils.water.compress.function.PicStreamAdapter;
import com.north.light.androidutils.water.compress.function.PicStreamProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lzt
 * @Date: 2022/1/28 8:52
 * @Description:
 */
public class Compress {

    public static class Builder implements Serializable {
        //输入流
        private List<PicStreamProvider> streamProviderList = new ArrayList();
        //Context
        private Context context;

        //设置数据----------------------------------------------------------------------------------
        public Builder load(File localFile, int width, int height, int rate, String outputPath) {
            streamProviderList.add(new PicStreamAdapter() {
                @Override
                public InputStream getInput() throws Exception {
                    return new FileInputStream(localFile);
                }

                @Override
                public int width() {
                    return width;
                }

                @Override
                public int height() {
                    return height;
                }

                @Override
                public int qualityRate() {
                    return rate;
                }

                @Override
                public String outputPath() {
                    return outputPath;
                }

            });
            return this;
        }

        public Builder load(PicStreamProvider provider) {
            streamProviderList.add(provider);
            return this;
        }

        public Builder load(String path) {
            if (TextUtils.isEmpty(path)) {
                return this;
            }
            return load(new File(path), 0, 0, ImgConstant.IMG_COMPRESS_QUALITY_RATE, null);
        }

        public Builder load(List<File> fileList) {
            if (fileList == null || fileList.size() == 0) {
                return this;
            }
            for (File file : fileList) {
                load(file, 0, 0, ImgConstant.IMG_COMPRESS_QUALITY_RATE, null);
            }
            return this;
        }

        //设置数据----------------------------------------------------------------------------------

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public List<String> launch() {
            //先检查后赋值
            if (context == null || streamProviderList == null || streamProviderList.size() == 0) {
                return new ArrayList<>();
            }
            //开始压缩并且返回本地路径
            try {
                return ImgCompress.getInstance().compress(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        public List<PicStreamProvider> getStreamProviderList() {
            return streamProviderList;
        }

        public Context getContext() {
            return context;
        }
    }
}
