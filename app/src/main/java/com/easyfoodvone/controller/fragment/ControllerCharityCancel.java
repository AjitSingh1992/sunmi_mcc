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
import com.easyfoodvone.app_ui.view.ViewDonateCancel;
import com.easyfoodvone.app_common.viewdata.DataPageDonateCancel;
import com.easyfoodvone.charity.webservice.responsebean.CommonResponseBean;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyfoodvone.utility.Helper.isInternetOn;
import static com.easyfoodvone.utility.Helper.showSnackBar;
import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControllerCharityCancel extends Fragment {
    public interface ParentInterface {
        void goToCharitySuccessPage();
        void goToCharityPage();
        void setBackAction(int backNo);
    }

    private DataPageDonateCancel viewData;
    private final boolean isPhone;
    private ViewDonateCancel view;

    private final DataPageDonateCancel.OutputEvents viewEventHandler =
            new DataPageDonateCancel.OutputEvents() {
                @Override
                public void onClickOk() {
                    updateCharityStatus();
                }

                @Override
                public void onClickCancel() {
                    parentInterface.goToCharityPage();
                }
            };


    private final ParentInterface parentInterface;
    private final PrefManager prefManager;
    private final String charityId;

    public ControllerCharityCancel(ParentInterface parentInterface, PrefManager prefManager, boolean isPhone, String charityId) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
        this.charityId = charityId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageDonateCancel(viewEventHandler);

        view  = new ViewDonateCancel(new LifecycleSafe(this), isPhone);
        view.onCreateView(viewData, inflater, container);

        return view.getUi().getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentInterface.setBackAction(2);
    }

    private void updateCharityStatus() {
        if (isInternetOn(getActivity())) {
            final LoadingDialog dialog = new LoadingDialog(getActivity(), "");
            dialog.setCancelable(false);
            dialog.show();
            JsonObject jsonObject = createUpdateReq();
            jsonObject.addProperty("restaurant_id", UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonResponseBean> call =
                    apiInterface.updateMealStatus(prefManager.getPreference(AUTH_TOKEN,""),jsonObject);

            Log.e("Login Request", "" + jsonObject);
            call.enqueue(new Callback<CommonResponseBean>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponseBean> call, @NonNull Response<CommonResponseBean> response) {
                    try {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            CommonResponseBean commonResponseBean = response.body();
                            if (commonResponseBean.isSuccess()) {
                                parentInterface.goToCharitySuccessPage();
                            } else {
                                showSnackBar(view.getUi().getBinding().getRoot(), commonResponseBean.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponseBean> call, @NonNull Throwable t) {
                    if (dialog != null)
                        dialog.dismiss();
                    showSnackBar(view.getUi().getBinding().getRoot(), getString(R.string.msg_please_try_later));
                }
            });
        } else {
            showSnackBar(view.getUi().getBinding().getRoot(), getString(R.string.no_internet_available_msg));
        }
    }

    private JsonObject createUpdateReq() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("restaurant_id", UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
        jsonObject.addProperty("rowid", charityId);
        jsonObject.addProperty("status", 3);
        return jsonObject;
    }
}
