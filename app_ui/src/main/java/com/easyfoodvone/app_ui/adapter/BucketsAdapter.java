package com.easyfoodvone.app_ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyfoodvone.app_common.ws.BucketDataModel;
import com.easyfoodvone.app_ui.R;

import java.util.List;

public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.ViewInitializer> {

    public interface OnAdapterItemClickListener {
        void onDeleteClick(int position,ViewInitializer holder);
    }

    private final @NonNull List<BucketDataModel> bucketDataModels;
    private final @NonNull OnAdapterItemClickListener onAdapterItemClickListener;
    private final @NonNull LayoutInflater layoutInflater;
    private final boolean isPhone;

    public BucketsAdapter(
            @NonNull List<BucketDataModel> bucketDataModels,
            @NonNull LayoutInflater layoutInflater,
            @NonNull OnAdapterItemClickListener onAdapterItemClickListener,
            boolean isPhone) {
        this.bucketDataModels = bucketDataModels;
        this.layoutInflater = layoutInflater;
        this.onAdapterItemClickListener = onAdapterItemClickListener;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public ViewInitializer onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layout = isPhone
                ? R.layout.row_offers_spend_x_get_discount_bucket_phone
                : R.layout.row_offers_spend_x_get_discount_bucket;

        return new ViewInitializer(layoutInflater.inflate(layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewInitializer holder, final int position) {
        holder.and.setText(bucketDataModels.get(position).getAnd());
        holder.between.setText(bucketDataModels.get(position).getBetween());
        holder.discount.setText(bucketDataModels.get(position).getDetDiscount());
        holder.deleteBucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                onAdapterItemClickListener.onDeleteClick(position, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bucketDataModels.size();
    }

    public static class ViewInitializer extends RecyclerView.ViewHolder {
        final TextView between, and, discount;
        final ImageView deleteBucket;

        public ViewInitializer(@NonNull View itemView) {
            super(itemView);
            between = itemView.findViewById(R.id.edit_between);
            and = itemView.findViewById(R.id.edit_and);
            discount = itemView.findViewById(R.id.edit_give_discount);
            deleteBucket = itemView.findViewById(R.id.deleteBucket);
        }
    }
}