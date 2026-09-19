package it.uniba.dib.sms222316;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

public class PopupSettings extends Dialog {
    SwitchCompat eVolume,mVolume;
    boolean effect_boolean,music_boolean;
    Audio audio;

    public PopupSettings(@NonNull Context context, Context context1) {
        super(context);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.settings);
        audio = Audio.getInstance(context);
        eVolume = findViewById(R.id.vol_effetti);
        mVolume = findViewById(R.id.vol_musica);

        eVolume.setOnClickListener(v -> SoundSwitchE());
        mVolume.setOnClickListener(v -> SoundSwitchM());

        Load();
        Update();

    }

    public void SoundSwitchM(){
        if(mVolume.isChecked()){
            SharedPreferences.Editor share = getContext().getSharedPreferences("editor", MODE_PRIVATE).edit();
            share.putBoolean("vol_musica",true);
            share.apply();
            mVolume.setChecked(true);
            audio.playAudio(R.raw.electricpistol);
        }else{
            SharedPreferences.Editor share = getContext().getSharedPreferences("editor", MODE_PRIVATE).edit();
            share.putBoolean("vol_musica",false);
            share.apply();
            mVolume.setChecked(false);
            audio.pauseAudio();
        }
    }

    public void SoundSwitchE(){
        if(eVolume.isChecked()){
            SharedPreferences.Editor share = getContext().getSharedPreferences("editor", MODE_PRIVATE).edit();
            share.putBoolean("vol_effetti",true);
            share.apply();
            eVolume.setChecked(true);
        }else{
            SharedPreferences.Editor editor = getContext().getSharedPreferences("editor", MODE_PRIVATE).edit();
            editor.putBoolean("vol_effetti",false);
            editor.apply();
            eVolume.setChecked(false);
        }
    }

    public void Load(){
        SharedPreferences getSound = getContext().getSharedPreferences("editor", MODE_PRIVATE);
        effect_boolean = getSound.getBoolean("vol_effetti",true);
        music_boolean = getSound.getBoolean("vol_musica",true);
    }
    public void Update(){
        eVolume.setChecked(effect_boolean);
        mVolume.setChecked(music_boolean);
    }
}
