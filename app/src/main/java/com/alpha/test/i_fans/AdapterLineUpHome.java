package com.alpha.test.i_fans;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterLineUpHome extends RecyclerView.Adapter<AdapterLineUpHome.LineUpHomeViewHolder>{

    private ArrayList<MatchLineUp> dataList;

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
    public LineUpHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_line_up,viewGroup,false);
        return new LineUpHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineUpHomeViewHolder holder, int i) {
        holder.position.setText(dataList.get(i).getPosition());
        holder.number.setText(dataList.get(i).getPlayer_number());
        holder.player.setText(dataList.get(i).getPlayer());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
