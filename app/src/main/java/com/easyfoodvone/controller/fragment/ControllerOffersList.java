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
import com.easyfoodvone.app_common.viewdata.DataPageListOfOffers;
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.OffersResponse;
import com.easyfoodvone.app_ui.view.ViewListOfOffers;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerOffersList extends Fragment {
    public interface ParentInterface {
        void goToSpendXGetDiscount();
        void goToFlatDiscountAmount(@Nullable DataPageNewOffer.ToEditFlatOrPercent toEdit);
        void goToPercentageDiscount(@Nullable DataPageNewOffer.ToEditFlatOrPercent toEdit);
        void goToComboMealsOffer();
    }

    private final @NonNull ParentInterface parentInterface;
    private final @NonNull PrefManager prefManager;
    private final boolean isPhone;

    private DataPageListOfOffers viewData;

    private @NonNull WS_Filter wsFilter = WS_Filter.NONE;
    private @NonNull WS_OfferType wsOfferType = WS_OfferType.NONE;

    public ControllerOffersList(@NonNull ParentInterface parentInterface, @NonNull PrefManager prefManager, boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageListOfOffers(
                outputEvents,
                new ObservableField<>(null),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableArrayList<>());

        ViewListOfOffers view = new ViewListOfOffers(
                viewData,
                new LifecycleSafe(this),
                inflater,
                getChildFragmentManager(),
                isPhone);
        view.onCreateView(container);

        getOffers(wsFilter, wsOfferType);

        return view.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        getOffers(wsFilter, wsOfferType);
    }

    private @NonNull DataPageNewOffer.ToEditFlatOrPercent parseItemToEdit(@NonNull OffersResponse.Data.Offers editItem) {
        @Nullable String availableFor = editItem.getAvailable_for();
        @NonNull List<String> availableForList = TextUtils.isEmpty(availableFor) ? Collections.emptyList() : Arrays.asList(availableFor.split("\\s*,\\s*"));

        @Nullable String daysAvailable = editItem.getDays_available();
        @NonNull List<String> daysAvailableList = TextUtils.isEmpty(daysAvailable) ? Collections.emptyList() : Arrays.asList(daysAvailable.split("\\s*,\\s*"));
        boolean isAllDays = TextUtils.isEmpty(daysAvailable) ? false : "All Days".equalsIgnoreCase(daysAvailable.trim());

        return new DataPageNewOffer.ToEditFlatOrPercent(
                editItem.getId(),
                isAllDays || daysAvailableList.contains("Mon"),
                isAllDays || daysAvailableList.contains("Tue"),
                isAllDays || daysAvailableList.contains("Wed"),
                isAllDays || daysAvailableList.contains("Thu"),
                isAllDays || daysAvailableList.contains("Fri"),
                isAllDays || daysAvailableList.contains("Sat"),
                isAllDays || daysAvailableList.contains("Sun"),
                editItem.getOffer_title(),
                editItem.getOffer_description(),
                editItem.getStart_date(),
                editItem.getStart_time(),
                editItem.getEnd_date(),
                editItem.getEnd_time(),
                editItem.getSpend_amount_to_avail_offer(),
                editItem.getDiscount_amount(),
                editItem.getMax_discount_amount(),
                editItem.getUsage_per_customer(),
                editItem.getUsage_total_usage(),
                availableForList.contains("delivery"),
                availableForList.contains("collection"),
                availableForList.contains("dine-in"),
                "1".equals(editItem.getShow_in_swipe()),
                editItem.getEasyfood_share(),
                editItem.getRestaurant_share(),
                editItem.getFranchise_share(),
                editItem.getTerms_conditions());
                // .getUser_app()
                // .getOffer_details()
    }

    private final DataPageListOfOffers.OutputEvents outputEvents = new DataPageListOfOffers.OutputEvents() {
        @Override
        public void onClickDeleteRow(final int deletePosition, @NonNull final OffersResponse.Data.Offers deleteItem) {
            if (deleteItem.getTotal_offer_used() != null && deleteItem.getTotal_offer_used().equalsIgnoreCase("0")) {
                viewData.getInputEvents().get().showDeleteConfirmation(
                        deleteItem,
                        () -> deleteOffers(deletePosition, deleteItem));
            } else {
                boolean activated = deleteItem.getStatus().equalsIgnoreCase("1");
                viewData.getInputEvents().get().showChangeActivationDialog(
                    false,
                    activated,
                    deleteItem,
                    () -> setOfferActive(deleteItem.getId(), ! activated));
            }
        }

        @Override
        public void onClickEditRow(int editPosition, @NonNull OffersResponse.Data.Offers editItem) {
            if (editItem.getTotal_offer_used() != null && editItem.getTotal_offer_used().equalsIgnoreCase("0")) {
                boolean isPercentageDiscount = "discount_percentage".equalsIgnoreCase(editItem.getOffer_type());
                boolean isFlatDiscount = "flat_offer".equalsIgnoreCase(editItem.getOffer_type());
                if (isPercentageDiscount) {
                    viewData.getInputEvents().get().showEditConfirmation(
                            editItem,
                            () -> parentInterface.goToPercentageDiscount(parseItemToEdit(editItem)));
                } else if (isFlatDiscount) {
                    viewData.getInputEvents().get().showEditConfirmation(
                            editItem,
                            () -> parentInterface.goToFlatDiscountAmount(parseItemToEdit(editItem)));
                } else {
                    Toast.makeText(getActivity(), "Offer type " + editItem.getOffer_type() + " cannot be edited", Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                boolean activated = editItem.getStatus().equalsIgnoreCase("1");
                viewData.getInputEvents().get().showChangeActivationDialog(
                        true,
                        activated,
                        editItem,
                        () -> setOfferActive(editItem.getId(), ! activated));
            }
        }

        @Override
        public void onClickAddOffer() {
            viewData.getInputEvents().get().showAddOfferDialog(
                    parentInterface::goToSpendXGetDiscount,
                    () -> parentInterface.goToFlatDiscountAmount(null),
                    () -> parentInterface.goToPercentageDiscount(null),
                    parentInterface::goToComboMealsOffer);
        }

        @Override
        public void onClickRunningOffer() {
            getOffers(WS_Filter.RUNNING, WS_OfferType.NONE);
        }

        @Override
        public void onClickExpiredOffer() {
            getOffers(WS_Filter.EXPIRED, WS_OfferType.NONE);
        }

        @Override
        public void onClickPercentageDiscount() {
            getOffers(WS_Filter.NONE, WS_OfferType.DISCOUNT_PERCENTAGE);
        }

        @Override
        public void onClickFlatDiscount() {
            getOffers(WS_Filter.NONE, WS_OfferType.FLAT_OFFER);
        }

        @Override
        public void onClickComboDiscount() {
            getOffers(WS_Filter.NONE, WS_OfferType.COMBO_OFFER);
        }
    };

    private enum WS_Filter {
        NONE(""),
        RUNNING("running"),
        EXPIRED("expired");

        final String wsString;

        WS_Filter(String wsString) {
            this.wsString = wsString;
        }
    }

    private enum WS_OfferType {
        NONE(""),
        DISCOUNT_PERCENTAGE("discount_percentage"),
        FLAT_OFFER("flat_offer"),
        COMBO_OFFER("combo_offer");

        final String wsString;


        WS_OfferType(String wsString) {
            this.wsString = wsString;
        }
    }

    private void getOffers(WS_Filter filter, WS_OfferType offerType) {
        wsFilter = filter;
        wsOfferType = offerType;

        final LoadingDialog dialog;
        if (wsFilter == WS_Filter.RUNNING && wsOfferType == WS_OfferType.NONE)
            dialog = new LoadingDialog(getActivity(), "Loading running offers");
        else if (wsFilter == WS_Filter.EXPIRED && wsOfferType == WS_OfferType.NONE)
            dialog = new LoadingDialog(getActivity(), "Loading expired offers");
        else if (wsFilter == WS_Filter.NONE && wsOfferType == WS_OfferType.DISCOUNT_PERCENTAGE)
            dialog = new LoadingDialog(getActivity(), "Loading discount with percentage offers");
        else if (wsFilter == WS_Filter.NONE && wsOfferType == WS_OfferType.FLAT_OFFER)
            dialog = new LoadingDialog(getActivity(), "Loading discount with amount offers");
        else if (wsFilter == WS_Filter.NONE && wsOfferType == WS_OfferType.COMBO_OFFER)
            dialog = new LoadingDialog(getActivity(), "Loading combo offers");
        else
            dialog = new LoadingDialog(getActivity(), "Loading offers");

        dialog.setCancelable(false);
        dialog.show();

        try {
            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(UserPreferences.get().getLoggedInResponse(getActivity()).getRestaurant_id());
            request.setFilter(wsFilter.wsString);
            request.setOffer_type(wsOfferType.wsString);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getOffers(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OffersResponse>() {
                        @Override
                        public void onSuccess(OffersResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess() && data.getData() != null) {
                                viewData.getRunningOffers().set(data.getData().getTotal_running_offers() != null || !data.getData().getTotal_running_offers().equals("") ? data.getData().getTotal_running_offers() : "0");
                                viewData.getExpiredOffers().set(data.getData().getTotal_expired_offers() != null || !data.getData().getTotal_expired_offers().equals("") ? data.getData().getTotal_expired_offers() : "0");
                                viewData.getPercentageDiscount().set(data.getData().getTotal_percentage_discount_offers() != null || !data.getData().getTotal_percentage_discount_offers().equals("") ? data.getData().getTotal_percentage_discount_offers() : "0");
                                viewData.getFlatDiscount().set(data.getData().getTotal_flat_discount_offers() != null || !data.getData().getTotal_flat_discount_offers().equals("") ? data.getData().getTotal_flat_discount_offers() : "0");
                                viewData.getComboDiscount().set(data.getData().getTotal_combo_discount_offers() != null || !data.getData().getTotal_combo_discount_offers().equals("") ? data.getData().getTotal_combo_discount_offers() : "0");

                                viewData.getOffersList().clear();
                                if (data.getData().getOffers() != null) {
                                    ArrayList<OffersResponse.Data.Offers> filteredList = new ArrayList<>();
                                    for (@Nullable OffersResponse.Data.Offers offer : data.getData().getOffers()) {
                                        if (offer != null) {
                                            filteredList.add(offer);
                                        }
                                    }
                                    viewData.getOffersList().addAll(filteredList);
                                }

                            } else {
                                Toast.makeText(getActivity(), "Products not found", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteOffers(final int deletePosition, final OffersResponse.Data.Offers deleteItem) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Deleting");
        dialog.setCancelable(false);
        dialog.show();

        try {
            CommonRequest request = new CommonRequest();
            request.setOffer_id(deleteItem.getId());

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.deleteOffer(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            viewData.getOffersList().remove(deletePosition);

                            Toast.makeText(getActivity(), "Offer deleted", Toast.LENGTH_LONG).show();

                            getOffers(wsFilter, wsOfferType);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setOfferActive(final String offerId, final boolean activated) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Deactivating");
        dialog.setCancelable(false);
        dialog.show();

        try {
            CommonRequest request = new CommonRequest();
            request.setOffer_id(offerId);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("offer_id", offerId);
            jsonObject.addProperty("status", activated ? "1" : "0");

            String authToken = prefManager.getPreference(AUTH_TOKEN, "");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.deactivateOffer(authToken, jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Offer deactivated", Toast.LENGTH_LONG).show();

                            // We can't simply remove the offer from viewData.getOffersList() as it is
                            // just deactivated, and still exists in some of the filter types
                            getOffers(wsFilter, wsOfferType);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_LONG).show();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }
}
