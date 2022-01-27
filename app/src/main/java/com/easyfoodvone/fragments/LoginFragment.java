package com.easyfoodvone.fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.BuildConfig;
import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_ui.databinding.PageSigninBinding;
import com.easyfoodvone.models.LoginRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class LoginFragment extends Fragment {

    public interface ParentInterface {
        void goToLoggedIn();
        void goToRegistration();
        void goToForgotPassword();
        void goToContactSupport();
        void showToastShort(@NonNull String message);
    }

    @NonNull
    private final ParentInterface parentInterface;
    @NonNull
    private final PrefManager prefManager;
    @NonNull
    private final UserPreferences userPreferences;

    @Nullable
    private PageSigninBinding binding = null;

    private LoadingDialog dialog;
    private int count = 0;

    public LoginFragment(@NonNull ParentInterface parentInterface, @NonNull PrefManager prefManager, @NonNull UserPreferences userPreferences) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.userPreferences = userPreferences;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.page_signin, container, false);

        setOnClickListeners();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding.unbind();
        binding = null;

        super.onDestroyView();
    }

    private void setOnClickListeners() {
        binding.txtForgotPassword.setOnClickListener(v -> parentInterface.goToForgotPassword());
        binding.btnContactSupport.setOnClickListener(v -> parentInterface.goToContactSupport());
        binding.btnSignup.setOnClickListener(view -> parentInterface.goToRegistration());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.editUsername.getText().toString())) {
                    binding.editUsername.setError("Enter user name");
                } else if (TextUtils.isEmpty(binding.editPassword.getText().toString())) {
                    binding.editPassword.setError("Enter password");
                } else {
                    binding.btnLogin.setClickable(false);
                    loginNow();
                }
            }
        });
    }

    private void loginNow() {
        dialog = new LoadingDialog(getActivity(), "Logging you please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            LoginRequest request = new LoginRequest();
            request.setDevice_id(android_id);
            request.setDevice_type("Android");
            request.setEmail(binding.editUsername.getText().toString());
            request.setPassword(binding.editPassword.getText().toString());

            // TODO Update the web service to permit empty string in the firebase field, it currently rejects login with ""
            @Nullable String storedFirebaseToken = userPreferences.getFirebaseToken(getActivity());
            request.setFirebase_id(storedFirebaseToken != null ? storedFirebaseToken : "");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.login(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                        @Override
                        public void onSuccess(LoginResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess() && data.getData() != null) {
                                Log.e("Login response ", ">>>>>>>>>>  " + data.toString());

                                prefManager.savePreference(AUTH_TOKEN, data.getData().getToken());

                                userPreferences.setLoggedIn(getActivity(), data.getData());

                                parentInterface.goToLoggedIn();

                            } else {
                                parentInterface.showToastShort(data.getMessage() != null ? data.getMessage() : "Error");
                                binding.btnLogin.setClickable(true);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            binding.btnLogin.setClickable(true);
                            parentInterface.showToastShort(getString(R.string.login_error_message));
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            binding.btnLogin.setClickable(true);
            parentInterface.showToastShort("Server not responding.");
        }
    }
}
