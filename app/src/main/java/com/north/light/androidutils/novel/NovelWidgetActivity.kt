package com.north.light.androidutils.novel

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.north.light.androidutils.R
import com.north.light.androidutils.log.LogUtil
import com.north.light.androidutils.novel.text.tv.FitAutoTextListener
import com.north.light.androidutils.novel.text.tv.FitAutoTextView


/**
 * 小说控件
 * */
class NovelWidgetActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_widget)
        var widget = findViewById<FitAutoTextView>(R.id.activity_novel_widget_txt)
        widget.setOnTextListener(object : FitAutoTextListener {
            override fun maxDrawCount(count: Int) {
                LogUtil.d("maxDrawCount:${count}")
            }

            override fun trueDrawCount(count: Int) {
                LogUtil.d("trueDrawCount:${count}")
            }
        })
        widget.setTextView("你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi你好，好\nhello hi")
        val fontSize = findViewById<SeekBar>(R.id.novel_size)
        val colSize = findViewById<SeekBar>(R.id.novel_col_size)
        val rowSize = findViewById<SeekBar>(R.id.novel_row_size)
        fontSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                widget.setTextSize(seekBar.progress)
            }
        })
        colSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                widget.setWidthInterval(seekBar.progress)
            }
        })
        rowSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                widget.setHeightInterval(seekBar.progress)
            }
        })
    }
}