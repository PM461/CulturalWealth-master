package it.uniba.dib.sms222316.Rank;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import it.uniba.dib.sms222316.R;
import it.uniba.dib.sms222316.Utente;

public class RecyclerViewUtente extends RecyclerView.Adapter<RecyclerViewUtente.MyViewHolderUser>  {
    Context uContext;
    List<Utente> uData;

    private enum TypeHeritage {
        Monuments, Paintings, Characters
    }

    public RecyclerViewUtente(Context uContext, List<Utente> uData) {
        this.uContext = uContext;
        this.uData = uData;
    }

    @NonNull
    @Override
    public MyViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(uContext);
        view = mInflater.inflate(R.layout.card_utente,parent,false);
        return new MyViewHolderUser(view);
    }



    public void onBindViewHolder(MyViewHolderUser holder, final int position) {

        File file = new File(uContext.getFilesDir() ,"CulturalWealth/ProfilesPictures/" + uData.get(position).getProfilePic());
        holder.User_name.setText(uData.get(position).getName());
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        holder.img.setImageBitmap(bitmap);
        Log.d("image", uData.get(position).getProfilePic());
        holder.points.setText(""+uData.get(position).getScore());
        //ColorStateList colorStateList = ColorStateList.valueOf(Color.rgb());

        if (uData.get(position).isMe())holder.me.setBackgroundResource(R.drawable.rank_highlighted);
        else holder.me.setBackgroundResource(R.drawable.rank_not_highlighted);

    }

    @Override
    public int getItemCount() {
        return uData.size();
    }

    public static class MyViewHolderUser extends RecyclerView.ViewHolder {

        TextView User_name, points;
        CardView cardView ;
        ImageView img;
        ConstraintLayout me;

        public MyViewHolderUser(View itemView) {
            super(itemView);

            User_name = itemView.findViewById(R.id.user_name1) ;
            cardView = itemView.findViewById(R.id.card_utente);
            img = itemView.findViewById(R.id.user_icon1);
            me = itemView.findViewById(R.id.me);
            points = itemView.findViewById(R.id.user_score);
        }
    }

}
