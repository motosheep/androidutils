package com.north.light.androidutils.dagger;

import dagger.Module;
import dagger.Provides;

/**
 * author:li
 * date:2021/8/23
 * desc:
 */
@Module
public class DaggerModel {

    @Provides
    DaggerBean getDaggerBean() {
        return new DaggerBean();
    }
}
