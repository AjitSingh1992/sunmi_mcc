package com.easyfoodvone.app_ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyfoodvone.app_common.utility.NewConstants;
import com.easyfoodvone.app_common.ws.TempModel;
import com.easyfoodvone.app_ui.R;

import java.util.List;

public class ComboProductAdapter extends RecyclerView.Adapter<ComboProductAdapter.Views> {

    private final LayoutInflater inflater;
    private final List<TempModel> data;
    private final boolean isPhone;

    public ComboProductAdapter(LayoutInflater inflater, List<TempModel> data, boolean isPhone) {
        this.inflater = inflater;
        this.data = data;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public Views onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layout = isPhone ? R.layout.row_offers_combo_selected_items_phone : R.layout.row_offers_combo_selected_items;
        return new Views(inflater.inflate(layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Views holder, int position) {
        if (!data.get(position).getProduct_price().equals("")
                && !data.get(position).getProduct_name().equals("")
                && !data.get(position).getQuantity().equals("")) {
            holder.name.setText(data.get(position).getProduct_name());
            holder.each.setText(NewConstants.POUND + data.get(position).getProduct_price() + "");
            holder.qty.setText(data.get(position).getQuantity());
            holder.total.setText(NewConstants.POUND + Double.parseDouble(data.get(position).getPrice())+ "");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class Views extends RecyclerView.ViewHolder {
        private final TextView name, each, qty, total;

        public Views(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            each = itemView.findViewById(R.id.each);
            qty = itemView.findViewById(R.id.qty);
            total = itemView.findViewById(R.id.total);
        }
    }
}
