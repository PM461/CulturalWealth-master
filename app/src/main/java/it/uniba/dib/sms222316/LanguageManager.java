package it.uniba.dib.sms222316;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private final Context ct;
    SharedPreferences sharedPreferences;
    public LanguageManager(Context ctx){
        ct=ctx;
        sharedPreferences = ct.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void ChangeLanguage(String language){
        Locale newLocale = new Locale(language);
        Locale.setDefault(newLocale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(newLocale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        SetLanguage(language);
    }

    public void SetLanguage(String language){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", language);
        editor.apply();
    }

    public String GetLang(){
        Locale locale = Locale.getDefault();
        return sharedPreferences.getString("lang",locale.getLanguage());
    }
}
