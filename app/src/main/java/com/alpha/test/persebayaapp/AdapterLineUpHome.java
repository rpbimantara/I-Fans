package com.alpha.test.persebayaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterLineUpHome extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MatchLineUp> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterLineUpHome(ArrayList<MatchLineUp> dataList) {
        this.dataList = dataList;
    }

    public class LineUpHomeViewHolder extends RecyclerView.ViewHolder{
        public TextView position,number,player;
        public LineUpHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.line_up_position);
            number = itemView.findViewById(R.id.line_up_number);
            player = itemView.findViewById(R.id.line_up_name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new LineUpHomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_line_up, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Line Up Home is empty!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            LineUpHomeViewHolder holder = ((LineUpHomeViewHolder) viewHolder);
            holder.position.setText(dataList.get(position).getPosition());
            holder.number.setText(dataList.get(position).getPlayer_number());
            holder.player.setText(dataList.get(position).getPlayer());
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
