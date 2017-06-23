package com.example.mkostiuk.android_audio_player.main;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mkostiuk.android_audio_player.R;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 23/06/2017.
 */

public class AppService extends Service {

    public static final int NOTIFICATION_ID = 1;

    public static final String ACTION_1 = "action_1";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread().run();

        return START_NOT_STICKY;
    }

    //fonction permettant d'afficher une notification test à l'utilisateur lors d'un évènement
    public static void displayNotification(Context context) {

        Intent action1Intent = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_1);

        PendingIntent action1PendingIntent = PendingIntent.getService(context, 0,
                action1Intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Accelerometer")
                        .setContentText("Cliquer ici pour fermer l'application")
                        .addAction(new NotificationCompat.Action(R.mipmap.ic_launcher_round,
                                "Fermer", action1PendingIntent))
                        .setContentIntent(action1PendingIntent)
                        .setAutoCancel(true)
                ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    //Classe interne permettant de créer un service gérant les actions faites sur la notification
    //L'utilisateur peut passer au fichier audio suivant ou précédent
    public static class NotificationActionService extends IntentService {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            Toaster.toast("Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                // TODO: handle action 1.
                System.out.println("Action 1 notification");

                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                getApplicationContext().sendBroadcast(it);

                System.exit(0);
            }
        }
    }

    public static void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
