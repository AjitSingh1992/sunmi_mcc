package com.easyfoodvone.app_ui.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.separation.ObservableUtils;
import com.easyfoodvone.app_common.viewdata.DataRowRestaurantMenu;
import com.easyfoodvone.app_common.ws.MenuCategoryList;
import com.easyfoodvone.app_ui.R;
import com.easyfoodvone.app_ui.binding.UIVariationRestaurantMenuCategoryRow;
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuCategoryBinding;
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuCategoryPhoneBinding;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MenuGridAdapter extends RecyclerView.Adapter<MenuGridAdapter.ItemViewHolder> {

    public interface ParentInterface {
        void openMenuDetails(@NonNull MenuCategoryList.MenuCategories item);
        void startReorderDrag(@NonNull RecyclerView.ViewHolder item);
    }

    private final ParentInterface parentInterface;
    private final LifecycleSafe lifecycle;
    private final List<MenuCategoryList.MenuCategories> menuCategories;
    private final LayoutInflater inflater;
    private final boolean isPhone;

    public MenuGridAdapter(
            ParentInterface parentInterface,
            LifecycleSafe lifecycle,
            List<MenuCategoryList.MenuCategories> menuCategories,
            LayoutInflater inflater,
            boolean isPhone) {
        this.parentInterface = parentInterface;
        this.lifecycle = lifecycle;
        this.menuCategories = menuCategories;
        this.inflater = inflater;
        this.isPhone = isPhone;
    }

    private final DataRowRestaurantMenu.OutputEvents rowOutputEvents = new DataRowRestaurantMenu.OutputEvents() {
        @Override
        public void onDragToReorder(RecyclerView.ViewHolder viewHolder) {
            parentInterface.startReorderDrag(viewHolder);
        }

        @Override
        public void onClickCategory(int adapterPosition) {
            parentInterface.openMenuDetails(menuCategories.get(adapterPosition));
        }
    };

    @Override
    @NonNull
    public MenuGridAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final UIVariationRestaurantMenuCategoryRow<?> ui;
        if (isPhone) {
            ui = new UIVariationRestaurantMenuCategoryRow.Phone(
                    RowRestaurantMenuCategoryPhoneBinding.inflate(inflater, parent, false));
        } else {
            ui = new UIVariationRestaurantMenuCategoryRow.Tablet(
                    RowRestaurantMenuCategoryBinding.inflate(inflater, parent, false));
        }

        DataRowRestaurantMenu data = new DataRowRestaurantMenu(
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(false),
                rowOutputEvents);

        return new ItemViewHolder(ui, data, lifecycle);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        @Nullable MenuCategoryList.MenuCategories itemAtPosition = menuCategories.get(position);

        //holder.data.getImageUrl().set(itemAtPosition.getMenu_category_image());
        holder.data.getTitle().set(itemAtPosition.getMenu_category_name());

        String count = itemAtPosition.getNumber_of_menu_product();
        holder.data.getItemCount().set("1".equals(count) ? "1 item" : (count + " items"));

        holder.ui.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return menuCategories.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public final UIVariationRestaurantMenuCategoryRow<?> ui;
        public final DataRowRestaurantMenu data;
        //private final Drawable placeholder;

        public ItemViewHolder(UIVariationRestaurantMenuCategoryRow<?> ui, DataRowRestaurantMenu data, LifecycleSafe lifecycle) {
            super(ui.getBinding().getRoot());

            this.ui = ui;
            this.data = data;
            //this.placeholder = ui.getImageView().getResources().getDrawable(R.color.bg_grey);

            ui.setData(data, lifecycle);

            // TODO unbind on destroy

            ui.getBinding().getRoot().setOnClickListener((v) -> data.getOutputEvents().onClickCategory(getAdapterPosition()));
            ui.getDragHandle().setOnTouchListener(dragHandleOnTouchListener);
            //data.getImageUrl().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getImageUrl(), imageURLObserver));
        }

        @Override
        public void onItemSelected() {
            data.getDragInProgress().set(true);
        }

        @Override
        public void onItemClear() {
            data.getDragInProgress().set(false);
        }

        /*private final Observer<String> imageURLObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String url) {
                if (TextUtils.isEmpty(url)) {
                    Picasso.get().cancelRequest(ui.getImageView());
                    ui.getImageView().setImageDrawable(null);
                } else {
                    Picasso.get().load(url)
                            .placeholder(placeholder)
                            .error(R.drawable.restaurant_icon)
                            .into(ui.getImageView());
                }
            }
        };*/

        private final View.OnTouchListener dragHandleOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    data.getOutputEvents().onDragToReorder(ItemViewHolder.this);
                }
                return false;
            }
        };
    }
}
