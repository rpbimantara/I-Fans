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

public class AdapterTiketList extends RecyclerView.Adapter<AdapterTiketList.TiketListViewHolder> {
    private ArrayList<TiketList> dataList;

    public AdapterTiketList(ArrayList<TiketList> dataList) {
        this.dataList = dataList;
    }

    public class TiketListViewHolder extends RecyclerView.ViewHolder{
        public TextView event_type_id,txtname;
        public ImageView image;

        public TiketListViewHolder(View itemView) {
            super(itemView);
            event_type_id = itemView.findViewById(R.id.txt_category_tiket_list);
            txtname = itemView.findViewById(R.id.txt_nama_tiket_list);
            image = itemView.findViewById(R.id.image_tiket_list);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TiketListViewHolder holder, int position) {
        holder.event_type_id.setText(dataList.get(position).event_type_id);
        holder.txtname.setText(dataList.get(position).name);
        if (!dataList.get(position).image.equalsIgnoreCase("false")) {
            holder.image.setImageBitmap(StringToBitMap(dataList.get(position).image));
        }
    }

    @NonNull
    @Override
    public TiketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tiket_list,parent,false);
        return new TiketListViewHolder(view);
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
