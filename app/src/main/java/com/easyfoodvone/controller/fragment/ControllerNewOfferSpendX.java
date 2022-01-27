package com.easyfoodvone.controller.fragment;

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

import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer;
import com.easyfoodvone.app_common.ws.BucketDataModel;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_ui.view.ViewOfferSpendX;
import com.easyfoodvone.controller.child.ControllerNewOfferDays;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.SpendXgetXdiscountRequest;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerNewOfferSpendX extends Fragment {

    public interface ParentInterface {
        void goBack();
    }

    private final @NonNull PrefManager prefManager;
    private final @NonNull ParentInterface parentInterface;
    private final boolean isPhone;

    private @Nullable ControllerNewOfferDays controllerDays = null;
    private @Nullable DataPageNewOffer.PageSpendX data = null;

    public ControllerNewOfferSpendX(
            @NonNull PrefManager prefManager,
            @NonNull ParentInterface parentInterface,
            boolean isPhone) {
        this.prefManager = prefManager;
        this.parentInterface = parentInterface;
        this.isPhone = isPhone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        controllerDays = new ControllerNewOfferDays();
        data = new DataPageNewOffer.PageSpendX(
                viewEventHandler,
                new ObservableField<>(null),
                controllerDays.data,
                new ObservableField<>(false),
                new ObservableArrayList<>(),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>("Start Time"),
                new ObservableField<>("End Time"),
                new ObservableField<>("Start Date"),
                new ObservableField<>("End Date"),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(true),
                new ObservableField<>(true),
                new ObservableField<>(""),
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(false));

        ViewOfferSpendX view = new ViewOfferSpendX(new LifecycleSafe(this), getActivity(), isPhone, data);
        view.onCreateView(inflater, container);

        return view.getRoot();
    }

    private final DataPageNewOffer.PageSpendX.OutputEvents viewEventHandler
            = new DataPageNewOffer.PageSpendX.OutputEvents() {

        @Override
        public void onClickAddMoreBucket() {
            String editBetween = data.getTxtEditBetween().get();
            String editAnd = data.getTxtEditAnd().get();
            String editGiveDiscount = data.getTxtEditGiveDiscount().get();

            if (TextUtils.isEmpty(editBetween)) {
                data.getInputEvents().get().showEditBetweenError("Enter price");

            } else if (TextUtils.isEmpty(editAnd)) {
                data.getInputEvents().get().showEditAndError("Enter price");

            } else if (TextUtils.isEmpty(editGiveDiscount)) {
                data.getInputEvents().get().showEditGiveDiscountError("Enter discount price");

            } else {
                data.getBucketsList().add(new BucketDataModel(editBetween, editAnd, editGiveDiscount));
                data.getBucketsRecyclerVisible().set(true);

                data.getTxtEditBetween().set("");
                data.getTxtEditAnd().set("");
                data.getTxtEditGiveDiscount().set("");
            }
        }

        @Override
        public void onClickDeleteBucket(int position) {
            if (data.getBucketsList().size() > 0) {
                data.getBucketsList().remove(position);
            } else {
                data.getBucketsRecyclerVisible().set(false);
            }
        }

        @Override
        public void onClickStartDate() {
            Constants.dateSelector1OnClick(data.getTxtStartDate(), getActivity());
        }

        @Override
        public void onClickEndDate() {
            if (data.getTxtStartDate().get().equalsIgnoreCase("Start Date") || data.getTxtStartDate().get().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please select start date first", Toast.LENGTH_SHORT).show();
            } else {
                Constants.endDateSelectorClick(data.getTxtEndDate(), getActivity(), data.getTxtStartDate().get());
            }
        }

        @Override
        public void onClickActiveFrom() {
            Constants.selectTime(data.getTxtActiveFrom(), getActivity());
        }

        @Override
        public void onClickActiveTo() {
            Constants.selectTime(data.getTxtActiveTo(), getActivity());
        }

        @Override
        public void onClickBtnSave() {
            createOffer();
        }

        @Override
        public void onClickBtnCancel() {
            parentInterface.goBack();
        }
    };

    private String getCompleteBucket() {
        String editBetween = data.getTxtEditBetween().get();
        String editAnd = data.getTxtEditAnd().get();
        String editGiveDiscount = data.getTxtEditGiveDiscount().get();

        if (TextUtils.isEmpty(editBetween)) {
            data.getInputEvents().get().showEditBetweenError("Enter price");
            return null;
        } else if (TextUtils.isEmpty(editAnd)) {
            data.getInputEvents().get().showEditAndError("Enter price");
            return null;
        } else if (TextUtils.isEmpty(editGiveDiscount)) {
            data.getInputEvents().get().showEditGiveDiscountError("Enter discount price");
            return null;
        } else {
            @NonNull String bucketFormattedData = getBucketFormatedData();

            if (! bucketFormattedData.equalsIgnoreCase("")) {
                return editBetween + "-" + editAnd + "-" + editGiveDiscount + "," + bucketFormattedData;
            } else {
                return editBetween + "-" + editAnd + "-" + editGiveDiscount;
            }
        }
    }

    private @NonNull String getBucketFormatedData() {
        if (data.getBucketsList().size() > 0) {
            BucketDataModel first = data.getBucketsList().get(0);
            @NonNull String temp = first.getBetween() + "-" + first.getAnd() + "-" + first.getDetDiscount();

            for (int i = 1; i < data.getBucketsList().size(); i++) {
                BucketDataModel next = data.getBucketsList().get(i);
                temp = temp + "," + next.getBetween() + "-" + next.getAnd() + "-" + next.getDetDiscount();
            }

            return temp;

        } else {
            return "";
        }
    }

    private void createOffer() {
        if (TextUtils.isEmpty(data.getTxtOfferTitle().get())) {
            data.getInputEvents().get().showOfferTitleError("Enter offer name");
        } else if (TextUtils.isEmpty(data.getTxtOfferDescription().get())) {
            data.getInputEvents().get().showOfferDescriptionError("Write description");
        } else if (data.getTxtStartDate().get().equalsIgnoreCase("Start Date") || data.getTxtStartDate().get().equalsIgnoreCase("")) {
            data.getErrorHighlightStartDate().set(true);
        } else if (data.getTxtEndDate().get().equalsIgnoreCase("End Date") || data.getTxtEndDate().get().equalsIgnoreCase("")) {
            data.getErrorHighlightEndDate().set(true);
        } else if (controllerDays.getWSStringSelectedDays().equals("")) {
            Toast.makeText(getActivity(), "Please select days", Toast.LENGTH_SHORT).show();
        } else if (data.getTxtActiveFrom().get().equalsIgnoreCase("Start Time") || data.getTxtActiveFrom().get().equalsIgnoreCase("")) {
            data.getErrorHighlightActiveFrom().set(true);
        } else if (data.getTxtActiveTo().get().equalsIgnoreCase("End Time") || data.getTxtActiveTo().get().equalsIgnoreCase("")) {
            data.getErrorHighlightActiveTo().set(true);
        } else if (TextUtils.isEmpty(data.getTxtEditBetween().get())) {
            data.getInputEvents().get().showEditBetweenError("Enter price");
        } else if (TextUtils.isEmpty(data.getTxtEditAnd().get())) {
            data.getInputEvents().get().showEditAndError("Enter price");
        } else if (TextUtils.isEmpty(data.getTxtEditGiveDiscount().get())) {
            data.getInputEvents().get().showEditGiveDiscountError("Enter discount price");
        } else if (TextUtils.isEmpty(data.getTxtTerms().get())) {
            data.getInputEvents().get().showTermsError("Write terms & conditions");
        } else {
            createSpendXGetXDiscount();
        }
    }

    private void clearFiledData() {
        data.getBucketsList().clear();
        data.getBucketsRecyclerVisible().set(false);

        data.getTxtOfferDescription().set("");
        data.getTxtOfferTitle().set("");
        data.getTxtEditBetween().set("");
        data.getTxtEditAnd().set("");
        data.getTxtEditGiveDiscount().set("");
        data.getTxtTerms().set("");

        data.getTxtStartDate().set(""); // TODO fix this back to default value
        data.getTxtEndDate().set(""); // TODO fix this back to default value
        data.getTxtActiveFrom().set(""); // TODO fix this back to default value
        data.getTxtActiveTo().set(""); // TODO fix this back to defaut value

        // Clear every "day" checkbox
        data.getDaysData().getAllBox().set(true);
        data.getDaysData().getAllBox().set(false);
    }

    private void createSpendXGetXDiscount() {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Creating offer...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data loginData = UserPreferences.get().getLoggedInResponse(getActivity());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String authToken = prefManager.getPreference(AUTH_TOKEN,"");

            SpendXgetXdiscountRequest request = new SpendXgetXdiscountRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setUser_id(loginData.getUser_id());

            request.setOffer_title(data.getTxtOfferTitle().get());
            request.setOffer_details(data.getTxtOfferDescription().get());
            request.setStart_date(data.getTxtStartDate().get());
            request.setEnd_date(data.getTxtEndDate().get());
            request.setDays_available(controllerDays.getWSStringSelectedDays());
            request.setStart_time(data.getTxtActiveFrom().get());
            request.setEnd_time(data.getTxtActiveTo().get());
            request.setSpendx(getCompleteBucket());

            // This can be different from the current serve style: loginData.getServe_style()
            ArrayList<String> serveStyle = new ArrayList<>();
            if (data.getApplicableOnCollection().get()) serveStyle.add("collection");
            if (data.getApplicableOnDelivery().get()) serveStyle.add("delivery");
            request.setAvailable_for(TextUtils.join(",", serveStyle));

            request.setTerms_conditions(data.getTxtTerms().get());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.createSpendXGetXDiscount(authToken,request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            if (data.isSuccess()) {
                                dialog.dismiss();

                                clearFiledData();

                                @Nullable DataPageNewOffer.PageSpendX.InputEvents inputEvents = ControllerNewOfferSpendX.this.data.getInputEvents().get();
                                if (inputEvents != null) {
                                    inputEvents.showAlertDialog("Offer has been created successfully");
                                }

                            } else {
                                onError(new Throwable(data.getMessage() == null ? "Unknown reason" : data.getMessage()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Offer creation failed", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }
}
