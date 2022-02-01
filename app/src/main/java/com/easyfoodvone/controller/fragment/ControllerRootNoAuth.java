package com.easyfoodvone.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.easyfoodvone.R;
import com.easyfoodvone.fragments.LoginFragment;
import com.easyfoodvone.fragments.RegisterFragment;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

public class ControllerRootNoAuth extends Fragment {
    public interface ParentInterface {
        void goToLoggedIn();
    }

    @NonNull
    private final ParentInterface parentInterface;
    @NonNull
    private final PrefManager prefManager;
    @NonNull
    private final UserPreferences userPreferences;
    private final boolean isPhone;

    public ControllerRootNoAuth(
            @NonNull ParentInterface parentInterface,
            @NonNull PrefManager prefManager,
            @NonNull UserPreferences userPreferences,
            boolean isPhone) {
        this.parentInterface = parentInterface;
        this.prefManager = prefManager;
        this.userPreferences = userPreferences;
        this.isPhone = isPhone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root, container, false);

        transactHome(false);

        return view;
    }

    public void handleBackPress() {

        boolean isHome = getChildFragmentManager().findFragmentById(R.id.root) instanceof LoginFragment;
        if ( ! isHome) {
            transactHome(true);
        }
    }

    private void transactHome(boolean animate) {
        transactFragment(new LoginFragment(interfaceLogin, prefManager, userPreferences), animate);
    }

    private void transactFragment(Fragment fragment, boolean animate) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.root, fragment)
                .setTransition(animate ? FragmentTransaction.TRANSIT_FRAGMENT_OPEN : FragmentTransaction.TRANSIT_NONE)
                .commitNowAllowingStateLoss();
    }

    private final LoginFragment.ParentInterface interfaceLogin = new LoginFragment.ParentInterface() {
        @Override
        public void goToLoggedIn() {
            parentInterface.goToLoggedIn();
        }

        @Override
        public void goToRegistration() {
            transactFragment(new RegisterFragment(interfaceRegister), true);
        }

        @Override
        public void goToForgotPassword() {
            transactFragment(new ControllerForgotPassword(interfaceForgotPassword, prefManager, isPhone), true);
        }

        @Override
        public void goToContactSupport() {
            transactFragment(new ControllerContactSupport(interfaceContactSupport, prefManager, isPhone), true);
        }

        @Override
        public void showToastShort(@NonNull String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RegisterFragment.ParentInterface interfaceRegister = new RegisterFragment.ParentInterface() {
        @Override
        public void onBackPress() {
            transactHome(true);
        }
    };

    private final ControllerForgotPassword.ParentInterface interfaceForgotPassword = new ControllerForgotPassword.ParentInterface() {
        @Override
        public void goBack() {
            transactHome(true);
        }

        @Override
        public void showLongText(@NonNull String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    };

    private final ControllerContactSupport.ParentInterface interfaceContactSupport = new ControllerContactSupport.ParentInterface() {
        @Override
        public void goBack() {
            transactHome(true);
        }

        @Override
        public void showShortToast(@NonNull String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void copyToClipboard(@Nullable String text) {
            text = text == null ? "" : text;

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);
            }
        }
    };
}
