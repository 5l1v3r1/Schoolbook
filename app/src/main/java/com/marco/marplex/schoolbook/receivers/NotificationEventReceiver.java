package com.marco.marplex.schoolbook.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.marco.marplex.schoolbook.services.NotificationIntentService;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;


/**
 * From http://stackoverflow.com/a/34207954
 */
public class NotificationEventReceiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";

    public static void setupAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                AlarmManager.INTERVAL_HALF_HOUR,
                alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SharedPreferences.loadBoolean(context, "pref", "setting_notification")) {
            String action = intent.getAction();
            Intent serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
            Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");

            startWakefulService(context, serviceIntent);
        }
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}