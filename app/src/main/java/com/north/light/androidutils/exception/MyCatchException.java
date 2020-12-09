package com.north.light.androidutils.exception;


/**
 * 全局错误捕捉器
 * MyCatchException handler = MyCatchException.getInstance();
 * Thread.setDefaultUncaughtExceptionHandler(handler);
 * ps:如果使用bugly,则需要在其之前调用
 */
public class MyCatchException implements Thread.UncaughtExceptionHandler {
    /**
     * MyCatchException实例
     */

    private static MyCatchException instance;

    /**
     * 获取MyCatchException实例 ,单例模式
     */

    public static MyCatchException getInstance() {
        if (instance == null) {
            instance = new MyCatchException();
        }
        return instance;
    }

    /**
     * 异常捕获
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("全局异常捕获--start---");
        //全局异常
        try {
            Thread.sleep(2000);
        } catch (Exception e1) {
            System.out.println("清空用户信息异常: " + e1.getMessage());
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.out.println("全局异常捕获--end---");
    }
}