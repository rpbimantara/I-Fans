package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterStoreVariant extends RecyclerView.Adapter<AdapterStoreVariant.VariantViewHolder>{
    private ArrayList<Variant> dataList;

    public AdapterStoreVariant(ArrayList<Variant> dataList) {
        this.dataList = dataList;
    }

    public class VariantViewHolder extends RecyclerView.ViewHolder{
        public TextView txtVariant,txtStock;

        public VariantViewHolder(View itemView) {
            super(itemView);
            this.txtVariant = itemView.findViewById(R.id.txt_variant);
            this.txtStock = itemView.findViewById(R.id.txt_stock_variant);
        }
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_store_variant,parent,false);
        return new VariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariantViewHolder holder, int position) {
       holder.txtVariant.setText(dataList.get(position).getVariant_text());
       holder.txtStock.setText(dataList.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
