package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterKlasemen extends RecyclerView.Adapter<AdapterKlasemen.KlasemenViewHolder>{

    ArrayList<Klasemen> dataList;

    public AdapterKlasemen(ArrayList<Klasemen> dataList) {
        this.dataList = dataList;
    }

    public class KlasemenViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNoUrut,txtTeamKlasemen,txtPlayKlasemen,txtSelisihGol,txtPoint;

        public KlasemenViewHolder(View itemView) {
            super(itemView);
            this.txtNoUrut = (TextView) itemView.findViewById(R.id.txt_urutklasemen);
            this.txtTeamKlasemen = (TextView) itemView.findViewById(R.id.txt_teamklasemen);
            this.txtPlayKlasemen = (TextView) itemView.findViewById(R.id.txt_playklasemen);
            this.txtSelisihGol = (TextView) itemView.findViewById(R.id.txt_selisihgol);
            this.txtPoint = (TextView) itemView.findViewById(R.id.txt_point);
        }
    }

    @NonNull
    @Override
    public KlasemenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_klasemen,parent,false);
        return new KlasemenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KlasemenViewHolder holder, int position) {
        holder.txtNoUrut.setText(dataList.get(position).getTxtNoUrut());
        holder.txtTeamKlasemen.setText(dataList.get(position).getTxtTeamKlasemen());
        holder.txtPlayKlasemen.setText(dataList.get(position).getTxtPlayKlasemen());
        holder.txtSelisihGol.setText(dataList.get(position).getTxtSelisihGol());
        holder.txtPoint.setText(dataList.get(position).getTxtPoint());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
