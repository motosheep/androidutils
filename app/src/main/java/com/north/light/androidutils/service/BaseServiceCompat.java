package com.north.light.androidutils.service;

/**
 * Created by lzt
 * time 2020/6/8
 * 描述：
 * 使用方法，继承以后,调用一下代码
 * val socketIntent = Intent(this@WebSocketRec.mContext?.applicationContext, WebSocketService::class.java)
 * BaseServiceCompat.startService(this@WebSocketRec.mContext?.applicationContext, socketIntent)
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.north.light.androidutils.R;


public abstract class BaseServiceCompat extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //适配安卓8.0
            String channelId = getChannelId() + "";
            String channelName = getChannelName();
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_MIN);
            NotificationManager manager = (NotificationManager)
                    this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            startForeground(getChannelId(), getNotification());
        }
    }

    /**
     * Notification channelName
     *
     */
    protected abstract String getChannelName();

    /**
     * Notification channelId,must not be 0
     *
     */
    protected abstract int getChannelId();


    /**
     * Default content for notification , subclasses can be overwritten and returned
     */
    public String getNotificationContent() {
        return "";
    }

    /**
     * Displayed notifications, subclasses can be overwritten and returned
     */
    public Notification getNotification() {
        return createNormalNotification(getNotificationContent());
    }

    protected Notification createNormalNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getChannelId() + "");
        if (TextUtils.isEmpty(content)) {
            return builder.build();
        }

        builder.setContentTitle(getString(R.string.app_name))
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(getLargeIcon())
                .build();

        return builder.build();
    }

    /**
     * Large icon for notification , subclasses can be overwritten and returned
     */
    public Bitmap getLargeIcon() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    /**
     * Small icon for notification , subclasses can be overwritten and returned
     */
    public int getSmallIcon() {
        return R.mipmap.ic_launcher;
    }


    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //适配安卓8.0
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}