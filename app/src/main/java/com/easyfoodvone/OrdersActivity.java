package com.easyfoodvone;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyfoodvone.controller.fragment.ControllerRootNoAuth;
import com.easyfoodvone.controller.fragment.ControllerRootWithAuth;
import com.easyfoodvone.utility.Helper;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.easyfoodvone.utility.printerutil.PrintEsayFood;
import com.ingenico.sdk.IngenicoException;
import com.ingenico.sdk.device.printer.IPrinterEventListener;
import com.ingenico.sdk.device.printer.Printer;
import com.ingenico.sdk.device.printer.data.PrinterRequest;
import com.ingenico.sdk.device.printer.data.PrinterStatus;
import com.ingenico.sdk.utils.BitmapUtils;
import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;
//import com.newrelic.agent.android.NewRelic;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import CTOS.CtPrint;
import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;
import pub.devrel.easypermissions.EasyPermissions;


public class OrdersActivity extends AppCompatActivity {

    private final static String TAG = OrdersActivity.class.getSimpleName();

    private Configuration oldConfig;
    private boolean isPhone;
    private boolean isDelayingSetOrientation;


    //CLOVER sdk
    private static final int REQUEST_ACCOUNT = 0;
    private static final Random RANDOM = new Random(SystemClock.currentThreadTimeMillis());

    private Account account;


