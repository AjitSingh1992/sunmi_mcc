package com.easyfoodvone.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.R;
import com.easyfoodvone.app_ui.databinding.PageRegisterBinding;

public class RegisterFragment extends Fragment {

    public interface ParentInterface {
        void onBackPress();
    }

    @NonNull
    private final ParentInterface parentInterface;

    @Nullable
    private PageRegisterBinding binding = null;

    public RegisterFragment(@NonNull ParentInterface parentInterface) {
        this.parentInterface = parentInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.page_register, container, false);
        init();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding.unbind();
        binding = null;

        super.onDestroyView();
    }

    private void init() {
        binding.progressBar.setVisibility(View.VISIBLE);

        // binding.webPrivacyPolicy.setWebViewClient(new MyWebViewClient());
        //binding.webPrivacyPolicy.loadUrl("https://www.easyfood.co.uk/restaurant-signup");
        binding.webPrivacyPolicy.setWebViewClient(new MyWebViewClient());
        binding.webPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        binding.webPrivacyPolicy.getSettings().setDomStorageEnabled(true);
        binding.webPrivacyPolicy.getSettings().setAppCacheEnabled(false);
        binding.webPrivacyPolicy.clearCache(true);
        binding.webPrivacyPolicy.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        binding.webPrivacyPolicy.loadUrl("https://www.easyfood.co.uk/restaurant-signup");

        binding.tvBack.setOnClickListener(view -> parentInterface.onBackPress());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (binding == null) {
                return;
            }

            binding.progressBar.setVisibility(View.GONE);

            try {
                view.clearCache(true);
                view.setVisibility(View.VISIBLE);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
