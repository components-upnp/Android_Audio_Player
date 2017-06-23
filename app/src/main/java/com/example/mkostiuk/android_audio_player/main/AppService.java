package com.example.mkostiuk.android_audio_player.main;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mkostiuk.android_audio_player.R;
import com.example.mkostiuk.android_audio_player.audio.LecteurAudioThread;
import com.example.mkostiuk.android_audio_player.upnp.AudioFileService;
import com.example.mkostiuk.android_audio_player.upnp.ServiceUpnp;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.LocalService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 23/06/2017.
 */

public class AppService extends Service {

    public static final int NOTIFICATION_ID = 1;

    public static final String ACTION_1 = "action_1";

    private ServiceUpnp service;
    private LocalService<AudioFileService> audioFileService;
    private String pathCurrentFile;
    private MediaPlayer mediaPlayer;
    private ArrayList<Thread> fileAudio;
    private Thread lectureCourante;
    private boolean isPlaying = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread().run();

        service = new ServiceUpnp();
        mediaPlayer = new MediaPlayer();

        getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
                service.getService(),
                Context.BIND_AUTO_CREATE);

        audioFileService = service.getRecorderLocalService();
        fileAudio = new ArrayList<>();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                set();

                //On arrete le service au bout de deux heures
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        onDestroy();
                    }
                }, 2 * 60 * 60 * 1000);
            }
        }, 5000);

        return START_NOT_STICKY;
    }

    private void set() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toaster.toast("Test fin lecture!!!");

                mediaPlayer.stop();
                mediaPlayer.reset();

                //Si des fichiers sont en attente de lecture, on passe au suivant
                if (!fileAudio.isEmpty()) {
                    Toaster.toast("Lecture fichier: "+pathCurrentFile);
                    lectureCourante = fileAudio.remove(0);
                    lectureCourante.start();
                    isPlaying = true;
                }
                else {  //sinon on stoppe les lectures et on attend
                    isPlaying = false;
                }
            }
        });

        audioFileService.getManager().getImplementation().getPropertyChangeSupport()
                .addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("path")) {
                            HashMap<String,String> args = (HashMap<String, String>) evt.getNewValue();
                            pathCurrentFile = args.get("FILEPATH");
                            fileAudio.add(new LecteurAudioThread(pathCurrentFile, mediaPlayer));

                            if (! isPlaying) {
                                isPlaying = true;
                                lectureCourante = fileAudio.remove(0);
                                lectureCourante.start();
                            }
                        }
                    }
                });
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
