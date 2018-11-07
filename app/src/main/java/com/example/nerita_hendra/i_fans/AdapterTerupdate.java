package com.example.nerita_hendra.i_fans;

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

public class AdapterTerupdate extends RecyclerView.Adapter<AdapterTerupdate.TerupdateViewHolder>{
    ArrayList<Terupdate> dataList;

    public AdapterTerupdate(ArrayList<Terupdate> dataList) {
        this.dataList = dataList;
    }

    public class TerupdateViewHolder extends RecyclerView.ViewHolder{
        public TextView txtHeadlineTerupdate;
        public ImageView imageterupdate;

        public TerupdateViewHolder(View itemView) {
            super(itemView);
            this.imageterupdate =  itemView.findViewById(R.id.hot_news_image);
            this.txtHeadlineTerupdate = (TextView) itemView.findViewById(R.id.txt_hot_headline);
        }
    }

    @NonNull
    @Override
    public TerupdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_hot_news,parent,false);
//        view.setMinimumWidth(parent.getMeasuredWidth());
        return new TerupdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TerupdateViewHolder holder, int position) {
        holder.imageterupdate.setImageBitmap(StringToBitMap(dataList.get(position).getImageTerupdate()));
        holder.txtHeadlineTerupdate.setText(dataList.get(position).getHeadline());
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
