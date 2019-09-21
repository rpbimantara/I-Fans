package com.alpha.test.i_fans;

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

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;

public class AdapterJadwal extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Jadwal> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            JadwalViewHolder Tholder = ((JadwalViewHolder) viewHolder);
            Tholder.txtNamaTeam.setText(dataList.get(position).getNamateam());
            Tholder.txtNamaLiga.setText(dataList.get(position).getNamaliga());
            Tholder.txtTglMain.setText(dataList.get(position).getTglmain());
            Tholder.txtNamaStadion.setText(dataList.get(position).getNamastadion());
            Tholder.txtWaktuMain.setText(dataList.get(position).getWaktumain());
            Tholder.imageClub.setImageBitmap(StringToBitMap(dataList.get(position).getFototeam()));
            Tholder.imageStatus.setImageResource(dataList.get(position).getStatusimage());
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new JadwalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_jadwal, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","This club doesn't have schedule yet!");
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
