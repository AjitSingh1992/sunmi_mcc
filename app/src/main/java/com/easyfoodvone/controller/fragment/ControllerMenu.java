package com.easyfoodvone.controller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableArrayListMoveable;
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantMenu;
import com.easyfoodvone.app_common.ws.MenuCategoryItemsResponse;
import com.easyfoodvone.app_common.ws.MenuCategoryList;
import com.easyfoodvone.app_common.ws.OrdersListResponse;
import com.easyfoodvone.app_ui.view.ViewRestaurantMenu;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.models.OrderRequest;
import com.easyfoodvone.models.OrdersRequest;
import com.easyfoodvone.models.menu_response.CategorySwipeModel;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerMenu extends Fragment {
    public interface ParentInterface {
        void openMenuDetails(MenuCategoryList.MenuCategories item);
    }

    private final ParentInterface parentInterface;
    private final PrefManager prefManager;
    private final boolean isPhone;

    private DataPageRestaurantMenu data;
    private @Nullable LoadingDialog loadingDialog = null;
    private @Nullable Disposable disposableWSGetMenu = null;

    public ControllerMenu(ParentInterface parentInterface, PrefManager prefManager, boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new DataPageRestaurantMenu(new ObservableArrayListMoveable<>(), viewEventHandler);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewRestaurantMenu view = new ViewRestaurantMenu(new LifecycleSafe(this), isPhone, data);
        view.onCreateView(inflater, container);
        return view.getRoot();
    }

    private final DataPageRestaurantMenu.OutputEvents viewEventHandler = new DataPageRestaurantMenu.OutputEvents() {
        @Override
        public void openMenuDetails(@NonNull MenuCategoryList.MenuCategories item) {
            parentInterface.openMenuDetails(item);
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            data.getMenuItems().moveItem(fromPosition, toPosition);

            LoginResponse.Data freshLoginData = UserPreferences.get().getLoggedInResponse(getActivity());
            // I commented this out because the WS to move an item doesn't work for categories
            // @NonNull MenuCategoryList.MenuCategories movedItem = data.getMenuItems().get(toPosition)
            ArrayList<OrderRequest> orderRequests = new ArrayList<>();
            for(int i=0;i<data.getMenuItems().size();i++){
                JSONObject orderData = new JSONObject();
                OrderRequest orderRequest = new OrderRequest(""+i,""+data.getMenuItems().get(i).getMenu_category_id());
                orderRequests.add(orderRequest);

            }
            //Completed by ajit
         //   changeCategoryPosition(orderRequests);
        }

        /*@Override
        public void onItemDismiss(int position) {
            // I commented this out because it seems wrong to be deleting categories... There is no WS for it.
            // data.getMenuItems().remove(position);
        }*/
    };

    private void fetchAllMenuCategories(String restaurantId) {
        showLoadingDialog();

        try {
            String authToken = prefManager.getPreference(AUTH_TOKEN, "");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(restaurantId);

            disposableWSGetMenu = apiService.getMenuCategories(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MenuCategoryList>() {
                        @Override
                        public void onSuccess(@NonNull MenuCategoryList data) {
                            if (isDisposed()) return;

                            if ( ! data.isSuccess()) {
                                onError(new Exception("Server gave failure response"));
                                return;
                            }

                            disposableWSGetMenu = null;
                            hideLoadingDialog();

                            ControllerMenu.this.data.getMenuItems().clear();
                            ControllerMenu.this.data.getMenuItems().addAll(data.getMenu_items());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            if (isDisposed()) return;

                            disposableWSGetMenu = null;
                            hideLoadingDialog();

                            e.printStackTrace();

                            Toast.makeText(getActivity(), "Loading failed", Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

        } catch (Exception e) {
            hideLoadingDialog();

            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveItemOrderingMoved(int fromPosition, int toPosition, @NonNull MenuCategoryList.MenuCategories movedItem) {
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String authToken = prefManager.getPreference(AUTH_TOKEN,"");
            LoginResponse.Data freshLoginData = UserPreferences.get().getLoggedInResponse(getActivity());

            // TODO fix this - it doesn't work at all
            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(freshLoginData.getRestaurant_id());
            request.setMenu_category_id(movedItem.getMenu_category_id());
            request.setMenu_category_current_position(fromPosition + "");
            request.setMenu_category_new_position(toPosition + "");

            Disposable noNeedToCancel = apiService.getMenuCategoryItemsPositin(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MenuCategoryItemsResponse>() {
                        @Override
                        public void onSuccess(@NonNull MenuCategoryItemsResponse data) {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(getActivity(), "Save menu position failed", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    });

        } catch (Exception e) {
            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void changeCategoryPosition(ArrayList<OrderRequest> order) {

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
        dialog.setCancelable(false);
        dialog.show();
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            LoginResponse.Data freshLoginData = UserPreferences.get().getLoggedInResponse(getActivity());

            // TODO fix this - it doesn't work at all
            CategorySwipeModel request = new CategorySwipeModel();
            request.setRestaurant_id(freshLoginData.getRestaurant_id());
            request.setOrder(order);

            Log.e("prinToken", "" + freshLoginData.getToken());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.changeCategoryPosition(freshLoginData.getToken(), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MenuCategoryItemsResponse>() {
                        @Override
                        public void onSuccess(MenuCategoryItemsResponse data) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), ""+data.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.e("onError", "onError: " + e.getMessage());
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }));


        } catch (Exception e) {
            dialog.dismiss();

            Log.e("Exception ", e.toString());
        }






    }

    @Override
    public void onResume() {
        super.onResume();

        LoginResponse.Data freshLoginData = UserPreferences.get().getLoggedInResponse(getActivity());
        fetchAllMenuCategories(freshLoginData.getRestaurant_id());
    }

    @Override
    public void onPause() {
        hideLoadingDialog();
        if (disposableWSGetMenu != null) {
            disposableWSGetMenu.dispose();
            disposableWSGetMenu = null;
        }

        super.onPause();
    }

    private void showLoadingDialog() {
        loadingDialog = new LoadingDialog(getActivity(), "Loading menus...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
