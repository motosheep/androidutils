package com.north.light.androidutils.aspectj;

import android.util.Log;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.checker.Interceptor;

import org.aspectj.lang.JoinPoint;

/**
 * Created by lzt
 * time 2021/6/18 15:35
 *
 * @author lizhengting
 * 描述：
 */
public class AspectJManager {

    private static final class SingleHolder {
        static AspectJManager mInstance = new AspectJManager();
    }

    public static AspectJManager getInstance() {
        return SingleHolder.mInstance;
    }


    public AspectJManager() {
        XAOP.setInterceptor(new Interceptor() {
            @Override
            public boolean intercept(int type, JoinPoint joinPoint) throws Throwable {
                switch (type) {
                    case AspectJConstant.INTERCEPT_LOGIN:
                        System.out.println("@INTERCEPT_LOGIN");
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}
