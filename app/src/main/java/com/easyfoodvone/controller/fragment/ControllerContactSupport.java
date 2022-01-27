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
import com.easyfoodvone.app_common.viewdata.DataPageContactSupport;
import com.easyfoodvone.app_ui.view.ViewContactSupport;
import com.easyfoodvone.contact_support.models.SupportRequest;
import com.easyfoodvone.contact_support.models.SupportResponse;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class ControllerContactSupport extends Fragment {

    public interface ParentInterface {
        void goBack();
        void showShortToast(@NonNull String message);
        void copyToClipboard(@Nullable String text);
    }

    @NonNull
    private final ParentInterface parentInterface;
    @NonNull
    private final PrefManager prefManager;
    private final boolean isPhone;

    @Nullable
    private LoadingDialog loadingDialog;

    private DataPageContactSupport data;

    public ControllerContactSupport(@NonNull ParentInterface parentInterface, @NonNull PrefManager prefManager, boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataIncludeHeader dataHeader = new DataIncludeHeader(
                getString(R.string.contact_support_page_name),
                "",
                "",
                "",
                false,
                headerEventHandler);
        data = new DataPageContactSupport(dataHeader, viewEventHandler);
        data.getInputEmailAddress().addOnPropertyChangedCallback(clearEmailErrorOnChange);
        data.getInputMessage().addOnPropertyChangedCallback(clearMessageErrorOnChange);

        ViewContactSupport view = new ViewContactSupport(new LifecycleSafe(this), isPhone);
        view.onCreateView(inflater, container, data);

        return view.getRoot();
    }

    private final DataPageContactSupport.OutputEvents viewEventHandler = new DataPageContactSupport.OutputEvents() {
        @Override
        public void onClickSend() {
            String emailText = data.getInputEmailAddress().get();
            String messageText = data.getInputMessage().get();

            if (TextUtils.isEmpty(emailText)) {
                data.getEmailError().set("Please enter your email");
            } else if (TextUtils.isEmpty(messageText)) {
                data.getMessageError().set("Please write your message");
            } else if (!isValidEmailAddress(emailText)) {
                data.getEmailError().set("Please enter a valid email");
            } else {
                sendToSupport();
            }
        }

        @Override
        public void onClickCancel() {
            parentInterface.goBack();
        }
    };

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

    private final Observable.OnPropertyChangedCallback clearEmailErrorOnChange = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            // Simulate EditText.setError behaviour
            data.getEmailError().set("");
        }
    };

    private final Observable.OnPropertyChangedCallback clearMessageErrorOnChange = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            // Simulate EditText.setError behaviour
            data.getMessageError().set("");
        }
    };

    private void alertDialog(String msg) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final AlertDialog mDialog = new AlertDialog.Builder(getActivity()).create();

        final View mDialogView = factory.inflate(R.layout.popup_contact_support, null);
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);

        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(v -> parentInterface.goBack());

        mDialog.show();
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void sendToSupport() {
        loadingDialog = new LoadingDialog(getActivity(), "Processing your request...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        try {
            SupportRequest request = new SupportRequest();

            String emailText = data.getInputEmailAddress().get();
            String messageText = data.getInputMessage().get();

            request.setEmail(emailText);
            request.setMessage(messageText);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            String authToken = prefManager.getPreference(AUTH_TOKEN,"");

            disposable.add(apiService.contactSupport(authToken, request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<SupportResponse>() {
                        @Override
                        public void onSuccess(SupportResponse data) {
                            loadingDialog.dismiss();

                            if (data.isSuccess()) {
                                @Nullable String request_id = data.getData().getRequest_id();
                                parentInterface.copyToClipboard(request_id);
                                alertDialog("Your message has been sent successfully. \n Please note " + request_id + " request id for reference.");
                            } else {
                                parentInterface.showShortToast(data.getMessage() != null ? data.getMessage() : "");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            loadingDialog.dismiss();

                            parentInterface.showShortToast("Failed! try again.");
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            loadingDialog.dismiss();

            parentInterface.showShortToast("Server not responding.");
        }
    }
}
