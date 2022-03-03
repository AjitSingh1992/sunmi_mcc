package com.easyfoodvone.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyfoodvone.MySingleTon;
import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataIncludeHeader;
import com.easyfoodvone.app_common.viewdata.DataIncludeOrdersHeader;
import com.easyfoodvone.app_common.viewdata.DataIncludeSideMenu;
import com.easyfoodvone.app_common.viewdata.DataPageHome;
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.MenuCategoryList;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.app_ui.view.ViewHome;
import com.easyfoodvone.fragments.ProfileFragment;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.RestaurantOpenCloseRequest;
import com.easyfoodvone.models.RestaurantOpenCloseResponse;
import com.easyfoodvone.models.ServeStyleResponse;
import com.easyfoodvone.models.SetServeStyleRequest;
import com.easyfoodvone.utility.ApplicationContext;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerRootWithAuth extends Fragment {

    public interface ParentInterface {
        void goToLoginActivity();
        void openAppStoreListing();
        void exitApp();
    }

    private final boolean isPhone;
    private final ParentInterface parentInterface;
    private final PrefManager prefManager;
    private final UserPreferences userPreferences;
    private final LocalBroadcastManager localBroadcastManager;

    private DataPageHome data;

    private LoginResponse.Data baseDetails;
    private boolean isDashBoard = true;
    private RoundedDialogFragment dialogNewOrder;

    private int fragCount = 1;

    public ControllerRootWithAuth(
            boolean isPhone,
            @NonNull ParentInterface parentInterface,
            @NonNull PrefManager prefManager,
            @NonNull UserPreferences userPreferences,
            @NonNull LocalBroadcastManager localBroadcastManager) {
        this.isPhone = isPhone;
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.userPreferences = userPreferences;
        this.localBroadcastManager = localBroadcastManager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseDetails = userPreferences.getLoggedInResponse(getActivity());

        DataIncludeHeader dataHeader = new DataIncludeHeader(
                "", // We use DataIncludeOrdersHeader.pageTitle instead of this when wanting a page with the extra orders header fields
                "",
                "",
                "",
                true,
                headerEventHandler);
        DataIncludeSideMenu dataSideMenu = new DataIncludeSideMenu(
                new ObservableField<>(false),
                new ObservableField<>(false),
                sideMenuEventHandler);
        // We use DataIncludeHeader.pageTitle instead of this when hiding the extra orders header);
        DataIncludeOrdersHeader dataOrdersHeader = new DataIncludeOrdersHeader(
                new ObservableField<>("page title here"));
        data = new DataPageHome(
                dataHeader,
                dataSideMenu,
                dataOrdersHeader,
                baseDetails.getLogo(),
                viewEventHandler,
                new ObservableField<>(null),
                new ObservableField<>(true),
                new ObservableField<>(0));

        loadHomeFragment();

        setRestaurantDetails();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewHome view = new ViewHome(new LifecycleSafe(this));
        view.onCreateView(inflater, container, data, isPhone);

        return view.getUi().getBinding().getRoot();
    }

    private final DataPageHome.OutputEvents viewEventHandler = new DataPageHome.OutputEvents() {

    };

    private final DataIncludeHeader.OutputEvents headerEventHandler = new DataIncludeHeader.OutputEvents() {
        @Override
        public void onClickBack() {
            onBackPressed();
        }

        @Override
        public void onClickBurger() {
            data.getInputEvents().get().toggleSideDrawer();
        }
    };

    private final DataIncludeSideMenu.OutputEvents sideMenuEventHandler = new DataIncludeSideMenu.OutputEvents() {
        @Override
        public void onClickClose() {
            data.getInputEvents().get().toggleSideDrawer();
        }

        @Override
        public void onClickOpenToggle() {
            boolean isOpen = data.getDataSideMenu().isRestaurantOpen().get();
            if (isOpen) {
                openRestaurant("closed", baseDetails.getRestaurant_id());
            } else {
                openRestaurant("open", baseDetails.getRestaurant_id());
            }
        }

        @Override
        public void onClickItem(@NonNull DataIncludeSideMenu.SideMenuItem item) {
            boolean deliverySettingsEnabled = data.getDataSideMenu().getDeliverySettingsEnabled().get();
            if ( ! deliverySettingsEnabled && item == DataIncludeSideMenu.SideMenuItem.DELIVERY_SETTINGS) {
                return;
            }

            openFragment(item);
            data.getInputEvents().get().toggleSideDrawer();
        }
    };

    private void openFragment(@NonNull DataIncludeSideMenu.SideMenuItem item) {
        if (item == DataIncludeSideMenu.SideMenuItem.SERVE_STYLE) {
            // Kick the user back to the home page - simple solution to needing a reload of the profile page
           // clearAllPreviousFragments();
           // loadHomeFragment();
            getServeStyle();

        } else if (item == DataIncludeSideMenu.SideMenuItem.ORDERS) {
            isDashBoard = true;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("Orders");
            data.getDataOrdersHeader().getPageTitle().set("");
            MySingleTon.getInstance().setFragmentName("");

            clearAllPreviousFragments();
            //loadHomeFragment();

        } else if (item == DataIncludeSideMenu.SideMenuItem.MENU) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Menu");
            MySingleTon.getInstance().setFragmentName("Menu");
            ControllerMenu fragment  =  new ControllerMenu(restaurantMenuParentInterface, prefManager, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        } else if (item == DataIncludeSideMenu.SideMenuItem.DELIVERY_SETTINGS) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(false);
            data.getDataHeader().getPageTitle().set("Set Delivery Charges");
            data.getDataOrdersHeader().getPageTitle().set("");
            MySingleTon.getInstance().setFragmentName("");

            ControllerDeliverySetting fragment  =  new ControllerDeliverySetting(getActivity(), prefManager, userPreferences, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }



        } else if (item == DataIncludeSideMenu.SideMenuItem.RESTAURANT_TIMINGS) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Restaurant Timings");
            MySingleTon.getInstance().setFragmentName("");

            ControllerRestaurantTimings fragment  =  new ControllerRestaurantTimings(prefManager, userPreferences, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }


        } else if (item == DataIncludeSideMenu.SideMenuItem.ORDERS_REPORT) {//
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Order Report");
            MySingleTon.getInstance().setFragmentName("");
            MySingleTon.getInstance().setYear(0);
            MySingleTon.getInstance().setDay(0);
            MySingleTon.getInstance().setMonth(0);
            ControllerOrderReport fragment  =  new ControllerOrderReport(orderReportParentInterface, prefManager, userPreferences, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }



        } else if (item == DataIncludeSideMenu.SideMenuItem.REVENUE_REPORT) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Revenue Report");
            MySingleTon.getInstance().setFragmentName("");
            MySingleTon.getInstance().setYear(0);
            MySingleTon.getInstance().setDay(0);
            MySingleTon.getInstance().setMonth(0);
            ControllerRevenueReport fragment  =  new ControllerRevenueReport(revenueReportParentInterface, prefManager, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }



        } else if (item == DataIncludeSideMenu.SideMenuItem.OFFERS) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("List of Offers");
            MySingleTon.getInstance().setFragmentName("List of Offers");

            ControllerOffersList fragment  =  new ControllerOffersList(listOfOffersInterface, prefManager, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }


        } else if (item == DataIncludeSideMenu.SideMenuItem.REVIEW_N_RATINGS) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Ratings & Reviews");
            MySingleTon.getInstance().setFragmentName("");

            ControllerRatingReview fragment  =  new ControllerRatingReview(getActivity(), prefManager, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }




        } else if (item == DataIncludeSideMenu.SideMenuItem.CHARITY) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(false);
            data.getDataHeader().getPageTitle().set("Charity");
            data.getDataOrdersHeader().getPageTitle().set("");
            MySingleTon.getInstance().setFragmentName("Charity");

            ControllerCharityHome fragment = new ControllerCharityHome(
                    charityPageParentInterface,
                    prefManager,
                    LocalBroadcastManager.getInstance(requireActivity()),
                    isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }



        } else if (item == DataIncludeSideMenu.SideMenuItem.PROFILE) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Profile");
            MySingleTon.getInstance().setFragmentName("");

            ProfileFragment fragment =  new ProfileFragment(isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }



        } else if (item == DataIncludeSideMenu.SideMenuItem.CHANGE_PASSWORD) {
            clearAllPreviousFragments();
            isDashBoard = false;
            data.getShowRestaurantHeader().set(false);
            data.getDataHeader().getPageTitle().set("Change Password");
            data.getDataOrdersHeader().getPageTitle().set("");
            MySingleTon.getInstance().setFragmentName("");

            ControllerChangePassword fragment =  new ControllerChangePassword(changePasswordParentInterface, prefManager, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }



        } else if (item == DataIncludeSideMenu.SideMenuItem.LOGOUT) {
            orderListParentInterface.stopNotificationSound();
            logoutNow();
        }
    }

    public void clearAllPreviousFragments(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
    private final ControllerMenu.ParentInterface restaurantMenuParentInterface = new ControllerMenu.ParentInterface() {
        @Override
        public void openMenuDetails(MenuCategoryList.MenuCategories item) {
            if (item == null) {
                return;
            }


            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Menu Details");
            ControllerMenuDetails fragment  =  new ControllerMenuDetails(
                    item,
                    prefManager,
                    userPreferences.getLoggedInResponse(getActivity()),
                    isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        }
    };

    private final ControllerOffersList.ParentInterface listOfOffersInterface = new ControllerOffersList.ParentInterface() {
        @Override
        public void goToSpendXGetDiscount() {
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Spend X and get X% Discount");
            ControllerNewOfferSpendX fragment  =  new ControllerNewOfferSpendX(prefManager, offerSpendXGetDiscountInterface, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        }

        @Override
        public void goToFlatDiscountAmount(@Nullable DataPageNewOffer.ToEditFlatOrPercent toEdit) {
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Money Off Discount");
            ControllerNewOfferFlat fragment  =  new ControllerNewOfferFlat(prefManager, offerFlatDiscountAmountInterface, isPhone, toEdit);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        }

        @Override
        public void goToPercentageDiscount(@Nullable DataPageNewOffer.ToEditFlatOrPercent toEdit) {
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Percentage Discount");
            ControllerNewOfferPercent fragment  =  new ControllerNewOfferPercent(prefManager, offerPercentDiscountInterface, isPhone, toEdit);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }


        }

        @Override
        public void goToComboMealsOffer() {
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Combo Discount");
            ControllerNewOfferCombo fragment  =  new ControllerNewOfferCombo(prefManager, offerComboMealsInterface, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }


        }
    };

    private final ControllerNewOfferSpendX.ParentInterface offerSpendXGetDiscountInterface = new ControllerNewOfferSpendX.ParentInterface() {
        @Override
        public void goBack() {
            openFragment(DataIncludeSideMenu.SideMenuItem.OFFERS);
        }
    };

    private final ControllerNewOfferFlat.ParentInterface offerFlatDiscountAmountInterface = new ControllerNewOfferFlat.ParentInterface() {
        @Override
        public void goBack() {
            openFragment(DataIncludeSideMenu.SideMenuItem.OFFERS);
        }
    };

    private final ControllerNewOfferCombo.ParentInterface offerComboMealsInterface = new ControllerNewOfferCombo.ParentInterface() {
        @Override
        public void goBack() {
            openFragment(DataIncludeSideMenu.SideMenuItem.OFFERS);
        }
    };

    private final ControllerNewOfferPercent.ParentInterface offerPercentDiscountInterface = new ControllerNewOfferPercent.ParentInterface() {
        @Override
        public void goBack() {
            openFragment(DataIncludeSideMenu.SideMenuItem.OFFERS);
        }
    };

    private final ControllerOrderList.ParentInterface orderListParentInterface = new ControllerOrderList.ParentInterface() {
        @Override
        public LoginResponse.Data getLoginData() {
            return userPreferences.getLoggedInResponse(getActivity());
        }

        @Override
        public void showToast(@Nullable String message) {
            Toast.makeText(getActivity(), message != null ? message : "Unexpected description", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void showToastLong(@NonNull String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void openAppStoreListing() {
            parentInterface.openAppStoreListing();
        }

        @Override
        public void stopNotificationSound() {
            ApplicationContext.getInstance().stopNotificationSound();
        }

        @Override
        public void updateStoreOpen(boolean isOpen) {
            data.getDataSideMenu().isRestaurantOpen().set(isOpen);
        }

        @Override
        public void onDeliveryPartnerFetch(@NonNull String partner) {
            boolean disabled = partner.equalsIgnoreCase("easyfood");
            data.getDataSideMenu().getDeliverySettingsEnabled().set( ! disabled);
        }
    };

    private final ControllerOrderReport.ParentInterface orderReportParentInterface = new ControllerOrderReport.ParentInterface() {
        @Override
        public void goToHome() {
            loadHomeFragment();
        }

        @Override
        public void showToastLong(@NonNull String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
                    .show();
        }
    };

    private final ControllerRevenueReport.ParentInterface revenueReportParentInterface = new ControllerRevenueReport.ParentInterface() {
        @Override
        public void goToHome() {
            loadHomeFragment();
        }
    };

    private final ControllerChangePassword.ParentInterface changePasswordParentInterface = new ControllerChangePassword.ParentInterface() {
        @Override
        public void goToHome() {
            loadHomeFragment();
        }
    };

    private final ControllerCharityHome.ParentInterface charityPageParentInterface = new ControllerCharityHome.ParentInterface() {
        @Override
        public void goToDonatePage() {

            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Charity");


            ControllerCharityDonate fragment = new ControllerCharityDonate(donateParentInterface, prefManager, isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }



        }

        @Override
        public void goToCharityCancelPage(String charityId) {
            isDashBoard = false;
            data.getShowRestaurantHeader().set(true);
            data.getDataHeader().getPageTitle().set("");
            data.getDataOrdersHeader().getPageTitle().set("Charity");


            ControllerCharityCancel fragment = new ControllerCharityCancel(charityCancelParentInterface, prefManager, isPhone, charityId);

            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        }

        @Override
        public void goToCharitySuccessPage() {

            ControllerCharitySuccess fragment = new ControllerCharitySuccess(charitySuccessParentInterface, isPhone, true);

            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }


        }

        @Override
        public void setBackAction(int backNo) {
            ControllerRootWithAuth.this.setBackAction(backNo);
        }
    };

    private final ControllerCharityCancel.ParentInterface charityCancelParentInterface = new ControllerCharityCancel.ParentInterface() {
        @Override
        public void goToCharitySuccessPage() {



            ControllerCharitySuccess fragment = new ControllerCharitySuccess(charitySuccessParentInterface, isPhone, true);

            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        }

        @Override
        public void goToCharityPage() {

            ControllerCharityHome fragment = new ControllerCharityHome(
                    charityPageParentInterface,
                    prefManager,
                    LocalBroadcastManager.getInstance(requireActivity()),
                    isPhone);
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }






        }

        @Override
        public void setBackAction(int backNo) {
            ControllerRootWithAuth.this.setBackAction(backNo);
        }
    };

    private final ControllerCharitySuccess.ParentInterface charitySuccessParentInterface = new ControllerCharitySuccess.ParentInterface() {
        @Override
        public void goToCharityPage() {
            ControllerCharityHome fragment = new ControllerCharityHome(
                    charityPageParentInterface,
                    prefManager,
                    LocalBroadcastManager.getInstance(requireActivity()),
                    isPhone);

            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }


        }

        @Override
        public void setBackAction(int backNo) {
            ControllerRootWithAuth.this.setBackAction(backNo);
        }
    };

    private final ControllerCharityDonate.ParentInterface donateParentInterface = new ControllerCharityDonate.ParentInterface() {
        @Override
        public void goToCharitySuccessPage() {



            ControllerCharitySuccess fragment = new ControllerCharitySuccess(charitySuccessParentInterface, isPhone, false);

            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped){ //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        }

        @Override
        public void setBackAction(int backNo) {
            ControllerRootWithAuth.this.setBackAction(backNo);
        }
    };

    private void setBackAction(int backNo) {
        fragCount = backNo;
    }

    private void getServeStyle() {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "getting serve style");
        dialog.setCancelable(false);
        dialog.show();

        SetServeStyleRequest request = new SetServeStyleRequest();
        request.setRestaurant_id(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());

        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getServerStyle(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ServeStyleResponse>() {
                        @Override
                        public void onSuccess(ServeStyleResponse serveStyleResponse) {
                            dialog.dismiss();

                            if (serveStyleResponse.isSuccess()) {
                                View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_serve_style, null, false);
                                RoundedDialogFragment dialog = new RoundedDialogFragment(layoutView, false);

                                final CheckBox delivery, diniin, collection;
                                Button save, cancel;

                                delivery = layoutView.findViewById(R.id.delivery);
                                diniin = layoutView.findViewById(R.id.dinein);
                                collection = layoutView.findViewById(R.id.collection);
                                save = layoutView.findViewById(R.id.save);
                                cancel = layoutView.findViewById(R.id.cancel);

                                if (serveStyleResponse.getData().getCollection() == 1) {
                                    collection.setChecked(true);
                                }
                                if (serveStyleResponse.getData().getDelivery() == 1) {
                                    delivery.setChecked(true);
                                }
                                if (serveStyleResponse.getData().getDine_in() == 1) {
                                    diniin.setChecked(true);
                                }

                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        saveServeStyle(delivery.isChecked(), diniin.isChecked(), collection.isChecked(), dialog);
                                    }
                                });

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show(getChildFragmentManager(), null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Unable to process your request", Toast.LENGTH_SHORT).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveServeStyle(final boolean isDelivery, final boolean isDineIn, final boolean isCollection, final RoundedDialogFragment dialog2) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Updating serve style");
        dialog.setCancelable(false);
        dialog.show();

        SetServeStyleRequest request = new SetServeStyleRequest();
        request.setRestaurant_id(userPreferences.getLoggedInResponse(getActivity()).getRestaurant_id());
        request.setCollection((isCollection ? 1 : 0) + "");
        request.setDine_in((isDineIn ? 1 : 0) + "");
        request.setDelivery((isDelivery ? 1 : 0) + "");

        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.setServeStyle(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ServeStyleResponse>() {
                        @Override
                        public void onSuccess(ServeStyleResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                dialog2.dismiss();
                                Toast.makeText(getActivity(), "Serve Style saved", Toast.LENGTH_SHORT).show();

                                // Updated the stored serve style, which is formatted like "collection,delivery,dine_in"
                                ArrayList<String> savedStyles = new ArrayList();
                                if (isCollection) savedStyles.add("collection");
                                if (isDelivery) savedStyles.add("delivery");
                                if (isDineIn) savedStyles.add("dine_in");
                                String savedStyleCSV = TextUtils.join(",", savedStyles);

                                LoginResponse.Data loginData = userPreferences.getLoggedInResponse(getActivity());
                                loginData.setServe_style(savedStyleCSV);
                                userPreferences.setLoggedInResponse(getActivity(), loginData);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            dialog2.dismiss();

                            Toast.makeText(getActivity(), "Unable to update Serve Style", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            dialog2.dismiss();

            Toast.makeText(getActivity(), "Unable to update Serve Style", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setRestaurantDetails() {
        if (baseDetails != null) {
            data.getDataSideMenu().isRestaurantOpen().set(baseDetails.isOpen());

            String addressWithoutPostcode = baseDetails.getAddress() == null ? "" : baseDetails.getAddress().trim().replaceAll("\n", ", ");
            String postcode = baseDetails.getPost_code() == null ? "" : baseDetails.getPost_code().trim();
            String landline = baseDetails.getLandline_number() == null ? "" : baseDetails.getLandline_number().trim();
            String mobile = baseDetails.getPhone_number() == null ? "" : baseDetails.getPhone_number().trim();

            data.getDataHeader().getRestaurantAddress().set(addressWithoutPostcode + ", " + postcode);
            data.getDataHeader().getRestaurantName().set(baseDetails.getRestaurant_name());
            data.getDataHeader().getRestaurantNumber().set(landline.length() > 0 ? landline : mobile.length() > 0 ? mobile : "");
        }
    }

    public void logoutNow() {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Logging out...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            CommonRequest commonRequest = new CommonRequest();
            commonRequest.setUser_id(userPreferences.getLoggedInResponse(getActivity()).getUser_id());

            @Nullable String storedFirebaseToken = userPreferences.getFirebaseToken(getActivity());
            commonRequest.setFcm_id(storedFirebaseToken != null ? storedFirebaseToken : "");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            String authToken = prefManager.getPreference(AUTH_TOKEN, "");

            disposable.add(apiService.logout(authToken, commonRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                isDashBoard = false;
                                userPreferences.setLoggedOut(getActivity());
                                parentInterface.goToLoginActivity();

                            } else {
                                Toast.makeText(getActivity(), "Unable to logout", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    }));
        } catch (Exception e) {
            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadHomeFragment() {
        String appVersionName;
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            appVersionName = packageManager.getPackageInfo(getActivity().getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appVersionName = "Unknown";
        }

        ControllerOrderList fragment = new ControllerOrderList(
                orderListParentInterface,
                prefManager,
                ApiClient.getClient().create(ApiInterface.class),
                localBroadcastManager,
                appVersionName,
                isPhone);

        isDashBoard = true;
        data.getShowRestaurantHeader().set(true);
        data.getDataHeader().getPageTitle().set("Orders");
        data.getDataOrdersHeader().getPageTitle().set("");

        String backStateName = fragment.getClass().getName();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.framePageContent, fragment);
        ft.addToBackStack(backStateName);
        ft.commit();
    }

    public void openRestaurant(final String openOrClose, String restaurantId) {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), openOrClose.equals("closed") ? "Closing Restaurant..." : "Opening Restaurant...");
        dialog.setCancelable(false);
        dialog.show();

        @Nullable String firebaseToken = UserPreferences.get().getFirebaseToken(getActivity());
        firebaseToken = firebaseToken == null ? "" : firebaseToken;

        RestaurantOpenCloseRequest restaurantOpenCloseRequest = new RestaurantOpenCloseRequest();
        restaurantOpenCloseRequest.setOpen_close(openOrClose);
        restaurantOpenCloseRequest.setDevice_id(firebaseToken);
        restaurantOpenCloseRequest.setRestaurant_id(restaurantId);

        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.openCloseRestaurant(prefManager.getPreference(AUTH_TOKEN, ""), restaurantOpenCloseRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<RestaurantOpenCloseResponse>() {
                        @Override
                        public void onSuccess(RestaurantOpenCloseResponse data) {
                            if (data.isSuccess()) {
                                dialog.dismiss();
                                LoginResponse.Data loginResponse = userPreferences.getLoggedInResponse(getActivity());
                                loginResponse.setHome_delivery(openOrClose.equals("open"));
                                loginResponse.setOpen(openOrClose.equals("open"));

                                userPreferences.setLoggedInResponse(getActivity(), loginResponse);

                                if (openOrClose.equalsIgnoreCase("closed")) {
                                    ControllerRootWithAuth.this.data.getDataSideMenu().isRestaurantOpen().set(false);
                                } else if (openOrClose.equalsIgnoreCase("open")) {
                                    ControllerRootWithAuth.this.data.getDataSideMenu().isRestaurantOpen().set(true);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Unable process your request", Toast.LENGTH_SHORT).show();
                            Log.e("onError", "onError: " + e.getMessage());
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            Log.e("Exception ", e.toString());
            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onBackPressed() {

//Comment by ajit
            /*if (fragCount > 1) {
                ControllerCharityHome fragment = new ControllerCharityHome(
                        charityPageParentInterface,
                        prefManager,
                        LocalBroadcastManager.getInstance(requireActivity()),
                        isPhone);

                isDashBoard = false;
                data.getShowRestaurantHeader().set(false);
                data.getDataHeader().getPageTitle().set("Charity");
                data.getDataOrdersHeader().getPageTitle().set("");

                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.replace(R.id.framePageContent, fragment);
                ft.commit();

            } else {
                loadHomeFragment();
            }*/

            int count = getFragmentManager().getBackStackEntryCount();

            if (count == 0) {


                View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_confirmation, null, false);
                RoundedDialogFragment dialog = new RoundedDialogFragment(layoutView, false);

                TextView yes = layoutView.findViewById(R.id.btn_yes);
                TextView no = layoutView.findViewById(R.id.btn_no);
                TextView messge = layoutView.findViewById(R.id.txt_message);

                messge.setText("Do you want to exit?");
                yes.setText("YES");
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        parentInterface.exitApp();
                    }
                });
                no.setText("NO");
                no.setTextColor(getResources().getColor(R.color.black));
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.showNow(getChildFragmentManager(), null);
                //additional code
            } else {
                getFragmentManager().popBackStack();
                if(count>=2){
                    if(MySingleTon.getInstance().getFragmentName().equals("List of Offers")){
                        isDashBoard = false;
                        data.getShowRestaurantHeader().set(true);
                        data.getDataHeader().getPageTitle().set("");
                        data.getDataOrdersHeader().getPageTitle().set("List of Offers");
                    }else if(MySingleTon.getInstance().getFragmentName().equals("Charity")) {
                        isDashBoard = false;
                        data.getShowRestaurantHeader().set(false);
                        data.getDataHeader().getPageTitle().set("Charity");
                        data.getDataOrdersHeader().getPageTitle().set("");
                    }else {
                        isDashBoard = false;
                        data.getShowRestaurantHeader().set(true);
                        data.getDataHeader().getPageTitle().set("");
                        data.getDataOrdersHeader().getPageTitle().set("Menu");
                    }
                }else{
                    isDashBoard = true;
                    data.getShowRestaurantHeader().set(true);
                    data.getDataHeader().getPageTitle().set("Orders");
                    data.getDataOrdersHeader().getPageTitle().set("");

                }}


    }

    @Override
    public void onResume() {
        super.onResume();

        localBroadcastManager.registerReceiver(newOrderBroadcastReceiver, new IntentFilter(Constants.NOTIFICATION_TYPE_ACCEPTED));
        localBroadcastManager.registerReceiver(newOrderBroadcastReceiver, new IntentFilter(Constants.OPEN_CLOSE_INTENT));

        loadRestaurantLogo();
    }

    @Override
    public void onPause() {
        localBroadcastManager.unregisterReceiver(newOrderBroadcastReceiver);

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (dialogNewOrder != null) {
            dialogNewOrder.dismiss();
            dialogNewOrder = null;
        }

        super.onDestroy();
    }

    private final BroadcastReceiver newOrderBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constants.NOTIFICATION_TYPE_ACCEPTED)) {

                if (dialogNewOrder != null) {
                    dialogNewOrder.dismiss();
                    dialogNewOrder = null;
                }

                int newOrderCount = 1 + data.getNewOrderToDisplayCount().get();
                data.getNewOrderToDisplayCount().set(newOrderCount);

                // String message = intent.getStringExtra("message");
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_new_order_arrived, null);
                dialogNewOrder = new RoundedDialogFragment(dialogView, false);

                TextView txtmsg = dialogView.findViewById(R.id.txtmsg);
                TextView feednow = dialogView.findViewById(R.id.feednow);
                TextView later = dialogView.findViewById(R.id.later);

                txtmsg.setText(newOrderCount + " new " + (newOrderCount == 1 ? "order" : "orders") + " arrived");

                feednow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadHomeFragment();

                        data.getNewOrderToDisplayCount().set(0);

                        dialogNewOrder.dismiss();
                        dialogNewOrder = null;
                    }
                });

                later.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Don't increment newOrderToDisplayCount to remind the user the next time
                        // another order arrives that they are piling up

                        dialogNewOrder.dismiss();
                        dialogNewOrder = null;
                    }
                });

                dialogNewOrder.showNow(getChildFragmentManager(), null);

            } else if (intent.getAction().equals(Constants.OPEN_CLOSE_INTENT)) {
                String action = intent.getStringExtra("type");

                boolean isNowClosed = action.equalsIgnoreCase("closed");
                data.getDataSideMenu().isRestaurantOpen().set( ! isNowClosed);
            }
        }
    };

    private void loadRestaurantLogo() {
        if (userPreferences.getRestaurantLogoBitmap(getActivity()) != null) {
            // Cache is already populated
            return;
        }

        Picasso.get().load(userPreferences.getLoggedInResponse(getActivity()).getLogo()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteImage = stream.toByteArray();
                userPreferences.setRestaurantLogoBitmap(getActivity(), byteImage);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("onBitmapFailed", "RestaurantLogo Not Load");

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e("onPrepareLoad", "RestaurantLogo Not Load");

            }
        });
    }
}
