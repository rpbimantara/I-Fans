package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTerupdate extends RecyclerView.Adapter<AdapterTerupdate.TerupdateViewHolder>{
    ArrayList<Terupdate> dataList;

    public AdapterTerupdate(ArrayList<Terupdate> dataList) {
        this.dataList = dataList;
    }

    public class TerupdateViewHolder extends RecyclerView.ViewHolder{
        public TextView txtHeadlineTerupdate;

        public TerupdateViewHolder(View itemView) {
            super(itemView);
            this.txtHeadlineTerupdate = (TextView) itemView.findViewById(R.id.txt_hot_headline);
        }
    }

    @NonNull
    @Override
    public TerupdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_hot_news,parent,false);
        view.setMinimumWidth(parent.getMeasuredWidth());
        return new TerupdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TerupdateViewHolder holder, int position) {
        holder.txtHeadlineTerupdate.setText(dataList.get(position).getTxtheadlineterupdate());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
