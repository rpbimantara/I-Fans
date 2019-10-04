package com.alpha.test.persebayaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;

public class AdapterTeamStaff extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Team> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterTeamStaff(ArrayList<Team> dataList) {
        this.dataList = dataList;
    }

    public class TeamStaffViewHolder extends RecyclerView.ViewHolder{
        public TextView txtname;
        public ImageView image;

        public TeamStaffViewHolder(View itemView) {
            super(itemView);
            this.txtname = itemView.findViewById(R.id.txt_namastaff);
            this.image = itemView.findViewById(R.id.image_staff);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TeamStaffViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_team_staff, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Staff list is empty!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            TeamStaffViewHolder holder = ((TeamStaffViewHolder) viewHolder);
            holder.txtname.setText(dataList.get(position).getNama());
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
