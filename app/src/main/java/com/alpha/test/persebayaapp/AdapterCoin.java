package com.alpha.test.persebayaapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class AdapterCoin extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<AccountCoin> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new CoinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_coin, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Your coin history is empty!");
        }
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_coin,parent,false);
//        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            CoinViewHolder Tholder = ((CoinViewHolder) viewHolder);
            Tholder.txtName.setText(dataList.get(position).getName());
            Tholder.txtDate.setText(dataList.get(position).getDate());
            if (dataList.get(position).getType().equalsIgnoreCase("customer") || dataList.get(position).getType().equalsIgnoreCase("tax")){
                Tholder.txtPrice.setText("-"+dataList.get(position).getPrice());
                Tholder.txtPrice.setTextColor(Color.RED);
            }else {
                Tholder.txtPrice.setText("+"+dataList.get(position).getPrice());
                Tholder.txtPrice.setTextColor(Color.GREEN);
            }
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

