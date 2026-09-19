package it.uniba.dib.sms222316;



import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PopupLoad extends Dialog {

    private Button mButton;
    private long backPressed;
    ArrayList<SavedGames> saves = new ArrayList<>();


    public PopupLoad(Context context) {
        super(context);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.popup_load);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String auth = FirebaseAuth.getInstance().getUid();
        CollectionReference saved_games = db.collection("Users").document(auth).collection("Games");
        Query query = saved_games.orderBy("date", Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot document : querySnapshot) {
                saves.add(new SavedGames(auth, document.getId() ,document.getLong("turnNumber")  , document.getDate("date"),document.getLong("numberOfPlayers")));
            }
            RecyclerView myrv = findViewById(R.id.recyclerSave);

            RecyclerViewSaves myAdapter = new RecyclerViewSaves(context, saves, this);
            myrv.setLayoutManager(new LinearLayoutManager(context));
            myrv.setAdapter(myAdapter);

        });


    }
}
