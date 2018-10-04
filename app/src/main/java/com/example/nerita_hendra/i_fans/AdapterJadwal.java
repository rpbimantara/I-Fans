package com.example.nerita_hendra.i_fans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterJadwal extends RecyclerView.Adapter<AdapterJadwal.JadwalViewHolder> {
    ArrayList<Jadwal> dataList;

    public AdapterJadwal(ArrayList<Jadwal> dataList) {
        this.dataList = dataList;
    }

    public class JadwalViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNamaTeam,txtNamaLiga,txtTglMain,txtNamaStadion,txtWaktuMain;

        public JadwalViewHolder(View itemView) {
            super(itemView);
            this.txtNamaTeam = (TextView) itemView.findViewById(R.id.txt_namateamjadwal);
            this.txtNamaLiga = (TextView) itemView.findViewById(R.id.txt_namaliga);
            this.txtTglMain = (TextView) itemView.findViewById(R.id.txt_tglmain);
            this.txtNamaStadion = (TextView) itemView.findViewById(R.id.txt_namastadion);
            this.txtWaktuMain = (TextView) itemView.findViewById(R.id.txt_waktumain);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalViewHolder holder, int position) {
        holder.txtNamaTeam.setText(dataList.get(position).getNamateam());
        holder.txtNamaLiga.setText(dataList.get(position).getNamaliga());
        holder.txtTglMain.setText(dataList.get(position).getTglmain());
        holder.txtNamaStadion.setText(dataList.get(position).getNamastadion());
        holder.txtWaktuMain.setText(dataList.get(position).getWaktumain());
    }

    @NonNull
    @Override
    public JadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_jadwal,parent,false);
        return new JadwalViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
