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

public class AdapterMatchMoment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MatchMoment> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MatchMomentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_moment, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Moment list is empty!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            MatchMomentViewHolder holder = ((MatchMomentViewHolder) viewHolder);
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
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return (this.dataList != null && this.dataList.size() > 0) ? VIEW_TYPE_NORMAL : VIEW_TYPE_EMPTY;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (this.dataList != null && this.dataList.size() > 0) ? dataList.size() : 1;
    }
}
