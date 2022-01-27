package com.easyfoodvone.menu_details;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.controller.fragment.ControllerMenuDetails;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.models.MenuProductDetails;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ViewMenuPopupEditItemPinCheck {

    public interface ParentInterface {
        void onPinCheckPassed(MenuProductDetails data);
    }

    private final @NonNull Activity activity;
    private final @NonNull ControllerMenuDetails fragment;
    private final @NonNull PrefManager prefManager;
    private final @NonNull ParentInterface parentInterface;

    public ViewMenuPopupEditItemPinCheck(@NonNull ControllerMenuDetails fragment, @NonNull PrefManager prefManager, @NonNull ParentInterface parentInterface) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.prefManager = prefManager;
        this.parentInterface = parentInterface;
    }

    public void alertDialogMPIN(final char[] pin, final String restaurantId, final String menu_product_id) {
        final View mDialogView = LayoutInflater.from(activity).inflate(R.layout.popup_enter_mpin, null);
        RoundedDialogFragment pinDialog = new RoundedDialogFragment(mDialogView, false);

        final EditText firstMpin = mDialogView.findViewById(R.id.mpin_first);
        final EditText secondMpin = mDialogView.findViewById(R.id.mpin_two);
        final EditText thirdMpin = mDialogView.findViewById(R.id.mpin_three);
        final EditText fourthMpin = mDialogView.findViewById(R.id.mpin_four);
        final TextView error = mDialogView.findViewById(R.id.txt_error_message);
        final TextView msg = mDialogView.findViewById(R.id.txt_message);
        msg.setText("Enter 4 Digit Code");
        firstMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    secondMpin.requestFocus();
                }
            }
        });
        secondMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    thirdMpin.requestFocus();
                else
                    firstMpin.requestFocus();
            }
        });
        thirdMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    fourthMpin.requestFocus();
                else
                    secondMpin.requestFocus();
            }
        });
        fourthMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                } else {
                    thirdMpin.requestFocus();
                }
            }
        });

        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[0]))
                        && secondMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[1]))
                        && thirdMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[2]))
                        && fourthMpin.getText().toString().trim().equalsIgnoreCase(String.valueOf(pin[3]))) {
                    pinDialog.dismiss();
                    error.setVisibility(View.GONE);

                    getMenuItemDetail(restaurantId, menu_product_id, pinDialog);

                } else {
                    error.setVisibility(View.VISIBLE);
                }
            }
        });
        mDialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                pinDialog.dismiss();
            }
        });

        pinDialog.showNow(fragment.getChildFragmentManager(), null);
    }

    private void getMenuItemDetail(String restaurantId, String menu_product_id, final RoundedDialogFragment mDialog) {
        final LoadingDialog dialog = new LoadingDialog(activity, "");
        dialog.setCancelable(false);
        dialog.show();
        try {
            CommonRequest commonRequest = new CommonRequest();
            commonRequest.setMenu_product_id(menu_product_id);
            commonRequest.setRestaurant_id(restaurantId);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String authToken = prefManager.getPreference(AUTH_TOKEN,"");

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.getMenuDetails(authToken, commonRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MenuProductDetails>() {
                        @Override
                        public void onSuccess(MenuProductDetails data) {
                            dialog.dismiss();
                            mDialog.dismiss();

                            if (data.isSuccess()) {
                                parentInterface.onPinCheckPassed(data);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            mDialog.dismiss();

                            e.printStackTrace();
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();
            mDialog.dismiss();

            e.printStackTrace();
        }
    }
}
