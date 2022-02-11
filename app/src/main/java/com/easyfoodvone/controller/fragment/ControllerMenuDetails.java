package com.easyfoodvone.controller.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableArrayListMoveable;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantMenuDetails;
import com.easyfoodvone.app_common.viewdata.DataRowRestaurantMenuDetails;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.MenuCategoryItemsResponse;
import com.easyfoodvone.app_common.ws.MenuCategoryList;
import com.easyfoodvone.app_ui.view.ViewRestaurantMenuDetail;
import com.easyfoodvone.dialogs.EditMenuItemsDialog;
import com.easyfoodvone.menu_details.ViewMenuPopupDisableItemTimeChooser;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.models.MenuProductDetails;
import com.easyfoodvone.menu_details.ViewMenuPopupEditCategory;
import com.easyfoodvone.menu_details.ViewMenuPopupEditItemPinCheck;
import com.easyfoodvone.models.OrderRequest;
import com.easyfoodvone.models.OrderRequestForItem;
import com.easyfoodvone.models.menu_response.CategorySwipeModel;
import com.easyfoodvone.models.menu_response.ItemSwipeModel;
import com.easyfoodvone.utility.Helper;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;


public class ControllerMenuDetails extends Fragment {

    private final @NonNull MenuCategoryList.MenuCategories menuCategories;
    private final @NonNull PrefManager prefManager;
    private final @NonNull LoginResponse.Data baseDetails;
    private final boolean isPhone;

    private @Nullable ViewMenuPopupEditCategory viewMenuPopupEditCategory;
    private @Nullable ViewMenuPopupEditItemPinCheck viewMenuPopupEditItemPinCheck;
    private @Nullable ViewMenuPopupDisableItemTimeChooser viewMenuPopupDisableItemTimeChooser;

    private @Nullable DataPageRestaurantMenuDetails data;
    private Call<MenuCategoryItemsResponse> callforCategoryItems;
    private ApiInterface apiServiceForCategoryItems;
    private LoadingDialog dialogforCategoryIems;

    public ControllerMenuDetails(
            @NonNull MenuCategoryList.MenuCategories menuCategories,
            @NonNull PrefManager prefManager,
            @NonNull LoginResponse.Data baseDetails,
            boolean isPhone) {
        this.menuCategories = menuCategories;
        this.prefManager = prefManager;
        this.baseDetails = baseDetails;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        data = new DataPageRestaurantMenuDetails(
                new DataRowRestaurantMenuDetails(
                        new ObservableField<>(false), // Category cannot be dragged
                        new ObservableField<>(""),
                        new ObservableField<>(null),
                        new ObservableField<>(null), // Category Price text is unused
                        new ObservableField<>(false),
                        new ObservableField<>(DataRowRestaurantMenuDetails.ToggleUI.CLICKABLE_IDLE),
                        new ObservableField<>(true), // Category Edit is always clickable
                        new ObservableField<>(false),
                        categoryRowEventHandler),
                new ObservableArrayListMoveable<>(),
                new ObservableField<>(false),
                viewEventHandler);

        dialogforCategoryIems = new LoadingDialog(getActivity(), "");
        dialogforCategoryIems.setCancelable(false);
        ViewRestaurantMenuDetail view = new ViewRestaurantMenuDetail(new LifecycleSafe(this), isPhone, data);
        view.onCreateView(inflater, container);

        ViewMenuPopupEditItemPinCheck.ParentInterface editItemPopupPinInterface =
                new MenuPopupEditItemPinCheckParentInterface(view.getChildFragmentLayoutId());
        viewMenuPopupEditCategory = new ViewMenuPopupEditCategory(ControllerMenuDetails.this, prefManager, editCategoryPopupInterface);
        viewMenuPopupEditItemPinCheck = new ViewMenuPopupEditItemPinCheck(ControllerMenuDetails.this, prefManager, editItemPopupPinInterface);
        viewMenuPopupDisableItemTimeChooser = new ViewMenuPopupDisableItemTimeChooser(ControllerMenuDetails.this);

        setData();
        getMenuDetails(menuCategories.getMenu_category_id());

        return view.getRoot();
    }

