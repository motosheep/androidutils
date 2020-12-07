package com.north.light.androidutils.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.Serializable;

/**
 * Created by lzt
 * time 2020/8/27
 * 描述：时间广播接收者
 */
public class TimeBroadcastUtils implements Serializable {
    private Context mContext;

    private static final class SingleHolder {
        static TimeBroadcastUtils mInstance = new TimeBroadcastUtils();
    }

    public static TimeBroadcastUtils getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 注册广播
     */
    public void init(Context context) {
        if (context == null) {
            return;
        }
        this.mContext = context.getApplicationContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        mContext.registerReceiver(broadcastReceiver, filter);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                //检测

            } else if (intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {

            }
        }
    };
}
