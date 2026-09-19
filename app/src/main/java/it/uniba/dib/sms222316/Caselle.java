package it.uniba.dib.sms222316;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

public class Caselle {

    public ImageView[] setCasella(Context context){

    ImageView[] casella = new ImageView[40];
    for(int i=0;i<40;i++){
        int exa = context.getResources().getIdentifier("exa_"+i,"id", context.getPackageName());
        casella[i] = ((Activity) context).findViewById(exa);
    }
    return casella;
    }
}
