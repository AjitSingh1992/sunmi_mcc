package com.easyfoodvone.app_ui.adapter;

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
import com.easyfoodvone.app_common.utility.NewConstants;
import com.easyfoodvone.app_common.viewdata.DataRowRestaurantMenuDetails;
import com.easyfoodvone.app_common.ws.MenuCategoryItemsResponse;
import com.easyfoodvone.app_ui.binding.UIVariationRestaurantMenuDetailsItemRow;
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuDetailsBinding;
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuDetailsPhoneBinding;
import com.easyfoodvone.app_ui.view.ViewRestaurantMenuDetail;

import java.util.List;

public class AdapterItemsList extends RecyclerView.Adapter<AdapterItemsList.ItemViewHolder> {

    private final LifecycleSafe lifecycle;
    private final List<MenuCategoryItemsResponse.Items> itemList;
    private final ObservableField<Boolean> wholeCategoryActive;
    private final ParentInterface parentInterface;
    private final LayoutInflater inflater;
    private final boolean isPhone;

    public interface ParentInterface {
        void onSetProductActive(MenuCategoryItemsResponse.Items menuCategories, boolean isActive);
        void onEditClicked(MenuCategoryItemsResponse.Items items);
        void showToast(@NonNull String message);
        void startReorderDrag(@NonNull RecyclerView.ViewHolder item);
    }

    public AdapterItemsList(
            LifecycleSafe lifecycle,
            ParentInterface parentInterface,
            LayoutInflater inflater,
            List<MenuCategoryItemsResponse.Items> itemList,
            ObservableField<Boolean> wholeCategoryActive,
            boolean isPhone) {
        this.lifecycle = lifecycle;
        this.parentInterface = parentInterface;
        this.itemList = itemList;
        this.wholeCategoryActive = wholeCategoryActive;
        this.inflater = inflater;
        this.isPhone = isPhone;

        lifecycle.addObserverOnceUntilDestroy(wholeCategoryActive, wholeCategoryActiveChangeListener, false);
    }

    private final Observer<Boolean> wholeCategoryActiveChangeListener = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isActive) {
            // All rows need to rebind to reflect the greyed out state of the parent
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final UIVariationRestaurantMenuDetailsItemRow<?> ui;
        if (isPhone) {
            ui = new UIVariationRestaurantMenuDetailsItemRow.Phone(
                    RowRestaurantMenuDetailsPhoneBinding.inflate(inflater, parent, false));
        } else {
            ui = new UIVariationRestaurantMenuDetailsItemRow.Tablet(
                    RowRestaurantMenuDetailsBinding.inflate(inflater, parent, false));
        }

        DataHolder dataHolder = new DataHolder();
        return new ItemViewHolder(ui, dataHolder.data, lifecycle);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int positionNow) {
        MenuCategoryItemsResponse.Items bindItem = itemList.get(positionNow);

        holder.data.getOutputEvents().bind(positionNow);

        holder.ui.getBinding().executePendingBindings();
    }

    private class DataHolder {
        private final @NonNull DataRowRestaurantMenuDetails data;

        private @Nullable MenuCategoryItemsResponse.Items item = null;

        public DataHolder() {
            data = new DataRowRestaurantMenuDetails(
                    new ObservableField<>(true),
                    new ObservableField<>(""),
                    new ObservableField<>(null),
                    new ObservableField<>(""),
                    new ObservableField<>(true),
                    new ObservableField<>(DataRowRestaurantMenuDetails.ToggleUI.UNCLICKABLE_IDLE),
                    new ObservableField<>(false),
                    new ObservableField<>(false),
                    viewEventHandler);
        }

        private final DataRowRestaurantMenuDetails.OutputEvents viewEventHandler = new DataRowRestaurantMenuDetails.OutputEvents() {
            @Override
            public void bind(int adapterPosition) {
                item = itemList.get(adapterPosition);

                data.getName().set(item.getMenu_product_name());
                data.getPrice().set(NewConstants.POUND + item.getMenu_product_price());

                if (wholeCategoryActive.get()) {
                    boolean itemActive = item.getActive().equals("1");
                    data.getToggleIsOn().set(itemActive);
                    data.getToggleUI().set(DataRowRestaurantMenuDetails.ToggleUI.CLICKABLE_IDLE);
                    data.isEditClickable().set(true);

                } else {
                    data.getToggleIsOn().set(false);
                    data.getToggleUI().set(DataRowRestaurantMenuDetails.ToggleUI.UNCLICKABLE_IDLE);
                    data.isEditClickable().set(false);
                }
            }

            @Override
            public void onClickToggle() {
                boolean isOn = data.getToggleIsOn().get();
                if (isOn) {
                    parentInterface.onSetProductActive(item, false);
                } else if (wholeCategoryActive.get()) {
                    parentInterface.onSetProductActive(item, true);
                } else {
                    parentInterface.showToast("Please turn on your menu category first");
                }
            }

            @Override
            public void onClickEdit() {
                parentInterface.onEditClicked(item);
            }

            @Override
            public void onTouchDownDragHandle(@NonNull RecyclerView.ViewHolder viewHolder) {
                parentInterface.startReorderDrag(viewHolder);
            }
        };
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public final UIVariationRestaurantMenuDetailsItemRow<?> ui;
        public final DataRowRestaurantMenuDetails data;

        public ItemViewHolder(UIVariationRestaurantMenuDetailsItemRow<?> ui, DataRowRestaurantMenuDetails data, LifecycleSafe lifecycle) {
            super(ui.getBinding().getRoot());

            this.ui = ui;
            this.data = data;

            ui.setData(data, new ViewRestaurantMenuDetail.Formatter(), lifecycle);

            ui.getDragHandle().setOnTouchListener(dragHandleOnTouchListener);
        }

        @Override
        public void onItemSelected() {
            data.getDragInProgress().set(true);
        }

        @Override
        public void onItemClear() {
            data.getDragInProgress().set(false);
        }

        private final View.OnTouchListener dragHandleOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    data.getOutputEvents().onTouchDownDragHandle(ItemViewHolder.this);
                }
                return false;
            }
        };
    }
}