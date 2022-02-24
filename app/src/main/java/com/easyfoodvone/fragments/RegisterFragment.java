package com.easyfoodvone.fragments;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.easyfoodvone.R;
import com.easyfoodvone.app_ui.databinding.PageRegisterBinding;
import com.easyfoodvone.app_ui.fragment.RoundedDialogFragment;
import com.easyfoodvone.controller.fragment.ControllerRootWithAuth;

import static android.app.Activity.RESULT_OK;
import static com.easyfoodvone.utility.Helper.isInternetOn;

public class RegisterFragment extends Fragment {
    public ValueCallback<Uri[]> uploadMessage;
    private ValueCallback<Uri> mUploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
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
        if (isInternetOn(getActivity())) {
            init();
        }else{
            View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_confirmation, null, false);
            RoundedDialogFragment dialog = new RoundedDialogFragment(layoutView, false);

            TextView yes = layoutView.findViewById(R.id.btn_yes);
            TextView no = layoutView.findViewById(R.id.btn_no);
            TextView messge = layoutView.findViewById(R.id.txt_message);

            no.setVisibility(View.GONE);
            messge.setText(getString(R.string.no_internet_available_msg));
            yes.setText("Got it!");
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    getActivity().onBackPressed();

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
        }

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
        binding.webPrivacyPolicy.setWebChromeClient(new MyWebChromeClient());

        binding.webPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        binding.webPrivacyPolicy.getSettings().setDomStorageEnabled(true);
        binding.webPrivacyPolicy.getSettings().setAllowContentAccess(true);
        binding.webPrivacyPolicy.getSettings().setAllowFileAccess(true);
        binding.webPrivacyPolicy.getSettings().setAppCacheEnabled(false);
        binding.webPrivacyPolicy.clearCache(true);
        binding.webPrivacyPolicy.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        binding.webPrivacyPolicy.loadUrl("https://www.easyfood.co.uk/restaurant-signup");

        binding.tvBack.setOnClickListener(view -> parentInterface.onBackPress());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

    class MyWebChromeClient extends WebChromeClient {
        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                Toast.makeText(getActivity(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }


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
