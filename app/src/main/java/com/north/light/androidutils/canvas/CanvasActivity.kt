package com.north.light.androidutils.canvas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.north.light.androidutils.R
import com.north.light.androidutils.canvas.bean.CircleInfo
import com.north.light.androidutils.canvas.bean.LinearInfo
import com.north.light.androidutils.canvas.builder.CircleBuilder
import com.north.light.androidutils.canvas.builder.LinearBuilder
import com.north.light.androidutils.canvas.view.CircleChart
import com.north.light.androidutils.canvas.view.LinearChart


/**
 * 图表绘制activity
 * */
class CanvasActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
        //饼状图
        val circleChart = findViewById<CircleChart>(R.id.activity_canvas_circle)
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
        //直方图
        val linearChart = findViewById<LinearChart>(R.id.activity_canvas_linear)
        val linearData = mutableListOf<LinearInfo>(LinearInfo().apply {
            this.color = R.color.colorPrimary
            this.yData =90f
        },LinearInfo().apply {
            this.color = R.color.blue_light
            this.yData = 9f
        },LinearInfo().apply {
            this.color = R.color.color_F06091
            this.yData = 19f
        },LinearInfo().apply {
            this.color = R.color.color_F06091
            this.yData = 29f
        },LinearInfo().apply {
            this.color = R.color.blue_light
            this.yData = 9f
        },LinearInfo().apply {
            this.color = R.color.blue_light
            this.yData = 9f
        },LinearInfo().apply {
            this.color = R.color.blue_light
            this.yData = 9f
        })
        linearChart.setData(LinearBuilder().apply {
            this.dataList = linearData
            this.dataList = linearData
        })
    }
}