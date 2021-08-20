package com.north.light.androidutils.drawer

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import com.north.light.androidutils.R

/**
 * @ClassName: BottomDrawerWidget
 * @Author: lzt
 * @CreateDate: 2021/7/19 15:29
 * @Version: 1.0
 * @Description:底部drawer widget
 */
class BottomDrawerWidget(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
   var mListener: EventListener? = null

    //drawer是否打开
    private var isOpenDrawer = false

    //是否初始化
    private var isInit = false

    //属性动画
    private var mValueAnimation: ValueAnimator? = null

    //drawer layout
    private var mDrawerLayout: FrameLayout? = null

    //root layout
    private var mRootFrameLayout: FrameLayout? = null

    //shadow
    private var mShadow: ImageView? = null

    private var viewTreeListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            init()
        }
    }

    init {
        viewTreeObserver.addOnGlobalLayoutListener(viewTreeListener)
    }

    override fun onDetachedFromWindow() {
        removeEventListener()
        mValueAnimation?.cancel()
        viewTreeObserver.removeOnGlobalLayoutListener(viewTreeListener)
        super.onDetachedFromWindow()
    }


    /**
     * 是否打开drawer
     * */
    fun isOpenDrawer(): Boolean {
        return isOpenDrawer
    }

    /**
     * 打开drawer
     * */
    fun openDrawer() {
        if (isOpenDrawer) {
            return
        }
        var max: Float = (height / 3 * 2).toFloat()
        mValueAnimation?.cancel()
        mValueAnimation = ValueAnimator.ofInt(0, max.toInt())
        mValueAnimation?.setDuration(200)
        mValueAnimation?.interpolator = LinearInterpolator()
        mShadow?.visibility = VISIBLE
        mValueAnimation?.addUpdateListener {
            val value = it.getAnimatedValue()
            val interval: Float = (value as Int) / max
            mShadow?.alpha = interval
            val params = (mDrawerLayout?.layoutParams as RelativeLayout.LayoutParams)
            params.apply {
                height = value as Int
            }
            mDrawerLayout?.layoutParams = params
        }
        mValueAnimation?.start()
        isOpenDrawer = true
    }

    /**
     * 关闭drawer
     * */
    fun closeDrawer() {
        if (!isOpenDrawer) {
            return
        }
        var max: Float = (height / 3 * 2).toFloat()
        mValueAnimation?.cancel()
        mValueAnimation = ValueAnimator.ofInt(max.toInt(), 0)
        mValueAnimation?.setDuration(200)
        mValueAnimation?.interpolator = LinearInterpolator()
        mValueAnimation?.addUpdateListener {
            val value = it.getAnimatedValue()
            val interval: Float = (value as Int) / max
            mShadow?.alpha = interval
            val params = (mDrawerLayout?.layoutParams as RelativeLayout.LayoutParams)
            params.apply {
                height = value as Int
            }
            mDrawerLayout?.layoutParams = params
        }
        mValueAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mShadow?.visibility = GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        mValueAnimation?.start()
        isOpenDrawer = false
    }


    /**
     * 初始化
     */
    private fun init() {
        if (isInit) {
            return
        }
        isInit = true
        mRootFrameLayout = FrameLayout(context)
        mRootFrameLayout?.id = hashCode() + 1
        addView(mRootFrameLayout)
        mRootFrameLayout?.layoutParams?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        mShadow = ImageView(context)
        addView(mShadow)
        mShadow?.layoutParams?.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        mShadow?.setBackgroundResource(R.color.color_4d000000)
        mShadow?.alpha = 0f
        mShadow?.visibility = GONE
        mShadow?.setOnClickListener {
            closeDrawer()
        }
        mDrawerLayout = FrameLayout(context)
        mDrawerLayout?.setOnClickListener {
            //do nothing
        }
        mDrawerLayout?.id = hashCode() + 2
        addView(mDrawerLayout)
        (mDrawerLayout?.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        (mDrawerLayout?.layoutParams as RelativeLayout.LayoutParams).apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = 0
        }
        mListener?.initFinish(
                mRootFrameLayout!!,
                mRootFrameLayout?.id ?: 0,
                mDrawerLayout!!,
                mDrawerLayout?.id ?: 0
        )
    }

    fun setOnEventListener(listener: EventListener) {
        this.mListener = listener
    }

    fun removeEventListener() {
        this.mListener = null
    }


    interface EventListener {
        fun initFinish(
                rootFrameLayout: FrameLayout,
                rootId: Int,
                drawerFragmentLayout: FrameLayout,
                drawerId: Int
        )
    }
}