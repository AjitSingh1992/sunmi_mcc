package com.easyfoodvone.app_ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.easyfoodvone.app_common.utility.NewConstants;
import com.easyfoodvone.app_common.ws.OrderReportResponse;
import com.easyfoodvone.app_ui.R;

import java.util.List;

public class AdapterReportOrderList extends RecyclerView.Adapter<AdapterReportOrderList.MyViewHolder> {

    private final List<OrderReportResponse.OrdersList> order_list;
    private final boolean isPhone;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout layoutRow;
        final TextView order_number, postcode, date, totalItems, amount;

        public MyViewHolder(View view) {
            super(view);

            layoutRow = view.findViewById(R.id.layout_row);
            order_number = view.findViewById(R.id.order_number);
            postcode = view.findViewById(R.id.postcode);
            date = view.findViewById(R.id.date);
            totalItems = view.findViewById(R.id.totalItems);
            amount = view.findViewById(R.id.amount);
        }
    }

    public AdapterReportOrderList(List<OrderReportResponse.OrdersList> order_list, boolean isPhone) {
        this.order_list = order_list;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(isPhone ? R.layout.row_report_order_phone : R.layout.row_report_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        @Nullable OrderReportResponse.OrdersList order = order_list.get(position);

        holder.amount.setText(NewConstants.POUND + order.getOrder_total());
        holder.order_number.setText(order.getOrder_id().substring(order.getOrder_id().length() - 8, order.getOrder_id().length()));
        holder.date.setText(order.getOrder_date());
        holder.totalItems.setText(order.getTotal_items());
        holder.postcode.setText(order.getCustomer_post_code());

        // TODO Order number to use as a lookup for showing order detail
    }

    @Override
    public int getItemCount() {
        return order_list.size();
    }
}