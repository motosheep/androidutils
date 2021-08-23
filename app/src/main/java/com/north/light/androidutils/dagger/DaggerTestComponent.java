package com.north.light.androidutils.dagger;

import dagger.Component;

/**
 * author:li
 * date:2021/8/23
 * desc:
 */
@Component(modules = {DaggerModel.class})
public interface DaggerTestComponent {

    void inject(DaggerActivity activity);
}
