package it.uniba.dib.sms222316.Goals;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniba.dib.sms222316.Home;
import it.uniba.dib.sms222316.R;

public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.MyViewHolder>{

    private final List<Category> cData;
    private ViewGroup Padre;
    List<DocumentReference> Posseduti;

    public RecyclerCategoryAdapter(List<Category> cData) {
        this.cData = cData;
    }

    @NonNull
    @Override
    public RecyclerCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.category_card,parent,false);
        this.Padre = parent;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCategoryAdapter.MyViewHolder holder, int position) {

        //Set nome e container Category
        holder.category.setText(cData.get(position).getCategory());
        String Catt = cData.get(position).getCategory();


        UpdateAchivements(Catt ,holder);
        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                UpdateAchivements(Catt,holder);
                refreshHandler.postDelayed(this, 20 * 1000);
            }
        };
        refreshHandler.postDelayed(runnable, 20 * 1000);






    }

    @Override
    public int getItemCount() {
        return cData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView category ;
        RecyclerView Achievement;

        public MyViewHolder(View itemView) {
            super(itemView);

            Achievement = itemView.findViewById(R.id.AchievementsRecycler);
            category = itemView.findViewById(R.id.CategoryName);
        }
    }

    private void UpdateAchivements(String cat, RecyclerCategoryAdapter.MyViewHolder holder){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Set recycler Achievements
        List<Achievements> goals = new ArrayList<>();
        //setup owned and point
        DocumentReference uRef = db.collection("Users").document(!Home.Guest? FirebaseAuth.getInstance().getCurrentUser().getUid():"Guest");
        uRef.get().addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                DocumentSnapshot Utente = task1.getResult();
                HashMap<String,Long> Achiv = (HashMap<String,Long>) Utente.get("Achievements");
                Posseduti = (List<DocumentReference>) Utente.get("Posseduti");
                //Creating recycler
                CollectionReference reference = db.collection("Achievements");
                Query query = reference.whereEqualTo(FieldPath.documentId(), cat).orderBy(FieldPath.documentId(), Query.Direction.ASCENDING);

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        goals.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Map<String, Object> map = document.getData();

                            for(Map.Entry<String, Object> entry : map.entrySet()){
                                Map<String, Object> obb = (Map<String, Object>) entry.getValue();

                                if(!obb.isEmpty()){
                                    boolean own;
                                    DocumentReference reward = (DocumentReference) obb.get("Reward");
                                    //add reward
                                    if(Achiv.get(cat)>=(Long) obb.get("Target")) {
                                        own = true;
                                        if(!Posseduti.contains((DocumentReference) obb.get("Reward"))){
                                            Posseduti.add((DocumentReference) obb.get("Reward"));
                                            uRef.update(new HashMap<String, Object>() {{
                                                put("Posseduti", Posseduti);
                                            }});
                                        }
                                    } else {
                                        own = false;
                                    }
                                    //add Achievements on the view
                                    reward.get().addOnCompleteListener(task2 -> {
                                        if(task2.isSuccessful()){
                                            DocumentSnapshot rew = task2.getResult();
                                            goals.add(new Achievements(obb.get("Target").toString(), obb.get("Name").toString(),rew.getString("Image"),own));
                                            Collections.sort(goals, Comparator.comparingInt(mapp -> Integer.parseInt(mapp.getTargetPoint())));
                                            List<Achievements> data = new ArrayList<>(goals);
                                            RecyclerView myrv = holder.Achievement;

                                            RecyclerAchievementsAdapter myAdapter = new RecyclerAchievementsAdapter(data);
                                            myrv.setLayoutManager(new LinearLayoutManager(Padre.getContext(), LinearLayoutManager.HORIZONTAL, false));
                                            myrv.setAdapter(myAdapter);

                                        }
                                    });

                                }
                            }
                        }
                    }
                });



            }
        });

    }

}
