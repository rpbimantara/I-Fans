package com.alpha.test.i_fans;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterCoin extends RecyclerView.Adapter<AdapterCoin.CoinViewHolder> {
    ArrayList<AccountCoin> dataList;

    public AdapterCoin(ArrayList<AccountCoin> dataList) {
        this.dataList = dataList;
    }

    public class CoinViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName,txtDate,txtPrice;

        public CoinViewHolder(View itemView) {
            super(itemView);
            this.txtName = (TextView) itemView.findViewById(R.id.txt_name);
            this.txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            this.txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
        }
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_coin,parent,false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtDate.setText(dataList.get(position).getDate());
        if (dataList.get(position).getType().equalsIgnoreCase("customer") || dataList.get(position).getType().equalsIgnoreCase("tax")){
            holder.txtPrice.setText("-"+dataList.get(position).getPrice());
            holder.txtPrice.setTextColor(Color.RED);
        }else {
            holder.txtPrice.setText("+"+dataList.get(position).getPrice());
            holder.txtPrice.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

}

