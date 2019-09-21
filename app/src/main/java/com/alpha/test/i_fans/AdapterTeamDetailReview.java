package com.alpha.test.i_fans;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTeamDetailReview extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<TeamReview> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterTeamDetailReview(ArrayList<TeamReview> dataList) {
        this.dataList = dataList;
    }

    public class TeamDetaiReviewViewHolder extends RecyclerView.ViewHolder{
        public TextView txtReview;
        public RatingBar ratingBarReview;

        public TeamDetaiReviewViewHolder(View itemView) {
            super(itemView);
            this.ratingBarReview = itemView.findViewById(R.id.rating_TeamDetail);
            this.txtReview = (TextView) itemView.findViewById(R.id.txt_review);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new TeamDetaiReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_review, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","This player doesn't have Rating and Review yet!");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (getItemViewType(i) == VIEW_TYPE_NORMAL) {
            TeamDetaiReviewViewHolder Tholder = ((TeamDetaiReviewViewHolder) viewHolder);
            Tholder.txtReview.setText(dataList.get(i).getReview());
            Tholder.ratingBarReview.setRating(Float.valueOf(dataList.get(i).getRating()));
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(i);
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
