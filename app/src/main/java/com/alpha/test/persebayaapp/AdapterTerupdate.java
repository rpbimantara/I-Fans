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

public class AdapterTerupdate extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<Terupdate> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterTerupdate(ArrayList<Terupdate> dataList) {
        this.dataList = dataList;
    }

    public class TerupdateViewHolder extends RecyclerView.ViewHolder{
        public TextView txtHeadlineTerupdate;
        public ImageView imageterupdate;

        public TerupdateViewHolder(View itemView) {
            super(itemView);
            this.imageterupdate =  itemView.findViewById(R.id.hot_news_image);
            this.txtHeadlineTerupdate = (TextView) itemView.findViewById(R.id.txt_hot_headline);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TerupdateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_hot_news, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false), "OOPS!", "Hot News List is empty!!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            TerupdateViewHolder holder = ((TerupdateViewHolder) viewHolder);
            holder.imageterupdate.setImageBitmap(StringToBitMap(dataList.get(position).getImageTerupdate()));
            holder.txtHeadlineTerupdate.setText(dataList.get(position).getHeadline());
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
