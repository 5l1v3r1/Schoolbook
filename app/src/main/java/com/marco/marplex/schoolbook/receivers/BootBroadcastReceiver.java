package com.marco.marplex.schoolbook.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marco.marplex.schoolbook.services.NotificationService;

/**
 * Created by marco on 5/20/16.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, NotificationService.class);
        context.startService(myIntent);

    }
}