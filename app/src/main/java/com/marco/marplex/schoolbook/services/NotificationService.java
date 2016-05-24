package com.marco.marplex.schoolbook.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Votes;

import java.util.ArrayList;

public class NotificationService extends Service {

    ClassevivaCaller mCaller;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCaller = new ClassevivaCaller(new ClassevivaCallback() {
            @Override
            public void onResponse(ArrayList list) {
                ArrayList<Voto> savedVotes = Votes.getVotes(getApplicationContext());
                //Check if the returned list isn't equal to the saved one.
                if(Votes.isThereAnyVotes(getApplicationContext()) && !savedVotes.equals(list)){
                    ArrayList<Voto> intruders = Votes.getIntruderVotes(savedVotes, list);
                    int index = 0;
                    for(Voto voto : intruders){
                        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);

                        if(voto.special && !voto.voto.equals("-") && !voto.voto.equals("+")){
                            contentView.setInt(R.id.voto, "setBackgroundColor", Color.parseColor("#3F51B5")); //Blu
                        } else contentView.setInt(R.id.voto, "setBackgroundColor",
                                Votes.getColorByVote(Votes.getVoteByString(voto.voto)));

                        contentView.setTextViewText(R.id.voto, voto.voto);
                        contentView.setTextViewText(R.id.voto_descrizione, riceviTesto(voto.materia, voto.data, voto.tipo));

                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_bookmark_black_24dp)
                                .setContent(contentView)
                                .setContentTitle("Nuovo voto!")
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setVibrate(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("setting_vibra", false) ? new long[1]: new long[0])
                                .build();

                        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(index, notification);

                        index++;
                    }
                }
            }
        }, getApplicationContext());
        mCaller.getVotes();
        new Thread(new Runnable(){
            public void run() {
                while(true)
                {
                    try {

                        Thread.sleep(1800000); //Run every 30 minutes
                        mCaller.getVotes();

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }).start();
        return START_STICKY;
    }

    private String riceviTesto(String materia, String data, String tipo){
        return materia+" "+data+"\n"+tipo;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
