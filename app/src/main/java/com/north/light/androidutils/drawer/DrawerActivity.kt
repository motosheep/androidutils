package com.north.light.androidutils.drawer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.north.light.androidutils.R
import kotlinx.android.synthetic.main.activity_drawer.*


/**
 * drawer activity
 * */
class DrawerActivity : AppCompatActivity() {
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        activity_drawer_bottom_drawer.setOnEventListener(object : BottomDrawerWidget.EventListener {
            override fun initFinish(rootFrameLayout: FrameLayout, rootId: Int, drawerFragmentLayout: FrameLayout, drawerId: Int) {
                drawerFragmentLayout.setBackgroundColor(resources.getColor(R.color.colorAccent))
            }
        })
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(Runnable {
            activity_drawer_bottom_drawer.openDrawer()
        }, 2000)
        handler?.postDelayed(Runnable {
            activity_drawer_bottom_drawer.closeDrawer()
        }, 4000)
    }
}
