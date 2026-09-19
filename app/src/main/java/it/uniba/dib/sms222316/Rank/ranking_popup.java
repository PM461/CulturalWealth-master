package it.uniba.dib.sms222316.Rank;


import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.uniba.dib.sms222316.Gallery.Heritage;
import it.uniba.dib.sms222316.R;
import it.uniba.dib.sms222316.Utente;

public class ranking_popup extends Dialog {

    private EditText mEditText;
    private Button mButton;
    private static final int TIME_INTERVALL = 2000;
    private long backPressed;
    ArrayList<Utente> data, fullHrg = new ArrayList<>();


    public ranking_popup(Context context) {
        super(context);
       // context.setTheme(android.R.style.Theme_DeviceDefault_NoActionBar);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.ranking_popup);



        String empty = "AdaLovelace.png";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("Users");
        Query query = reference.orderBy("points", Query.Direction.DESCENDING);
        ExecutorService executorService = Executors.newFixedThreadPool(1); // Puoi regolare il numero di thread a seconda delle tue esigenze


        query.get().addOnSuccessListener(querySnapshot -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (QueryDocumentSnapshot document : querySnapshot) {
                if (!document.getId().equals("Guest")) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        if (document.get("Profilepic") == null) {
                            if (document.getId().equals(FirebaseAuth.getInstance().getUid()))fullHrg.add(new Utente(document.getString("nome"), "", document.getLong("points"), empty , true));
                            else fullHrg.add(new Utente(document.getString("nome"), "", document.getLong("points"), empty));
                        } else {
                            DocumentReference userRef = db.document(document.getDocumentReference("Profilepic").getPath());

                            try {
                                DocumentSnapshot userSnapshot = Tasks.await(userRef.get());
                                if (document.getId().equals(FirebaseAuth.getInstance().getUid()))fullHrg.add(new Utente(document.getString("nome"), "", document.getLong("points"), userSnapshot.getString("Image"), true));
                                else fullHrg.add(new Utente(document.getString("nome"), "", document.getLong("points"), userSnapshot.getString("Image")));
                                Log.d("documento", userSnapshot.getString("Image"));
                            } catch (Exception e) {
                                Log.d("documento", "AAAAA");
                            }
                        }
                    }, executorService);

                    futures.add(future);
                }
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            allOf.thenRun(() -> {
                // Ora puoi eseguire il codice successivo una volta che tutte le operazioni sono state completate
                data = new ArrayList<>(fullHrg);
                RecyclerView myrv = findViewById(R.id.recicler_ranking);
                RecyclerViewUtente myAdapter = new RecyclerViewUtente(context, data);
                myrv.setLayoutManager(new LinearLayoutManager(context));
                myrv.setAdapter(myAdapter);
                for (Utente element : fullHrg) {
                    Log.d("documentoss", element.getName() + "  " + element.getProfilePic());
                }
            });

        }).addOnFailureListener(e -> {
            Log.e("Query-ranking", "Not found query", e);
        });




    }


}