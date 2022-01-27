
package com.easyfoodvone.charity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyfoodvone.R;
import com.easyfoodvone.app_ui.binding.UIVariationCharityDonationRow;
import com.easyfoodvone.app_ui.databinding.RowPreviousDonationBinding;
import com.easyfoodvone.app_ui.databinding.RowPreviousDonationPhoneBinding;
import com.easyfoodvone.charity.webservice.responsebean.CharityInfoBean;
import com.easyfoodvone.utility.Helper;


import java.util.List;


public class PreviousCharityAdapter extends RecyclerView.Adapter<PreviousCharityAdapter.ViewResource> {

    public interface PreviousMealListener {
        void onItemClick(int position);
        void onCancel(int position);
        void onYes(int position);
        void onNo(int position);
    }

    private final Activity activity;
    private final PreviousMealListener previousMealListener;
    private final List<CharityInfoBean.MealDonatedBean.PreviousMealsBean> previousMealsBeans;
    private final boolean isPhone;

    public PreviousCharityAdapter(
            Activity activity,
            PreviousMealListener previousMealListener,
            List<CharityInfoBean.MealDonatedBean.PreviousMealsBean> previousMealsBeans,
            boolean isPhone) {
        this.activity = activity;
        this.previousMealListener = previousMealListener;
        this.previousMealsBeans = previousMealsBeans;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public PreviousCharityAdapter.ViewResource onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final UIVariationCharityDonationRow<?> ui;
        if (isPhone) {
            ui = new UIVariationCharityDonationRow.Phone(RowPreviousDonationPhoneBinding.inflate(inflater, parent, false));
        } else {
            ui = new UIVariationCharityDonationRow.Tablet(RowPreviousDonationBinding.inflate(inflater, parent, false));
        }

        return new ViewResource(ui);
    }

    @Override
    public void onBindViewHolder(@NonNull final PreviousCharityAdapter.ViewResource holder, int position) {
        final CharityInfoBean.MealDonatedBean.PreviousMealsBean mealBeanItem = previousMealsBeans.get(position);

        if (Integer.parseInt(mealBeanItem.getNo_of_meals()) > 1) {
            holder.ui.getTvMeals().setText(mealBeanItem.getNo_of_meals() + " Meals");
        } else {
            holder.ui.getTvMeals().setText(mealBeanItem.getNo_of_meals() + " Meal");
        }

        holder.ui.getTvDate().setText(Helper.formatDate(mealBeanItem.getCreated_at()));
        if (mealBeanItem.getCharity_meal_request() != null && mealBeanItem.getCharity_meal_request().size() > 0) {
            if (mealBeanItem.getIs_collected() == 1 && mealBeanItem.getCharity_meal_request().get(0).getIs_accepted() == 1) {
                holder.ui.getRlFoodCollected().setVisibility(View.GONE);
                holder.ui.getTvStatus().setText(activity.getResources().getString(R.string.collected));
                holder.ui.getTvStatus().setBackground(null);
            } else if (mealBeanItem.getIs_collected() == 0 && mealBeanItem.getCharity_meal_request().get(0).getIs_accepted() == 1) {
                holder.ui.getRlFoodCollected().setVisibility(View.VISIBLE);
                holder.ui.getTvStatus().setText(activity.getResources().getString(R.string.accepted));
                holder.ui.getTvStatus().setBackground(null);
            } else if (mealBeanItem.getIs_collected() == 2 && mealBeanItem.getCharity_meal_request().get(0).getIs_accepted() == 1) {
                holder.ui.getTvStatus().setText(activity.getResources().getString(R.string.not_collected));
                holder.ui.getTvStatus().setBackground(null);
                holder.ui.getRlFoodCollected().setVisibility(View.GONE);
            } else if (mealBeanItem.getIs_collected() == 3) {
                holder.ui.getRlFoodCollected().setVisibility(View.GONE);
                holder.ui.getTvStatus().setText(activity.getResources().getString(R.string.cancelled));
                holder.ui.getTvStatus().setBackground(null);
            }
        } else {
            if (mealBeanItem.getIs_collected() == 0) {
                holder.ui.getRlFoodCollected().setVisibility(View.GONE);
                holder.ui.getTvStatus().setText(activity.getResources().getString(R.string.cancel));
            } else if (mealBeanItem.getIs_collected() == 3) {
                holder.ui.getRlFoodCollected().setVisibility(View.GONE);
                holder.ui.getTvStatus().setText(activity.getResources().getString(R.string.cancelled));
                holder.ui.getTvStatus().setBackground(null);
            }
        }

        holder.ui.getCvNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMealListener.onNo(holder.getAdapterPosition());
            }
        });

        holder.ui.getCvYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMealListener.onYes(holder.getAdapterPosition());
            }
        });

        holder.ui.getTvStatus().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ui.getTvStatus().getText().toString().trim().equals(activity.getResources().getString(R.string.cancel))) {
                    previousMealListener.onCancel(holder.getAdapterPosition());
                }
            }
        });

        holder.ui.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return previousMealsBeans == null ? 0 : previousMealsBeans.size();
    }

    public static class ViewResource extends RecyclerView.ViewHolder {
        public final UIVariationCharityDonationRow<?> ui;

        ViewResource(UIVariationCharityDonationRow<?> ui) {
            super(ui.getBinding().getRoot());
            this.ui = ui;
        }
    }
}
