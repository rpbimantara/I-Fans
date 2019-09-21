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

public class AdapterTiketList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TiketList> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterTiketList(ArrayList<TiketList> dataList) {
        this.dataList = dataList;
    }

    public class TiketListViewHolder extends RecyclerView.ViewHolder{
        public TextView event_type_id,txtname;
        public ImageView image;

        public TiketListViewHolder(View itemView) {
            super(itemView);
            event_type_id = itemView.findViewById(R.id.txt_category_tiket_list);
            txtname = itemView.findViewById(R.id.txt_nama_tiket_list);
            image = itemView.findViewById(R.id.image_tiket_list);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            TiketListViewHolder holder = ((TiketListViewHolder) viewHolder);
            holder.event_type_id.setText(dataList.get(position).event_type_id);
            holder.txtname.setText(dataList.get(position).name);
            if (!dataList.get(position).image.equalsIgnoreCase("false")) {
                holder.image.setImageBitmap(StringToBitMap(dataList.get(position).image));
            }
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TiketListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tiket_list, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","This player doesn't have Rating and Review yet!");
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
