package com.north.light.androidutils;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.aspectj.AspectJManager;
import com.north.light.androidutils.imageview.RotateImageView;
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
    }

    public void initView() {
        final RotateImageView like = findViewById(R.id.activity_main_rotate);
//       like.startAnim();


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