    private final DataPageRestaurantMenuDetails.OutputEvents viewEventHandler = new DataPageRestaurantMenuDetails.OutputEvents() {
        @Override
        public void onSetProductActive(@NonNull MenuCategoryItemsResponse.Items item, boolean isActive) {
            if (isActive) {
                saveProductActive(item, isActive, true);

            } else {
                viewMenuPopupDisableItemTimeChooser.alertDialog(
                        new ViewMenuPopupDisableItemTimeChooser.ParentInterface() {
                            @Override
                            public void onClickDisableForToday() { saveProductActive(item, isActive, false); }
                            @Override
                            public void onClickDisableForEver() { saveProductActive(item, isActive, true); }
                        });
            }
        }

        @Override
        public void onEditClicked(@NonNull MenuCategoryItemsResponse.Items items) {
            final char[] pin = UserPreferences.get().getLoggedInResponse(getActivity()).getPincode().toCharArray();
            final String restaurantId = UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id();
            viewMenuPopupEditItemPinCheck.alertDialogMPIN(pin, restaurantId, items.getMenu_product_id());
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            MenuCategoryItemsResponse.Items movedItem = data.getMenuItems().get(fromPosition);
            MenuCategoryItemsResponse.Items replacedItem = ControllerMenuDetails.this.data.getMenuItems().get(toPosition);
           // saveItemOrderingMoved(movedItem, replacedItem);


            data.getMenuItems().moveItem(fromPosition, toPosition);

            ArrayList<OrderRequestForItem> orderRequests = new ArrayList<>();
            for(int i=0;i<data.getMenuItems().size();i++){
                OrderRequestForItem orderRequest = new OrderRequestForItem(""+i,""+data.getMenuItems().get(i).getMenu_product_id());
                orderRequests.add(orderRequest);

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 1000);

            if(callforCategoryItems!=null) {
                callforCategoryItems.cancel();
                callforCategoryItems=null;
                dialogforCategoryIems.hide();
                changeItemPosition(orderRequests);


            }else{
                changeItemPosition(orderRequests);

            }
        }

        /*@Override
        public void onItemDismiss(int position) {
            // I commented this out because there isn't a web service to reflect the remove item operation
            // itemList.remove(position)
            // notifyItemRemoved(position)
        }*/
    };

    private final DataRowRestaurantMenuDetails.OutputEvents categoryRowEventHandler = new DataRowRestaurantMenuDetails.OutputEvents() {
        @Override
        public void bind(int adapterPosition) {
            // Not required, we manipulate the data directly
        }

        @Override
        public void onClickToggle() {
            // TODO make this web service cancellable
            boolean isMainOn = data.getCategoryRow().getToggleIsOn().get();
            saveCategoryActive( ! isMainOn);
        }

        @Override
        public void onClickEdit() {
            viewMenuPopupEditCategory.alertDialog(menuCategories);
        }

        @Override
        public void onTouchDownDragHandle(RecyclerView.ViewHolder viewHolder) {
            // Not used for the fixed category row
        }
    };

    private void setData() {
        data.getCategoryRow().getName().set(menuCategories.getMenu_category_name());

        String itemCount = menuCategories.getNumber_of_menu_product();
        data.getCategoryRow().getNameExtra().set("1".equals(itemCount) ? "1 Item" : (itemCount + " Items"));

        if (menuCategories.getMenu_category_status() != null) {
        }

        boolean isCategoryActive = "1".equals(menuCategories.getMenu_category_status());
        setCategoryActive(isCategoryActive);

        //TODO: Open dialer on click the phone icon...
    }

    private void setCategoryActive(boolean active) {
        menuCategories.setMenu_category_status(active ? "1" : "0");

        data.getCategoryRow().getToggleIsOn().set(active);

        // The adapter observes data.getCategoryRow().getToggleIsOn() and will respond in kind for all rows
    }

    private void getMenuDetails(String menuCategoryId) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
        dialog.setCancelable(false);
        dialog.show();

