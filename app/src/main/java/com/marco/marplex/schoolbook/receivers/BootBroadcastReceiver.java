package com.marco.marplex.schoolbook.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marco.marplex.schoolbook.utilities.SharedPreferences;

/**
 * Created by marco on 5/20/16.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(SharedPreferences.loadBoolean(context, "pref", "setting_notification")){
            NotificationEventReceiver.setupAlarm(context);
        }
    }
}