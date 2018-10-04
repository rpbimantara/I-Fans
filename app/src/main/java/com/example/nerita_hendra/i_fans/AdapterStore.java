package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterStore extends RecyclerView.Adapter<AdapterStore.StoreViewHolder> {

    private ArrayList<Store> dataList;

    public AdapterStore(ArrayList<Store> dataList) {
        this.dataList = dataList;
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNamaBarang,txtHargaBarang;

        public StoreViewHolder(View itemView) {
            super(itemView);
            this.txtNamaBarang = (TextView) itemView.findViewById(R.id.txt_namaBarangStore);
            this.txtHargaBarang = (TextView) itemView.findViewById(R.id.txt_hargaBarangStore);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStore.StoreViewHolder holder, int position) {
        holder.txtNamaBarang.setText(dataList.get(position).getNamabarang());
        holder.txtHargaBarang.setText(dataList.get(position).getHargabarang());
    }


    @NonNull
    @Override
    public AdapterStore.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_store,parent,false);
        return new StoreViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
