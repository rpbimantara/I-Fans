package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdapterListBerita extends RecyclerView.Adapter<AdapterListBerita.BeritaViewHolder> {

    private ArrayList<ListBerita> dataList;

    public AdapterListBerita(ArrayList<ListBerita> dataList){
        this.dataList = dataList;
    }

    public class BeritaViewHolder extends RecyclerView.ViewHolder{
        public TextView txtKategori,txtHeadline,txtTanggal;

        public BeritaViewHolder(View itemView) {
            super(itemView);
            this.txtKategori = (TextView) itemView.findViewById(R.id.txt_kategori);
            this.txtHeadline = (TextView) itemView.findViewById(R.id.txt_headline);
            this.txtTanggal = (TextView) itemView.findViewById(R.id.txt_tanggal);
        }
    }


    @NonNull
    @Override
    public AdapterListBerita.BeritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_berita,parent,false);
        return new BeritaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaViewHolder holder, int position) {
        holder.txtKategori.setText(dataList.get(position).getKategori());
        holder.txtHeadline.setText(dataList.get(position).getHeadline());
        holder.txtTanggal.setText(dataList.get(position).getTanggal());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
