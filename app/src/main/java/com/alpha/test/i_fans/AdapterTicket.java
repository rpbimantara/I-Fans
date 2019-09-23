package com.alpha.test.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTicket extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Tiket> dataList;
    private TicketListener mListener;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public interface TicketListener { // create an interface
        void onChangeButtonTicket(Tiket tiket,int jumlah); // create callback function
    }

    public AdapterTicket( ArrayList<Tiket> dataList,TicketListener mListener) {
        this.dataList = dataList;
        this.mListener = mListener;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder{
        public TextView kategoriTiket,hargaTiket;
        public RadioButton rb0seat,rb1seat,rb2seat;
        public RadioGroup rgseat;
        public TicketViewHolder(View itemView) {
            super(itemView);
            kategoriTiket = itemView.findViewById(R.id.txt_kategori_tiket);
            hargaTiket = itemView.findViewById(R.id.txt_harga_tiket);
            rb0seat = itemView.findViewById(R.id.radioButton_0seat);
            rb1seat = itemView.findViewById(R.id.radioButton_1seat);
            rb2seat = itemView.findViewById(R.id.radioButton_2seat);
            rgseat = itemView.findViewById(R.id.radioGrup_seat);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            TicketViewHolder holder = ((TicketViewHolder) viewHolder);
            holder.kategoriTiket.setText(dataList.get(position).getKategoriTiket());
            holder.hargaTiket.setText(dataList.get(position).getHargaTiket());
            holder.rgseat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i){
                        case R.id.radioButton_0seat:
                            mListener.onChangeButtonTicket(dataList.get(position),0);
                            break;
                        case R.id.radioButton_1seat:
                            mListener.onChangeButtonTicket(dataList.get(position),1);
                            break;
                        case R.id.radioButton_2seat:
                            mListener.onChangeButtonTicket(dataList.get(position),2);
                            break;
                    }
                }
            });
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TicketViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tiket, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Ticket List is empty!");
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
