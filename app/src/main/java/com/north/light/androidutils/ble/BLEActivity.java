package com.north.light.androidutils.ble;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;
import com.north.light.libble.BLEManager;
import com.north.light.libble.bean.BLEInfo;
import com.north.light.libble.listener.BLEScanResultListener;

import java.util.ArrayList;
import java.util.List;


/**
 * ble activity
 */
public class BLEActivity extends AppCompatActivity {
    //蓝牙设备集合
    private List<BLEInfo> infoList = new ArrayList<>();
    private String UUID = "00001115ab-0000-1000-8000-00805f9B34FB";
    private BLEStringRecyclerView ble;

    private BLEScanResultListener scanResultListener = new BLEScanResultListener() {
        @Override
        public void result(List<BLEInfo> result) {
            List<String> r = new ArrayList<>();
            for (BLEInfo b : result) {
                r.add(b.getDevice().getAddress());
            }
            ble.initData(r);
            infoList.addAll(result);
        }

        @Override
        public void error(String tips) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
         ble = findViewById(R.id.activity_ble_content);
        //设置服务端--外部连接监听
        BLEManager.getBLEObj().receive(UUID);
        BLEManager.getBLEObj().setOnResultListener(scanResultListener);
        ble.setOnClickEvent(new BLEStringRecyclerView.OnClickEvent() {
            @Override
            public void click(String data) {
                //连接蓝牙
                BLEInfo obj = null;
                for (BLEInfo info : infoList) {
                    if (data.equals(info.getDevice().getAddress())) {
                        obj = info;
                        break;
                    }
                }
                BLEManager.getBLEObj().connect(obj, UUID);
            }
        });
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
                infoList.clear();
                ble.resetData();
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
        findViewById(R.id.stopScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(getClass().getSimpleName(), "BLEManager.getBLEObj().stopScan()：" + BLEManager.getBLEObj().stopScan());
            }
        });
        EditText input = findViewById(R.id.txt);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                BLEManager.getBLEObj().sendData(text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        BLEManager.getBLEObj().removeResultListener(scanResultListener);
        BLEManager.getBLEObj().releaseAll();
        super.onDestroy();
    }
}
