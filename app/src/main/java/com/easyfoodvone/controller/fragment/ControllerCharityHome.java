package com.easyfoodvone.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.viewdata.DataPageCharity;
import com.easyfoodvone.app_ui.view.ViewCharity;
import com.easyfoodvone.charity.PreviousCharityAdapter;
import com.easyfoodvone.charity.webservice.responsebean.CharityInfoBean;
import com.easyfoodvone.charity.webservice.responsebean.CommonResponseBean;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyfoodvone.utility.Constants.CHARITY_STATUS_INTENT;
import static com.easyfoodvone.utility.Helper.isInternetOn;
import static com.easyfoodvone.utility.Helper.showSnackBar;
import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerCharityHome extends Fragment implements
        PreviousCharityAdapter.PreviousMealListener,
        SwipeRefreshLayout.OnRefreshListener {

    public interface ParentInterface {
        void goToDonatePage();
        void goToCharityCancelPage(String charityId);
        void goToCharitySuccessPage();
        void setBackAction(int backNo);
    }

    private final ParentInterface parentInterface;
    private final PrefManager prefManager;
    private final LocalBroadcastManager localBroadcastManager;
    private final boolean isPhone;

    private DataPageCharity viewData;
    private ViewCharity view;
    private PreviousCharityAdapter previousCharityAdapter;
    private final List<CharityInfoBean.MealDonatedBean.PreviousMealsBean> previousMealsBeans = new ArrayList<>();

    public ControllerCharityHome(
            ParentInterface parentInterface,
            PrefManager prefManager,
            LocalBroadcastManager localBroadcastManager,
            boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.localBroadcastManager = localBroadcastManager;
        this.isPhone = isPhone;
    }

    private final DataPageCharity.OutputEvents viewEventHandler =
            new DataPageCharity.OutputEvents() {
                @Override
                public void onClickDonate() {
                    parentInterface.goToDonatePage();
                }
            };

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            previousMealsBeans.clear();
            getCharityInfo();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageCharity(viewEventHandler);

        view = new ViewCharity(new LifecycleSafe(this), isPhone);
        view.onCreateView(viewData, inflater, container);

        previousCharityAdapter = new PreviousCharityAdapter(getActivity(), this, previousMealsBeans, isPhone);
        view.getUi().getPreviousDonationsList().setAdapter(previousCharityAdapter);

        parentInterface.setBackAction(1);

        getCharityInfo();

        IntentFilter intentFilter = new IntentFilter(CHARITY_STATUS_INTENT);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        return view.getUi().getBinding().getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
    
    /**********User Defined Method to disable seekbar*********/
    private void disableSeekBar() {
        view.getUi().getCharitySeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int originalProgress;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Nothing here..
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                originalProgress = seekBar.getProgress();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int arg1, boolean fromUser) {
                seekBar.setProgress(originalProgress);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onCancel(int position) {
        String charityId = previousMealsBeans.get(position).getId();
        parentInterface.goToCharityCancelPage(charityId);
    }

    @Override
    public void onYes(int position) {
        updateCharityStatus(previousMealsBeans.get(position).getId(), 1);
    }

    @Override
    public void onNo(int position) {
        updateCharityStatus(previousMealsBeans.get(position).getId(), 2);
    }

    private void getCharityInfo() {
        if (isInternetOn(getActivity())) {
            final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
            dialog.setCancelable(false);
            dialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("restaurant_id", UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CharityInfoBean> call =
                    apiInterface.getCharityDetail(prefManager.getPreference(AUTH_TOKEN,""),jsonObject);

            Log.e("Login Request", "" + jsonObject);
            call.enqueue(new Callback<CharityInfoBean>() {
                @Override
                public void onResponse(@NonNull Call<CharityInfoBean> call, @NonNull Response<CharityInfoBean> response) {
                    try {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            CharityInfoBean charityInfoBean = response.body();
                            if (charityInfoBean.isSuccess()) {
                                viewData.getTargetMeals().set(charityInfoBean.getMeal_donated().getMeal_targeted());
                                viewData.getDonatedMeals().set(charityInfoBean.getMeal_donated().getNo_of_meals());

                                float targetAchived =
                                        ((Float.valueOf(charityInfoBean.getMeal_donated().getNo_of_meals()) / Float.valueOf(charityInfoBean.getMeal_donated().getMeal_targeted())) * 100);
                                view.getUi().getCharitySeekBar().setProgress((int) targetAchived);
                                disableSeekBar();

                                if (charityInfoBean.getMeal_donated().getPrevious_meals() != null
                                        && charityInfoBean.getMeal_donated().getPrevious_meals().size() > 0) {
                                    previousMealsBeans.clear();
                                    previousMealsBeans.addAll(charityInfoBean
                                            .getMeal_donated()
                                            .getPrevious_meals());
                                    previousCharityAdapter.notifyDataSetChanged();
                                }

                            } else {
                                showSnackBar(view.getUi().getBinding().getRoot(), charityInfoBean.getMessage());
                            }

                        } else {
                            showSnackBar(view.getUi().getBinding().getRoot(), response.message() != null ? response.message() : "Unexpected response");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CharityInfoBean> call, @NonNull Throwable t) {
                    dialog.dismiss();

                    if (view.getUi() != null) {
                        showSnackBar(view.getUi().getBinding().getRoot(), getString(R.string.msg_please_try_later));
                    }
                }
            });

        } else {
            showSnackBar(view.getUi().getBinding().getRoot(), getString(R.string.no_internet_available_msg));
        }
    }

    private void updateCharityStatus(final String charityId, final int charityStatus) {
        if (isInternetOn(getActivity())) {
            final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
            dialog.setCancelable(false);
            dialog.show();
            JsonObject jsonObject = createUpdateReq(charityId, charityStatus);

            jsonObject.addProperty("restaurant_id", UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonResponseBean> call =
                    apiInterface.updateMealStatus(prefManager.getPreference(AUTH_TOKEN,""),jsonObject);

            Log.e("Login Request", "" + jsonObject);
            call.enqueue(new Callback<CommonResponseBean>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponseBean> call, @NonNull Response<CommonResponseBean> response) {
                    try {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            CommonResponseBean commonResponseBean = response.body();
                            if (commonResponseBean.isSuccess()) {
                                if (charityStatus == 3) {
                                    parentInterface.goToCharitySuccessPage();
                                } else {
                                    getCharityInfo();
                                }

                            } else {
                                showSnackBar(view.getUi().getBinding().getRoot(), commonResponseBean.getMessage());
                            }

                        } else {
                            showSnackBar(view.getUi().getBinding().getRoot(), response.message() != null ? response.message() : "Unexpected response");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponseBean> call, @NonNull Throwable t) {
                    dialog.dismiss();

                    if (view.getUi() != null) {
                        showSnackBar(view.getUi().getBinding().getRoot(), getString(R.string.msg_please_try_later));
                    }
                }
            });

        } else {
            showSnackBar(view.getUi().getBinding().getRoot(), getString(R.string.no_internet_available_msg));
        }
    }

    private JsonObject createUpdateReq(String rowID, int status) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("restaurant_id", UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
        jsonObject.addProperty("rowid", rowID);
        jsonObject.addProperty("status", status);
        return jsonObject;
    }

    @Override
    public void onRefresh() {
        previousMealsBeans.clear();
        getCharityInfo();
    }
}
