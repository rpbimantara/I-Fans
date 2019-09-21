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

public class AdapterCheckout extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Checkout> dataList;
    private CheckoutListener mListener;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new CheckoutViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_checkout, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Your Cart is empty!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == VIEW_TYPE_NORMAL) {
            final CheckoutViewHolder Tholder = ((CheckoutViewHolder) viewHolder);
            Tholder.txt_name.setText(dataList.get(i).getNama());
            Tholder.txt_harga.setText(dataList.get(i).getHarga());
            Tholder.img_item.setImageBitmap(StringToBitMap(dataList.get(i).getImage()));
            mListener.CheckoutCallback(dataList.get(i),Integer.valueOf(dataList.get(i).getQty()),"load");
            if (dataList.get(i).type.equalsIgnoreCase("lelang")){
                Tholder.quantityView.setVisibility(View.INVISIBLE);
            }else{
                Tholder.quantityView.setVisibility(View.VISIBLE);
                if (dataList.get(i).getType().equalsIgnoreCase("service")){
                    Tholder.quantityView.setMaxQuantity(4);
                }else{
                    Tholder.quantityView.setMaxQuantity(Integer.valueOf(dataList.get(i).getStock()));
                }
                Tholder.quantityView.setQuantity(Integer.valueOf(dataList.get(i).getQty()));

            }
            Tholder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.CheckoutDeleted(dataList.get(i));
                }
            });
            Tholder.quantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
                @Override
                public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                    mListener.CheckoutCallback(dataList.get(i),Tholder.quantityView.getQuantity(),"onchange");
                }

                @Override
                public void onLimitReached() {

                }
            });
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(i);
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull final CheckoutViewHolder holder, final int position) {

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
//    }
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