        try {
            final String authToken = prefManager.getPreference(AUTH_TOKEN, "");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(baseDetails.getRestaurant_id());
            request.setMenu_category_id(menuCategoryId);
            Gson gson = new Gson();
            gson.toJson(request);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getMenuCategoryItems(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MenuCategoryItemsResponse>() {
                        @Override
                        public void onSuccess(MenuCategoryItemsResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                // boolean isActive = menuCategories.getMenu_category_status().equals("1");
                                // resetAdapter(menu_items, isActive);

                                while (data.getItems().remove(null)) {
                                    // Filtered a null item
                                }

                                Collections.sort(data.getItems(), menuItemOrderingComparator);

                                ControllerMenuDetails.this.data.getMenuItems().clear();
                                ControllerMenuDetails.this.data.getMenuItems().addAll(data.getItems());

                            } else {
                                Toast.makeText(getActivity(), "Loading failed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Loading failed", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private final Comparator<MenuCategoryItemsResponse.Items> menuItemOrderingComparator = new Comparator<MenuCategoryItemsResponse.Items>() {
        @Override
        public int compare(MenuCategoryItemsResponse.Items o1, MenuCategoryItemsResponse.Items o2) {
            int o1Position;
            try {
                o1Position = o1.getMenu_product_position() == null ? -1 : Integer.parseInt(o1.getMenu_product_position());
            } catch (NumberFormatException e) {
                o1Position = -1;
            }

            int o2Position;
            try {
                o2Position = o2.getMenu_product_position() == null ? -1 : Integer.parseInt(o2.getMenu_product_position());
            } catch (NumberFormatException e) {
                o2Position = -1;
            }

            return Integer.compare(o1Position, o2Position);
        }
    };

    private void saveCategoryActive(final boolean isActive) {
        try {
            final String authToken = prefManager.getPreference(AUTH_TOKEN,"");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(baseDetails.getRestaurant_id());
            request.setMenu_category_id(menuCategories.getMenu_category_id());
            request.setMenu_category_status(isActive ? "1" : "0");

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.activeDeactiveMenu(authToken,request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            if (data.isSuccess()) {
                                setCategoryActive(isActive);

                                // resetAdapter(itemList, isActive);

                            } else {
                                Toast.makeText(getActivity(), "Unable to update category state", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), "Unable to update category state", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }));

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Cannot update category state", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveItemOrderingMoved(@NonNull MenuCategoryItemsResponse.Items movedItem, @NonNull MenuCategoryItemsResponse.Items replacedItem) {
        // These positions aren't the nice indexes you expect, but the list order is sorted on them.
        String positionReplacedItem = replacedItem.getMenu_product_position();
        String positionMovedItem = movedItem.getMenu_product_position();

        boolean isShiftingForwards;
        try {
            isShiftingForwards = Integer.parseInt(positionReplacedItem) > Integer.parseInt(positionMovedItem);
        } catch (NumberFormatException e) {
            isShiftingForwards = false;
        }

        try {
            final String restaurantId = UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id();
            final String authToken = prefManager.getPreference(AUTH_TOKEN, "");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(restaurantId);

            // If you try to move an item forward in the list (replacing the one with a number that's greater)
            // then it won't swap the two items, but will push their IDs higher and higher, keeping the same order.
            if (isShiftingForwards) {
                request.setMenu_product_id(replacedItem.getMenu_product_id());
                request.setMenu_product_new_position(positionMovedItem);
                request.setMenu_product_current_position(positionReplacedItem);
            } else {
                request.setMenu_product_id(movedItem.getMenu_product_id());
                request.setMenu_product_new_position(positionReplacedItem);
                request.setMenu_product_current_position(positionMovedItem);
            }

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getMenuCategoryItemsPositin(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            // Toast.makeText(MenuDetailsActivity.this, "Item position saved", Toast.LENGTH_SHORT).show();
                            movedItem.setMenu_product_position(positionReplacedItem);
                            replacedItem.setMenu_product_position(positionMovedItem);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeItemPosition(ArrayList<OrderRequestForItem> order) {

        dialogforCategoryIems.show();
        try {
            apiServiceForCategoryItems = ApiClient.getClient().create(ApiInterface.class);
            LoginResponse.Data freshLoginData = UserPreferences.get().getLoggedInResponse(getActivity());

            // TODO fix this - it doesn't work at all
            ItemSwipeModel request = new ItemSwipeModel();
            request.setRestaurant_id(freshLoginData.getRestaurant_id());
            request.setOrder(order);

            Log.e("prinToken", "" + freshLoginData.getToken());
            callforCategoryItems = apiServiceForCategoryItems.changeCategoryItemPosition(freshLoginData.getToken(), request);
            callforCategoryItems.enqueue(new Callback<MenuCategoryItemsResponse>() {
                @Override
                public void onResponse(Call<MenuCategoryItemsResponse> call, Response<MenuCategoryItemsResponse> response) {
                    dialogforCategoryIems.dismiss();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getMenuDetails(menuCategories.getMenu_category_id());
                }

                @Override
                public void onFailure(Call<MenuCategoryItemsResponse> call, Throwable t) {
                    dialogforCategoryIems.dismiss();
                    Log.e("onError", "onError: " + t.getMessage());
                   // Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });



        } catch (Exception e) {
            dialogforCategoryIems.dismiss();

            Log.e("Exception ", e.toString());
        }






    }

    private void saveProductActive(MenuCategoryItemsResponse.Items item, final boolean isActive, final boolean isPermanent) {
        try {
            final String restaurantId = UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id();
            final String authToken = prefManager.getPreference(AUTH_TOKEN, "");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(restaurantId);
            request.setMenu_product_id(item.getMenu_product_id());
            request.setMenu_status(isActive ? "1" : "0");
            request.setIs_permanent(isPermanent ? "1" : "0");

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.activeDeactiveMenuProduct(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            if (data.isSuccess()) {
                                int itemIndex = ControllerMenuDetails.this.data.getMenuItems().indexOf(item);

                                if (itemIndex >= 0) {
                                    item.setActive(isActive ? "1" : "0");

                                    ObservableArrayListMoveable<MenuCategoryItemsResponse.Items> list = ControllerMenuDetails.this.data.getMenuItems();
                                    list.getCachedRegistry().notifyChanged(list, itemIndex, 1);
                                }

                            } else {
                                Toast.makeText(getActivity(), "Unable to change product status", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), "Unable to change product status", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }));

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Cannot change product status", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private final EditMenuItemsDialog.ParentInterface editItemPopupInterface = new EditMenuItemsDialog.ParentInterface() {
        @Override
        public void close() {
            //Comment By Ajit
//            FragmentManager fm = getChildFragmentManager();
//            Fragment child = fm.getFragments().get(0);
//            fm.beginTransaction()
//                    .remove(child)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .commitNow();
            getFragmentManager().popBackStack();

            ControllerMenuDetails.this.data.getChildDialogVisible().set(false);

            Helper.hideKeyboard(getActivity());
        }

        @Override
        public void onItemUpdated() {
            getMenuDetails(menuCategories.getMenu_category_id());
        }
    };

    private final ViewMenuPopupEditCategory.ParentInterface editCategoryPopupInterface = new ViewMenuPopupEditCategory.ParentInterface() {
        @Override
        public void onCategoryChanged(String newName, boolean isActive) {
            setCategoryActive(isActive);

            menuCategories.setMenu_category_name(newName);
            data.getCategoryRow().getName().set(newName);
        }
    };

    private class MenuPopupEditItemPinCheckParentInterface implements ViewMenuPopupEditItemPinCheck.ParentInterface {
        private final @IdRes int childFragmentId;

        public MenuPopupEditItemPinCheckParentInterface(@IdRes int childFragmentId) {
            this.childFragmentId = childFragmentId;
        }

        @Override
        public void onPinCheckPassed(MenuProductDetails data) {


            EditMenuItemsDialog fragment = new EditMenuItemsDialog(
                    getActivity(),
                    prefManager,
                    editItemPopupInterface,
                    data,
                    menuCategories,
                    isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(childFragmentId, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }


            ControllerMenuDetails.this.data.getChildDialogVisible().set(true);
        }
    }
}
