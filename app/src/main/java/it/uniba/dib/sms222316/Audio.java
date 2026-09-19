package it.uniba.dib.sms222316;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class Audio {

    private static Audio instance;
    private final Context context;
    private MediaPlayer mediaPlayer;

    private Audio(Context context) {
        this.context = context;
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), 0);
    }

    public static Audio getInstance(Context context) {
        if (instance == null) {
            instance = new Audio(context);
        }
        return instance;
    }

    public void playAudio(int audioResId) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, audioResId);
        }
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    public void pauseAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /*public void releaseAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/
}
