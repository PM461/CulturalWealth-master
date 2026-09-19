package it.uniba.dib.sms222316.Goals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222316.R;

public class FragmentAchievements extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UpdateAchivements(view);
    }

    private void UpdateAchivements(View view){
        List<Category> Category = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("Achievements");
        Query query = reference.orderBy(FieldPath.documentId(), Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Category.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {

                    Category.add(new Category(document.getId()));

                }
                List<Category> data = new ArrayList<>(Category);

                RecyclerView myrv = view.findViewById(R.id.CategoryRecycler);
                RecyclerCategoryAdapter myAdapter = new RecyclerCategoryAdapter(data);
                myrv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                myrv.setAdapter(myAdapter);

            } else {
                Log.e("Query-Gallery", "Not found query");

            }
        });
    }

}