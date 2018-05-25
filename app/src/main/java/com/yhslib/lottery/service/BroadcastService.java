package com.yhslib.lottery.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.yhslib.lottery.R;
import com.yhslib.lottery.activity.MainActivity;

/**
 * Created by 74011 on 2018/5/4.
 */

public class BroadcastService extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION)) {
            Intent service = new Intent(context, MainActivity.class);
            context.startService(service);
        }
    }
}
