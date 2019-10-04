package com.alpha.test.persebayaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;

public class AdapterTicketBarcode extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TicketBarcode> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;
    public AdapterTicketBarcode(ArrayList<TicketBarcode> dataList) {
        this.dataList = dataList;
    }

    public class TiketBarcodeViewHolder extends RecyclerView.ViewHolder{
        public TextView ticket_type,txtname,event_name,date;
        public ImageView image;

        public TiketBarcodeViewHolder(View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.txt_nama_event_tiket);
            date = itemView.findViewById(R.id.txt_tanggal_event);
            image = itemView.findViewById(R.id.image_tiket_barcode);
            ticket_type = itemView.findViewById(R.id.txt_tipe_tiket);
            txtname = itemView.findViewById(R.id.txt_nama_tiket);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            TiketBarcodeViewHolder holder = ((TiketBarcodeViewHolder) viewHolder);
            holder.event_name.setText(dataList.get(position).event_name);
            holder.date.setText(dataList.get(position).date_open);
            holder.ticket_type.setText(dataList.get(position).ticket_type);
            holder.txtname.setText(dataList.get(position).name);
            if (!dataList.get(position).barcode.equalsIgnoreCase("false")) {
                holder.image.setImageBitmap(StringToBitMap(dataList.get(position).barcode));
            }
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TiketBarcodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tiket_barcode, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Your ticket is empty!");
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
