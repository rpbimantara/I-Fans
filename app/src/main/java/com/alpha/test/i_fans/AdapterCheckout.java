package com.alpha.test.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import me.himanshusoni.quantityview.QuantityView;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;

public class AdapterCheckout extends RecyclerView.Adapter<AdapterCheckout.CheckoutViewHolder>{
    private ArrayList<Checkout> dataList;
    private CheckoutListener mListener;

    public interface CheckoutListener { // create an interface
        void CheckoutCallback(Checkout checkout, Integer jumlah,String mode); // create callback function
        void CheckoutDeleted(Checkout checkout); // create callback function
    }

    public AdapterCheckout(ArrayList<Checkout> dataList, CheckoutListener mListener) {
        this.dataList = dataList;
        this.mListener = mListener;
    }


    public class CheckoutViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_name,txt_harga;
        public Button btn_delete;
        public QuantityView quantityView;
        public ImageView img_item;
        public CheckoutViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_checkout_item);
            txt_harga = itemView.findViewById(R.id.txt_checkout_harga);
            quantityView = itemView.findViewById(R.id.quantityView_default);
            img_item = itemView.findViewById(R.id.checkout_image);
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
        holder.img_item.setImageBitmap(StringToBitMap(dataList.get(position).getImage()));
        mListener.CheckoutCallback(dataList.get(position),Integer.valueOf(dataList.get(position).getQty()),"load");
        if (dataList.get(position).type.equalsIgnoreCase("lelang")){
            holder.quantityView.setVisibility(View.INVISIBLE);
        }else{
            holder.quantityView.setVisibility(View.VISIBLE);
            if (dataList.get(position).getType().equalsIgnoreCase("service")){
                holder.quantityView.setMaxQuantity(4);
            }else{
                holder.quantityView.setMaxQuantity(Integer.valueOf(dataList.get(position).getStock()));
            }
            holder.quantityView.setQuantity(Integer.valueOf(dataList.get(position).getQty()));

        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.CheckoutDeleted(dataList.get(position));
            }
        });
        holder.quantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                mListener.CheckoutCallback(dataList.get(position),holder.quantityView.getQuantity(),"onchange");
            }

            @Override
            public void onLimitReached() {

            }
        });
//        final Integer qty_awal = Integer.valueOf(dataList.get(position).getQty());
//        holder.btn_min.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (qty_awal == 1){
//                    holder.btn_min.setEnabled(false);
//                }else {
//                    holder.btn_min.setEnabled(true);
//                    String qty = String.valueOf(qty_awal - 1);
//                    holder.et_qty.setText(qty);
//                }
//            }
//        });
//        final Integer stock = Integer.valueOf(dataList.get(position).getStock());
//
//        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (qty_awal > stock){
//                    holder.btn_plus.setEnabled(false);
//                }else {
//                    holder.btn_plus.setEnabled(true);
//                    String qty = String.valueOf(qty_awal + 1);
//                    holder.et_qty.setText(qty);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

}
