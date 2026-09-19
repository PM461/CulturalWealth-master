package it.uniba.dib.sms222316;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class endgame_popup extends AppCompatActivity {

    int selected = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_endgame);

        Intent intent = getIntent();
        int punteggioprimo = intent.getIntExtra("punteggioprimo" ,0);
        int punteggiosecondo =intent.getIntExtra("punteggiosecondo", 0);
        int punteggioterzo =intent.getIntExtra("punteggioterzo" , 0);
        String nomeprimo = intent.getStringExtra("nomeprimo");
        String nomesecondo =intent.getStringExtra("nomesecondo");
        String nometerzo =intent.getStringExtra("nometerzo");




        TextView pprimo = findViewById(R.id.firstpoints);
        TextView psecondo = findViewById(R.id.secondpoints);
        TextView pterzo = findViewById(R.id.thirdpoints);
        TextView nprimo = findViewById(R.id.firstname);
        TextView nsecondo = findViewById(R.id.secondname);
        TextView nterzo = findViewById(R.id.thirdname);
        ImageView continu = findViewById(R.id.continu);



        pprimo.setText(""+punteggioprimo);
        psecondo.setText(""+punteggiosecondo);
        pterzo.setText(""+punteggioterzo);

        if (nomeprimo.equals("Giocatore 1")) {
            findusername(Home.Guest, nprimo);
            nsecondo.setText(nomesecondo);
            nterzo.setText(nometerzo);
        } else if (nomesecondo.equals("Giocatore 1")) {
            findusername(Home.Guest, nsecondo);
            nprimo.setText(nomeprimo);
            nterzo.setText(nometerzo);
        } else if (nometerzo.equals("Giocatore 1")) {
            findusername(Home.Guest, nterzo);
            nsecondo.setText(nomesecondo);
            nprimo.setText(nomeprimo);
        }

        continu.setOnClickListener(v -> {
            Intent i = new Intent(endgame_popup.this, Home.class);
            Addpoints(punteggioprimo);
            startActivity(i);
        });





    }

    private void findusername(Boolean Guest , TextView v)
    {
        FirebaseAuth fb = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(!Guest?fb.getUid():"Guest")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    v.setText(queryDocumentSnapshots.getString("nome"));

                });


    }

    //TODO Spostare questo concetto nelle missioni quando saranno state teorizzate
    private void Addpoints(int Points)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid());
        documentRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Il documento esiste

                            // Ottenere il valore del campo "nomeCampo"
                            Map<String, Long> campoAnnidato = (Map<String, Long>) documentSnapshot.get("Achievements");
                            Long punti = campoAnnidato.get("Punti");
                            punti+= Points;
                            campoAnnidato.remove("Punti");
                            campoAnnidato.put("Punti" , punti);
                            documentRef.update("Achievements",campoAnnidato)     .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Il documento è stato aggiornato con successo
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Gestisci l'errore in caso di fallimento dell'aggiornamento
                                        }
                                    });

                            documentRef.update("points",punti)     .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Il documento è stato aggiornato con successo
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Gestisci l'errore in caso di fallimento dell'aggiornamento
                                        }
                                    });

                        } else {
                            // Il documento non esiste
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gestisci l'errore in caso di fallimento del recupero del documento
                    }
                });



    }
    }




