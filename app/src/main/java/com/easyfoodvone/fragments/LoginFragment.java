package com.easyfoodvone.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.BuildConfig;
import com.easyfoodvone.OrdersActivity;
import com.easyfoodvone.PrinterTestActivity;
import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_ui.databinding.PageSigninBinding;
import com.easyfoodvone.models.LoginRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.Helper;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        //Toast.makeText(getActivity(), "" + Helper.getDeviceName(), Toast.LENGTH_SHORT).show();

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
                } else if (binding.editPassword.getText().toString().length()<6) {
                    alertDialog("The password must be at least 6 characters.",
                            getActivity(), "0",parentInterface);
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
            request.setPos_id(userPreferences.getDEVICE_TYPE(getActivity()));
            request.setDevice_type("Android");
            request.setEmail(binding.editUsername.getText().toString());
            request.setPassword(binding.editPassword.getText().toString());

            // TODO Update the web service to permit empty string in the firebase field, it currently rejects login with ""

        //
            if(userPreferences.getDEVICE_TYPE(getActivity()).equals("0")){
                try {
                    @Nullable String storedFirebaseToken = userPreferences.getFirebaseToken(getActivity());
                    request.setFirebase_id(storedFirebaseToken != null ? storedFirebaseToken : "");
                } catch (Exception e) {
                }
                //request.setFirebase_id(userPreferences.getPushyToken(getContext()));


            }else if(userPreferences.getDEVICE_TYPE(getActivity()).equals("2")) {
                request.setFirebase_id(userPreferences.getPushyToken(getContext()));
            }else if(userPreferences.getDEVICE_TYPE(getActivity()).equals("3")) {
                request.setFirebase_id(userPreferences.getPushyToken(getContext()));
            }else if(userPreferences.getDEVICE_TYPE(getActivity()).equals("4")) {
                request.setFirebase_id(userPreferences.getPushyToken(getContext()));
            }else{
                request.setFirebase_id(userPreferences.getPushyToken(getContext()));

            }

            Gson gson = new Gson();
            gson.toJson(request);
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
                                //Log.e("Login response ", ">>>>>>>>>>  " + data.toString());

                                prefManager.savePreference(AUTH_TOKEN, data.getData().getToken());

                                userPreferences.setLoggedIn(getActivity(), data.getData());

                                parentInterface.goToLoggedIn();

                            } else {
                                //parentInterface.showToastShort(data.getMessage() != null ? data.getMessage() : "Error");
                                binding.btnLogin.setClickable(true);
                                if(data.getLogin_attempt()!=null) {
                                    if (data.getLogin_attempt().equals("4")) {
                                        alertDialog(
                                                "Too many unsuccessful login attempts. Please reset your password.",
                                                getActivity(), "1",parentInterface);
                                    }else{
                                        alertDialog(
                                                data.getMessage(),
                                                getActivity(), "2",parentInterface);

                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                            dialog.dismiss();

                            binding.btnLogin.setClickable(true);
                            if (e instanceof HttpException) {
                                HttpException httpException = (HttpException) e;
                                ResponseBody errorBody = httpException.response().errorBody();

                                try{
                                    if (errorBody != null) {
                                        String errorResponse = errorBody.string();

                                        Gson gson = new Gson();
                                        JsonElement jsonElement = gson.fromJson(errorResponse, JsonElement.class);

                                        // Check if the parsed element is an object
                                        if (jsonElement.isJsonObject()) {
                                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                                            // Access individual fields
                                            boolean success = jsonObject.getAsJsonPrimitive("success").getAsBoolean();
                                            String message = jsonObject.has("message") ? jsonObject.getAsJsonPrimitive("message").getAsString() : null;
                                            String login_attempt = jsonObject.has("login_attempt") ? jsonObject.getAsJsonPrimitive("login_attempt").getAsString() : null;

                                            if(login_attempt!=null && login_attempt.equals("4")){
                                                alertDialog(
                                                        "Too many unsuccessful login attempts. Please reset your password.",
                                                        getActivity(),"1",parentInterface);
                                            }else if (jsonObject.has("errors")) {
                                                    if (jsonObject.getAsJsonObject("errors").size() > 0) {
                                                        JsonObject errorsObject = jsonObject.getAsJsonObject("errors");
                                                        JsonElement emailErrorElement = errorsObject.getAsJsonArray("email").get(0);
                                                        String emailError = emailErrorElement.getAsString();

                                                                alertDialog(          emailError,
                                                                getActivity(),"2",parentInterface);

                                                }
                                            }
                                            // Handle the data as needed

                                        }

                                    }
                                }catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }


                    }));

        } catch (Exception e) {
            e.printStackTrace();

            dialog.dismiss();

            binding.btnLogin.setClickable(true);
            parentInterface.showToastShort("Server not responding.");
        }
    }

    public static void alertDialog(String msg, Activity activity, String flag, ParentInterface parentInterface) {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(activity).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag.equals("1")){
                    parentInterface.goToForgotPassword();
                    mDialog.dismiss();

                }else{
                    mDialog.dismiss();

                }
                //your business logic
            }
        });

        mDialog.show();
    }

}
