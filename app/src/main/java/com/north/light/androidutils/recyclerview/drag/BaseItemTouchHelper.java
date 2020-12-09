package com.north.light.androidutils.recyclerview.drag;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Created by lzt
 * time 2020/5/14
 * 描述：
 */
public class BaseItemTouchHelper extends ItemTouchHelper {
    private Callback callback;

    public BaseItemTouchHelper(@NonNull Callback callback) {
        super(callback);
        this.callback = callback;
    }
    public Callback getCallback(){
        return this.callback;//返回了CallBack
    }

}
