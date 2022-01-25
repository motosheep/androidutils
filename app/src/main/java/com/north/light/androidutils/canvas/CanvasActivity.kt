package com.north.light.androidutils.canvas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.north.light.androidutils.R
import com.north.light.androidutils.canvas.bean.CircleInfo
import com.north.light.androidutils.canvas.builder.CircleBuilder


/**
 * 图表绘制activity
 * */
class CanvasActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
        val circleChart = findViewById<CircleChart>(R.id.activity_canvas_linear)
        val circleData = mutableListOf<CircleInfo>(CircleInfo().apply {
            this.color = R.color.colorPrimary
            this.percent = 90f
        },CircleInfo().apply {
            this.color = R.color.blue_light
            this.percent = 9f
        })
        circleChart.setData(CircleBuilder().apply {
            this.dataList = circleData
            this.bottomColor = R.color.colorAccent
        })
    }
}