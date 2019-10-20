package com.alpha.test.persebayaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.formater;




public class AdapterHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<History> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;
    ShippingListener scListener;

    public interface ShippingListener{
        void sendCallback(Integer line_id);
        void receivedCalback(Integer line_id);
    }



    public AdapterHistory(ArrayList<History> dataList, ShippingListener scListener) {
        this.dataList = dataList;
        this.scListener = scListener;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_item,txt_price,txt_unit,txt_total,txt_state,txt_name,txt_date,txt_order_name;
        public ImageView img_item;
        public Button btn_received,btn_send;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            img_item = itemView.findViewById(R.id.history_image);
            txt_item = itemView.findViewById(R.id.txt_history_item);
            txt_price = itemView.findViewById(R.id.txt_history_harga);
            txt_unit = itemView.findViewById(R.id.txt_history_jumlah);
            txt_total = itemView.findViewById(R.id.txt_history_total);
            txt_state = itemView.findViewById(R.id.txt_history_state);
            txt_name = itemView.findViewById(R.id.txt_history_name);
            txt_date = itemView.findViewById(R.id.txt_history_date);
            txt_order_name = itemView.findViewById(R.id.txt_order_name);
            btn_received = itemView.findViewById(R.id.btn_received);
            btn_send = itemView.findViewById(R.id.btn_send);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_history, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","History list is empty!");
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == VIEW_TYPE_NORMAL) {
            final AdapterHistory.HistoryViewHolder Tholder = ((AdapterHistory.HistoryViewHolder) viewHolder);
            Tholder.txt_item.setText(dataList.get(i).getProduct_name());
            Tholder.txt_price.setText(formater(Float.parseFloat(dataList.get(i).getHarga())));
            Tholder.txt_unit.setText(dataList.get(i).getQty());
            Tholder.txt_total.setText(formater(Float.parseFloat(dataList.get(i).getHarga()) * Float.parseFloat(dataList.get(i).getQty())));
            if (dataList.get(i).getState().equalsIgnoreCase("Purchase")){
                Tholder.txt_state.setText("Seller");
                if (dataList.get(i).is_received == true){
                    Tholder.btn_received.setVisibility(View.INVISIBLE);
                }
                Tholder.btn_send.setVisibility(View.INVISIBLE);
                Tholder.btn_received.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scListener.receivedCalback(Integer.valueOf(dataList.get(i).getId()));
                    }
                });
            }else{
                Tholder.txt_state.setText("Customer");
                Tholder.btn_received.setVisibility(View.INVISIBLE);
                if (dataList.get(i).is_send == true){
                    Tholder.btn_send.setVisibility(View.INVISIBLE);
                }
                Tholder.btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scListener.sendCallback(Integer.valueOf(dataList.get(i).getId()));
                    }
                });
            }
            Tholder.txt_name.setText(dataList.get(i).getOwner());
            Tholder.txt_date.setText(dataList.get(i).getDate());
            Tholder.img_item.setImageBitmap(StringToBitMap(dataList.get(i).getImage()));
            Tholder.txt_order_name.setText(dataList.get(i).getOrder_name());
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(i);
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

