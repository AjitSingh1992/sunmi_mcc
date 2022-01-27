package com.easyfoodvone.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyfoodvone.OrdersActivity;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.ws.NewDetailBean;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.utility.ApplicationContext;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.NotificationUtils;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;
import com.easyfoodvone.utility.printerutil.PrintEsayFood;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyfoodvone.utility.Constants.CHARITY_STATUS_INTENT;
import static com.easyfoodvone.utility.Helper.isInternetOn;
import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private UserPreferences userPreferences;

    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        Log.d(TAG, "RefreshedToken :" + refreshedToken);

        UserPreferences.get().setFirebaseToken(this, refreshedToken);

        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "onMessageReceived from: " + remoteMessage.getFrom());

            userPreferences = UserPreferences.get();
            notificationUtils = new NotificationUtils(getApplicationContext());

            @Nullable String message = remoteMessage.getData().get("message");
            @Nullable String timestamp = remoteMessage.getData().get("timestamp");
            @Nullable String notif_type = remoteMessage.getData().get("type");
            @Nullable String order_number = remoteMessage.getData().get("order_number");
            @Nullable String res_status = remoteMessage.getData().get("res_status");

            if (notif_type == null) {
                // Nothing we can do, unexpected event type

            } else if (notif_type.equals(Constants.NOTIFICATION_TYPE_ACCEPTED)) {
                ApplicationContext.getInstance().playNotificationSound();
                showNewOrderNotification(message == null ? "" : message, timestamp);
                broadcastNewOrder(message == null ? "" : message, order_number == null ? "" : order_number);
                if ( ! TextUtils.isEmpty(order_number)) {
                    printOrderDetails(order_number);
                }

            } else if (notif_type.equals(Constants.NOTIFICATION_CHARITY_STATUS)) {
                Intent charityIntent = new Intent(CHARITY_STATUS_INTENT);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(charityIntent);

            } else if (notif_type.equalsIgnoreCase("restautant_open_status")) {
                if (res_status == null) {
                    // Nothing we can do

                } else if (res_status.equalsIgnoreCase("open")) {
                    // timestamp AND order_number will be null
                    persistIsOpen(userPreferences, true);
                    broadcastOpenClose(res_status);
                    showOpenCloseNotification(message == null ? "" : message, true);

                } else if (res_status.equalsIgnoreCase("closed")) {
                    // timestamp AND order_number will be null
                    persistIsOpen(userPreferences, false);
                    broadcastOpenClose(res_status);
                    showOpenCloseNotification(message == null ? "" : message, false);
                }

            } else if (notif_type.equals(Constants.NOTIFICATION_TYPE_CANCELED)) {

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void persistIsOpen(@NonNull UserPreferences userPreferences, boolean isNowOpen) {
        LoginResponse.Data loginResponse = userPreferences.getLoggedInResponse(this);
        loginResponse.setIs_open(isNowOpen);
        userPreferences.setLoggedInResponse(this, loginResponse);
    }

    private void showNewOrderNotification(@NonNull String message, @Nullable String timestamp) {
        final Intent resultIntent = new Intent(this, OrdersActivity.class);
        resultIntent.putExtra(Constants.NOTIFICATION_TYPE_ACCEPTED, Constants.NOTIFICATION_TYPE_ACCEPTED);
        resultIntent.putExtra("message", message);
        showNotificationMessage("New Orders!", message, timestamp, resultIntent);
    }

    private void showOpenCloseNotification(@NonNull String message, boolean isNowOpen) {
        final Intent resultIntent = new Intent(this, OrdersActivity.class);
        if (isNowOpen) {
            showNotificationMessage("Hey! It's Restaurant Opening Time", message, null, resultIntent);
        } else {
            showNotificationMessage("Hey! It's Restaurant Closing Time", message, null, resultIntent);
        }
    }

    private void broadcastNewOrder(@NonNull String message, @NonNull String order_number) {
        Intent intent = new Intent(Constants.NOTIFICATION_TYPE_ACCEPTED);
        intent.putExtra("message", message);
        intent.putExtra(Constants.ORDER_NUMBER, order_number);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastOpenClose(@NonNull String res_status) {
        Intent intent = new Intent(Constants.OPEN_CLOSE_INTENT);
        intent.putExtra("type", res_status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void showNotificationMessage(@NonNull String title, @NonNull String message, @Nullable String timeStamp, @NonNull Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    private void printOrderDetails(@NonNull String orderNumber) {
        if (TextUtils.isEmpty(orderNumber)) {
            return;
        }

        if ( ! isInternetOn(this)) {
            return;
        }

        if ( ! userPreferences.isLoggedIn(this)) {
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String authToken = PrefManager.getInstance(getApplicationContext()).getPreference(AUTH_TOKEN,"");
        @NonNull LoginResponse.Data loggedInData = userPreferences.getLoggedInResponse(getApplicationContext());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("order_number", orderNumber);

        Call<NewDetailBean> call = apiInterface.getOrderDetail(authToken, jsonObject);
        call.enqueue(new Callback<NewDetailBean>() {
            @Override
            public void onResponse(@NonNull Call<NewDetailBean> call, @NonNull Response<NewDetailBean> response) {
                try {
                    if (response.isSuccessful()) {
                        NewDetailBean newDetailBean = response.body();
                        if (newDetailBean.isSuccess()) {
                            byte[] logoByte = userPreferences.getRestaurantLogoBitmap(getApplicationContext());
                            Bitmap logo = null;
                            if (logoByte != null) {
                                logo = BitmapFactory.decodeByteArray(logoByte, 0, logoByte.length);
                            }
                            if (newDetailBean.getOrders_details() != null) {
                                boolean serviceReady = PrintEsayFood.printOrderDetails(
                                        getApplicationContext(), logo, loggedInData, newDetailBean.getOrders_details());

                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewDetailBean> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
