package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTicket extends RecyclerView.Adapter<AdapterTicket.TicketViewHolder> {
    int count = 0;
    int maxtiket = 2;
    private ArrayList<Tiket> dataList;

    public AdapterTicket( ArrayList<Tiket> dataList) {
        this.dataList = dataList;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder{
        public TextView kategoriTiket,hargaTiket;
        public EditText jumlahTiket;
        public Button btn_plus,btn_minus;
        public TicketViewHolder(View itemView) {
            super(itemView);
            kategoriTiket = itemView.findViewById(R.id.txt_kategori_tiket);
            hargaTiket = itemView.findViewById(R.id.txt_harga_tiket);
            jumlahTiket = itemView.findViewById(R.id.editText_number_tiket);
            btn_plus = itemView.findViewById(R.id.button_up);
            btn_minus = itemView.findViewById(R.id.button_down);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final TicketViewHolder holder, int position) {
        holder.kategoriTiket.setText(dataList.get(position).getKategoriTiket());
        holder.hargaTiket.setText(dataList.get(position).getHargaTiket());
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count+1;
                holder.jumlahTiket.setText(String.valueOf(count));
                if (count >= 2){
                    holder.btn_plus.setEnabled(false);
                    holder.btn_minus.setEnabled(true);
                }
            }
        });
        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count-1;
                holder.jumlahTiket.setText(String.valueOf(count));
                if (count <= 0){
                    holder.btn_minus.setEnabled(false);
                    holder.btn_plus.setEnabled(true);
                }
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

    public Integer tambah(Integer count,Integer maxtiket){
        count = ++count;
        return count;
    }

    public Integer kurang(Integer count,Integer maxtiket){
        count = --count;
        return count;
    }
}
