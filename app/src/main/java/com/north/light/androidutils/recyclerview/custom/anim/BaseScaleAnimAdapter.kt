package com.north.light.androidutils.recyclerview.custom.anim

import android.content.Context
import android.view.animation.LinearInterpolator

/**
 * Created by lzt
 * time 2020/6/8
 * 描述：动画adapter
 */
abstract class BaseScaleAnimAdapter<W : Any, T : androidx.recyclerview.widget.RecyclerView.ViewHolder>(context: Context) : BaseSimpleAdapter<W, T>(context) {
    var scaleAnim = ScaleInAnimation()

    private fun addAnimation(holder: T) {
        for (anim in scaleAnim.getAnimators(holder.itemView)) {
            anim.setDuration(300).start()
            anim.interpolator = LinearInterpolator()
        }
    }

    override fun onViewAttachedToWindow(holder: T) {
        super.onViewAttachedToWindow(holder)
        addAnimation(holder)
    }
}