package com.north.light.androidutils;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.aspectj.AspectJManager;
import com.north.light.androidutils.mulchannel.ChannelUtils;
import com.north.light.libumeng.UMManager;
import com.umeng.commonsdk.UMConfigure;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.annotation.Intercept;

import static com.north.light.androidutils.aspectj.AspectJConstant.INTERCEPT_LOGIN;

public class MainActivity extends AppCompatActivity {
    int type = 1;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        AspectJManager.getInstance();
        UMManager.getInstance().init(this, "61011d8026e9627944aac7c7",
                "UMENG_CHANNEL", UMConfigure.DEVICE_TYPE_PHONE, null, 1);
        Log.d("MianActivity", "channel:" + ChannelUtils.getChannel(this));
    }

    public void initView() {
        //X AOP
        XAOP.init(this.getApplication()); //初始化插件
        XAOP.debug(true); //日志打印切片开启
        XAOP.setPriority(Log.INFO); //设置日志打印的等级,默认为0
    }

    @Intercept(INTERCEPT_LOGIN)
    public void test(View view) {
        System.out.println("@test");
    }
}
