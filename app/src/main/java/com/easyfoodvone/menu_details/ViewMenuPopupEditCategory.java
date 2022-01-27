package com.easyfoodvone.menu_details;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.separation.ObservableUtils;
import com.easyfoodvone.app_common.viewdata.DataDialogRestaurantMenuCategoryEdit;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.MenuCategoryList;
import com.easyfoodvone.app_ui.databinding.PopupMenuEditCategoryBinding;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.controller.fragment.ControllerMenuDetails;
import com.easyfoodvone.models.UpdateMenuCategoryRequest;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;


public class ViewMenuPopupEditCategory {
    public interface ParentInterface {
        void onCategoryChanged(String newName, boolean isActive);
    }

    private final @NonNull Activity activity;
    private final @NonNull ControllerMenuDetails fragment;
    private final @NonNull PrefManager prefManager;
    private final @NonNull ParentInterface parentInterface;

    public ViewMenuPopupEditCategory(
            @NonNull ControllerMenuDetails fragment,
            @NonNull PrefManager prefManager,
            @NonNull ParentInterface parentInterface) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.prefManager = prefManager;
        this.parentInterface = parentInterface;
    }

    public void alertDialog(@NonNull MenuCategoryList.MenuCategories menuCategories) {
        final PopupMenuEditCategoryBinding dialogBinding = PopupMenuEditCategoryBinding.inflate(LayoutInflater.from(activity));
        final RoundedDialogFragment dialog = new RoundedDialogFragment(dialogBinding.getRoot(), false);
        final DialogOutputEvents dialogOutputEvents = new DialogOutputEvents(menuCategories, dialog);

        final DataDialogRestaurantMenuCategoryEdit data = new DataDialogRestaurantMenuCategoryEdit(
                new ObservableField<>(menuCategories.getMenu_category_name() == null ? "" : menuCategories.getMenu_category_name()),
                new ObservableField<>(""),
                new ObservableField<>("1".equals(menuCategories.getMenu_category_status())),
                dialogOutputEvents);

        dialogOutputEvents.data = data;

        // Clear the error text when the category name is modified
        data.getCategoryName().addOnPropertyChangedCallback(new ObservableUtils.TypedObserver<>(data.getCategoryName(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                data.getCategoryNameError().set("");
            }
        }));

        dialogBinding.setData(data);

        dialog.showNow(fragment.getChildFragmentManager(), null);
    }

    private class DialogOutputEvents implements DataDialogRestaurantMenuCategoryEdit.OutputEvents {
        private final @NonNull MenuCategoryList.MenuCategories menuCategories;
        private final @NonNull RoundedDialogFragment dialog;

        // Async set required to compile
        public DataDialogRestaurantMenuCategoryEdit data;

        public DialogOutputEvents(@NonNull MenuCategoryList.MenuCategories menuCategories, @NonNull RoundedDialogFragment dialog) {
            this.menuCategories = menuCategories;
            this.dialog = dialog;
        }

        @Override
        public void onClickToggle() {
            data.getCategoryActive().set( ! data.getCategoryActive().get());
        }

        @Override
        public void onClickUpdate() {
            if (TextUtils.isEmpty(data.getCategoryName().get())) {
                data.getCategoryNameError().set("Enter Category Name");

            } else {
                data.getCategoryNameError().set("");
                updateMenuCategory(
                        menuCategories.getMenu_category_id(),
                        data.getCategoryName().get(),
                        data.getCategoryActive().get(),
                        dialog);
            }
        }

        @Override
        public void onClickCancel() {
            dialog.dismiss();
        }
    }

    private void updateMenuCategory(final String menuCategoryId, final String name, final boolean isActive, final RoundedDialogFragment mDialog) {
        final LoadingDialog dialog = new LoadingDialog(activity, "Updating menu....");
        dialog.setCancelable(false);
        dialog.show();
        try {
            UpdateMenuCategoryRequest request = new UpdateMenuCategoryRequest();
            request.setMenu_category_id(menuCategoryId);
            request.setMenu_category_name(name);
            request.setMenu_category_status(isActive ? "1" : "0");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String authToken = prefManager.getPreference(AUTH_TOKEN,"");

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.updateMenuCategory(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();
                            mDialog.dismiss();

                            if (data.isSuccess()) {
                                parentInterface.onCategoryChanged(name, isActive);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            mDialog.dismiss();

                            Toast.makeText(activity, "Updation failed", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            mDialog.dismiss();

            Log.e("Exception ", e.toString());
            Toast.makeText(activity, "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
