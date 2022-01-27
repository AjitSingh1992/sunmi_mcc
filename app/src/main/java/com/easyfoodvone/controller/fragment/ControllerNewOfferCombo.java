package com.easyfoodvone.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.separation.ObservableUtils;
import com.easyfoodvone.app_common.utility.NewConstants;
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.TempModel;
import com.easyfoodvone.app_ui.view.ViewOfferComboMeals;
import com.easyfoodvone.controller.child.ControllerNewOfferDays;
import com.easyfoodvone.controller.child.ControllerNewOfferSelectImage;
import com.easyfoodvone.dialogs.ComboMenuProductSelectorDialog;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.models.CreateComboOfferRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.MenuProducts;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerNewOfferCombo extends Fragment {

    public interface ParentInterface {
        void goBack();
    }

    private final @NonNull PrefManager prefManager;
    private final @NonNull ParentInterface parentInterface;
    private final boolean isPhone;

    private @Nullable ControllerNewOfferDays controllerDays = null;
    private @Nullable ControllerNewOfferSelectImage controllerSelectImage = null;
    private @Nullable DataPageNewOffer.PageCombo data = null;
    private @Nullable ViewOfferComboMeals view = null;

    private @Nullable List<MenuProducts.Data> menuList = null;

    private int selectedMenuPosition = 0;
    private double mainItemDiscountPrice = 0;
    private double comboTotalPrice = 0;
    private boolean isCombo = false;
    private String filter = "";
    private String mainProductId = "";
    private JSONArray mainArray;

    public ControllerNewOfferCombo(@NonNull PrefManager prefManager, @NonNull ParentInterface parentInterface, boolean isPhone) {
        this.prefManager = prefManager;
        this.parentInterface = parentInterface;
        this.isPhone = isPhone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        controllerDays = new ControllerNewOfferDays();
        controllerSelectImage = new ControllerNewOfferSelectImage(imagePickerInputEventHandler);
        data = new DataPageNewOffer.PageCombo(
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
                new ObservableField<>(true),
                new ObservableField<>(true),
                new ObservableField<>(""),
                new ObservableField<>(null),
                new ObservableArrayList<>(),
                new ObservableField<>(false),
                new ObservableField<>(true),
                new ObservableField<>(true),
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(false),
                new ObservableField<>(false));

        data.getTxtEditDiscountAmount().addOnPropertyChangedCallback(
                new ObservableUtils.TypedObserver<>(data.getTxtEditDiscountAmount(), onTxtEditDiscountAmountChange));

        view = new ViewOfferComboMeals(new LifecycleSafe(this), getActivity(), isPhone, data);
        view.onCreateView(inflater, container);

        getAllMenuProducts(false);

        return view.getRoot();
    }

    private final DataPageNewOffer.PageCombo.OutputEvents viewEventHandler
            = new DataPageNewOffer.PageCombo.OutputEvents() {

        @Override
        public void onClickEditDiscountAmount() {
            data.getInputEvents().get().showKeyboardFromEditDiscountAmount(selectedMenuPosition != 0);
        }

        @Override
        public void onSpinnerItemSelect(int position) {
            selectedMenuPosition = position;

            if (position == 0) {
                mainItemDiscountPrice = 0;
                filter = "";
                data.getTxtEditDiscountAmount().set("");
                mainProductId = "";

            } else {
                isCombo = false;
                filter = menuList.get(selectedMenuPosition - 1).getProduct_id();
                data.getTxtTotalComboAmt().set("0");
                data.getTotalAmtLayoutVisible().set(false);
                mainProductId = menuList.get(position - 1).getProduct_id();

                Log.e("main item name ", menuList.get(position - 1).getProduct_name());
                Log.e("main item price ", menuList.get(position - 1).getProduct_price());
                Log.e("Discounted price ", position + "");
            }

            data.getRecyclerSelectedMenuItemsVisible().set(false);
            data.getRecyclerHeaderLayoutVisible().set(false);
            data.getSelectedMenuItems().clear();
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
        public void onClickStartDate() {
            Constants.dateSelector1OnClick(data.getTxtStartDate(), getActivity());
        }

        @Override
        public void onClickEndDate() {
            if (data.getTxtStartDate().get().equalsIgnoreCase("Start Date") || data.getTxtStartDate().get().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please Select start date first", Toast.LENGTH_SHORT).show();
            } else {
                Constants.endDateSelectorClick(data.getTxtEndDate(), getActivity(), data.getTxtStartDate().get());
            }
        }

        @Override
        public void onClickSelectComboItems() {
            if (selectedMenuPosition > 0) {
                getAllMenuProducts(true);
            } else {
                Toast.makeText(getActivity(), "Please select menu item first", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClickCancel() {
            parentInterface.goBack();
        }

        @Override
        public void onClickSave() {
            createOffer();
        }

        @Override
        public void onClickImageTop() {
            data.getImagePickerData().getOutputEvents().onClickSelectImage(getActivity());
        }
    };

    private final Observer<String> onTxtEditDiscountAmountChange = new Observer<String>() {
        @Override
        public void onChanged(String amount) {
            if ( ! TextUtils.isEmpty(amount)) {
                if (Integer.parseInt(amount) > 100) {
                    data.getTxtEditDiscountAmount().set("");
                    Toast.makeText(getActivity(), "Discount could not be greater than 100", Toast.LENGTH_SHORT).show();

                } else {
                    if (selectedMenuPosition > 0) {
                        MenuProducts.Data selectedProduct = menuList.get(selectedMenuPosition - 1);

                        double selectedProductPrice = Double.parseDouble(selectedProduct.getProduct_price());
                        double afterEditDiscountAmount = Double.parseDouble(amount);

                        mainItemDiscountPrice = selectedProductPrice - ((selectedProductPrice * afterEditDiscountAmount) / 100);
                        data.getTxtTotalComboAmt().set(NewConstants.POUND + (new DecimalFormat("##.##").format(comboTotalPrice + mainItemDiscountPrice)));

                        Log.e("main item name ", selectedProduct.getProduct_name());
                        Log.e("main item price ", selectedProduct.getProduct_price());
                        Log.e("Disconuted price ", selectedMenuPosition - 1 + "");
                    }
                }

            } else {
                if (selectedMenuPosition > 0) {
                    MenuProducts.Data selectedProduct = menuList.get(selectedMenuPosition - 1);

                    mainItemDiscountPrice = Double.parseDouble(selectedProduct.getProduct_price());
                    data.getTxtTotalComboAmt().set(NewConstants.POUND + (new DecimalFormat("##.##").format(comboTotalPrice + mainItemDiscountPrice)));
                }
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
            Fragment fragment = ControllerNewOfferCombo.this;
            fragment.startActivityForResult(intent, requestCode);
        }
    };

    private void getAllMenuProducts(final boolean genuineClick) {
        try {
            LoginResponse.Data loginData = UserPreferences.get().getLoggedInResponse(getActivity());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String authToken = prefManager.getPreference(AUTH_TOKEN,"");

            CommonRequest request = new CommonRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setFilter(filter);

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getProducts(authToken,request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MenuProducts>() {
                        @Override
                        public void onSuccess(MenuProducts data) {
                            menuList = data.getData();

                            Log.e("Menu product ", data.getData().toString());
                            if (genuineClick) {
                                ComboMenuProductSelectorDialog dialog = new ComboMenuProductSelectorDialog(
                                        getActivity(),
                                        data.getData(),
                                        comboDialogButtonClickListener,
                                        isPhone);
                                dialog.setCancelable(false);
                                dialog.show();

                            } else {
                                if (data.getData().size() > 0) {
                                    String[] menuArray = new String[data.getData().size() + 1];
                                    menuArray[0] = "Select menu item";
                                    for (int i = 0; i < data.getData().size(); i++) {
                                        String productName = data.getData().get(i).getProduct_name();
                                        menuArray[i + 1] = productName != null ? productName : "";
                                    }

                                    ControllerNewOfferCombo.this.data.getSpinnerMainItems().set(menuArray);

                                } else {
                                    String[] menuArray = new String[1];
                                    menuArray[0] = "Select menu item";
                                    ControllerNewOfferCombo.this.data.getSpinnerMainItems().set(menuArray);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createOffer() {
        data.getErrorHighlightStartDate().set(false);
        data.getErrorHighlightEndDate().set(false);
        data.getErrorHighlightActiveFrom().set(false);
        data.getErrorHighlightActiveTo().set(false);

        if (TextUtils.isEmpty(data.getTxtOfferTitle().get())) {
            data.getInputEvents().get().showOfferTitleError("Enter offer name");
        } else if (TextUtils.isEmpty(data.getTxtOfferDescription().get())) {
            data.getInputEvents().get().showOfferDescriptionError("Write description");
        } else if (TextUtils.isEmpty(data.getTxtEditDiscountAmount().get())) {
            data.getInputEvents().get().showDiscountAmountError("Enter discount amount");
        } else if (TextUtils.isEmpty(data.getTxtHowMuchCustomer().get())) {
            data.getInputEvents().get().showHowMuchCustomerError("Enter minimum Order amount");
        } else if (selectedMenuPosition == 0) {
            Toast.makeText(getActivity(), "Please select menu item", Toast.LENGTH_SHORT).show();
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
        } else if (!isCombo) {
            Toast.makeText(getActivity(), "Nothing found like combo", Toast.LENGTH_SHORT).show();
        } else {
            createComboOffer();
        }
    }

    private final ComboMenuProductSelectorDialog.OnDialogButtonClickListener comboDialogButtonClickListener = new ComboMenuProductSelectorDialog.OnDialogButtonClickListener() {
        @Override
        public void onOkClick(String ids, List<MenuProducts.Data> selectedItemList) {
            mainArray = new JSONArray();
            JSONArray childArray = null;
            JSONObject parentObject;
            JSONObject subChild;

            List<TempModel> tm = new ArrayList<>();

            for (int i = 0; i < selectedItemList.size(); i++) {
                parentObject = new JSONObject();
                childArray = new JSONArray();

                if (selectedItemList.get(i).getChecked()
                        && selectedItemList.get(i).getProduct_sizes() != null
                        && selectedItemList.get(i).getProduct_sizes().size() == 0
                        && !selectedItemList.get(i).getQuantity().equals("")
                        && !selectedItemList.get(i).getPrice().equals("")) {

                    tm.add(new TempModel(
                            selectedItemList.get(i).getProduct_id(),
                            selectedItemList.get(i).getProduct_name(),
                            selectedItemList.get(i).getProduct_price(),
                            selectedItemList.get(i).getPrice(),
                            selectedItemList.get(i).getQuantity(),
                            selectedItemList.get(i).getChecked()));

                    try {
                        parentObject.put("product_id", selectedItemList.get(i).getProduct_id());
                        parentObject.put("product_name", selectedItemList.get(i).getProduct_name());
                        parentObject.put("product_price", selectedItemList.get(i).getProduct_price());
                        parentObject.put("price", selectedItemList.get(i).getPrice());
                        parentObject.put("quantity", selectedItemList.get(i).getQuantity());
                        parentObject.put("product_sizes", childArray);

                        mainArray.put(parentObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (selectedItemList.get(i).getChecked() && selectedItemList.get(i).getProduct_sizes() != null) {
                    childArray = new JSONArray();

                    List<MenuProducts.Data.SubProducts> items = selectedItemList.get(i).getProduct_sizes();
                    List<MenuProducts.Data.SubProducts> t = new ArrayList<>();

                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).getChecked() && !items.get(j).getQuantity().equals("") && !items.get(j).getPrice().equals("")) {
                            t.add(items.get(j));
                        }
                    }
                    if (t.size() > 0) {
                        double price = 0;
                        double qty = 0;

                        for (int j = 0; j < t.size(); j++) {
                            subChild = new JSONObject();
                            try {
                                subChild.put("size_id", t.get(j).getSize_id());
                                subChild.put("size_name", t.get(j).getSize_name());
                                subChild.put("sell_price", t.get(j).getSell_price());
                                subChild.put("quantity", t.get(j).getQuantity());
                                subChild.put("price", t.get(j).getPrice());

                                qty = qty + Double.parseDouble(t.get(j).getQuantity());
                                price = price + Double.parseDouble(t.get(j).getPrice());

                                childArray.put(subChild);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            tm.add(new TempModel(t.get(j).getSize_id(),
                                    t.get(j).getSize_name(),
                                    t.get(j).getSell_price(),
                                    t.get(j).getPrice(),
                                    t.get(j).getQuantity(),
                                    t.get(j).getChecked()
                            ));
                        }

                        try {
                            parentObject.put("product_id", selectedItemList.get(i).getProduct_id());
                            parentObject.put("product_name", selectedItemList.get(i).getProduct_name());
                            parentObject.put("product_price", selectedItemList.get(i).getProduct_price());
                            parentObject.put("price", price + ""); // getPrice()
                            parentObject.put("quantity", qty + ""); // getQuantity()
                            parentObject.put("product_sizes", childArray);

                            mainArray.put(parentObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            Log.e("Json format", ">>>>>>>>>>>>>>>>>>>" + mainArray.toString());

            if (tm.size() <= 0) {
                data.getTotalAmtLayoutVisible().set(false);
                comboTotalPrice = 0;
                isCombo = false;
            } else {
                isCombo = true;
                data.getTotalAmtLayoutVisible().set(true);
                //TODO: calculate combo total price:
                for (int i = 0; i < tm.size(); i++) {
                    comboTotalPrice = comboTotalPrice + Double.parseDouble(tm.get(i).getPrice());
                }
                data.getTxtTotalComboAmt().set(NewConstants.POUND + (new DecimalFormat("##.##").format(comboTotalPrice + mainItemDiscountPrice)));
            }

            data.getSelectedMenuItems().clear();
            data.getSelectedMenuItems().addAll(tm);

            data.getRecyclerSelectedMenuItemsVisible().set(true);
            data.getRecyclerHeaderLayoutVisible().set(true);
            data.getTotalAmtLayoutVisible().set(true);
        }

        @Override
        public void onCancelClick() {
            data.getRecyclerSelectedMenuItemsVisible().set(false);
            data.getRecyclerHeaderLayoutVisible().set(false);
            data.getTotalAmtLayoutVisible().set(false);
        }
    };

    private void clearFiledData() {
        data.getTxtOfferDescription().set("");
        data.getTxtOfferTitle().set("");
        data.getTxtStartDate().set(""); // TODO fix this back to default value
        data.getTxtEndDate().set(""); // TODO fix this back to default value
        data.getTxtActiveFrom().set(""); // TODO fix this back to default value
        data.getTxtActiveTo().set(""); // TODO fix this back to defaut value

        data.getTxtTerms().set("");

        // Clear every "day" checkbox
        data.getDaysData().getAllBox().set(true);
        data.getDaysData().getAllBox().set(false);

        data.getImagePickerData().getPickedImage().set(null);

        data.getSelectedMenuItems().clear();

        data.getRecyclerSelectedMenuItemsVisible().set(false);
        data.getRecyclerHeaderLayoutVisible().set(false);
        data.getTotalAmtLayoutVisible().set(false);

        data.getTxtEditDiscountAmount().set("");
        data.getSpinnerMainItems().set(null);
        data.getTxtHowMuchCustomer().set("");
    }

    private void createComboOffer() {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Creating offer with percentage.");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginResponse.Data loginData = UserPreferences.get().getLoggedInResponse(getActivity());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String authToken = prefManager.getPreference(AUTH_TOKEN,"");

            CreateComboOfferRequest request = new CreateComboOfferRequest();
            request.setRestaurant_id(loginData.getRestaurant_id());
            request.setUser_id(loginData.getUser_id());
            request.setSpend_amount_to_avail_offer(data.getTxtHowMuchCustomer().get());
            request.setDiscount_percentage(data.getTxtEditDiscountAmount().get());
            request.setDays_available(controllerDays.getWSStringSelectedDays());
            request.setOffer_image(controllerSelectImage.base64EncodeImage());
            request.setOffer_description(data.getTxtOfferDescription().get());
            request.setEnd_date(data.getTxtEndDate().get());
            request.setStart_date(data.getTxtStartDate().get());
            request.setStart_time(data.getTxtActiveFrom().get());
            request.setEnd_time(data.getTxtActiveTo().get());
            request.setOffer_title(data.getTxtOfferTitle().get());

            // This can be different from the current serve style: loginData.getServe_style()
            ArrayList<String> serveStyle = new ArrayList<>();
            if (data.getApplicableOnCollection().get()) serveStyle.add("collection");
            if (data.getApplicableOnDelivery().get()) serveStyle.add("delivery");
            request.setAvailable_for(TextUtils.join(",", serveStyle));

            request.setTerms_conditions(data.getTxtTerms().get());
            request.setCombo_total(data.getTxtTotalComboAmt().get().replace(NewConstants.POUND, ""));
            request.setMain_product_id(mainProductId);
            request.setOffer_details(mainArray.toString());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.createComboOffer(authToken,request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            if (data.isSuccess()) {
                                dialog.dismiss();

                                @Nullable DataPageNewOffer.PageCombo.InputEvents inputEvents = ControllerNewOfferCombo.this.data.getInputEvents().get();
                                if (inputEvents != null) {
                                    inputEvents.showAlertDialog("Offer has been created successfully");
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
