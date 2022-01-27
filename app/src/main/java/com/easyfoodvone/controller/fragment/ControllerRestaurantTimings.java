package com.easyfoodvone.controller.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataDialogRestaurantTime;
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantTimings;
import com.easyfoodvone.app_common.ws.AllDaysRestaurantTiming;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_ui.view.ViewRestaurantTimings;
import com.easyfoodvone.models.AddNewTimingRequest;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;


public class ControllerRestaurantTimings extends Fragment {

    private final @NonNull PrefManager prefManager;
    private final @NonNull UserPreferences userPreferences;
    private final boolean isPhone;

    private DataPageRestaurantTimings viewData;

    public ControllerRestaurantTimings(
            @NonNull PrefManager prefManager,
            @NonNull UserPreferences userPreferences,
            boolean isPhone) {
        this.prefManager = prefManager;
        this.userPreferences = userPreferences;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageRestaurantTimings(
                viewEventHandler,
                new ObservableField<>(null),
                new ObservableArrayList<>(),
                new ObservableField<>(null),
                new ObservableField<>(null));

        ViewRestaurantTimings view = new ViewRestaurantTimings(
                viewData,
                inflater,
                new LifecycleSafe(this),
                getChildFragmentManager(),
                isPhone);
        view.onCreateView(container);

        getRestaurantTiming(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());

        return view.getRoot();
    }

    private final DataPageRestaurantTimings.OutputEvents viewEventHandler = new DataPageRestaurantTimings.OutputEvents() {
        @Override
        public void onClickAdd(final @NonNull AllDaysRestaurantTiming.Data timings) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            String timeNow = String.format("%02d:%02d", hour, minute);

            TimingPresenter presenter = new TimingPresenter(
                    new TimingInterfaceAddNew(timings), true,
                    timeNow, timeNow, timeNow, timeNow, timeNow, timeNow);

            viewData.getShowingPopupAdd().set(presenter.data);
        }

        @Override
        public void onClickEdit(@NonNull AllDaysRestaurantTiming.Data.TimingData timings) {
            TimingPresenter presenter = new TimingPresenter(
                    new TimingInterfaceEditExisting(timings),
                    timings.getStatus().equalsIgnoreCase("open"),
                    timings.getOpening_start_time(),
                    timings.getOpening_end_time(),
                    timings.getCollection_start_time(),
                    timings.getCollection_end_time(),
                    timings.getDelivery_start_time(),
                    timings.getDelivery_end_time());

            viewData.getShowingPopupEdit().set(presenter.data);
        }

