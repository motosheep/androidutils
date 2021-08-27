package com.north.light.androidutils.ble;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;
import com.north.light.libble.BLEManager;


/**
 * ble activity
 */
public class BLEActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        //点击事件
        findViewById(R.id.isOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(getClass().getSimpleName(), "BLEManager.getBLEObj().isOpenBLE()：" + BLEManager.getBLEObj().isOpenBLE());
            }
        });
        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(getClass().getSimpleName(), "BLEManager.getBLEObj().scanDevice()：" + BLEManager.getBLEObj().scanDevice());
            }
        });
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(getClass().getSimpleName(), "BLEManager.getBLEObj().openBLE()：" + BLEManager.getBLEObj().openBLE());
            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(getClass().getSimpleName(), "BLEManager.getBLEObj().closeBLE()：" + BLEManager.getBLEObj().closeBLE());
            }
        });
    }
}
