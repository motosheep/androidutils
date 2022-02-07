package com.north.light.androidutils.water

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.north.light.androidutils.R
import com.north.light.androidutils.water.compress.Compress

/**
 * 水印activity
 * */
class WaterActivity : AppCompatActivity() {
    private val TAG = WaterActivity::class.java.simpleName

    //模板imageview
    private lateinit var mTargetImg: ImageView
    private lateinit var mRg: RadioGroup
    private lateinit var mRbTL: RadioButton
    private lateinit var mRbTR: RadioButton
    private lateinit var mRbBL: RadioButton
    private lateinit var mRbBR: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)
        mTargetImg = findViewById(R.id.activity_water_target)
        mRg = findViewById(R.id.activity_water_rg)
        mRbTL = findViewById(R.id.activity_water_topleft)
        mRbTR = findViewById(R.id.activity_water_topright)
        mRbBL = findViewById(R.id.activity_water_btleft)
        mRbBR = findViewById(R.id.activity_water_btRight)

        //处理
        val path = Environment.getExternalStorageDirectory().path + "/001.jpg"
        val result = Compress.Builder().with(this).load(path).launch()
        Log.d(TAG, "压缩路径:$result")
//        val path = ImgCompress.getInstance()
//            .compress(this, , 0, 0)
//        val path = ImgUtils.getInstance()
//            .saveBitmap(this, bitmap, Environment.getExternalStorageDirectory().path+"/aaa11-2.jpg")
        //event------------------------------------------------------------
        mRg.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.activity_water_topleft -> {
                        //左上角水印
                    }
                    R.id.activity_water_topright -> {
                        //右上角水印
                    }
                    R.id.activity_water_btleft -> {
                        //左下角水印
                    }
                    R.id.activity_water_btRight -> {
                        //右下角水印
                    }
                }

            }
        })
    }


}