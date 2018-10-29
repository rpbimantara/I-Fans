package com.example.nerita_hendra.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterKlasemen extends RecyclerView.Adapter<AdapterKlasemen.KlasemenViewHolder>{

    ArrayList<Klasemen> dataList;

    public AdapterKlasemen(ArrayList<Klasemen> dataList) {
        this.dataList = dataList;
    }

    public class KlasemenViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNoUrut,txtTeamKlasemen,txtPlayKlasemen,txtSelisihGol,txtPoint;
        public ImageView imageClub;
        public LinearLayout layout;

        public KlasemenViewHolder(View itemView) {
            super(itemView);
            this.txtNoUrut = (TextView) itemView.findViewById(R.id.txt_urutklasemen);
            this.txtTeamKlasemen = (TextView) itemView.findViewById(R.id.txt_teamklasemen);
            this.txtPlayKlasemen = (TextView) itemView.findViewById(R.id.txt_playklasemen);
            this.txtSelisihGol = (TextView) itemView.findViewById(R.id.txt_selisihgol);
            this.txtPoint = (TextView) itemView.findViewById(R.id.txt_point);
            this.imageClub = (ImageView) itemView.findViewById(R.id.klasemen_image);
            this.layout = (LinearLayout) itemView.findViewById(R.id.linearLayout_klasemen);
        }
    }

    @NonNull
    @Override
    public KlasemenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_klasemen,parent,false);
        return new KlasemenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KlasemenViewHolder holder, int position) {
        holder.txtNoUrut.setText(dataList.get(position).getTxtNoUrut());
        holder.txtTeamKlasemen.setText(dataList.get(position).getTxtTeamKlasemen());
        holder.txtPlayKlasemen.setText(dataList.get(position).getTxtPlayKlasemen());
        holder.txtSelisihGol.setText(dataList.get(position).getTxtSelisihGol());
        holder.txtPoint.setText(dataList.get(position).getTxtPoint());
        holder.imageClub.setImageBitmap(StringToBitMap(dataList.get(position).getTxtFotoClub()));
        holder.layout.setBackgroundColor(dataList.get(position).getTxtLayoutColor());

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
