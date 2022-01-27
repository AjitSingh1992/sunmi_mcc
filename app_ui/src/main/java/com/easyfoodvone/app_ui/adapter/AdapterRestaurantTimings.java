package com.easyfoodvone.app_ui.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.easyfoodvone.app_common.ws.AllDaysRestaurantTiming;
import com.easyfoodvone.app_ui.R;

import java.util.List;

public class AdapterRestaurantTimings extends RecyclerView.Adapter<AdapterRestaurantTimings.MyViewHolder> {
    private final @NonNull List<AllDaysRestaurantTiming.Data> allDaysList;
    private final @NonNull OnAdapterItemClickListener onAdapterItemClickListener;
    private final boolean isPhone;

    public AdapterRestaurantTimings(
            @NonNull OnAdapterItemClickListener onAdapterItemClickListener,
            @NonNull List<AllDaysRestaurantTiming.Data> allDaysList,
            boolean isPhone) {
        this.allDaysList = allDaysList;
        this.isPhone = isPhone;
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }

    public interface OnAdapterItemClickListener {
        void onAddClick(int position, @NonNull AllDaysRestaurantTiming.Data timings);
        void onEditClick(int position, @Nullable AllDaysRestaurantTiming.Data.TimingData timings);
        void onDeleteClick(int position, @Nullable AllDaysRestaurantTiming.Data.TimingData timings);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView daysName;
        final TextView openingTime;
        final TextView collectionTime;
        final TextView deliveryTime;
        final ImageView addTiming;
        final ImageView editTiming;
        final ImageView deleteTiming;

        public MyViewHolder(View view) {
            super(view);
            daysName = view.findViewById(R.id.restaurant_timings_item_day_title);
            openingTime = view.findViewById(R.id.restaurant_timings_item_opening_time);
            collectionTime = view.findViewById(R.id.restaurant_timings_item_collection_time);
            deliveryTime = view.findViewById(R.id.restaurant_timings_item_delivery_time);
            addTiming = view.findViewById(R.id.restaurant_timings_btn_add);
            editTiming = view.findViewById(R.id.restaurant_timings_btn_edit);
            deleteTiming = view.findViewById(R.id.restaurant_timings_btn_del);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        @LayoutRes int layoutId = isPhone ? R.layout.row_restaurant_timings_list_phone : R.layout.row_restaurant_timings_list;

        return new MyViewHolder(inflater.inflate(layoutId, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        AllDaysRestaurantTiming.Data currentDay = allDaysList.get(position);
        holder.daysName.setText(currentDay.getDay());

        if (currentDay.getData().size() > 0) {
            holder.addTiming.setVisibility(View.GONE);
            holder.editTiming.setVisibility(View.VISIBLE);
            holder.deleteTiming.setVisibility(View.VISIBLE);

            AllDaysRestaurantTiming.Data.TimingData timing = currentDay.getData().get(0);

            holder.collectionTime.setText(timing.getCollection_start_time() + "-" + timing.getCollection_end_time());
            holder.openingTime.setText(timing.getOpening_start_time() + "-" + timing.getOpening_end_time());
            holder.deliveryTime.setText(timing.getDelivery_start_time() + "-" + timing.getDelivery_end_time());

            holder.editTiming.setOnClickListener(v -> {
                int positionNow = holder.getAdapterPosition();
                onAdapterItemClickListener.onEditClick(positionNow, currentDay.getData().get(0));
            });

            holder.deleteTiming.setOnClickListener(v -> {
                int positionNow = holder.getAdapterPosition();
                onAdapterItemClickListener.onDeleteClick(positionNow, currentDay.getData().get(0));
            });

        } else {
            holder.collectionTime.setText("");
            holder.openingTime.setText("");
            holder.deliveryTime.setText("");

            holder.editTiming.setVisibility(View.GONE);
            holder.deleteTiming.setVisibility(View.GONE);
            holder.addTiming.setVisibility(View.VISIBLE);

            holder.addTiming.setOnClickListener(v -> {
                int positionNow = holder.getAdapterPosition();
                onAdapterItemClickListener.onAddClick(positionNow, currentDay);
            });
        }
    }

    @Override
    public int getItemCount() {
        return allDaysList.size();
    }
}