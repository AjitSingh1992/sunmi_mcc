package com.easyfoodvone.menu_details.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.easyfoodvone.R;
import com.easyfoodvone.models.MenuProductDetails;

import java.util.List;

public class EditMenuProductModifierAdapter extends RecyclerView.Adapter<EditMenuProductModifierAdapter.MenuProductModifierViews> {
    private final Context context;
    private final MenuProductDetails details;
    private final boolean isPhone;

    public EditMenuProductModifierAdapter(Context context, MenuProductDetails details, boolean isPhone) {
        this.context = context;
        this.details = details;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public MenuProductModifierViews onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new MenuProductModifierViews(inflater.inflate(
                isPhone ? R.layout.row_menu_edit_product_detail_modifier_phone
                        : R.layout.row_menu_edit_product_detail_modifier,
                viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuProductModifierViews holder, int i) {
        holder.modifierName.setText(details.getData().getProduct_modifiers().get(i).getModifier_name());
        holder.modifierName.setTextColor(context.getResources().getColor(R.color.black));

        holder.modifierProductList.setLayoutManager(new LinearLayoutManager(context));
        holder.modifierProductList.setAdapter(
                new ProductModifierSizeAdapter(context, details.getData().getProduct_modifiers().get(i).getModifier_products(), isPhone));
    }

    @Override
    public int getItemCount() {
        return details.getData().getProduct_modifiers().size();
    }

    public class MenuProductModifierViews extends RecyclerView.ViewHolder {
        final TextView modifierName;
        final RecyclerView modifierProductList;

        public MenuProductModifierViews(@NonNull View itemView) {
            super(itemView);

            modifierName = itemView.findViewById(R.id.modifireName);
            modifierProductList = itemView.findViewById(R.id.modifireProductList);
        }
    }

    public class ProductModifierSizeAdapter extends RecyclerView.Adapter<ProductModifierSizeAdapter.ModifierSizeViews> {
        private final Context context;
        private final List<MenuProductDetails.ModifierProducts> modifier_products;
        private final boolean isPhone;

        public ProductModifierSizeAdapter(Context context, List<MenuProductDetails.ModifierProducts> modifier_products, boolean isPhone) {
            this.context = context;
            this.modifier_products = modifier_products;
            this.isPhone = isPhone;
        }

        @NonNull
        @Override
        public ModifierSizeViews onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);

            return new ModifierSizeViews(inflater.inflate(
                    isPhone ? R.layout.row_menu_edit_product_detail_child_of_modifier_or_size_sub_modifier_phone
                            : R.layout.row_menu_edit_product_detail_child_of_modifier_or_size_sub_modifier,
                    viewGroup,
                    false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ModifierSizeViews holder, final int position) {
            holder.modifierName.setText(modifier_products.get(position).getProduct_name());
            holder.modifierPrice.setText(modifier_products.get(position).getSell_price());
            holder.modifierPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equalsIgnoreCase("")) {
                        modifier_products.get(position).setSell_price("0.0");
                    } else {
                        modifier_products.get(position).setSell_price(s.toString());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return modifier_products.size();
        }

        class ModifierSizeViews extends RecyclerView.ViewHolder {
            final TextView modifierName;
            final EditText modifierPrice;

            public ModifierSizeViews(@NonNull View itemView) {
                super(itemView);
                modifierName = itemView.findViewById(R.id.modifireName);
                modifierPrice = itemView.findViewById(R.id.modifirePrice);
            }
        }
    }
}
