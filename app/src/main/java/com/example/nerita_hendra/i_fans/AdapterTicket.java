package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterTicket extends RecyclerView.Adapter<AdapterTicket.TicketViewHolder> {
    private ArrayList<Tiket> dataList;

    public AdapterTicket( ArrayList<Tiket> dataList) {
        this.dataList = dataList;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder{
        public TextView kategoriTiket,hargaTiket;
        public RadioButton rb0seat,rb1seat,rb2seat;
        public RadioGroup rgseat;
        public TicketViewHolder(View itemView) {
            super(itemView);
            kategoriTiket = itemView.findViewById(R.id.txt_kategori_tiket);
            hargaTiket = itemView.findViewById(R.id.txt_harga_tiket);
//            rb0seat = itemView.findViewById(R.id.radioButton_0seat);
            rb1seat = itemView.findViewById(R.id.radioButton_1seat);
            rb2seat = itemView.findViewById(R.id.radioButton_2seat);
            rgseat = itemView.findViewById(R.id.radioGrup_seat);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final TicketViewHolder holder, final int position) {
        holder.kategoriTiket.setText(dataList.get(position).getKategoriTiket());
        holder.hargaTiket.setText(dataList.get(position).getHargaTiket());
        holder.rgseat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int jumlahtiket=0;
                switch (i){
//                    case R.id.radioButton_0seat:
//                        jumlahtiket = 0;
//                        break;
                    case R.id.radioButton_1seat:
                        jumlahtiket = 1;
                        break;
                    case R.id.radioButton_2seat:
                        jumlahtiket = 2;
                        break;
                }
                System.out.println(dataList.get(position).getId().toString() +" : "+holder.kategoriTiket.getText().toString() +" - "+ jumlahtiket);
            }
        });
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tiket,parent,false);
        return new TicketViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

}