    @Override
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Swap from FullscreenThemeWithSplash (in the manifest) to FullscreenTheme
        setTheme(R.style.FullscreenTheme);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Disable automatic fragment restoration, it is not worth the trouble
        savedInstanceState = null;

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        if (Helper.getDeviceName().contains("Sunmi")) {
            UserPreferences.get().setDEVICE_TYPE(OrdersActivity.this, "0");
        } else if (Helper.getDeviceName().contains("DX8000")) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(OrdersActivity.this);
                    UserPreferences.get().setPushyToken(OrdersActivity.this, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });

            UserPreferences.get().setDEVICE_TYPE(OrdersActivity.this, "2");
        } else if (Helper.getDeviceName().contains("A920Pro") || Helper.getDeviceName().contains("Lephone")) {
            UserPreferences.get().setDEVICE_TYPE(OrdersActivity.this, "3");
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(OrdersActivity.this);
                    UserPreferences.get().setPushyToken(OrdersActivity.this, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });
        } else if (Helper.getDeviceName().contains("Qualcomm Saturn1000F2")) {
            UserPreferences.get().setDEVICE_TYPE(OrdersActivity.this, "4");
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(OrdersActivity.this);
                    UserPreferences.get().setPushyToken(OrdersActivity.this, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });
        } else {
            UserPreferences.get().setDEVICE_TYPE(OrdersActivity.this, "4");
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(OrdersActivity.this);
                    UserPreferences.get().setPushyToken(OrdersActivity.this, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });
        }


        // new RegisterForPushNotificationsAsync(OrdersActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Configuration config = getResources().getConfiguration();
        this.oldConfig = new Configuration(config);
        setIsPhone(
                true,
                ConfigurationSwitcher.UICurrent.PHONE == new ConfigurationSwitcher().getUICurrent(config));

       /* NewRelic.withApplicationToken(
                "eu01xx8e8c3e4790f9795fc7133941ac935ff9a204"
        ).start(this.getApplication());
*/
        /*if (getIntent().hasExtra(Constants.NOTIFICATION_TYPE_ACCEPTED)) {
            if (getIntent().getStringExtra(Constants.NOTIFICATION_TYPE_ACCEPTED).equals(Constants.NOTIFICATION_TYPE_ACCEPTED)
                    && UserPreferences.get().isLoggedIn(OrdersActivity.this)) {
                loadHomeFragment();

            } else {
                Constants.switchActivity(OrdersActivity.this, OrdersActivity.class);
                finish();
            }

        } else {
            loadHomeFragment();
        }*/

        setContentView(R.layout.root);

        loadRoot();

        if (!UserPreferences.get().isLoggedIn(OrdersActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissions();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (isDelayingSetOrientation) {
            /*
             * onResume is too early to call setRequestedOrientation but onPostResume works
             *
             * This is to handle the first-run (from splash...onCreate..onResume), and specifically
             * where the device was in an orientation for the splash which is now different to the
             * orientation we calculated for the app. Without this delay the splash will continue to
             * show after the rotation (caused by setRequestedOrientation) and be messed up until it
             * disappears.
             */
            setIsPhone(false, isPhone);
        }
    }

    @Override
    public void onBackPressed() {


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.root);

        if (fragment instanceof ControllerRootNoAuth) {
            ((ControllerRootNoAuth) fragment).handleBackPress();

        } else if (fragment instanceof ControllerRootWithAuth) {
            ((ControllerRootWithAuth) fragment).onBackPressed();

        } else {
            throw new IllegalArgumentException("Never happen case");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "onNewIntent " + intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ConfigurationSwitcher.UIChange change = new ConfigurationSwitcher().checkConfigurationChange(oldConfig, newConfig);
        this.oldConfig = new Configuration(newConfig);

        Log.d(TAG, "onConfigurationChanged change=" + change);

        switch (change) {
            case IGNORE:
                break;
            case RESET_TO_PHONE:
                setIsPhone(false, true);
                loadRoot();
                break;
            case RESET_TO_TABLET:
                setIsPhone(false, false);
                loadRoot();
                break;
            default:
                throw new IllegalArgumentException("Missing case");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            String[] perms = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.CALL_PHONE
            };

            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, "All permissions are required in order to run this application", 101, perms);
            }
        }
    }

    private void setIsPhone(boolean delayOrientationChange, boolean isPhone) {
        this.isPhone = isPhone;
        this.isDelayingSetOrientation = delayOrientationChange;

        if (!delayOrientationChange) {
            // Tablet could be either SCREEN_ORIENTATION_FULL_USER or SCREEN_ORIENTATION_USER_LANDSCAPE
            setRequestedOrientation(isPhone
                    ? ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                    : ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        }
    }

    private void loadRoot() {
        final Fragment fragment;

        if (UserPreferences.get().isLoggedIn(OrdersActivity.this)) {
            fragment = new ControllerRootWithAuth(
                    isPhone,
                    interfaceRootAuthFragment,
                    PrefManager.getInstance(OrdersActivity.this),
                    UserPreferences.get(),
                    LocalBroadcastManager.getInstance(OrdersActivity.this));
        } else {
            fragment = new ControllerRootNoAuth(
                    interfaceRootNoAuth,
                    PrefManager.getInstance(OrdersActivity.this),
                    UserPreferences.get(),
                    isPhone);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, fragment)
                .commitNowAllowingStateLoss();
    }

    private final ControllerRootNoAuth.ParentInterface interfaceRootNoAuth = new ControllerRootNoAuth.ParentInterface() {
        @Override
        public void goToLoggedIn() {
            loadRoot();
        }
    };

    private final ControllerRootWithAuth.ParentInterface interfaceRootAuthFragment = new ControllerRootWithAuth.ParentInterface() {
        @Override
        public void goToLoginActivity() {
            loadRoot();
        }

        @Override
        public void openAppStoreListing() {
            final String appPackageName = OrdersActivity.this.getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                Toast.makeText(OrdersActivity.this, "Unable to open app store", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void exitApp() {
            finish();
        }
    };


    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Object> {
        Activity mActivity;

        public RegisterForPushNotificationsAsync(Activity activity) {
            this.mActivity = activity;
        }

        protected Object doInBackground(Void... params) {
            try {
                // Register the device for notifications (replace MainActivity with your Activity class name)
                String deviceToken = Pushy.register(OrdersActivity.this);
                return deviceToken;
            } catch (Exception exc) {
                // Registration failed, provide exception to onPostExecute()
                return exc;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            String message;

            // Registration failed?
            if (result instanceof Exception) {
                // Log to console
                Log.e("Pushy", result.toString());

                // Display error in alert
                message = ((Exception) result).getMessage();
            } else {
                message = "Pushy device token: " + result.toString() + "\n\n(copy from logcat)";
            }

            // Registration succeeded, display an alert with the device token
            new android.app.AlertDialog.Builder(this.mActivity)
                    .setTitle("Pushy")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }


}