        @Override
        public void onClickDelete(int position, final @NonNull AllDaysRestaurantTiming.Data.TimingData timings) {
            viewData.getInputEvents().get().showPopupDeleteConfirm(
                    () -> callDeleteTimingWS(position, timings.getId()));
        }
    };

    private void getRestaurantTiming(String restaurantId) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(restaurantId);
            request.setUser_id(userPreferences.getLoggedInResponse(getActivity()).getUser_id());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getRestaurantTiming(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<AllDaysRestaurantTiming>() {
                        @Override
                        public void onSuccess(AllDaysRestaurantTiming data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                viewData.getRestaurantTimings().clear();
                                viewData.getRestaurantTimings().addAll(data.getData());
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

    private void callAddTimingWS(
            boolean isOpen,
            String openingTime,
            String deliveryTime,
            String collectionTiming,
            String day) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Adding new timing...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data loginData = userPreferences.getLoggedInResponse(getActivity());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String authToken = prefManager.getPreference(AUTH_TOKEN, "");

            AddNewTimingRequest request = new AddNewTimingRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setUser_id(loginData.getUser_id());
            request.setCollection_time(collectionTiming);
            request.setDelivery_time(deliveryTime);
            request.setOpen_close(isOpen ? "open" : "close");
            request.setOpen_close_time(openingTime);
            request.setDay(day);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.addNewTiming(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                Toast.makeText(getActivity(), "Timing added successfully", Toast.LENGTH_SHORT).show();
                                getRestaurantTiming(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Adding failed ", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callEditTimingWS(
            boolean isOpen,
            String openingTime,
            String deliveryTime,
            String collectionTiming,
            String id,
            String day) {

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Updating timing...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            AddNewTimingRequest request = new AddNewTimingRequest();
            request.setRestaurant_id(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());
            request.setId(id);
            request.setCollection_time(collectionTiming);
            request.setDelivery_time(deliveryTime);
            request.setOpen_close(isOpen ? "open" : "close");
            request.setOpen_close_time(openingTime);

            String s1 = String.valueOf(day.charAt(0));
            request.setDay(s1.toUpperCase() + day.substring(1, 3));

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.updateTiming(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                getRestaurantTiming(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());
                                Toast.makeText(getActivity(), "Timing updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Loading failed ", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callDeleteTimingWS(final int position, String id) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Deleting timing...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());
            request.setId(id);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.deleteTiming(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                viewData.getRestaurantTimings().remove(position);

                                Toast.makeText(getActivity(), "Timing deleted successfully", Toast.LENGTH_SHORT).show();

                                getRestaurantTiming(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());

                            } else {
                                Toast.makeText(getActivity(), "Timing could not be deleted", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Loading failed ", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }

    private class TimingInterfaceAddNew implements TimingPresenter.ParentInterface {
        private final @NonNull AllDaysRestaurantTiming.Data timings;

        private TimingInterfaceAddNew(@NonNull AllDaysRestaurantTiming.Data timings) {
            this.timings = timings;
        }

        @Override
        public void showTimePicker(@NonNull ObservableField<String> dataField) {
            Constants.selectTime(dataField, getActivity());
        }

        @Override
        public void showToast(@NonNull String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void dismiss() {
            viewData.getShowingPopupAdd().set(null);
        }

        @Override
        public void onSubmit(boolean isOpen, String openingTime, String deliveryTime, String collectionTiming) {
            String day = timings.getDay();
            callAddTimingWS(isOpen, openingTime, deliveryTime, collectionTiming, day);
        }
    }

    private class TimingInterfaceEditExisting implements TimingPresenter.ParentInterface {
        private final @NonNull AllDaysRestaurantTiming.Data.TimingData timings;

        private TimingInterfaceEditExisting(@NonNull AllDaysRestaurantTiming.Data.TimingData timings) {
            this.timings = timings;
        }

        @Override
        public void showTimePicker(@NonNull ObservableField<String> dataField) {
            Constants.selectTime(dataField, getActivity());
        }

        @Override
        public void showToast(@NonNull String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void dismiss() {
            viewData.getShowingPopupEdit().set(null);
        }

        @Override
        public void onSubmit(boolean isOpen, String openingTime, String deliveryTime, String collectionTiming) {
            String id = timings.getId();
            String day = timings.getDay();
            callEditTimingWS(isOpen, openingTime, deliveryTime, collectionTiming, id, day);
        }
    }

    private static class TimingPresenter {
        public interface ParentInterface {
            void showTimePicker(@NonNull ObservableField<String> dataField);
            void showToast(@NonNull String message);
            void dismiss();
            void onSubmit(boolean isOpen, String openingTime, String deliveryTime, String collectionTiming);
        }

        private final @NonNull ParentInterface parentInterface;
        private final @NonNull DataDialogRestaurantTime data;

        public TimingPresenter(
                @NonNull ParentInterface parentInterface,
                boolean initialIsOpen,
                String initialOpeningTimeFrom,
                String initialOpeningTimeTo,
                String initialCollectionTimeFrom,
                String initialCollectionTimeTo,
                String initialDeliveryTimeFrom,
                String initialDeliveryTimeTo) {

            this.parentInterface = parentInterface;
            this.data = new DataDialogRestaurantTime(
                    new ObservableField<>(initialIsOpen),
                    new ObservableField<>(initialOpeningTimeFrom),
                    new ObservableField<>(initialOpeningTimeTo),
                    new ObservableField<>(initialCollectionTimeFrom),
                    new ObservableField<>(initialCollectionTimeTo),
                    new ObservableField<>(initialDeliveryTimeFrom),
                    new ObservableField<>(initialDeliveryTimeTo),
                    viewEventHandler);
        }

        private final DataDialogRestaurantTime.OutputEvents viewEventHandler = new DataDialogRestaurantTime.OutputEvents() {
            @Override
            public void onClickIsOpen() {
                data.isOpen().set( ! data.isOpen().get());
            }

            @Override
            public void onClickTime(@NonNull ObservableField<String> dataField) {
                parentInterface.showTimePicker(dataField);
            }

            @Override
            public void onClickUpdate() {
                if (TextUtils.isEmpty(data.getOpeningTimeFrom().get())) {
                    parentInterface.showToast("Please enter from time");
                } else if (TextUtils.isEmpty(data.getOpeningTimeTo().get())) {
                    parentInterface.showToast("Please enter to time");
                } else if (TextUtils.isEmpty(data.getDeliveryTimeFrom().get())) {
                    parentInterface.showToast("Please enter delivery time");
                } else if (TextUtils.isEmpty(data.getDeliveryTimeTo().get())) {
                    parentInterface.showToast("Please enter to delivery time");
                } else if (TextUtils.isEmpty(data.getCollectionTimeFrom().get())) {
                    parentInterface.showToast("Please enter from collection time");
                } else if (TextUtils.isEmpty(data.getCollectionTimeTo().get())) {
                    parentInterface.showToast("Please enter to collection time");
                } else {
                    parentInterface.onSubmit(
                            data.isOpen().get(),
                            data.getOpeningTimeFrom().get() + "-" + data.getOpeningTimeTo().get(),
                            data.getDeliveryTimeFrom().get() + "-" + data.getDeliveryTimeTo().get(),
                            data.getCollectionTimeFrom().get() + "-" + data.getCollectionTimeTo().get());

                    parentInterface.dismiss();
                }
            }

            @Override
            public void onClickCancel() {
                parentInterface.dismiss();
            }
        };
    }
}
