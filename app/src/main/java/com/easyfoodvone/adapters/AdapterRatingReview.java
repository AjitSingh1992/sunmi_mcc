package com.easyfoodvone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyfoodvone.app_ui.databinding.RowRatingsListBinding;
import com.easyfoodvone.models.RatingResponse;

import java.util.List;

public class AdapterRatingReview extends RecyclerView.Adapter<AdapterRatingReview.MyViewHolder> {

    private final List<RatingResponse.Data.UserRatingsList> user_review_ratings;
    private final RatingCommentClickListner ratingCommentClickListner;

    public interface RatingCommentClickListner {
        void onReplyClicked(int position, MyViewHolder holder, RatingResponse.Data.UserRatingsList data);
        void onThankyouClicked(int position, MyViewHolder holder, RatingResponse.Data.UserRatingsList data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowRatingsListBinding binding;

        public MyViewHolder(RowRatingsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public AdapterRatingReview(List<RatingResponse.Data.UserRatingsList> user_review_ratings, RatingCommentClickListner ratingCommentClickListner) {
        this.user_review_ratings = user_review_ratings;
        this.ratingCommentClickListner = ratingCommentClickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRatingsListBinding binding = RowRatingsListBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RatingResponse.Data.UserRatingsList rating = user_review_ratings.get(position);

        holder.binding.btnSendThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingCommentClickListner.onThankyouClicked(holder.getAdapterPosition(), holder, rating);
            }
        });
        holder.binding.btnSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingCommentClickListner.onReplyClicked(holder.getAdapterPosition(), holder, rating);
            }
        });

        holder.binding.name.setText(rating.getCustomer_name());
        holder.binding.date.setText(rating.getRating_date());
        holder.binding.ratingBarFood.setRating(Float.parseFloat(rating.getFood_quality_rating()));
        holder.binding.ratingBarOnTime.setRating(Float.parseFloat(rating.getDelivery_rating()));
        holder.binding.ratingBarOrderAgain.setRating(Float.parseFloat(rating.getOrder_again_rating()));
        holder.binding.ratingBarRecom.setRating(Float.parseFloat(rating.getRecommendation_rating()));
        holder.binding.ratingBarOverall.setRating(Float.parseFloat(rating.getOverall_rating()));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return user_review_ratings.size();
    }
}