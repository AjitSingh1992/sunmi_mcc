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

public class EditMenuProductSizeAdapter extends RecyclerView.Adapter<EditMenuProductSizeAdapter.MenuProductSizeViews> {
    private final Context context;
    private final MenuProductDetails details;
    private final boolean isPhone;

    public EditMenuProductSizeAdapter(Context context, MenuProductDetails details, boolean isPhone) {
        this.context = context;
        this.details = details;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public MenuProductSizeViews onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MenuProductSizeViews(inflater.inflate(
                isPhone ? R.layout.row_menu_edit_product_detail_size_phone
                        : R.layout.row_menu_edit_product_detail_size,
                viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuProductSizeViews holder, final int i) {
        holder.menuName.setText(details.getData().getMenu_product_size().get(i).getSize_name());
        holder.menuName.setTextColor(context.getResources().getColor(R.color.black));
        holder.menuPrice.setText(details.getData().getMenu_product_size().get(i).getSize_price());
        holder.menuPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")) {
                    details.getData().getMenu_product_size().get(i).setSize_price("0");
                } else {
                    details.getData().getMenu_product_size().get(i).setSize_price(s.toString());
                }
            }
        });

        holder.modifiersList.setLayoutManager(new LinearLayoutManager(context));

        if (details.getData().getMenu_product_size().get(i).getSize_modifiers() != null) {
            holder.modifiersList.setAdapter(
                    new SizeModifierAdapter(context, details.getData().getMenu_product_size().get(i).getSize_modifiers(), isPhone));
        }
    }

    @Override
    public int getItemCount() {
        return details.getData().getMenu_product_size().size();
    }

    public class MenuProductSizeViews extends RecyclerView.ViewHolder {
        final TextView menuName;
        final EditText menuPrice;
        final RecyclerView modifiersList;

        public MenuProductSizeViews(@NonNull View itemView) {
            super(itemView);

            menuName = itemView.findViewById(R.id.sizeName);
            menuPrice = itemView.findViewById(R.id.price);
            modifiersList = itemView.findViewById(R.id.modifiresList);
        }
    }

    //TODO::      Size Modifier Adapter.............

    public class SizeModifierAdapter extends RecyclerView.Adapter<SizeModifierAdapter.SizeModifierViews> {
        private final Context context;
        private final List<MenuProductDetails.SizeModifiers> size_modifiers;
        private final boolean isPhone;

        public SizeModifierAdapter(Context context, List<MenuProductDetails.SizeModifiers> size_modifiers, boolean isPhone) {
            this.context = context;
            this.size_modifiers = size_modifiers;
            this.isPhone = isPhone;
        }

        @NonNull
        @Override
        public SizeModifierViews onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);

            return new SizeModifierViews(inflater.inflate(
                    isPhone ? R.layout.row_menu_edit_product_detail_size_sub_modifier_phone
                            : R.layout.row_menu_edit_product_detail_size_sub_modifier,
                    viewGroup,
                    false));
        }

        @Override
        public void onBindViewHolder(@NonNull SizeModifierViews holder, int position) {
            holder.sizeModifierName.setText(size_modifiers.get(position).getModifier_name());
            holder.sizeModifierName.setTextColor(context.getResources().getColor(R.color.black));
            holder.sizeModifiersProductsList.setLayoutManager(new LinearLayoutManager(context));

            if (size_modifiers.get(position).getSize_modifier_products() != null) {
                holder.sizeModifiersProductsList.setAdapter(
                        new SizeModifiersproductAdapter(context, size_modifiers.get(position).getSize_modifier_products(), isPhone));
            }
        }

        @Override
        public int getItemCount() {
            return size_modifiers.size();
        }

        public class SizeModifierViews extends RecyclerView.ViewHolder {
            final TextView sizeModifierName;
            final RecyclerView sizeModifiersProductsList;

            public SizeModifierViews(@NonNull View itemView) {
                super(itemView);
                sizeModifierName = itemView.findViewById(R.id.sizeName);
                sizeModifiersProductsList = itemView.findViewById(R.id.modifiresList);
            }
        }
    }


    //TODO::      Size Modifier Product Adapter.............
    public class SizeModifiersproductAdapter extends RecyclerView.Adapter<SizeModifiersproductAdapter.SizeModifierProductViews> {
        private final Context context;
        private final List<MenuProductDetails.SizeModifierProducts> size_modifier_products;
        private final boolean isPhone;

        public SizeModifiersproductAdapter(Context context, List<MenuProductDetails.SizeModifierProducts> size_modifier_products, boolean isPhone) {
            this.context = context;
            this.size_modifier_products = size_modifier_products;
            this.isPhone = isPhone;
        }

        @NonNull
        @Override
        public SizeModifierProductViews onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);

            return new SizeModifierProductViews(inflater.inflate(
                    isPhone ? R.layout.row_menu_edit_product_detail_child_of_modifier_or_size_sub_modifier_phone
                            : R.layout.row_menu_edit_product_detail_child_of_modifier_or_size_sub_modifier,
                    viewGroup,
                    false));
        }

        @Override
        public void onBindViewHolder(@NonNull final SizeModifierProductViews holder, final int position) {
            holder.modifierName.setText(size_modifier_products.get(position).getProduct_name());
            holder.modifierPrice.setText(size_modifier_products.get(position).getSell_price());
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
                        size_modifier_products.get(position).setSell_price("0.0");
                    } else {
                        size_modifier_products.get(position).setSell_price(s.toString());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return size_modifier_products.size();
        }

        public class SizeModifierProductViews extends RecyclerView.ViewHolder {
            final TextView modifierName;
            final EditText modifierPrice;

            public SizeModifierProductViews(@NonNull View itemView) {
                super(itemView);
                modifierName = itemView.findViewById(R.id.modifireName);
                modifierPrice = itemView.findViewById(R.id.modifirePrice);
            }
        }
    }
}
