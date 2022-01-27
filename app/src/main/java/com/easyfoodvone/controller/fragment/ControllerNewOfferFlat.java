package com.easyfoodvone.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_ui.view.ViewOfferFlat;
import com.easyfoodvone.controller.child.ControllerNewOfferDays;
import com.easyfoodvone.controller.child.ControllerNewOfferSelectImage;
import com.easyfoodvone.models.DiscountWithPercentageRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerNewOfferFlat extends Fragment {

    public interface ParentInterface {
        void goBack();
    }

    private final @NonNull PrefManager prefManager;
    private final @NonNull ParentInterface parentInterface;
    private final @Nullable String toEditId;
    private final boolean isPhone;

    private final @Nullable ControllerNewOfferDays controllerDays;
    private final @Nullable ControllerNewOfferSelectImage controllerSelectImage;
    private final @Nullable DataPageNewOffer.PageFlatAmount data;

    public ControllerNewOfferFlat(
            @NonNull PrefManager prefManager,
            @NonNull ParentInterface parentInterface,
            boolean isPhone,
            @Nullable DataPageNewOffer.ToEditFlatOrPercent toEdit) {

        this.prefManager = prefManager;
        this.parentInterface = parentInterface;
        this.isPhone = isPhone;
        this.controllerDays = new ControllerNewOfferDays();
        this.controllerSelectImage = new ControllerNewOfferSelectImage(imagePickerInputEventHandler);
        this.data = new DataPageNewOffer.PageFlatAmount(
                viewEventHandler,
                new ObservableField<>(null),
                controllerDays.data,
                controllerSelectImage.data,
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>("Active From"),
                new ObservableField<>("Active To"),
                new ObservableField<>("Start Date"),
                new ObservableField<>("End Date"),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(true),
                new ObservableField<>(true),
                new ObservableField<>(true),
                new ObservableField<>(false),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(false));

        if (toEdit != null) {
            toEditId = toEdit.getId();
            populateWith(toEdit);
        } else {
            toEditId = null;
        }
    }

    private void populateWith(@NonNull DataPageNewOffer.ToEditFlatOrPercent toEdit) {
        controllerDays.data.getMonBox().set(toEdit.getCheckedDayMon());
        controllerDays.data.getTueBox().set(toEdit.getCheckedDayTue());
        controllerDays.data.getWedBox().set(toEdit.getCheckedDayWed());
        controllerDays.data.getThuBox().set(toEdit.getCheckedDayThu());
        controllerDays.data.getFriBox().set(toEdit.getCheckedDayFri());
        controllerDays.data.getSatBox().set(toEdit.getCheckedDaySat());
        controllerDays.data.getSunBox().set(toEdit.getCheckedDaySun());

        data.getTxtOfferTitle().set(toEdit.getOfferTitle());
        data.getTxtOfferDescription().set(toEdit.getOfferDescription());
        data.getTxtStartDate().set(toEdit.getStartDate());
        data.getTxtActiveFrom().set(toEdit.getStartTime());
        data.getTxtEndDate().set(toEdit.getEndDate());
        data.getTxtActiveTo().set(toEdit.getEndTime());
        data.getTxtHowMuchCustomer().set(toEdit.getHowMuchCustomer());
        data.getTxtEditDiscountAmount().set(toEdit.getEditDiscountAmount());
        data.getTxtMaxDiscount().set(toEdit.getMaxDiscount());
        data.getTxtPerCustomerUsage().set(toEdit.getPerCustomerUsage());
        data.getTxtTotalUsage().set(toEdit.getTotalUsage());
        data.getApplicableOnDelivery().set(toEdit.getApplicableOnDelivery());
        data.getApplicableOnCollection().set(toEdit.getApplicableOnCollection());
        data.getApplicableOnDineIn().set(toEdit.getApplicableOnDineIn());
        data.getApplicableOnShowInSwipe().set(toEdit.getApplicableOnShowInSwipe());
        data.getTxtShareEasyfood().set(toEdit.getShareEasyfood());
        data.getTxtShareRestaurant().set(toEdit.getShareRestaurant());
        data.getTxtShareFranchise().set(toEdit.getShareFranchise());
        data.getTxtTerms().set(toEdit.getTerms());

        //val errorHighlightStartDate: ObservableField<Boolean>,
        //val errorHighlightEndDate: ObservableField<Boolean>,
        //val errorHighlightActiveFrom: ObservableField<Boolean>,
        //val errorHighlightActiveTo: ObservableField<Boolean>
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewOfferFlat view = new ViewOfferFlat(new LifecycleSafe(this), getActivity(), isPhone, data);
        view.onCreateView(inflater, container);
        return view.getRoot();
    }

    private final DataPageNewOffer.PageFlatAmount.OutputEvents viewEventHandler
            = new DataPageNewOffer.PageFlatAmount.OutputEvents() {

        @Override
        public void onClickBtnCancel() {
            parentInterface.goBack();
        }

        @Override
        public void onClickBtnSave() {
            createOffer();
        }

        @Override
        public void onClickImageTop() {
            data.getImagePickerData().getOutputEvents().onClickSelectImage(getActivity());
        }

        @Override
        public void onClickActiveTo() {
            Constants.selectTime(data.getTxtActiveTo(), getActivity());
        }

        @Override
        public void onClickActiveFrom() {
            Constants.selectTime(data.getTxtActiveFrom(), getActivity());
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
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        data.getImagePickerData().getOutputEvents().onActivityResult(getActivity(), requestCode, resultCode, intent);
    }

    private final DataPageNewOffer.ImagePickerData.InputEvents imagePickerInputEventHandler
            = new DataPageNewOffer.ImagePickerData.InputEvents() {
        @Override
        public void startActivityForResult(@NonNull Intent intent, int requestCode) {
            Fragment fragment = ControllerNewOfferFlat.this;
            fragment.startActivityForResult(intent, requestCode);
        }
    };

    private void clearFiledData() {
        data.getTxtOfferDescription().set("");
        data.getTxtOfferTitle().set("");
        data.getTxtEditDiscountAmount().set("");
        data.getTxtHowMuchCustomer().set("");
        data.getTxtMaxDiscount().set("");
        data.getTxtPerCustomerUsage().set("");
        data.getTxtTotalUsage().set("");
        data.getTxtTerms().set("");

        data.getTxtStartDate().set(""); // TODO fix this back to default value
        data.getTxtEndDate().set(""); // TODO fix this back to default value
        data.getTxtActiveFrom().set(""); // TODO fix this back to default value
        data.getTxtActiveTo().set(""); // TODO fix this back to defaut value

        // Clear every "day" checkbox
        data.getDaysData().getAllBox().set(true);
        data.getDaysData().getAllBox().set(false);

        // radioOnEntireMenu.setChecked(false);
        // radioSelectedItems.setChecked(false);

        data.getApplicableOnDelivery().set(true);
        data.getApplicableOnCollection().set(true);
        data.getApplicableOnDineIn().set(true);
        data.getApplicableOnShowInSwipe().set(false);

        data.getTxtShareEasyfood().set("");
        data.getTxtShareRestaurant().set("");
        data.getTxtShareFranchise().set("");

        data.getImagePickerData().getPickedImage().set(null);
    }

    /*
    private String idsList = "";

    private void getAllMenuProducts() {
        try {
            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(Constants.getStoredData(getActivity()).getRestaurant_id());

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getProducts(prefManager.getPreference(AUTH_TOKEN,""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MenuProducts>() {
                        @Override
                        public void onSuccess(MenuProducts data) {
                            dialog = new MenuProductSelectorDialog(getActivity(), data.getData(), menuDialogButtonClickListener);
                            dialog.setCancelable(false);
                            dialog.show();
                            Log.e("Menu product ", data.getData().toString());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), "Products not found", Toast.LENGTH_LONG).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private final MenuProductSelectorDialog.OnDialogButtonClickListener menuDialogButtonClickListener
            = new MenuProductSelectorDialog.OnDialogButtonClickListener() {
        @Override
        public void onOkClick(String ids, List<MenuProducts.Data> selectedItemList) {
            OfferFlatDiscountAmountFragment.this.idsList = ids;
            if (ids.equalsIgnoreCase("")) {
                radioOnEntireMenu.setChecked(false);
                radioSelectedItems.setChecked(false);
                selcetdItemsList.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "You did't select any menu item or category", Toast.LENGTH_SHORT).show();
            }

            if (selectedItemList != null) {
                selectedProductsListAdapter = new SelectedProductsListAdapter(getActivity(), selectedItemList);
                selcetdItemsList.setAdapter(selectedProductsListAdapter);
                selcetdItemsList.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelClick() {
            radioOnEntireMenu.setChecked(false);
            radioSelectedItems.setChecked(false);
            selcetdItemsList.setVisibility(View.GONE);
        }
    };*/

    private void createOffer() {
        if (TextUtils.isEmpty(data.getTxtOfferTitle().get())) {
            data.getInputEvents().get().showOfferTitleError("Enter offer name");
        } /*else if (TextUtils.isEmpty(data.getTxtOfferDescription().get())) {
            data.getInputEvents().get().showOfferDescriptionError("Write description");
        }*/ else if (TextUtils.isEmpty(data.getTxtHowMuchCustomer().get())) {
            data.getInputEvents().get().showHowMuchCustomerError("Enter amount");
        } else if (TextUtils.isEmpty(data.getTxtEditDiscountAmount().get())) {
            data.getInputEvents().get().showDiscountAmountError("Enter discount amount");
        } else if (TextUtils.isEmpty(data.getTxtMaxDiscount().get())) {
            data.getInputEvents().get().showMaxDiscountError("Enter maximum discount amount");
        } else if (TextUtils.isEmpty(data.getTxtPerCustomerUsage().get())) {
            data.getInputEvents().get().showPerCustomerUsageError("Enter usage per customer");
        } else if (TextUtils.isEmpty(data.getTxtTotalUsage().get())) {
            data.getInputEvents().get().showTotalUsageError("Enter total usage");
        } else if (TextUtils.isEmpty(data.getTxtShareEasyfood().get())) {
            data.getInputEvents().get().showShareEasyfoodError("Enter easyfood share");
        } else if (TextUtils.isEmpty(data.getTxtShareRestaurant().get())) {
            data.getInputEvents().get().showShareRestaurantError("Enter restaurant share");
        } else if (TextUtils.isEmpty(data.getTxtShareFranchise().get())) {
            data.getInputEvents().get().showShareFranchiseError("Enter franchise share");
        } else if (data.getTxtStartDate().get().equalsIgnoreCase("Start Date") || data.getTxtStartDate().get().equalsIgnoreCase("")) {
            data.getErrorHighlightStartDate().set(true);
        } else if (data.getTxtEndDate().get().equalsIgnoreCase("End Date") || data.getTxtEndDate().get().equalsIgnoreCase("")) {
            data.getErrorHighlightEndDate().set(true);
        } else if (controllerDays.getWSStringSelectedDays().equals("")) {
            Toast.makeText(getActivity(), "Please select days", Toast.LENGTH_SHORT).show();
        } else if (data.getTxtActiveFrom().get().equalsIgnoreCase("Active From") || data.getTxtActiveFrom().get().equalsIgnoreCase("")) {
            data.getErrorHighlightActiveFrom().set(true);
        } else if (data.getTxtActiveTo().get().equalsIgnoreCase("Active To") || data.getTxtActiveTo().get().equalsIgnoreCase("")) {
            data.getErrorHighlightActiveTo().set(true);
        } else if (TextUtils.isEmpty(data.getTxtTerms().get())) {
            data.getInputEvents().get().showTermsError("Write terms & conditions");
        } else {
            createOfferWithAmount();
        }
    }

    private void createOfferWithAmount() {
        boolean isEditing = toEditId != null;

        String loadingMessage = (isEditing ? "Updating" : "Creating") + " amount offer with discount.";
        final LoadingDialog dialog = new LoadingDialog(getActivity(), loadingMessage);
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data storedLoginData = UserPreferences.get().getLoggedInResponse(getActivity());
            String authToken = prefManager.getPreference(AUTH_TOKEN, "");

            DiscountWithPercentageRequest request = new DiscountWithPercentageRequest();
            request.setRestaurant_id(storedLoginData.getRestaurant_id());
            request.setUser_id(storedLoginData.getUser_id());
            if (toEditId != null) {
                request.setId(toEditId);
            }
            request.setSpend_amount_to_avail_offer(data.getTxtHowMuchCustomer().get());
            request.setDiscount_amount(data.getTxtEditDiscountAmount().get());
            request.setMax_discount_amount(data.getTxtMaxDiscount().get().trim());
            request.setUsage_total_usage(data.getTxtTotalUsage().get().trim());
            request.setUsage_per_customer(data.getTxtPerCustomerUsage().get().trim());
            request.setEasyfood_share(data.getTxtShareEasyfood().get().trim());
            request.setRestaurant_share(data.getTxtShareRestaurant().get().trim());
            request.setFranchise_share(data.getTxtShareFranchise().get().trim());
            request.setUser_app("1");
            request.setDays_available(controllerDays.getWSStringSelectedDays());
            request.setOffer_image(controllerSelectImage.base64EncodeImage());
            request.setOffer_description(data.getTxtOfferDescription().get());
            request.setEnd_date(data.getTxtEndDate().get());
            request.setStart_date(data.getTxtStartDate().get());
            request.setStart_time(data.getTxtActiveFrom().get());
            request.setEnd_time(data.getTxtActiveTo().get());
            request.setOffer_title(data.getTxtOfferTitle().get());
            request.setShow_in_swipe(data.getApplicableOnShowInSwipe().get() ? "1" : "0");

            // This can be different from the current serve style: loginData.getServe_style()
            ArrayList<String> serveStyle = new ArrayList<>();
            if (data.getApplicableOnCollection().get()) serveStyle.add("collection");
            if (data.getApplicableOnDelivery().get()) serveStyle.add("delivery");
            if (data.getApplicableOnDineIn().get()) serveStyle.add("dine-in");
            request.setAvailable_for(TextUtils.join(",", serveStyle));

            request.setTerms_conditions(data.getTxtTerms().get());
            request.setOfferDetail("menuitems");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Single<CommonResponse> service;
            if (isEditing) {
                service = apiService.updateOfferWithAmt(authToken, request);
            } else {
                service = apiService.createOfferWithAmt(authToken, request);
            }

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(service
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            if (data.isSuccess()) {
                                dialog.dismiss();

                                @Nullable DataPageNewOffer.PageFlatAmount.InputEvents inputEvents = ControllerNewOfferFlat.this.data.getInputEvents().get();
                                if (inputEvents != null) {
                                    inputEvents.showAlertDialog("Offer has been " + (isEditing ? "updated" : "created") + " successfully");
                                }

                                clearFiledData();

                            } else {
                                onError(new Throwable(data.getMessage() == null ? "Unknown reason" : data.getMessage()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Offer " + (isEditing ? "update" : "creation") + " failed", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }
}
