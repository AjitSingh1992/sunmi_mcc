package com.easyfoodvone.controller.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataDialogAcceptDeliveryTime;
import com.easyfoodvone.app_common.viewdata.DataPageOrderList;
import com.easyfoodvone.app_common.viewdata.DataRowOrderDetail;
import com.easyfoodvone.app_common.viewdata.DataRowOrderOverview;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.NewDetailBean;
import com.easyfoodvone.app_common.ws.OrdersListResponse;
import com.easyfoodvone.app_ui.databinding.PopupOrderAcceptDeliveryTimeBinding;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.app_ui.view.ViewOrderList;
import com.easyfoodvone.controller.child.ControllerOrderDetail;
import com.easyfoodvone.controller.child.ControllerRowOrderOverview;
import com.easyfoodvone.utility.printerutil.PrintEsayFood;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.RestaurantClosingTimeByDataModel;
import com.easyfoodvone.new_order.models.AcceptRejectOrderRequest;
import com.easyfoodvone.new_order.models.TimeSlotRequest;
import com.easyfoodvone.models.OrdersRequest;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.Helper;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.easyfoodvone.utility.Constants.NOTIFICATION_TYPE_ACCEPTED;
import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerOrderList extends Fragment {

    public interface ParentInterface {
        // TODO move this into an ObservableField from the parent
        LoginResponse.Data getLoginData();

        void showToast(@Nullable String message);
        void showToastLong(@NonNull String message);
        void openAppStoreListing();
        void stopNotificationSound();
        void updateStoreOpen(boolean isOpen);
        void onDeliveryPartnerFetch(@NonNull String partner);
    }

    private static final String limitFrom = "0";
    private static final String limitTo = "100";

    private final ParentInterface parentInterface;
    private final PrefManager prefManager;
    private final ApiInterface apiService;
    private final LocalBroadcastManager localBroadcastManager;
    private final String appVersionName;
    private final boolean isPhone;

    private DataPageOrderList data;

    public ControllerOrderList(
            ParentInterface parentInterface,
            PrefManager prefManager,
            ApiInterface apiService,
            LocalBroadcastManager localBroadcastManager,
            String appVersionName,
            boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.apiService = apiService;
        this.localBroadcastManager = localBroadcastManager;
        this.appVersionName = appVersionName;
        this.isPhone = isPhone;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = new DataPageOrderList(
                new ObservableField<>(false),
                new ObservableField<>(DataPageOrderList.ActiveTab.NEW),
                new ObservableField<>(0),
                new ObservableField<>(0),
                new ObservableField<>(0),
                new ObservableField<>(0),
                new ObservableArrayList<>(),
                viewEventHandler,
                new ObservableField<>(null));

        ViewOrderList view = new ViewOrderList(new LifecycleSafe(this), isPhone);
        view.onCreateView(data, inflater, container);

        IntentFilter intentFilter = new IntentFilter(NOTIFICATION_TYPE_ACCEPTED);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        return view.getUi().getBinding().getRoot();
    }

    @Override
    public void onDestroyView() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);

        super.onDestroyView();
    }

    private final DataPageOrderList.OutputEvents viewEventHandler = new DataPageOrderList.OutputEvents() {
        @Override
        public void onSwipeRefresh() {
            if (isResumed()) {
                getAllOrders();
            }
        }

        @Override
        public void onClickTabSelector(@NonNull DataPageOrderList.ActiveTab tab) {
            data.setActiveTab(tab);

            getAllOrders();
        }

        @Override
        public DataRowOrderOverview newRowPresenterOverview() {
            ControllerRowOrderOverview presenterOverview = new ControllerRowOrderOverview(
                    rowOverviewParentInterface,
                    data.getActiveTab());

            return presenterOverview.getData();
        }

        @Override
        public DataRowOrderDetail newRowPresenterDetail() {
            ControllerOrderDetail presenterDetail = new ControllerOrderDetail(rowDetailParentInterface);

            return presenterDetail.getData();
        }
    };

    private final ControllerRowOrderOverview.ParentInterface rowOverviewParentInterface
            = new ControllerRowOrderOverview.ParentInterface() {
        @Override
        public void onAcceptClick(@NonNull OrdersListResponse.Orders orderDetail) {
            showChangeDeliveryTimeDialog(orderDetail);
        }

        @Override
        public void onRejectClick(@NonNull OrdersListResponse.Orders orderDetail) {
            showRejectConfirmationDialog(orderDetail);
        }

        @Override
        public void onSpinnerStatus(DataPageOrderList.AcceptedOrderRequestStatus statusEnum, OrdersListResponse.Orders orderDetail) {
            orderStatus(statusEnum, orderDetail);
        }

        @Override
        public void openOrderDetail(OrdersListResponse.Orders clickedOrder) {
            data.getInputEvents().get().scrollToOverviewOf(clickedOrder);

            @Nullable DataRowOrderDetail detailData = data.getInputEvents().get().findDetailFor(clickedOrder);
            if (detailData != null) {
                detailData.getOutputEvents().toggleVisible();
            }
        }

        @Override
        public void stopNotificationSound() {
            parentInterface.stopNotificationSound();
        }
    };

    private final ControllerOrderDetail.ParentInterface rowDetailParentInterface
            = new ControllerOrderDetail.ParentInterface() {
        @Override
        @NonNull
        public Call<NewDetailBean> callOrderDetail(@NonNull String orderNumber) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("order_number", orderNumber);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.e("Order Detail Request", "" + jsonObject);
            return apiInterface.getOrderDetail(prefManager.getPreference(AUTH_TOKEN, ""), jsonObject);
        }

        @Override
        public void print(@NonNull NewDetailBean.OrdersDetailsBean orderDetail) {
            byte[] logoByte = UserPreferences.get().getRestaurantLogoBitmap(getActivity());
            Bitmap logo = null;
            if (logoByte != null) {
                logo = BitmapFactory.decodeByteArray(logoByte, 0, logoByte.length);
            }

            LoginResponse.Data loginData = parentInterface.getLoginData();
            if (loginData != null) {
                boolean serviceReady = PrintEsayFood.printOrderDetails(getActivity(), logo, loginData, orderDetail);
                if ( ! serviceReady) {
                    parentInterface.showToastLong("The print service is not connected");
                }
            }
        }
    };

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getAllOrders();
        }
    };

    private void getAllOrders() {
        final String requestStatus;
        switch (data.getActiveTab().get()) {
            case NEW:      requestStatus = OrdersRequest.STATUS_NEW;      break;
            case ACCEPTED: requestStatus = OrdersRequest.STATUS_ACCEPTED; break;
            case REJECTED: requestStatus = OrdersRequest.STATUS_REJECTED; break;
            case REFUNDED: requestStatus = OrdersRequest.STATUS_REFUNDED; break;
            default: throw new IllegalArgumentException("Missing case");
        }

        data.isSwipeRefreshing().set(true);

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data loginData = parentInterface.getLoginData();

            OrdersRequest request = new OrdersRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setLimit(limitTo);
            request.setOffset(limitFrom);
            request.setStatus(requestStatus);

            Log.e("prinToken", "" + loginData.getToken());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getOrders(loginData.getToken(), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OrdersListResponse>() {
                        @Override
                        public void onSuccess(OrdersListResponse data) {
                            dialog.dismiss();

                            ControllerOrderList.this.data.isSwipeRefreshing().set(false);
                            if (data.isSuccess()) {
                                if (/*com.easyfoodvone.BuildConfig.FORCE_UPDATE_TO_GOOGLE_PLAY_VERSION
                                        &&*/ ! data.getData().getAndroid_version().equals(appVersionName)) {
                                    updateDialog();
                                } else {
                                }

                                @Nullable String partner = data.getData().getDelivery_partner();
                                // prefManager.savePreference("DELIVERY_TYPE", partner);
                                parentInterface.onDeliveryPartnerFetch(partner == null ? "" : partner);

                                ControllerOrderList.this.data.setOrdersList(
                                        data.getData().getOrders(),
                                        data.getData().getTotal_new_order(),
                                        data.getData().getTotal_accepted_order(),
                                        data.getData().getTotal_rejected_order(),
                                        data.getData().getTotal_refunded_order()
                                        );

                                parentInterface.updateStoreOpen(data.getData().isIs_open());

                            } else {
                                ControllerOrderList.this.data.isSwipeRefreshing().set(false);
                                parentInterface.showToast(data.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            data.isSwipeRefreshing().set(false);
                            parentInterface.showToast("Server connection failed");
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            data.isSwipeRefreshing().set(false);

            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getAllOrders();
    }

    @Override
    public void onPause() {
        super.onPause();

        parentInterface.stopNotificationSound();
    }

    @Override
    public void onStop() {
        super.onStop();

        parentInterface.stopNotificationSound();
    }

    private static class ChangeDeliveryTimeFields {
        private final @NonNull SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy   HH:mm:ss", Locale.getDefault());

        public final @Nullable String originalDeliveryDateTime;
        private final @Nullable Date originalDeliveryDateTimeParsed;

        private int extraMins = 0;
        private long asyncRestaurantCloseMin = -1;

        public ChangeDeliveryTimeFields(@Nullable String originalDeliveryDateTime) {
            this.originalDeliveryDateTime = originalDeliveryDateTime;
            this.originalDeliveryDateTimeParsed = Helper.parseDeliveryDate(originalDeliveryDateTime);
        }

        public void add5() {
            if (asyncRestaurantCloseMin <= 0) {
                // Don't allow extra time?
                return;
            }

            // NewOrderActivity had untested/unused code which used a different web service field and method for displaying the minutes:
            // the dialog layout had an invisible TextView which said "You can extend delivery time maximum to 90 minutes from average delivery time"
            // 1. orderDetail.getAverage_delivery_time()
            // 2. Clamp the lower bound to this average
            // 3. Clamp the upper bound to 90
            // 4. Instead of request.setDelivey_date_time it uses request.setDelivey_time
            // 5. That is passed the integer as a string: getAverage_delivery_time() <= "int" <= 90
            if (extraMins >= 90) {
                return;
            }

            if (asyncRestaurantCloseMin > extraMins + 5) {
                extraMins = extraMins + 5;
            }
        }

        public void remove5() {
            if (asyncRestaurantCloseMin <= 0) {
                // Don't allow extra time?
                return;
            }

            if (extraMins > 0) {
                extraMins = extraMins - 5;
            }
        }

        public int getExtraTimeMins() {
            return extraMins;
        }

        public String formatDeliveryDateTimeWithExtra() {
            if (originalDeliveryDateTimeParsed == null) {
                int hour = extraMins / 60;
                int minutes = extraMins % 60;
                return originalDeliveryDateTime
                        + "\n+"
                        + new DecimalFormat("00").format(hour)
                        + ":"
                        + new DecimalFormat("00").format(minutes);

            } else {
                return dateFormatter.format(addMinutes(originalDeliveryDateTimeParsed, extraMins));
            }
        }

        // Expects input in the format "yyyy-MM-dd HH:mm:ss" and returns in the same format
        public String formatAPIOriginalDateTimeWithExtra() {
            if (originalDeliveryDateTimeParsed == null) {
                return originalDeliveryDateTime;

            } else {
                return Helper.formatDateForAPIWithExtraTime(addMinutes(originalDeliveryDateTimeParsed, extraMins));
            }
        }

        private @NonNull Date addMinutes(@NonNull Date original, int minutes) {
            return new Date(original.getTime() + TimeUnit.MINUTES.toMillis(minutes));
        }
    }

    private class ChangeDeliveryDialogOutputEvents implements DataDialogAcceptDeliveryTime.OutputEvents {
        private final @NonNull RoundedDialogFragment mDialog;
        private final @NonNull ChangeDeliveryTimeFields dialogFields;
        private final @NonNull OrdersListResponse.Orders orderDetail;

        public DataDialogAcceptDeliveryTime data;

        public ChangeDeliveryDialogOutputEvents(
                @NonNull RoundedDialogFragment mDialog,
                @NonNull ChangeDeliveryTimeFields dialogFields,
                @NonNull OrdersListResponse.Orders orderDetail) {
            this.mDialog = mDialog;
            this.dialogFields = dialogFields;
            this.orderDetail = orderDetail;
        }

        @Override
        public void onClickAccept() {
            mDialog.dismiss();

            parentInterface.stopNotificationSound();
            acceptRejectOrder("accepted", orderDetail, dialogFields.formatAPIOriginalDateTimeWithExtra(), "");
        }

        @Override
        public void onClickCancel() {
            mDialog.dismiss();
        }

        @Override
        public void onClickAdd() {
            dialogFields.add5();

            data.getDeliveryDateTime().set(dialogFields.formatDeliveryDateTimeWithExtra());
            data.getMinsAboveAverage().set(dialogFields.getExtraTimeMins());
        }

        @Override
        public void onClickSubtract() {
            dialogFields.remove5();

            data.getDeliveryDateTime().set(dialogFields.formatDeliveryDateTimeWithExtra());
            data.getMinsAboveAverage().set(dialogFields.getExtraTimeMins());
        }
    }

    private void showChangeDeliveryTimeDialog(final OrdersListResponse.Orders orderDetail) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        PopupOrderAcceptDeliveryTimeBinding binding = PopupOrderAcceptDeliveryTimeBinding.inflate(factory);
        RoundedDialogFragment mDialog = new RoundedDialogFragment(binding.getRoot(), false);

        final ChangeDeliveryTimeFields dialogFields = new ChangeDeliveryTimeFields(orderDetail.getDelivery_date_time());
        final ChangeDeliveryDialogOutputEvents outputEventHandler = new ChangeDeliveryDialogOutputEvents(mDialog, dialogFields, orderDetail);

        final DataDialogAcceptDeliveryTime data = new DataDialogAcceptDeliveryTime(
                new ObservableField<>(dialogFields.formatDeliveryDateTimeWithExtra()),
                new ObservableField<>(dialogFields.getExtraTimeMins()),
                new ObservableField<>(orderDetail.getAverage_delivery_time()),
                new ObservableField<>(false),
                new ObservableField<>(false),
                outputEventHandler);
        outputEventHandler.data = data;

        binding.setData(data);

        getRestaurantClosingTimeByDate(data, orderDetail.getRestaurant_id(), dialogFields);

        mDialog.showNow(getChildFragmentManager(), null);
    }

    private void showRejectConfirmationDialog(final OrdersListResponse.Orders orderDetail) {
        final String[] reasons = new String[] {
                "Select reason",
                "Too busy to fulfill order",
                "Items not in stock",
                "Delivery driver unavailable ",
                "Closing soon",
                "collection only",
                "Other"};

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View mDialogView = factory.inflate(R.layout.popup_order_reject_confirmation, null);

        RoundedDialogFragment mDialog = new RoundedDialogFragment(mDialogView, false);

        final EditText notes = (EditText) mDialogView.findViewById(R.id.notes);
        final Spinner spinnerMainItem = mDialogView.findViewById(R.id.spinner_main_item);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_dropdown_item, reasons);
        spinnerMainItem.setAdapter(spinnerArrayAdapter);

        final int[] selectedPos = { 0 };

        spinnerMainItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPos[0] = position;

                if (position == 0) {
                    notes.setVisibility(View.GONE);
                    notes.setText("");
                } else if (position == 6) {
                    notes.setText("");
                    notes.setVisibility(View.VISIBLE);
                } else {
                    notes.setVisibility(View.GONE);
                    notes.setText(reasons[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPos[0] == 0) {
                    parentInterface.showToast("Please select rejection reason");
                } else {
                    acceptRejectOrder("rejected", orderDetail, orderDetail.getDelivery_date_time(), notes.getText().toString());
                    mDialog.dismiss();
                }
            }
        });
        mDialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.showNow(getChildFragmentManager(), null);
    }

    private void acceptRejectOrder(final String acceptReject, OrdersListResponse.Orders orderDetail, String deliveryTime, String notes) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            LoginResponse.Data loginData = parentInterface.getLoginData();

            AcceptRejectOrderRequest request = new AcceptRejectOrderRequest();
            request.setCustomer_id(orderDetail.getCustomer_id());
            request.setOrder_number(orderDetail.getOrder_num());
            request.setOrder_response(acceptReject);
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setDelivey_date_time(deliveryTime);
            request.setNotes(notes);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.acceptRejectOrders(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                parentInterface.stopNotificationSound();

                                if (acceptReject.equals("accepted")) {
                                    ControllerOrderList.this.data.moveOrderItem(orderDetail, DataPageOrderList.ActiveTab.NEW, DataPageOrderList.ActiveTab.ACCEPTED);

                                    parentInterface.showToast("Order Accepted");

                                } else {
                                    ControllerOrderList.this.data.moveOrderItem(orderDetail, DataPageOrderList.ActiveTab.NEW, DataPageOrderList.ActiveTab.REJECTED);

                                    Constants.DialogClickedListener listener = new Constants.DialogClickedListener() {
                                        @Override
                                        public void onDialogClicked() { }
                                        @Override
                                        public void onDialogRejectClicked() { }
                                    };

                                    Constants.alertDialogReject(
                                            "You have rejected this order.\nCustomer has been\nnotified",
                                            getActivity(),
                                            listener);
                                }

                            } else {
                                alertDialog(data.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
            e.printStackTrace();
        }
    }

    private void orderStatus(@NonNull DataPageOrderList.AcceptedOrderRequestStatus statusEnum, OrdersListResponse.Orders orderDetail) {
        final String status;
        switch (statusEnum) {
            case PREPARING:        status = "preparing"; break;
            case OUT_FOR_DELIVERY: status = "out_for_delivery"; break;
            case DELIVERED:        status = "delivered"; break;
            case SERVED:           status = "served"; break;
            default: throw new IllegalArgumentException("Missing case");
        }

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            AcceptRejectOrderRequest request = new AcceptRejectOrderRequest();
            request.setOrderStatus(status);
            request.setOrder_number(orderDetail.getOrder_num());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.orderStatus(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();
                            if (data.isSuccess()) {
                                // This should only happen for accepted orders, layoutOrderStatus
                                // should be Visibility.GONE for any other active tab
                                getAllOrders();
                                parentInterface.showToastLong("Successful");
                            } else {
                                parentInterface.showToastLong("Unsuccessful");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
            e.printStackTrace();
        }
    }

    private void getRestaurantClosingTimeByDate(final DataDialogAcceptDeliveryTime uiData, String restaurantId, ChangeDeliveryTimeFields deliveryFields) {
        uiData.getButtonsDataIsLoading().set(true);

        try {
            TimeSlotRequest request = new TimeSlotRequest(restaurantId, deliveryFields.originalDeliveryDateTime);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getRestaurantClosingTimeByDate(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<RestaurantClosingTimeByDataModel>() {
                        @Override
                        public void onSuccess(RestaurantClosingTimeByDataModel data) {
                            uiData.getButtonsDataIsLoading().set(false);

                            if (data.getSuccess()) {
                                if (data != null
                                        && data.getData().getOpeningEndTime() != null
                                        && ! data.getData().getOpeningEndTime().equalsIgnoreCase("")) {
                                    try {
                                        deliveryFields.asyncRestaurantCloseMin = getDifferenceMin(
                                                Constants.getDateFromString(deliveryFields.originalDeliveryDateTime, "yyyy-MM-dd HH:mm:ss"),
                                                Constants.getDateFromString(data.getData().getOpeningEndTime(), "yyyy-MM-dd HH:mm:ss"));

                                        uiData.getButtonsDataIsAvailable().set(true);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            uiData.getButtonsDataIsLoading().set(false);
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            uiData.getButtonsDataIsLoading().set(false);
            Log.e("Exception ", e.toString());
            parentInterface.showToast("Server not responding.");
            e.printStackTrace();
        }
    }

    private long getDifferenceMin(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedMinutes;
    }

    private void alertDialog(String msg) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(getActivity()).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void updateDialog() {
        final View dialogView = LayoutInflater.from(getActivity()).inflate(
                isPhone ? R.layout.popup_update_google_play_phone : R.layout.popup_update_google_play_tablet,
                null);

        RoundedDialogFragment dialog = new RoundedDialogFragment(dialogView, false);

        TextView tvOk = (TextView) dialogView.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                parentInterface.openAppStoreListing();
            }
        });

        dialog.showNow(getChildFragmentManager(), null);
    }
}