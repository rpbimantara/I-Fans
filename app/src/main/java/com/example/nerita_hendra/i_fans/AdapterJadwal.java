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

public class AdapterJadwal extends RecyclerView.Adapter<AdapterJadwal.JadwalViewHolder> {
    ArrayList<Jadwal> dataList;

    public AdapterJadwal(ArrayList<Jadwal> dataList) {
        this.dataList = dataList;
    }

    public class JadwalViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNamaTeam,txtNamaLiga,txtTglMain,txtNamaStadion,txtWaktuMain;
        public ImageView imageClub, imageStatus;

        public JadwalViewHolder(View itemView) {
            super(itemView);
            this.txtNamaTeam = (TextView) itemView.findViewById(R.id.txt_namateamjadwal);
            this.txtNamaLiga = (TextView) itemView.findViewById(R.id.txt_namaliga);
            this.txtTglMain = (TextView) itemView.findViewById(R.id.txt_tglmain);
            this.txtNamaStadion = (TextView) itemView.findViewById(R.id.txt_namastadion);
            this.txtWaktuMain = (TextView) itemView.findViewById(R.id.txt_waktumain);
            this.imageClub = (ImageView) itemView.findViewById(R.id.jadwal_image);
            this.imageStatus = (ImageView) itemView.findViewById(R.id.status_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalViewHolder holder, int position) {
        holder.txtNamaTeam.setText(dataList.get(position).getNamateam());
        holder.txtNamaLiga.setText(dataList.get(position).getNamaliga());
        holder.txtTglMain.setText(dataList.get(position).getTglmain());
        holder.txtNamaStadion.setText(dataList.get(position).getNamastadion());
        holder.txtWaktuMain.setText(dataList.get(position).getWaktumain());
        holder.imageClub.setImageBitmap(StringToBitMap(dataList.get(position).getFototeam()));
        holder.imageStatus.setImageResource(dataList.get(position).getStatusimage());
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
