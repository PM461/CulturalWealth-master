package it.uniba.dib.sms222316.Gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222316.Home;
import it.uniba.dib.sms222316.PopupSettings;
import it.uniba.dib.sms222316.R;

public class Gallery extends AppCompatActivity {
    ImageButton btnBack;
    Button btnMonuments, btnPaintings, btnCharacters;
    PopupSettings popupSettings; //Gestione audio
    SwitchCompat mVolume;
    enum heritage {
            Monuments, Paintings, Characters, All}
    Query query;

    heritage pressed = heritage.All;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_gallery);

        List<Heritage>  fullHrg;
        popupSettings = new PopupSettings(Gallery.this,Gallery.this);
        mVolume=popupSettings.findViewById(R.id.vol_musica);
        btnBack = findViewById(R.id.btnBack);
        btnMonuments = findViewById(R.id.btnMonuments);
        btnPaintings = findViewById(R.id.btnPaintings);
        btnCharacters = findViewById(R.id.btnCharacters);
        ArrayList Owned = (ArrayList) getIntent().getSerializableExtra("Owned");//TO-Do OGGETTO PLAYER NON SOLO ARRAY POSSEDUTI

        //Button back
        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(Gallery.this, Home.class);
            startActivity(i);
            finish();
        });


        fullHrg = new ArrayList<>();
        LoadHeritage(fullHrg,Owned,null);


        btnMonuments.setOnClickListener(v -> {
            btnMonuments.setEnabled(false);
            if(pressed != heritage.Monuments){
                fullHrg.clear();
                pressed = heritage.Monuments;
                btnMonuments.setBackgroundResource(R.drawable.pressed);
                btnPaintings.setBackgroundResource(R.drawable.defaultbtn);
                btnCharacters.setBackgroundResource(R.drawable.defaultbtn);
                LoadPart(fullHrg, Owned, heritage.Monuments.toString(),btnMonuments);
            }
            else{
                fullHrg.clear();
                pressed = heritage.All;
                LoadHeritage(fullHrg, Owned, btnMonuments);
            }
        });

        btnPaintings.setOnClickListener(v -> {
            btnPaintings.setEnabled(false);
            if(pressed != heritage.Paintings){
                fullHrg.clear();
                pressed = heritage.Paintings;
                btnMonuments.setBackgroundResource(R.drawable.defaultbtn);
                btnPaintings.setBackgroundResource(R.drawable.pressed);
                btnCharacters.setBackgroundResource(R.drawable.defaultbtn);
                LoadPart(fullHrg, Owned, heritage.Paintings.toString(), btnPaintings);
            }
            else{
                fullHrg.clear();
                pressed = heritage.All;
                LoadHeritage(fullHrg, Owned, btnPaintings);

            }
        });

        btnCharacters.setOnClickListener(v -> {
            btnCharacters.setEnabled(false);
            if(pressed != heritage.Characters){
                fullHrg.clear();
                pressed = heritage.Characters;
                btnMonuments.setBackgroundResource(R.drawable.defaultbtn);
                btnPaintings.setBackgroundResource(R.drawable.defaultbtn);
                btnCharacters.setBackgroundResource(R.drawable.pressed);
                LoadPart(fullHrg, Owned, heritage.Characters.toString(), btnCharacters);
            }
            else{
                fullHrg.clear();
                pressed = heritage.All;
                LoadHeritage(fullHrg, Owned, btnCharacters);
            }
        });
    }

    private void LoadHeritage(List<Heritage> fullHrg, ArrayList Owned, Button btn){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("Heritage");
        query = reference.orderBy("Title", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fullHrg.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("Title");
                    boolean own = Owned.contains(title);
                    fullHrg.add(new Heritage(title , document.getString("Description") , document.getString("Type") , document.getString("Image"), own));

                }
                List<Heritage> data = new ArrayList<>(fullHrg);

                RecyclerView myrv = findViewById(R.id.RecyclerView);

                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                float RecyclerWidth = (displayMetrics.widthPixels / displayMetrics.density) - 300; //larghezza sezione bottoni
                int spanCount = (int) (RecyclerWidth / 100) - 1;

                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,data);
                myrv.setLayoutManager(new GridLayoutManager(this,spanCount));
                myrv.setAdapter(myAdapter);
                TextView load = findViewById(R.id.loading);
                load.setText(R.string.loaded);
            } else {
                Log.e("Query-Gallery", "Not found query");

            }
            if(btn != null) {
                btn.setEnabled(true);
                btn.setBackgroundResource(R.drawable.defaultbtn);
            }
        });

    }
    private void LoadPart(List<Heritage> fullHrg, ArrayList Owned, String type, Button btn){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("Heritage");
        Query query = reference.orderBy("Title", Query.Direction.ASCENDING).whereEqualTo("Type" , type);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("Title");
                    boolean own = Owned.contains(title);
                    fullHrg.add(new Heritage(title , document.getString("Description") , document.getString("Type") , document.getString("Image"), own));
                }
                List<Heritage> data = new ArrayList<>(fullHrg);
                RecyclerView myrv = findViewById(R.id.RecyclerView);

                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                float RecyclerWidth = (displayMetrics.widthPixels / displayMetrics.density) - 300; //larghezza sezione bottoni
                int spanCount = (int) (RecyclerWidth / 100) - 1;

                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,data);
                myrv.setLayoutManager(new GridLayoutManager(this,spanCount));
                myrv.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                TextView load = findViewById(R.id.loading);
                long count = fullHrg.stream().filter(m -> m.getType().contains(type)).count();
                if(count<1) {
                    switch (type){
                        case "Monuments":
                            load.setText(R.string.noMonu);
                            break;
                        case "Paintings":
                            load.setText(R.string.noPaint);
                            break;
                        case "Characters":
                            load.setText(R.string.noChar);
                            break;
                    }
                }


            } else {
                Log.e("Query-Gallery", "Not found query");
            }
            btn.setEnabled(true);
        });
    }
    protected void onResume() {
        super.onResume();
        if(mVolume.isChecked()){
            popupSettings.SoundSwitchM();
        }
    }

}