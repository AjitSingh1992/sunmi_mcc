package com.easyfoodvone.controller.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.utility.NewConstants;
import com.easyfoodvone.app_common.viewdata.DataPageOrderReport;
import com.easyfoodvone.app_ui.view.ViewOrderReport;
import com.easyfoodvone.dialogs.PinDialog;
import com.easyfoodvone.utility.printerutil.PrintEsayFood;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.OrderReportRequest;
import com.easyfoodvone.app_common.ws.OrderReportResponse;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import java.util.Calendar;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerOrderReport extends Fragment {

    public interface ParentInterface {
        void goToHome();
        void showToastLong(@NonNull String message);
    }

    private final @NonNull ParentInterface parentInterface;
    private final @NonNull PrefManager prefManager;
    private final @NonNull UserPreferences userPreferences;
    private final boolean isPhone;

    private DataPageOrderReport data;

    private @Nullable OrderReportRequest lastSuccessfulRequest = null;
    private @Nullable OrderReportResponse lastSuccessfulResponse = null;

    public ControllerOrderReport(
            @NonNull ParentInterface parentInterface,
            @NonNull PrefManager prefManager,
            @NonNull UserPreferences userPreferences,
            boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.userPreferences = userPreferences;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        data = new DataPageOrderReport(
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(null),
                viewEventHandler);

        ViewOrderReport view = new ViewOrderReport(new LifecycleSafe(this), isPhone);
        view.onCreateView(data, inflater, container);

        new PinDialog(this, userPreferences.getLoggedInResponse(getActivity()), pinCallbacks)
                .alertDialogMPIN("Enter 4 Digit Code\nTo View Order Report");

        return view.getRoot();
    }

    private final PinDialog.ParentInterface pinCallbacks = new PinDialog.ParentInterface() {
        @Override
        public void onDismiss() {
            parentInterface.goToHome();
        }

        @Override
        public void onSubmitCorrect() {
            data.isPinCheckSuccessful().set(true);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            //data.getStartDate().set(DateFormat.format("dd-MMM-yyyy", cal.getTimeInMillis()).toString());

            cal.add(Calendar.MONTH, 1);
            //data.getEndDate().set(DateFormat.format("dd-MMM-yyyy", cal.getTimeInMillis()).toString());

            getOrderReport(false, false, true);
        }

        @Override
        public void onSubmitIncorrect() {
            data.isPinCheckSuccessful().set(false);
        }
    };

    private final DataPageOrderReport.OutputEvents viewEventHandler = new DataPageOrderReport.OutputEvents() {
        @Override
        public void onClickYesterday() {
            data.getEndDate().set("");
            data.getStartDate().set("");

            getOrderReport(false, true, false);
        }

        @Override
        public void onClickToday() {
            data.getEndDate().set("");
            data.getStartDate().set("");

            getOrderReport(true, false, false);
        }

        @Override
        public void onClickStartDate() {
            Constants.showDateSelectorForPastDate(getActivity(), date -> data.getStartDate().set(date));
        }

        @Override
        public void onClickEndDate() {
            Constants.showDateSelectorForPastDate(getActivity(), date -> data.getEndDate().set(date));
        }

        @Override
        public void onClickSearchBetweenDates() {
            String startDate = data.getStartDate().get();
            String endDate = data.getEndDate().get();

            if (TextUtils.isEmpty(startDate) || startDate.equalsIgnoreCase("Start Date")) {
                Toast.makeText(getActivity(), "Enter correct start date", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(endDate) || endDate.equalsIgnoreCase("End Date")) {
                Toast.makeText(getActivity(), "Enter correct end date", Toast.LENGTH_SHORT).show();
            } else {
                getOrderReport(false, false, true);
            }
        }

        @Override
        public void onClickEmailReport() {
            sentReport();
        }

        @Override
        public void onClickPrintReport() {
            printReport();
        }
    };

    private @NonNull Single<OrderReportResponse> getAPIFor(@NonNull OrderReportRequest request) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String authToken = prefManager.getPreference(AUTH_TOKEN,"");

        if ( ! TextUtils.isEmpty(request.getDate())) {
            return apiService.getOrderReportByDate(authToken, request);

        } else if ( ! TextUtils.isEmpty(request.getFrom_date()) && ! TextUtils.isEmpty(request.getEnd_date())) {
            return apiService.getOrderReportBetweenDate(authToken, request);

        } else {
            // TODO this web service doesn't actually work...
            return apiService.getOrderReport(authToken, request);
        }
    }

    private void sentReport() {
        if (lastSuccessfulRequest == null) {
            return;
        }

        final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data loginData = userPreferences.getLoggedInResponse(getActivity());

            final OrderReportRequest request = new OrderReportRequest();
            request.setRestaurant_id(lastSuccessfulRequest.getRestaurant_id());
            request.setUser_id(lastSuccessfulRequest.getUser_id());
            request.setEnd_date(lastSuccessfulRequest.getEnd_date());
            request.setFrom_date(lastSuccessfulRequest.getFrom_date());
            request.setDate(lastSuccessfulRequest.getDate());

            // TODO check this should be the email which the report is sent to
            request.setEmail(loginData.getEmail());

            final Single<OrderReportResponse> api = getAPIFor(request);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(api
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OrderReportResponse>() {
                        @Override
                        public void onSuccess(OrderReportResponse data) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Report sent to " + request.getEmail(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Server connection failed", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }));

        } catch(Exception e) {
            dialog.dismiss();

            Toast.makeText(getActivity(), "Unable to send email", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    private void getOrderReport(boolean todayRevenue, boolean yesterdayRevenue, boolean betweenRevenue) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading orders report...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            LoginResponse.Data loginData = userPreferences.getLoggedInResponse(getActivity());

            final OrderReportRequest request = new OrderReportRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setUser_id(loginData.getUser_id());
            if (yesterdayRevenue) {
                request.setDate(Constants.getYesterdayDateString1());
            } else if (todayRevenue) {
                request.setDate(Constants.getCurrentDateString1());
            } else if (betweenRevenue) {
                request.setFrom_date(data.getStartDate().get());
                request.setEnd_date(data.getEndDate().get());
            }

            final Single<OrderReportResponse> api = getAPIFor(request);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(api
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OrderReportResponse>() {
                        @Override
                        public void onSuccess(OrderReportResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                lastSuccessfulRequest = request;
                                lastSuccessfulResponse = data;

                                String totalOrders = data.getData().getTotal_orders();
                                String totalRevenue = data.getData().getTotal_revenue();
                                String totalItems = data.getData().getTotal_items();
                                String totalDiscount = data.getData().getTotal_discount();
                                String acceptedPercent = data.getData().getTotal_orders_accepted_per();
                                String acceptedAmount = data.getData().getTotal_orders_accepted_amount();
                                String acceptedCount = data.getData().getTotal_orders_accepted();
                                String declinedAmount = data.getData().getTotal_orders_declined_amount();
                                String declinedCount = data.getData().getTotal_orders_declined();
                                String declinedPercent = data.getData().getTotal_orders_declined_per();
                                String total_taxes = data.getData().getTotal_taxes();
                                String restaurant_wallet = data.getData().getRestaurant_wallet();
                                ControllerOrderReport.this.data.isDataLoaded().set(true);

                                ControllerOrderReport.this.data.getTotalOrders().set(TextUtils.isEmpty(totalOrders) ? "0" : totalOrders);
                                ControllerOrderReport.this.data.getTotalRevenue().set(NewConstants.POUND + (TextUtils.isEmpty(totalRevenue) ? "0" : totalRevenue));
                                ControllerOrderReport.this.data.getTotalItems().set(TextUtils.isEmpty(totalItems) ? "0" : totalItems);
                                ControllerOrderReport.this.data.getTotalDiscount().set(NewConstants.POUND + (TextUtils.isEmpty(totalDiscount) ? "0" : totalDiscount));
                                // wallet_balance.setText(data.getData().getWallet_balance() == null || data.getData().getWallet_balance().equalsIgnoreCase("") ? NewConstants.POUND + "0" : NewConstants.POUND + data.getData().getWallet_balance());
                                // credit_count.setText(data.getData().getTotal_orders_by_credit_card() == null || data.getData().getTotal_orders_by_credit_card().equalsIgnoreCase("") ? "Credit(0)" : "Credit(" + data.getData().getTotal_orders_by_credit_card() + ")");
                                // credit_amt.setText(data.getData().getTotal_orders_by_credit_card_amount() == null || data.getData().getTotal_orders_by_credit_card_amount().equalsIgnoreCase("") ? NewConstants.POUND + "0" : NewConstants.POUND + data.getData().getTotal_orders_by_credit_card_amount());
                                ControllerOrderReport.this.data.getAcceptedPercent().set(TextUtils.isEmpty(acceptedPercent) ? "0%" : (acceptedPercent + "%"));
                                ControllerOrderReport.this.data.getAcceptedAmount().set(NewConstants.POUND + (TextUtils.isEmpty(acceptedAmount) ? "0" : acceptedAmount));
                                ControllerOrderReport.this.data.getAcceptedCount().set(TextUtils.isEmpty(acceptedCount) ? "0" : acceptedCount);
                                ControllerOrderReport.this.data.getDeclinedAmount().set(NewConstants.POUND + (TextUtils.isEmpty(declinedAmount) ? "0" : declinedAmount));
                                ControllerOrderReport.this.data.getDeclinedCount().set(TextUtils.isEmpty(declinedCount) ? "0" : declinedCount);
                                ControllerOrderReport.this.data.getDeclinedPercent().set(TextUtils.isEmpty(declinedPercent) ? "0%" : declinedPercent + "%");
                                ControllerOrderReport.this.data.getTotaltaxes().set(TextUtils.isEmpty(total_taxes) ? "0" : total_taxes + "");
                                ControllerOrderReport.this.data.getRestaurantwallet().set(TextUtils.isEmpty(restaurant_wallet) ? "0" : restaurant_wallet + "");

                                // cash_per.setText(data.getData().getTotal_orders_by_cash_per() == null || data.getData().getTotal_orders_by_cash_per().equalsIgnoreCase("") ? "0%" : data.getData().getTotal_orders_by_cash_per() + "%");
                                // cash_amt.setText(data.getData().getTotal_orders_by_cash_amount() == null || data.getData().getTotal_orders_by_cash_amount().equalsIgnoreCase("") ? NewConstants.POUND + "0" : NewConstants.POUND + data.getData().getTotal_orders_by_cash_amount());
                                // cash_count.setText(data.getData().getTotal_orders_by_cash() == null || data.getData().getTotal_orders_by_cash().equalsIgnoreCase("") ? "CASH (0)" : "CASH (" + data.getData().getTotal_orders_by_cash() + ")");

                                ControllerOrderReport.this.data.getOrdersList().set(data.getData().getOrder_list());

                            } else {
                                errorClearData();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            errorClearData();
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Loading failed ", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            errorClearData();
            dialog.dismiss();

            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void errorClearData() {
        lastSuccessfulRequest = null;
        lastSuccessfulResponse = null;

        ControllerOrderReport.this.data.isDataLoaded().set(false);

        data.getTotalOrders().set("");
        data.getTotalRevenue().set("");
        data.getTotalItems().set("");
        data.getTotalDiscount().set("");
        data.getAcceptedPercent().set("");
        data.getAcceptedCount().set("");
        data.getAcceptedAmount().set("");
        data.getDeclinedAmount().set("");
        data.getDeclinedCount().set("");
        data.getDeclinedPercent().set("");
    }

    private void printReport() {
        if (lastSuccessfulResponse == null) {
            return;
        }

        byte[] logoByte = userPreferences.getRestaurantLogoBitmap(getActivity());
        Bitmap logo = null;
        if (logoByte != null) {
            logo = BitmapFactory.decodeByteArray(logoByte, 0, logoByte.length);
        }

        boolean serviceReady = PrintEsayFood.printSummaryOrdersReport(getActivity(), logo, userPreferences.getLoggedInResponse(getActivity()), lastSuccessfulResponse.getData());
        if ( ! serviceReady) {
            parentInterface.showToastLong("The print service is not connected");
        }
    }
}
