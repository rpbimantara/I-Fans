package com.alpha.test.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTicketBarcode extends RecyclerView.Adapter<AdapterTicketBarcode.TiketBarcodeViewHolder> {
    private ArrayList<TicketBarcode> dataList;
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
    public void onBindViewHolder(@NonNull TiketBarcodeViewHolder holder, int position) {
        holder.event_name.setText(dataList.get(position).event_name);
        holder.date.setText(dataList.get(position).date_open);
        holder.ticket_type.setText(dataList.get(position).ticket_type);
        holder.txtname.setText(dataList.get(position).name);
        if (!dataList.get(position).barcode.equalsIgnoreCase("false")) {
            holder.image.setImageBitmap(StringToBitMap(dataList.get(position).barcode));
        }
    }

    @NonNull
    @Override
    public TiketBarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tiket_barcode,parent,false);
        return new TiketBarcodeViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }


    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
