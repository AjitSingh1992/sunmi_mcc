/*Created by Omnisttechhub Solution*/
package com.easyfoodvone.charity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyfoodvone.app_ui.binding.UIVariationDonateTimeRow;
import com.easyfoodvone.app_ui.databinding.RowDonateTimeBinding;
import com.easyfoodvone.app_ui.databinding.RowDonateTimePhoneBinding;

import java.util.List;

/*for setting item for the recycler view for   */
public class DonationTimeAdapter extends RecyclerView.Adapter<DonationTimeAdapter.ViewResource> {

    public interface RecyclerItemListener {
        void onItemClick(int position);
    }

    private Activity activity;
    private int selectedPos = -1;
    private List<String> donationTimeList;
    private RecyclerItemListener recyclerItemListener;
    private final boolean isPhone;

    public DonationTimeAdapter(Activity activity, List<String> donationTimeList, RecyclerItemListener recyclerItemListener, boolean isPhone) {
        this.activity = activity;
        this.donationTimeList = donationTimeList;
        this.recyclerItemListener = recyclerItemListener;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public DonationTimeAdapter.ViewResource onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final UIVariationDonateTimeRow<?> ui;
        if (isPhone) {
            ui = new UIVariationDonateTimeRow.Phone(RowDonateTimePhoneBinding.inflate(inflater, parent, false));
        } else {
            ui = new UIVariationDonateTimeRow.Tablet(RowDonateTimeBinding.inflate(inflater, parent, false));
        }

        return new DonationTimeAdapter.ViewResource(ui);
    }

    @Override
    public void onBindViewHolder(@NonNull final DonationTimeAdapter.ViewResource holder, final int position) {
        if (position == donationTimeList.size() - 1) {
            holder.ui.getTvTime().setText("10 hrs+");
        } else {
            holder.ui.getTvTime().setText(donationTimeList.get(position) + " hrs");
        }

        holder.ui.getTvTime().setSelected(position == selectedPos);
        holder.ui.getTvTime().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos = position;
                if (selectedPos >= 0)
                    notifyDataSetChanged();

                recyclerItemListener.onItemClick(selectedPos);
            }
        });

        holder.ui.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return donationTimeList == null ? 0 : donationTimeList.size();
    }

    public static class ViewResource extends RecyclerView.ViewHolder {
        public final UIVariationDonateTimeRow<?> ui;

        ViewResource(UIVariationDonateTimeRow<?> ui) {
            super(ui.getBinding().getRoot());
            this.ui = ui;
        }
    }
}
