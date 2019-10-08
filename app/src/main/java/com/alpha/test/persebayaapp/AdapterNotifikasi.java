package com.alpha.test.persebayaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterNotifikasi extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Notifikasi> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterNotifikasi(ArrayList<Notifikasi> dataList) {
        this.dataList = dataList;
    }

    public class NotifikasiViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNameNotifikasi,txtDate,txtBody;

        public NotifikasiViewHolder(View itemView) {
            super(itemView);
            this.txtNameNotifikasi = (TextView) itemView.findViewById(R.id.txt_name_notifikasi);
            this.txtDate = (TextView) itemView.findViewById(R.id.txt_tanggal_notifikasi);
            this.txtBody = itemView.findViewById(R.id.txt_body_notifikasi);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new NotifikasiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_notifikasi, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Your Notification is empty!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            NotifikasiViewHolder holder = ((NotifikasiViewHolder) viewHolder);
            holder.txtNameNotifikasi.setText(dataList.get(position).getName());
            holder.txtBody.setText(Html.fromHtml(dataList.get(position).getBody()));
            holder.txtDate.setText(dataList.get(position).getDate());
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
