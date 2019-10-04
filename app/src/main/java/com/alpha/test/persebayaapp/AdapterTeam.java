package com.alpha.test.persebayaapp;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;

public class AdapterTeam extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Team> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterTeam(ArrayList<Team> dataList) {
        this.dataList = dataList;
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView txtname,txtNoPunggung;
        public ImageView image;
        public Bitmap icon;
        public FloatingActionButton fab_icon;

        public TeamViewHolder(View itemView) {
            super(itemView);
            this.txtname = itemView.findViewById(R.id.txt_namastaff);
            this.image = itemView.findViewById(R.id.image_staff);
            this.txtNoPunggung = itemView.findViewById(R.id.txt_nostaff);
            this.fab_icon = itemView.findViewById(R.id.team_fab);
            this.fab_icon.setImageResource(R.drawable.ic_medis);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TeamViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_team, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Players list is empty!!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            TeamViewHolder holder = ((TeamViewHolder) viewHolder);
            holder.txtname.setText(dataList.get(position).getNama());
            if (dataList.get(position).getNo_punggung() == "0"){
                holder.txtNoPunggung.setText("");
            }else {
                holder.txtNoPunggung.setText(dataList.get(position).getNo_punggung());
            }
            if(dataList.get(position).getPosisi().equalsIgnoreCase("GoalKeeper")){
                holder.fab_icon.setImageResource(R.drawable.ic_keeper);
            }
            if(dataList.get(position).getPosisi().equalsIgnoreCase("Forward")){
                holder.fab_icon.setImageResource(R.drawable.ic_forward);
            }
            if(dataList.get(position).getPosisi().equalsIgnoreCase("Midfielder")){
                holder.fab_icon.setImageResource(R.drawable.ic_midfielder);
            }
            if(dataList.get(position).getPosisi().equalsIgnoreCase("Defender")){
                holder.fab_icon.setImageResource(R.drawable.ic_defender);
            }
            holder.image.setImageBitmap(StringToBitMap(dataList.get(position).getFoto()));
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
