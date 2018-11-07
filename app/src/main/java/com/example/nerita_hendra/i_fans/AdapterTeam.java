package com.example.nerita_hendra.i_fans;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTeam extends RecyclerView.Adapter<AdapterTeam.TeamViewHolder> {

    private ArrayList<Team> dataList;

    public AdapterTeam(ArrayList<Team> dataList) {
        this.dataList = dataList;
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView txtname;
        public ImageView image;
        public Bitmap icon;
        public FloatingActionButton fab_icon;

        public TeamViewHolder(View itemView) {
            super(itemView);
            this.txtname = itemView.findViewById(R.id.txt_namastaff);
            this.image = itemView.findViewById(R.id.image_staff);
            this.fab_icon = itemView.findViewById(R.id.team_fab);
            this.fab_icon.setImageResource(R.drawable.ic_medis);
        }
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_team,parent,false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.txtname.setText(dataList.get(position).getNama());
        holder.image.setImageBitmap(StringToBitMap(dataList.get(position).getFoto()));
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
