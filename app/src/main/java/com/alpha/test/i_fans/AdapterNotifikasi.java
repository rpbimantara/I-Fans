package com.alpha.test.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterNotifikasi extends RecyclerView.Adapter<AdapterNotifikasi.NotifikasiViewHolder>{
    private ArrayList<Notifikasi> dataList;

    public AdapterNotifikasi(ArrayList<Notifikasi> dataList) {
        this.dataList = dataList;
    }

    public class NotifikasiViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNameNotifikasi,txtDate;

        public NotifikasiViewHolder(View itemView) {
            super(itemView);
            this.txtNameNotifikasi = (TextView) itemView.findViewById(R.id.txt_name_notifikasi);
            this.txtDate = (TextView) itemView.findViewById(R.id.txt_tanggal_notifikasi);
        }
    }

    @NonNull
    @Override
    public NotifikasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_notifikasi,parent,false);
        return new AdapterNotifikasi.NotifikasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiViewHolder holder, int i) {
        holder.txtNameNotifikasi.setText(dataList.get(i).getName());
        holder.txtDate.setText(dataList.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
