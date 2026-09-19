package it.uniba.dib.sms222316;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

public class PopupDialog extends Dialog {

    private static final int TIME_INTERVALL = 2000;
    private long backPressed;

    public PopupDialog(@NonNull Context context, final Home activity) {
        super(context);
        setContentView(R.layout.dialog_layout);

        Button mButton = findViewById(R.id.submitUserButton);

        mButton.setOnClickListener(v -> dismiss());
    }
    public void onBackPressed() {
        if(backPressed + TIME_INTERVALL > System.currentTimeMillis()){
            return;
        }
        backPressed = System.currentTimeMillis();
    }
}