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

public class AdapterTeam extends RecyclerView.Adapter<AdapterTeam.TeamViewHolder> {

    private ArrayList<Team> dataList;

    public AdapterTeam(ArrayList<Team> dataList) {
        this.dataList = dataList;
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView txtname,txtNoPunggung;
        public ImageView image;
        public Bitmap icon;
        public FloatingActionButton fab_icon;

        public TeamViewHolder(View itemView) {
            super(itemView);
            this.txtname = itemView.findViewById(R.id.txt_namastaff);
            this.image = itemView.findViewById(R.id.image_staff);
            this.txtNoPunggung = itemView.findViewById(R.id.txt_nostaff);
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
        if (dataList.get(position).getNo_punggung() == "0"){
            holder.txtNoPunggung.setText("");
        }else {
            holder.txtNoPunggung.setText(dataList.get(position).getNo_punggung());
        }
        if(dataList.get(position).getPosisi().equalsIgnoreCase("GoalKeeper")){
            holder.fab_icon.setImageResource(R.drawable.ic_keeper);
        }
        if(dataList.get(position).getPosisi().equalsIgnoreCase("Forward")){
            holder.fab_icon.setImageResource(R.drawable.ic_forward);
        }
        if(dataList.get(position).getPosisi().equalsIgnoreCase("Midfielder")){
            holder.fab_icon.setImageResource(R.drawable.ic_midfielder);
        }
        if(dataList.get(position).getPosisi().equalsIgnoreCase("Defender")){
            holder.fab_icon.setImageResource(R.drawable.ic_defender);
        }
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
