package com.north.light.androidutils.location

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.north.light.androidutils.R
import com.north.light.androidutils.location.function.LocationStatusCallBack
import com.north.light.androidutils.location.main.LocManager

class LocationActivity : AppCompatActivity() {
    val TAG = LocationActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        LocManager.getInstance().location.start()
        LocManager.getInstance().location.setLocListener(mCallBack)
    }

    private var mCallBack = object : LocationStatusCallBack {
        override fun NoPermission() {
            Log.d(TAG, "NoPermission")
        }

        override fun GPSClose() {
            Log.d(TAG, "GPSClose")
        }

        override fun NoInit() {
            Log.d(TAG, "NoInit")
        }

        override fun Pos(latitude: Double, longitude: Double) {
            Log.d(TAG, "Pos:${latitude}\t${longitude}")
            runOnUiThread(Runnable {
                findViewById<TextView>(R.id.activity_location_content).append("Pos:${latitude}\t${longitude}\n")
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocManager.getInstance().location.removeLocListener(mCallBack)
        LocManager.getInstance().location.release()
    }
}