package com.example.nerita_hendra.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCheckout extends RecyclerView.Adapter<AdapterCheckout.CheckoutViewHolder>{
    private ArrayList<Checkout> dataList;

    public AdapterCheckout(ArrayList<Checkout> dataList) {
        this.dataList = dataList;
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_name,txt_harga;
        public EditText et_qty;
        public Button btn_min,btn_plus,btn_delete;
        public ImageView img_item;
        public CheckoutViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_checkout_item);
            txt_harga = itemView.findViewById(R.id.txt_checkout_harga);
            et_qty = itemView.findViewById(R.id.editText_qty);
            img_item = itemView.findViewById(R.id.checkout_image);
            btn_min = itemView.findViewById(R.id.button_minus);
            btn_plus = itemView.findViewById(R.id.button_plus);
            btn_delete = itemView.findViewById(R.id.button_delete_checkout);
        }
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_checkout,parent,false);
        return new  CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckoutViewHolder holder, final int position) {
        holder.txt_name.setText(dataList.get(position).getNama());
        holder.txt_harga.setText(dataList.get(position).getHarga());
        holder.et_qty.setText(dataList.get(position).getQty());
        holder.img_item.setImageBitmap(StringToBitMap(dataList.get(position).getImage()));
        final Integer qty_awal = Integer.valueOf(dataList.get(position).getQty());
        holder.btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty_awal < 2){
                    holder.btn_min.setEnabled(false);
                }else {
                    String qty = String.valueOf(qty_awal - 1);
                    holder.et_qty.setText(qty);
                }
            }
        });
        final Integer stock = Integer.valueOf(dataList.get(position).getStock());
        System.out.println(String.valueOf(stock)+"SDASDSADDDDDDDDDDDDDDDDD");
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty_awal > stock){
                    holder.btn_plus.setEnabled(false);
                }else {
                    String qty = String.valueOf(qty_awal + 1);
                    holder.et_qty.setText(qty);
                }
            }
        });
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
