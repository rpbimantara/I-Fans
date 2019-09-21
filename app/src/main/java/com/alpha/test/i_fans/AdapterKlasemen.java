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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;

public class AdapterKlasemen extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<Klasemen> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterKlasemen(ArrayList<Klasemen> dataList) {
        this.dataList = dataList;
    }

    public class KlasemenViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNoUrut,txtTeamKlasemen,txtPlayKlasemen,txtSelisihGol,txtPoint;
        public ImageView imageClub;
        public LinearLayout layout;

        public KlasemenViewHolder(View itemView) {
            super(itemView);
            this.txtNoUrut = (TextView) itemView.findViewById(R.id.txt_urutklasemen);
            this.txtTeamKlasemen = (TextView) itemView.findViewById(R.id.txt_teamklasemen);
            this.txtPlayKlasemen = (TextView) itemView.findViewById(R.id.txt_playklasemen);
            this.txtSelisihGol = (TextView) itemView.findViewById(R.id.txt_selisihgol);
            this.txtPoint = (TextView) itemView.findViewById(R.id.txt_point);
            this.imageClub = (ImageView) itemView.findViewById(R.id.klasemen_image);
            this.layout = (LinearLayout) itemView.findViewById(R.id.linearLayout_klasemen);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new KlasemenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_klasemen, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Standings are not ready now!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            KlasemenViewHolder holder = ((KlasemenViewHolder) viewHolder);
            holder.txtNoUrut.setText(dataList.get(position).getTxtNoUrut());
            holder.txtTeamKlasemen.setText(dataList.get(position).getTxtTeamKlasemen());
            holder.txtPlayKlasemen.setText(dataList.get(position).getTxtPlayKlasemen());
            holder.txtSelisihGol.setText(dataList.get(position).getTxtSelisihGol());
            holder.txtPoint.setText(dataList.get(position).getTxtPoint());
            holder.imageClub.setImageBitmap(StringToBitMap(dataList.get(position).getTxtFotoClub()));
            holder.layout.setBackgroundColor(dataList.get(position).getTxtLayoutColor());
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
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
