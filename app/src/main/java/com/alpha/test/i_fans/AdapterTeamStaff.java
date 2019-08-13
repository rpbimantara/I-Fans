package com.alpha.test.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTeamStaff extends RecyclerView.Adapter<AdapterTeamStaff.TeamStaffViewHolder>{
    private ArrayList<Team> dataList;

    public AdapterTeamStaff(ArrayList<Team> dataList) {
        this.dataList = dataList;
    }

    public class TeamStaffViewHolder extends RecyclerView.ViewHolder{
        public TextView txtname;
        public ImageView image;

        public TeamStaffViewHolder(View itemView) {
            super(itemView);
            this.txtname = itemView.findViewById(R.id.txt_namastaff);
            this.image = itemView.findViewById(R.id.image_staff);
        }
    }

    @NonNull
    @Override
    public AdapterTeamStaff.TeamStaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_team_staff,parent,false);
        return new AdapterTeamStaff.TeamStaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTeamStaff.TeamStaffViewHolder holder, int position) {
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
