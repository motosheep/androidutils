package com.north.light.androidutils.network;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;


/**
 * 网络状态监听测试类
 * */
public class NetStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_status);
        NetWorkStatusUtils.getInstance().init(this);
        NetWorkStatusUtils.getInstance().setOnNetStatusListener(listener);
    }

    @Override
    protected void onDestroy() {
        NetWorkStatusUtils.getInstance().removeNetStatusListener(listener);
        NetWorkStatusUtils.getInstance().release();
        super.onDestroy();
    }

    private NetWorkStatusUtils.NetStatusListener listener = new NetWorkStatusUtils.NetStatusListener() {
        @Override
        public void status(int status) {
            //无网络(返回0)、WF(返回1)、2G(返回2)、3G(返回3)、4G(返回4)、5G(返回5) 网络
            LogUtil.d("status:" + status);
        }
    };
}