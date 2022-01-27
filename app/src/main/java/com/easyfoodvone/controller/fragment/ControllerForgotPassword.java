package com.easyfoodvone.controller.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.viewdata.DataIncludeHeader;
import com.easyfoodvone.app_common.viewdata.DataPageForgotPassword;
import com.easyfoodvone.app_ui.view.ViewForgotPassword;
import com.easyfoodvone.models.LoginRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerForgotPassword extends Fragment {

    public interface ParentInterface {
        void goBack();
        void showLongText(@NonNull String message);
    }

    @NonNull
    private final ParentInterface parentInterface;
    @NonNull
    private final PrefManager prefManager;
    private final boolean isPhone;

    private DataPageForgotPassword data;

    public ControllerForgotPassword(@NonNull ParentInterface parentInterface, @NonNull PrefManager prefManager, boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataIncludeHeader dataHeader = new DataIncludeHeader(
                "Forgotten Password",
                "",
                "",
                "",
                false,
                headerEventHandler);
        data = new DataPageForgotPassword(dataHeader, viewEventHandler);
        data.getInputEmailAddress().addOnPropertyChangedCallback(clearEmailErrorOnChange);

        ViewForgotPassword view = new ViewForgotPassword(new LifecycleSafe(this), isPhone);
        view.onCreateView(inflater, container, data);

        return view.getRoot();
    }

    private final DataIncludeHeader.OutputEvents headerEventHandler = new DataIncludeHeader.OutputEvents() {
        @Override
        public void onClickBack() {
            parentInterface.goBack();
        }

        @Override
        public void onClickBurger() {
            // No burger on the forgotten password page
        }
    };

    private final DataPageForgotPassword.OutputEvents viewEventHandler = new DataPageForgotPassword.OutputEvents() {
        @Override
        public void onClickReset() {
            String emailText = data.getInputEmailAddress().get();
            if (TextUtils.isEmpty(emailText)) {
                data.getEmailError().set("Enter email.");
            } else if (!isValidEmailAddress(emailText)) {
                data.getEmailError().set("Please enter a valid email address");
            } else {
                forgotPassword();
            }
        }

        @Override
        public void onClickCancel() {
            parentInterface.goBack();
        }
    };

    private Observable.OnPropertyChangedCallback clearEmailErrorOnChange = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            // Simulate EditText.setError behaviour
            data.getEmailError().set("");
        }
    };

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    //TODO:  Login now....
    public void forgotPassword() {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Requesting please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            LoginRequest request = new LoginRequest();
            request.setEmail(data.getInputEmailAddress().get());

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.forgotPassword(prefManager.getPreference(AUTH_TOKEN, ""), request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                        @Override
                        public void onSuccess(LoginResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                showSuccessDialog();
                            } else {
                                parentInterface.showLongText(data.getMessage() != null ? data.getMessage() : "");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();

                            parentInterface.showLongText("Invalid Email!");
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            parentInterface.showLongText("Server not responding");
        }
    }

    private void showSuccessDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        dialog.setView(mDialogView);

        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText("If your email is correct, You will get a reset password link on your e-mail. \n Thank you");

        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                parentInterface.goBack();
            }
        });

        dialog.show();
    }
}
