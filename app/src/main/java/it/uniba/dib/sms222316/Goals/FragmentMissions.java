package it.uniba.dib.sms222316.Goals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222316.Home;
import it.uniba.dib.sms222316.R;

public class FragmentMissions extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_missions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Missions> DailyMissions;
        DailyMissions = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference reference = db.collection("Users").document(!Home.Guest?currentId:"Guest").collection("DailyMissions");//TODO: userID da oggetto utente
        Query query = reference.orderBy(FieldPath.documentId(), Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DailyMissions.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {

                    DocumentReference mission = (DocumentReference) document.get("Base");
                    mission.get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            DocumentSnapshot miss = task1.getResult();

                            DailyMissions.add(new Missions(miss.getId(), Integer.parseInt(document.get("Progress").toString()), Integer.parseInt(miss.get("value").toString()), Integer.parseInt(miss.get("Target").toString()), miss.getString("Type")));

                            List<Missions> data = new ArrayList<>(DailyMissions);

                            RecyclerView myrv = view.findViewById(R.id.MissionsRecycler);
                            RecyclerMissionsAdapter myAdapter = new RecyclerMissionsAdapter(data);
                            myrv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                            myrv.setAdapter(myAdapter);

                        }
                    });
                }
            } else {
                Log.e("Query-Gallery", "Not found query");
            }
        });
    }
}