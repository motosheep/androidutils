package com.north.light.androidutils.coordinatorLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: lzt
 * @CreateDate: 2021/7/30 11:32
 * @Version: 1.0
 * @Description:coordinatorlayout behavior
 */
public class CoorBehavior extends CoordinatorLayout.Behavior<View> {
    // 列表顶部和title底部重合时，列表的滑动距离。
    private float deltaY;

    public CoorBehavior() {
    }

    public CoorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull @NotNull CoordinatorLayout parent,
                                   @NonNull @NotNull View child, @NonNull @NotNull View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull @NotNull CoordinatorLayout parent, @NonNull @NotNull View child, @NonNull @NotNull View dependency) {
        if (deltaY == 0) {
            deltaY = dependency.getY() - child.getHeight();
        }
        Log.d("coor behavior", "be first:deltaY:" + deltaY);
        Log.d("coor behavior", "be first:getY:" + dependency.getY());
        Log.d("coor behavior", "be first:getHeight:" + child.getHeight());
        float dy = dependency.getY() - child.getHeight();
        dy = dy < 0 ? 0 : dy;
        float y = -(dy / deltaY) * child.getHeight();
        float alpha = ((deltaY - dy) / deltaY);
        //透明度
        child.setAlpha(alpha);
        //Y轴偏移
        child.setTranslationY(y);
        return true;
    }
}
