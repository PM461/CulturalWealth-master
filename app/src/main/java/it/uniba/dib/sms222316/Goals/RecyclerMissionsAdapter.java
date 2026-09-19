package it.uniba.dib.sms222316.Goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import it.uniba.dib.sms222316.R;

public class RecyclerMissionsAdapter extends RecyclerView.Adapter<RecyclerMissionsAdapter.MyViewHolder>{


    private final List<Missions> mData;

    public RecyclerMissionsAdapter(List<Missions> mData) {
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.mission_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMissionsAdapter.MyViewHolder holder, int position) {

        holder.Title.setText(mData.get(position).getTitle());
        holder.ProgressNumber.setText(mData.get(position).getProgressNumber()+"/"+mData.get(position).getTarget());
        holder.Rewards.setText(String.valueOf(mData.get(position).getRewards()));
        if(mData.get(position).getType().equals("xp"))
            holder.RewardImg.setImageResource(R.drawable.xp);
        else
            holder.RewardImg.setImageResource(R.drawable.coin);
        holder.ProgressBar.setMax(mData.get(position).getTarget());
        holder.ProgressBar.setProgress(mData.get(position).getProgressNumber());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Title, ProgressNumber, Rewards;
        CardView cardView ;
        ImageView RewardImg;

        ProgressBar ProgressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.MissionCard);
            Title = itemView.findViewById(R.id.TitleMission) ;
            ProgressNumber = itemView.findViewById(R.id.ProgressNumber);
            Rewards = itemView.findViewById(R.id.Rewards);
            RewardImg = itemView.findViewById(R.id.RewardsImg);
            ProgressBar = itemView.findViewById(R.id.ProgressBar);
        }
    }
}
