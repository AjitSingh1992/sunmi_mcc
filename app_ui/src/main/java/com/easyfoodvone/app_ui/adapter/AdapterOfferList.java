package com.easyfoodvone.app_ui.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.easyfoodvone.app_common.ws.OffersResponse;
import com.easyfoodvone.app_ui.R;

import java.util.List;

public class AdapterOfferList extends RecyclerView.Adapter<AdapterOfferList.MyViewHolder> {

    public interface OnActionButtonClick {
        void onClick(int position, @NonNull OffersResponse.Data.Offers item);
    }

    private final List<OffersResponse.Data.Offers> offersLists;
    private final OnActionButtonClick onEditButtonClick;
    private final OnActionButtonClick onDeleteButtonClick;
    private final boolean isPhone;

    public AdapterOfferList(
            List<OffersResponse.Data.Offers> offersLists,
            OnActionButtonClick onEditButtonClick,
            OnActionButtonClick onDeleteButtonClick,
            boolean isPhone) {
        this.offersLists = offersLists;
        this.onEditButtonClick = onEditButtonClick;
        this.onDeleteButtonClick = onDeleteButtonClick;
        this.isPhone = isPhone;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView offer_name, start_end_date, day_of_week, min_order_amt;
        final Button edit, del;

        public MyViewHolder(View view) {
            super(view);

            offer_name = view.findViewById(R.id.offer_name);
            start_end_date = view.findViewById(R.id.start_end_date);
            day_of_week = view.findViewById(R.id.day_of_week);
            min_order_amt = view.findViewById(R.id.min_order_amt);
            edit = view.findViewById(R.id.btn_edit);
            del = view.findViewById(R.id.btn_del);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        @LayoutRes int layoutId = isPhone ? R.layout.row_offers_list_phone : R.layout.row_offers_list;

        return new MyViewHolder(inflater.inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OffersResponse.Data.Offers offer = offersLists.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditButtonClick.onClick(holder.getAdapterPosition(), offer);
            }
        });

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonClick.onClick(holder.getAdapterPosition(), offer);
            }
        });

        holder.day_of_week.setText(offer.getDays_available());
        // holder.min_order_amt.setText("NA");
        holder.min_order_amt.setText(offer.getSpend_amount_to_avail_offer());
        holder.start_end_date.setText(offer.getStart_date() + "\n" + offer.getEnd_date());

        String offerType = "discount_percentage".equalsIgnoreCase(offer.getOffer_type()) ? "%"
                : "flat_offer".equalsIgnoreCase(offer.getOffer_type()) ? "£ Off"
                : offer.getOffer_type();
        holder.offer_name.setText(offer.getOffer_title() + "\n(" + offerType + ")");
    }

    @Override
    public int getItemCount() {
        return offersLists.size();
    }
}