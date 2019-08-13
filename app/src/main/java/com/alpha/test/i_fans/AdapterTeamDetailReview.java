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

public class AdapterTeamDetailReview extends RecyclerView.Adapter<AdapterTeamDetailReview.TeamDetaiReviewViewHolder>{
    private ArrayList<TeamReview> dataList;

    public AdapterTeamDetailReview(ArrayList<TeamReview> dataList) {
        this.dataList = dataList;
    }

    public class TeamDetaiReviewViewHolder extends RecyclerView.ViewHolder{
        public TextView txtReview;
        public RatingBar ratingBarReview;

        public TeamDetaiReviewViewHolder(View itemView) {
            super(itemView);
            this.txtReview = (TextView) itemView.findViewById(R.id.txt_review);
        }
    }

    @NonNull
    @Override
    public TeamDetaiReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_review,parent,false);
        return new TeamDetaiReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamDetaiReviewViewHolder holder, int i) {
        holder.txtReview.setText(dataList.get(i).getReview());
        holder.ratingBarReview.setRating(dataList.get(i).getRating());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
