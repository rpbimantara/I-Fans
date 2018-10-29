package com.example.nerita_hendra.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterLelang extends RecyclerView.Adapter<AdapterLelang.LelangViewHolder> {

    private ArrayList<lelang> dataList;

    public AdapterLelang(ArrayList<lelang> dataList) {
        this.dataList = dataList;
    }

    public class LelangViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNamaLelang,txtWaktuLelang;
        public Button btnBidLelang,btnBinLelang;
        public ImageView imageLelang;

        public LelangViewHolder(View itemView) {
            super(itemView);
            this.txtNamaLelang = (TextView) itemView.findViewById(R.id.txt_namalelang);
            this.txtWaktuLelang = (TextView) itemView.findViewById(R.id.txt_waktulelang);
            this.btnBidLelang = (Button) itemView.findViewById(R.id.button_bid);
            this.btnBinLelang = (Button) itemView.findViewById(R.id.button_bin);
            this.imageLelang = (ImageView) itemView.findViewById(R.id.lelang_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LelangViewHolder holder, int position) {
        holder.txtNamaLelang.setText(dataList.get(position).getNamalelang());
        holder.txtWaktuLelang.setText(dataList.get(position).getWaktulelang());
        holder.btnBidLelang.setText(dataList.get(position).getBidlelang());
        holder.btnBinLelang.setText(dataList.get(position).getBinlelang());
        holder.imageLelang.setImageBitmap(StringToBitMap(dataList.get(position).getLelangimage()));
    }

    @NonNull
    @Override
    public LelangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_lelang,parent,false);
        return new LelangViewHolder(view);
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
