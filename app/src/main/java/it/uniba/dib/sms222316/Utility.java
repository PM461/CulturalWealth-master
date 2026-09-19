package it.uniba.dib.sms222316;

import android.content.Context;
import android.widget.Toast;

public class Utility {
    static void  showToast(Context context , String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
