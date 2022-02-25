package com.easyfoodvone.controller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.viewdata.DataPageDonate;
import com.easyfoodvone.app_ui.view.ViewDonate;
import com.easyfoodvone.charity.DonationTimeAdapter;
import com.easyfoodvone.charity.webservice.responsebean.CommonResponseBean;
import com.easyfoodvone.utility.Helper;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyfoodvone.utility.Helper.isInternetOn;
import static com.easyfoodvone.utility.Helper.showSnackBar;
import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControllerCharityDonate extends Fragment implements DonationTimeAdapter.RecyclerItemListener {

    public interface ParentInterface {
        void goToCharitySuccessPage();
        void setBackAction(int backNo);
    }

    private final ParentInterface parentInterface;
    private final PrefManager prefManager;
    private final boolean isPhone;

    private DataPageDonate viewData;
    private ViewDonate viewDonate;

    private DonationTimeAdapter donationTimeAdapter;
    private List<String> donationTimeList;
    private String donationTime = "";

    public ControllerCharityDonate(ParentInterface parentInterface, PrefManager prefManager, boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    private final DataPageDonate.OutputEvents viewEventHandler = new DataPageDonate.OutputEvents() {
        @Override
        public void onClickDonate() {
            if (isValid()) {

                donateMeal();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageDonate(viewEventHandler);

        viewDonate = new ViewDonate(new LifecycleSafe(this), isPhone);
        viewDonate.onCreateView(viewData, inflater, container);

        return viewDonate.getUi().getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donationTimeList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.donation_time)));
        donationTimeAdapter = new DonationTimeAdapter(getActivity(), donationTimeList, this, isPhone);
        viewDonate.getUi().getRvTime().setAdapter(donationTimeAdapter);

        parentInterface.setBackAction(2);
    }

    /**********User Defined Method for checking validations**********/
    private boolean isValid() {
        if (viewData.getNumberMeals().get().trim().isEmpty()) {
            clearFocus();
            viewDonate.getUi().getEtNoOfMeals().requestFocus();
            showSnackBar(viewDonate.getUi().getBinding().getRoot(), getString(R.string.please_enter_no_of_meals));
            return false;
        } else if (Double.parseDouble(viewDonate.getUi().getEtNoOfMeals().getText().toString().trim()) < 1) {
            clearFocus();
            viewDonate.getUi().getEtNoOfMeals().requestFocus();
            showSnackBar(viewDonate.getUi().getBinding().getRoot(), getString(R.string.please_enter_valid_no_of_meals));
            return false;
        } else if ("".equals(donationTime)) {
            clearFocus();
            viewDonate.getUi().getRvTime().requestFocus();
            showSnackBar(viewDonate.getUi().getBinding().getRoot(), getString(R.string.please_select_time));
            return false;
        }
        return true;
    }

    /**********User Defined Method for clearing Focus**********/
    private void clearFocus() {
        viewDonate.getUi().getEtNoOfMeals().clearFocus();
    }

    @Override
    public void onItemClick(int position) {
        donationTime = donationTimeList.get(position);
        Helper.hideKeyboard(getActivity());
    }

    /**********User Defined Method for Calling Api **********/
    private void donateMeal() {
        if (isInternetOn(getActivity())) {
            final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
            dialog.setCancelable(false);
            dialog.show();
            JsonObject jsonObject = createDonateRequest();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonResponseBean> call = apiInterface.donateMeal(prefManager.getPreference(AUTH_TOKEN,""),jsonObject);

            Log.e("Login Request", "" + jsonObject);
            call.enqueue(new Callback<CommonResponseBean>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponseBean> call, @NonNull Response<CommonResponseBean> response) {
                    dialog.dismiss();

                    try {
                        if (response.isSuccessful()) {
                            CommonResponseBean commonResponseBean = response.body();
                            if (commonResponseBean.isSuccess()) {
                                parentInterface.goToCharitySuccessPage();
                            } else {
                                showSnackBar(viewDonate.getUi().getBinding().getRoot(), commonResponseBean.getMessage());
                            }
                        } else {
                            showSnackBar(viewDonate.getUi().getBinding().getRoot(), response.message() != null ? response.message() : "Unexpected error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponseBean> call, @NonNull Throwable t) {
                    dialog.dismiss();

                    showSnackBar(viewDonate.getUi().getBinding().getRoot(), getString(R.string.msg_please_try_later));
                }
            });
        } else {
            showSnackBar(viewDonate.getUi().getBinding().getRoot(), getString(R.string.no_internet_available_msg));
        }
    }

    private JsonObject createDonateRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("restaurant_id", UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
        jsonObject.addProperty("no_of_meals", viewData.getNumberMeals().get().trim());
        jsonObject.addProperty("ready_to_collect", donationTime);
        jsonObject.addProperty("is_collected", "0");
        return jsonObject;
    }
}
