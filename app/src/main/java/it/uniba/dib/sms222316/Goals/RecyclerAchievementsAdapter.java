package it.uniba.dib.sms222316.Goals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import it.uniba.dib.sms222316.R;

public class RecyclerAchievementsAdapter extends RecyclerView.Adapter<RecyclerAchievementsAdapter.MyViewHolder>{


    private final List<Achievements> mData;
    private Context context;

    public RecyclerAchievementsAdapter(List<Achievements> mData) {
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.achievement_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAchievementsAdapter.MyViewHolder holder, int position) {
        Bitmap bitmap;
        File file = new File(context.getFilesDir(), "CulturalWealth/ProfilesPictures/" + mData.get(position).getRelic());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        if (!mData.get(position).isOwn()) {

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

            holder.img.setColorFilter(colorFilter);

        }
        else
        {
            holder.img.invalidate();
            holder.img.setColorFilter(null);
        }

        holder.TargetPoint.setText(mData.get(position).getTargetPoint());
        //holder.Relic.setText(mData.get(position).getRelic()); //TODO aggiustare il titolo degli obiettivi/vecchio ragionamento nome relic - path
        holder.img.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TargetPoint, Relic;
        CardView cardView ;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.CardAchievements);
            TargetPoint = itemView.findViewById(R.id.target) ;
            Relic = itemView.findViewById(R.id.relic);
            img = itemView.findViewById(R.id.bg);
        }
    }
}
