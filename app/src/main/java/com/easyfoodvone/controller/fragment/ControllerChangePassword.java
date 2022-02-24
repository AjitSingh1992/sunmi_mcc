package com.easyfoodvone.controller.fragment;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.separation.LifecycleSafe;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataPageChangePassword;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.app_ui.view.ViewChangePassword;
import com.easyfoodvone.models.ChangePasswordRequest;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ControllerChangePassword extends Fragment {

    public interface ParentInterface {
        void goToHome();
    }

    private final @NonNull ParentInterface parentInterface;
    private final @NonNull PrefManager prefManager;
    private final boolean isPhone;

    private DataPageChangePassword viewData;

    private final DataPageChangePassword.OutputEvents viewEventHandler =
            new DataPageChangePassword.OutputEvents() {
                @Override
                public void onClickReset() {
                    String newPassword = viewData.getInputNewPassword().get();
                    String confirmPassword = viewData.getInputConfirmNewPassword().get();
                    String currentPass = viewData.getInputCurrentPassword().get();

                    if (TextUtils.isEmpty(currentPass)) {
                        viewData.getInputEvents().get().setErrorCurrentPassword("Enter current password");
                    } else if (TextUtils.isEmpty(newPassword)) {
                        viewData.getInputEvents().get().setErrorNewPassword("Enter new password");
                    } else if (TextUtils.isEmpty(confirmPassword)) {
                        viewData.getInputEvents().get().setErrorConfirmPassword("Enter new password again");
                    } else if ( ! newPassword.equals(confirmPassword)) {
                        viewData.getInputEvents().get().setErrorConfirmPassword("Enter a matching password");
                    } else {
                        changePassword();
                    }
                }

                @Override
                public void onClickCancel() {
                    parentInterface.goToHome();
                }
            };

    public ControllerChangePassword(@NonNull ParentInterface parentInterface,
                                    @NonNull PrefManager prefManager,
                                    boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewData = new DataPageChangePassword(
                viewEventHandler,
                new ObservableField<>(null),
                new ObservableField<>(""),
                new ObservableField<>(""),
                new ObservableField<>(""));

        ViewChangePassword view = new ViewChangePassword(new LifecycleSafe(this), isPhone);
        view.onCreateView(inflater, container, viewData);

        return view.getRoot();
    }

    private void changePassword() {
        final LoadingDialog dialog = new LoadingDialog(getActivity(), "Changing please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setUser_id(UserPreferences.get().getLoggedInResponse(getActivity()).getUser_id());
            request.setCurrent_password(viewData.getInputCurrentPassword().get());
            request.setPassword(viewData.getInputNewPassword().get());
            request.setPassword_confirmation(viewData.getInputConfirmNewPassword().get());

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.changePassword(prefManager.getPreference(AUTH_TOKEN,""),request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                viewData.getInputCurrentPassword().set("");
                                viewData.getInputNewPassword().set("");
                                viewData.getInputConfirmNewPassword().set("");
                                View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_confirmation, null, false);
                                RoundedDialogFragment dialog = new RoundedDialogFragment(layoutView, false);

                                TextView yes = layoutView.findViewById(R.id.btn_yes);
                                TextView no = layoutView.findViewById(R.id.btn_no);
                                TextView messge = layoutView.findViewById(R.id.txt_message);

                                no.setVisibility(View.GONE);
                                messge.setText(data.getMessage());
                                yes.setText("Got it!");
                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        Fragment fragment = getFragmentManager().findFragmentById(R.id.root);

                                        ((ControllerRootWithAuth) fragment).onBackPressed();
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



                            } else {
                                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();
                        }
                    }));


        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            Toast.makeText(getActivity(), "Server not responding.", Toast.LENGTH_SHORT).show();
        }
    }
}
