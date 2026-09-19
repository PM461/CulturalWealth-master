package it.uniba.dib.sms222316;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.dib.sms222316.Gameplay.GameActivity;

public class RecyclerViewSaves extends RecyclerView.Adapter<RecyclerViewSaves.MyViewHolderSaves>  {
    Context uContext ;
    PopupLoad load;
    List<SavedGames> uData;


    public RecyclerViewSaves(Context uContext, List<SavedGames> uData , PopupLoad c) {
        this.uContext = uContext;
        this.uData = uData;
        load = c;

    }

    @NonNull
    @Override
    public it.uniba.dib.sms222316.RecyclerViewSaves.MyViewHolderSaves onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(uContext);
        view = mInflater.inflate(R.layout.recycler_load,parent,false);
        return new RecyclerViewSaves.MyViewHolderSaves(view);
    }




    public void onBindViewHolder(RecyclerViewSaves.MyViewHolderSaves holder, final int position) {


       // holder.User_name.setText(uData.get(position).getName());
        holder.date.setText("Date:"+uData.get(position).getCreationDate().toString());
        holder.turns.setText("Turns:"+uData.get(position).getNumberofturn().toString());
        holder.players.setText("Players:"+uData.get(position).getPlayerNumber().toString());
        holder.Save.setOnClickListener(v -> {

                Intent intent = new Intent(uContext, GameActivity.class);
                intent.putExtra("GameId",uData.get(position).getGameId());
                uContext.startActivity(intent);
                load.hide();



            });

    }

    @Override
    public int getItemCount() {
        return uData.size();
    }

    public static class MyViewHolderSaves extends RecyclerView.ViewHolder {

        TextView date, turns , players;
        CardView Save;
        public MyViewHolderSaves(View itemView) {
            super(itemView);
            Save = itemView.findViewById(R.id.Save) ;
           date = itemView.findViewById(R.id.textViewDate) ;
            turns = itemView.findViewById(R.id.textViewTurns);
            players = itemView.findViewById(R.id.textViewPlayers);

        }
    }

}
