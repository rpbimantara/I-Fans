package com.alpha.test.i_fans;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMatchMoment extends RecyclerView.Adapter<AdapterMatchMoment.MatchMomentViewHolder> {

    private ArrayList<MatchMoment> dataList;

    public AdapterMatchMoment(ArrayList<MatchMoment> dataList) {
        this.dataList = dataList;
    }

    public class MatchMomentViewHolder extends RecyclerView.ViewHolder{
        public TextView time,event,player,sub_player,club;
        public Bitmap icon;
        public MatchMomentViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.times_moment);
            event = itemView.findViewById(R.id.name_moment);
            player = itemView.findViewById(R.id.players_moment);
            sub_player = itemView.findViewById(R.id.sub_players_moment);
            club = itemView.findViewById(R.id.club_players_moment);
        }
    }

    @NonNull
    @Override
    public MatchMomentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_moment,viewGroup,false);
        return new MatchMomentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchMomentViewHolder holder, int position) {
        holder.time.setText(dataList.get(position).getTime()+"'");
        holder.time.setTextColor(ColorStateList.valueOf(Color.parseColor("#178C05")));
        holder.event.setText(dataList.get(position).getEvent());
        holder.player.setText(dataList.get(position).getPlayer_name());
        if (dataList.get(position).getSub_player_name().equalsIgnoreCase("False")){
            holder.sub_player.setText("-");
        }else{
            holder.sub_player.setText(dataList.get(position).getSub_player_name());
        }
        holder.club.setText(dataList.get(position).getClub_name());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
