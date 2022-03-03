package com.easyfoodvone.controller.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.MySingleTon;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.utility.NewConstants;
import com.easyfoodvone.app_common.viewdata.DataPageRevenueReport;
import com.easyfoodvone.app_ui.view.ViewRevenueReport;
import com.easyfoodvone.dialogs.PinDialog;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.RevenueReportRequest;
import com.easyfoodvone.models.RevenueReportResponse;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerRevenueReport extends Fragment {

    public interface ParentInterface {
        void goToHome();
    }

    private final @NonNull ParentInterface parentInterface;
    private final @NonNull PrefManager prefManager;
    private final boolean isPhone;

    private DataPageRevenueReport data;

    public ControllerRevenueReport(@NonNull ParentInterface parentInterface, @NonNull PrefManager prefManager, boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = new DataPageRevenueReport(
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
                viewEventHandler);

        ViewRevenueReport view = new ViewRevenueReport(new LifecycleSafe(this), isPhone);
        view.onCreateView(data, inflater, container);

        new PinDialog(this, UserPreferences.get().getLoggedInResponse(getActivity()), pinCallbacks)
                .alertDialogMPIN("Enter 4 Digit Code\nTo View Revenue Report");

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

            getRevenueReport(true, false, false, false);
        }

        @Override
        public void onSubmitIncorrect() {
            data.isPinCheckSuccessful().set(false);
        }
    };

    private final DataPageRevenueReport.OutputEvents viewEventHandler = new DataPageRevenueReport.OutputEvents() {
        @Override
        public void onClickAll() {
            data.getStartDate().set("");
            data.getEndDate().set("");

            getRevenueReport(true, false, false, false);
        }

        @Override
        public void onClickToday() {
            data.getStartDate().set("");
            data.getEndDate().set("");

            getRevenueReport(false, true, false, false);
        }

        @Override
        public void onClickYesterday() {
            data.getStartDate().set("");
            data.getEndDate().set("");

            getRevenueReport(false, false, true, false);
        }

        @Override
        public void onClickFindBetweenDates() {
            String startDate = data.getStartDate().get();
            String endDate = data.getEndDate().get();

            if (TextUtils.isEmpty(startDate) || startDate.equalsIgnoreCase("Start Date")) {
                Toast.makeText(getActivity(), "Enter correct start date", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(endDate) || endDate.equalsIgnoreCase("End Date")) {
                Toast.makeText(getActivity(), "Enter correct end date", Toast.LENGTH_SHORT).show();
            } else {
                getRevenueReport(false, false, false, true);
            }
        }

        @Override
        public void onClickStartDate() {
            Constants.showDateSelectorForPastDate(getActivity(), date -> data.getStartDate().set(date));
        }

        @Override
        public void onClickEndDate() {
         //   Constants.showDateSelectorForPastDate(getActivity(), date -> data.getEndDate().set(date));
            Constants.showDateSelectorEndDate(getActivity(), date -> data.getEndDate().set(date));

        }
    };

    private void getRevenueReport(boolean getAllRevenue, boolean todayRevenue, boolean yesterdayRevenue, boolean betweenRevenue) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Loading revenue report...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data loginData = UserPreferences.get().getLoggedInResponse(getActivity());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            RevenueReportRequest request = new RevenueReportRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setUser_id(loginData.getUser_id());
            if (yesterdayRevenue) {
                request.setDate(Constants.getYesterdayDateString());
            } else if (todayRevenue) {
                request.setDate(Constants.getCurrentDateString());
            } else if (betweenRevenue) {
                request.setEnd_date(data.getEndDate().get());
                request.setFrom_date(data.getStartDate().get());
            } else {
                // Don't set any date fields
            }

            final Single<RevenueReportResponse> apiRequest;
            if (getAllRevenue) {
                apiRequest = apiService.getRevenueReport(prefManager.getPreference(AUTH_TOKEN,""),request);
            } else if (betweenRevenue) {
                apiRequest = apiService.getRevenueReportBetweenDate(prefManager.getPreference(AUTH_TOKEN,""),request);
            } else {
                apiRequest = apiService.getRevenueReportByDate(prefManager.getPreference(AUTH_TOKEN, ""), request);
            }

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiRequest
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<RevenueReportResponse>() {
                        @Override
                        public void onSuccess(RevenueReportResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                String totalOrders = data.getTotal_orders().getTotal_orders();
                                String totalProductSold = data.getTotal_orders().getTotal_product_sold();
                                String totalTaxApplied = data.getTotal_orders().getTotal_taxes_applied();
                                String totalDiscountApplied = data.getTotal_orders().getTotal_discount_applied();
                                String totalRevenueCollected = data.getTotal_orders().getTotal_revenue_collected();
                                String grossProfit = data.getTotal_orders().getGross_profit();

                                ControllerRevenueReport.this.data.isDataLoaded().set(true);
                                ControllerRevenueReport.this.data.getTotalOrders().set(TextUtils.isEmpty(totalOrders) ? 0 + "" : totalOrders);
                                ControllerRevenueReport.this.data.getTotalProductSold().set(TextUtils.isEmpty(totalProductSold) ? "0" : totalProductSold);
                                ControllerRevenueReport.this.data.getTotalTaxApplied().set(NewConstants.POUND + (TextUtils.isEmpty(totalTaxApplied) ? "0" : totalTaxApplied));
                                ControllerRevenueReport.this.data.getTotalDiscountApplied().set(NewConstants.POUND + (TextUtils.isEmpty(totalDiscountApplied) ? "0" : totalDiscountApplied));
                                ControllerRevenueReport.this.data.getTotalRevenueCollected().set(NewConstants.POUND + (TextUtils.isEmpty(totalRevenueCollected) ? "0" : totalRevenueCollected));
                                ControllerRevenueReport.this.data.getGrossProfit().set(NewConstants.POUND + (TextUtils.isEmpty(grossProfit) ? "0" : grossProfit));

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
        ControllerRevenueReport.this.data.isDataLoaded().set(false);

        data.getTotalOrders().set("");
        data.getTotalProductSold().set("");
        data.getTotalTaxApplied().set("");
        data.getTotalDiscountApplied().set("");
        data.getTotalRevenueCollected().set("");
        data.getGrossProfit().set("");
    }
}
