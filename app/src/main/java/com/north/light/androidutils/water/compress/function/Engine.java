package com.north.light.androidutils.water.compress.function;

import java.io.Serializable;

/**
 * @Author: lzt
 * @Date: 2022/1/28 8:43
 * @Description:图片压缩引擎
 */
public class Engine implements Serializable {


    private static class SingleHolder implements Serializable {
        static Engine mInstance = new Engine();
    }

    public static Engine getInstance() {
        return SingleHolder.mInstance;
    }


}
