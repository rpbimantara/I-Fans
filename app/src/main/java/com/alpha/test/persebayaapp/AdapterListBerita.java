package com.alpha.test.persebayaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListBerita extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ListBerita> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;


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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BeritaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_berita, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","News List is empty!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            BeritaViewHolder holder = ((BeritaViewHolder) viewHolder);
            holder.txtKategori.setText(dataList.get(position).getKategori());
            holder.txtHeadline.setText(dataList.get(position).getHeadline());
            holder.txtTanggal.setText(dataList.get(position).getTanggal());
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
