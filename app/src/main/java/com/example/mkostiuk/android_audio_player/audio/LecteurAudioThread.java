package com.example.mkostiuk.android_audio_player.audio;

import android.media.MediaPlayer;
import android.os.Environment;
import java.io.IOException;

/**
 * Created by mkostiuk on 27/04/2017.
 */

/**
 * Classe décrivant un Thread permettant de lancer la lecture d'un fichier audio
 * On passe à ce Thread un MediaPlayer déjà instancié ainsi que le chemin du fichier à lire
 * */
public class LecteurAudioThread extends Thread {

    private String path;
    private MediaPlayer mp;

    public LecteurAudioThread(String p, MediaPlayer m) {
        path = p;
        mp = m;
    }
    @Override
    public void run() {
        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
