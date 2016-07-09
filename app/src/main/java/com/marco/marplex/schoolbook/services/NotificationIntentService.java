package com.marco.marplex.schoolbook.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.RemoteViews;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

/**
 * From http://stackoverflow.com/a/34207954
 */
public class NotificationIntentService extends IntentService {

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        processStartNotification();
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void processStartNotification() {
        Log.d(getClass().getSimpleName(), "processStartNotification called");

        ClassevivaCaller mCaller = new ClassevivaCaller(new ClassevivaCallback<Voto>() {
            @Override
            public void onResponse(ArrayList<Voto> list) {
                ArrayList<Voto> savedVotes = Votes.getVotes(getApplicationContext());

                //Check if the returned list isn't equal to the saved one.
                if(Votes.isThereAnyVotes(getApplicationContext()) && !savedVotes.equals(list)){
                    ArrayList<Voto> intruders = Votes.getIntruderVotes(savedVotes, list);
                    System.out.println("Intruders: " + intruders.size());
                    int index = 1;
                    for(Voto voto : intruders){
                        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);

                        if(voto.special && !voto.voto.equals("-") && !voto.voto.equals("+")){
                            contentView.setInt(R.id.voto, "setBackgroundColor", Color.parseColor("#3F51B5")); //Blue
                        } else contentView.setInt(R.id.voto, "setBackgroundColor",
                                Votes.getColorByVote(Votes.getVoteByString(voto.voto)));

                        contentView.setTextViewText(R.id.voto, voto.voto);
                        contentView.setTextViewText(R.id.voto_descrizione, voto.materia+" "+voto.data+"\n"+voto.tipo);

                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_school_white_24dp)
                                .setContent(contentView)
                                .setContentTitle("Nuovo voto!")
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(SharedPreferences.loadBoolean(getBaseContext(), "pref", "setting_suona") ? Settings.System.DEFAULT_NOTIFICATION_URI : null)
                                .setVibrate(SharedPreferences.loadBoolean(getBaseContext(), "pref", "setting_vibra") ? new long[] { 1000, 1000, 1000, 1000, 1000 }: new long[0])
                                .build();

                        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(index, notification);

                        index++;
                    }
                }
            }
        }, getApplicationContext());
        mCaller.getVotes();
    }

}
