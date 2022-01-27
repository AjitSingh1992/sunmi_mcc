package com.easyfoodvone.controller.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_ui.view.ViewDeliverySettingsAndPostcodes;
import com.easyfoodvone.app_common.viewdata.DataPageDeliverySettingsAndPostcodes;
import com.easyfoodvone.models.DeleverySetting;
import com.easyfoodvone.models.DeliverySettingRequest;
import com.easyfoodvone.models.UpdatePostCodeDeliveryTimeRequest;
import com.easyfoodvone.new_order.models.DeleverySettingResponse;
import com.easyfoodvone.app_common.ws.DeliveryPostCodeBean;
import com.easyfoodvone.new_order.models.DeliverySettingResponse;
import com.easyfoodvone.new_order.models.UpdatePostCodeDeliveryTimeResponse;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.easyfoodvone.utility.Helper.isInternetOn;
import static com.easyfoodvone.utility.Helper.showSnackBar;
import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerDeliverySetting extends Fragment {

    private final Activity mActivity;
    private final UserPreferences userPreferences;
    private final PrefManager prefManager;
    private final boolean isPhone;

    private DataPageDeliverySettingsAndPostcodes viewData;
    private boolean checkboxEnabled = true;

    public ControllerDeliverySetting(Activity activity, PrefManager prefManager, UserPreferences userPreferences, boolean isPhone) {
        this.mActivity = activity;
        this.prefManager = prefManager;
        this.userPreferences = userPreferences;
        this.isPhone = isPhone;
    }

    private final Observer<Boolean> onCheckboxChange = new Observer<Boolean>() {
        @Override
        public void onChanged(@NonNull Boolean checked) {
            if (checkboxEnabled && checked) {
                String msg = "Enter the delivery charges for all the postcodes within " +
                        viewData.getInputMiles().get() +
                        " miles of radius for deliveries";

                if (getInputEventsOrNull() != null) {
                    getInputEventsOrNull().alertDialog(
                            msg,
                            null,
                            (@NonNull DataPageDeliverySettingsAndPostcodes.DialogCharges charges) -> updateAllPostCode(charges));
                }
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageDeliverySettingsAndPostcodes(
                viewEventHandler,
                new ObservableField<>(null),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(false),
                new ObservableArrayList<>());

        LifecycleSafe lifecycle = new LifecycleSafe(this);

        // Listen to Checkbox change with ObservableBoolean
        checkboxEnabled = true;
        lifecycle.addObserverOnceUntilDestroy(viewData.getAllPostcodesChecked(), onCheckboxChange, false);

        ViewDeliverySettingsAndPostcodes view = new ViewDeliverySettingsAndPostcodes(
                lifecycle, inflater, getChildFragmentManager(), isPhone);
        view.onCreateView(viewData, container);

        String restaurantId = userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id();
        getDeliverySetting(restaurantId);
        getPostCodeInfo(restaurantId);

        return view.getRoot();
    }

    private @Nullable DataPageDeliverySettingsAndPostcodes.InputEvents getInputEventsOrNull() {
        return viewData.getInputEvents().get();
    }

    private void deliverySetting(
            String deliveryTime,
            String distance,
            String deliveryCharge,
            String minimumOrder,
            String freeDelivery,
            String avgPreparationTime) {

        String restaurant_id = userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id();

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading Delivery Settings...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            DeliverySettingRequest request = new DeliverySettingRequest();
            request.setRestaurant_id(restaurant_id);
            request.setAverage_delivery_time(deliveryTime);
            request.setDistance(distance);
            request.setDelivery_charges(deliveryCharge);
            request.setMin_order_value(minimumOrder);
            request.setFree_delivery(freeDelivery);
            request.setAvg_preparation_time(avgPreparationTime);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.deliverySettingResponse(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<DeliverySettingResponse>() {
                        @Override
                        public void onSuccess(DeliverySettingResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                if (getInputEventsOrNull() != null) {
                                    getInputEventsOrNull().alertDialog(data.getMessage(), () -> { });
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Loading failed", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDeliverySetting(String restaurentId) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading Delivery Settings...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            DeleverySetting request = new DeleverySetting();
            request.setRestaurant_id(restaurentId);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getDeleverySetting(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<DeleverySettingResponse>() {
                        @Override
                        public void onSuccess(DeleverySettingResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                viewData.getInputMiles().set(String.valueOf(data.getDistance()));
                                viewData.getInputDeliveryTime().set(String.valueOf(data.getAverage_delivery_time()));
                                viewData.getInputFreeDelivery().set(String.valueOf(data.getFree_delivery()));
                                viewData.getInputDeliveryCharge().set(String.valueOf(data.getDelivery_charges()));
                                viewData.getInputMinOrder().set(String.valueOf(data.getMin_order_value()));
                                viewData.getInputAvgPrepTime().set(String.valueOf(data.getAvg_preparation_time()));

                                checkboxEnabled = false;
                                viewData.getAllPostcodesChecked().set(data.isSet_one_amount());
                                checkboxEnabled = true;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Loading failed", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPostCodeInfo(String restaurentId) {
        if (isInternetOn(mActivity)) {
            final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading Delivery Settings...");
            dialog.setCancelable(false);
            dialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("restaurant_id", restaurentId);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DeliveryPostCodeBean> call = apiInterface.getDeliveryPostCode(prefManager.getPreference(AUTH_TOKEN,""),jsonObject);

            call.enqueue(new Callback<DeliveryPostCodeBean>() {
                @Override
                public void onResponse(@NonNull Call<DeliveryPostCodeBean> call, @NonNull Response<DeliveryPostCodeBean> response) {
                    try {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            DeliveryPostCodeBean deliveryPostCodeBean = response.body();
                            if (deliveryPostCodeBean.isSuccess()) {
                                @Nullable List<DeliveryPostCodeBean.DataBean> postcodes = deliveryPostCodeBean.getData();
                                viewData.getPostcodesList().clear();
                                if (postcodes != null) {
                                    viewData.getPostcodesList().addAll(postcodes);
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DeliveryPostCodeBean> call, @NonNull Throwable throwable) {
                    throwable.printStackTrace();

                    dialog.dismiss();

                    showSnackBar(getView(), getString(R.string.msg_please_try_later));
                }
            });

        } else {
            showSnackBar(getView(), getString(R.string.no_internet_available_msg));
        }
    }

    private final DataPageDeliverySettingsAndPostcodes.OutputEvents viewEventHandler
            = new DataPageDeliverySettingsAndPostcodes.OutputEvents() {
        @Override
        public void onClickUpdate() {
            if (TextUtils.isEmpty(viewData.getInputDeliveryCharge().get())) {
                viewData.getInputEvents().get().setErrorEtDeliveryCharge("Enter delivery charge");
            } else if (TextUtils.isEmpty(viewData.getInputMinOrder().get())) {
                viewData.getInputEvents().get().setErrorEtMinimumOrder("Enter minimum order value");
            } else if (TextUtils.isEmpty(viewData.getInputFreeDelivery().get())) {
                viewData.getInputEvents().get().setErrorEtFreeDelivery("Enter amount");
            } else {
                deliverySetting(
                        viewData.getInputDeliveryTime().get(),
                        viewData.getInputMiles().get(),
                        viewData.getInputDeliveryCharge().get(),
                        viewData.getInputMinOrder().get(),
                        viewData.getInputFreeDelivery().get(),
                        viewData.getInputAvgPrepTime().get());
            }
        }

        @Override
        public void onEditClick(int position, @Nullable DeliveryPostCodeBean.DataBean data) {
            if (data == null) {
                return;
            }

            DataPageDeliverySettingsAndPostcodes.DialogChargesOkAction okAction =
                    (@NonNull DataPageDeliverySettingsAndPostcodes.DialogCharges charges) -> {
                        updatePostCode(
                            data.getPostcode(),
                            charges,
                            (@Nullable String responseMessage) -> {
                                data.setDelivery_min_value(charges.getMinOrder());
                                data.setShip_cost(charges.getDeliveryCharge());
                                data.setFree_delivery_over(charges.getFreeDelivery());

                                if (viewData.getPostcodesList().indexOf(data) == position) {
                                    // Trigger ObservableList update on this one item
                                    viewData.getPostcodesList().set(position, data);
                                }

                                if (getInputEventsOrNull() != null) {
                                    getInputEventsOrNull().alertDialog(responseMessage, () -> {});
                                }
                            });
                    };

            if (getInputEventsOrNull() != null) {
                getInputEventsOrNull().alertDialog(
                        getString(R.string.update_postcode_dialog_title),
                        new DataPageDeliverySettingsAndPostcodes.DialogCharges(
                                data.getDelivery_min_value(),
                                data.getShip_cost(),
                                data.getFree_delivery_over()),
                        okAction);
            }
        }

        @Override
        public void onDeleteClick(int position, @Nullable DeliveryPostCodeBean.DataBean data) {
            if (data == null) {
                return;
            }

            DataPageDeliverySettingsAndPostcodes.DialogOkAction okAction = new DataPageDeliverySettingsAndPostcodes.DialogOkAction() {
                @Override
                public void onOk() {
                    deletePostCode(
                            data.getPostcode(),
                            (@Nullable String responseMessage) -> {
                                if (viewData.getPostcodesList().indexOf(data) == position) {
                                    viewData.getPostcodesList().remove(position);
                                }

                                if (getInputEventsOrNull() != null) {
                                    getInputEventsOrNull().alertDialog(responseMessage, () -> {});
                                }
                            });
                }
            };

            if (getInputEventsOrNull() != null) {
                getInputEventsOrNull().alertDialogDelete(data.getPostcode(), okAction);
            }
        }
    };

    private void deletePostCode(String post_code, final WSSuccessAction successAction) {
        String restaurant_id = userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id();

        if (isInternetOn(mActivity)) {
            final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading Delivery Settings...");
            dialog.setCancelable(false);
            dialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("restaurant_id", restaurant_id);
            jsonObject.addProperty("postcode", post_code);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonResponse> call = apiInterface.deletDeliveryPostCode(prefManager.getPreference(AUTH_TOKEN,""),jsonObject);

            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                    try {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            CommonResponse commonResponse = response.body();
                            if (commonResponse.isSuccess()) {
                                successAction.onSuccess(commonResponse.getMessage());
                            } else {
                                Toast.makeText(mActivity, "" + commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable throwable) {
                    throwable.printStackTrace();

                    dialog.dismiss();

                    showSnackBar(getView(), getString(R.string.msg_please_try_later));
                }
            });

        } else {
            showSnackBar(getView(), getString(R.string.no_internet_available_msg));
        }
    }

    private interface WSSuccessAction {
        void onSuccess(@Nullable String responseMessage);
    }

    private void updatePostCode(
            final String post_code,
            final @NonNull DataPageDeliverySettingsAndPostcodes.DialogCharges charges,
            final @NonNull WSSuccessAction wsSuccessAction) {

        String restaurant_id = userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id();

        if (isInternetOn(mActivity)) {
            final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading Delivery Settings...");
            dialog.setCancelable(false);
            dialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("free_over", charges.getFreeDelivery());
            jsonObject.addProperty("min_delivery", charges.getMinOrder());
            jsonObject.addProperty("restaurant_id", restaurant_id);
            jsonObject.addProperty("ship_cost", charges.getDeliveryCharge());
            jsonObject.addProperty("postcode", post_code);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonResponse> call = apiInterface.updatePostCodeDelivery(prefManager.getPreference(AUTH_TOKEN,""),jsonObject);

            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                    try {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            CommonResponse commonResponse = response.body();
                            if (commonResponse.isSuccess()) {
                                wsSuccessAction.onSuccess(commonResponse.getMessage());
                            } else {
                                Toast.makeText(mActivity, "" + commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable throwable) {
                    throwable.printStackTrace();

                    dialog.dismiss();

                    showSnackBar(getView(), getString(R.string.msg_please_try_later));
                }
            });

        } else {
            showSnackBar(getView(), getString(R.string.no_internet_available_msg));
        }
    }

    private void updateAllPostCode(@NonNull DataPageDeliverySettingsAndPostcodes.DialogCharges charges) {
        final String restaurant_id = userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id();
        String post_code = userPreferences.getLoggedInResponse(getActivity()).getPost_code();

        final LoadingDialog dialog = new LoadingDialog(mActivity, "Loading Delivery Settings...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            UpdatePostCodeDeliveryTimeRequest request = new UpdatePostCodeDeliveryTimeRequest();
            request.setRestaurant_id(restaurant_id);
            request.setPost_code(post_code);
            request.setMin_order_value(Double.parseDouble(charges.getMinOrder()));
            request.setDelivery_charges(Double.parseDouble(charges.getDeliveryCharge()));
            request.setFree_delivery_amount(Double.parseDouble(charges.getFreeDelivery()));

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.updatAllPostCodeDelivery(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<UpdatePostCodeDeliveryTimeResponse>() {
                        @Override
                        public void onSuccess(UpdatePostCodeDeliveryTimeResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                if (getInputEventsOrNull() != null) {
                                    getInputEventsOrNull().alertDialog(
                                            data.getMessage(),
                                            () -> {
                                                getDeliverySetting(restaurant_id);
                                                getPostCodeInfo(restaurant_id);
                                            });
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(mActivity, "Loading failed ", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
