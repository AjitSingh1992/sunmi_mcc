package com.easyfoodvone.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyfoodvone.OrdersActivity;
import com.easyfoodvone.R;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "onMessageReceived from: " + remoteMessage.getFrom());

            userPreferences = UserPreferences.get();
            notificationUtils = new NotificationUtils(getApplicationContext());
            String timestamp = "",order_number="";

            @Nullable String message = remoteMessage.getData().get("message");
            if(remoteMessage.getData().get("timestamp")!=null)
            timestamp = remoteMessage.getData().get("timestamp");
            @Nullable String notif_type = remoteMessage.getData().get("type");
            if(remoteMessage.getData().get("order_number")!=null)
            order_number = remoteMessage.getData().get("order_number");
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void generateJobNotification(String message,String title,Intent intent) {
        String CHANNEL_ID = "easyFood_01";// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        boolean useNewMethod = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        final PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        Bitmap bitmap = null;
        if (useNewMethod)
            bitmap = ((BitmapDrawable) getDrawable(R.drawable.ic_launcher)).getBitmap();
        else
            bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();


        //  Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or updJetpackate. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(false);
            if (channel == null) {
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            }
            mNotificationManager.createNotificationChannel(channel);
        }

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(bitmap)
                .setContentTitle(getString(R.string.app_name)+" "+title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)

                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setSound(null);
        mNotificationManager.notify(4, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNewOrderNotification(@NonNull String message, @Nullable String timestamp) {
        final Intent resultIntent = new Intent(this, OrdersActivity.class);
        resultIntent.putExtra(Constants.NOTIFICATION_TYPE_ACCEPTED, Constants.NOTIFICATION_TYPE_ACCEPTED);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        resultIntent.putExtra("message", message);
        showNotificationMessage("New Orders!", message, timestamp, resultIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showOpenCloseNotification(@NonNull String message, boolean isNowOpen) {
        final Intent resultIntent = new Intent(this, OrdersActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNotificationMessage(@NonNull String title, @NonNull String message, @Nullable String timeStamp, @NonNull Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       // notificationUtils.showNotificationMessage(title, message, timeStamp, intent);

        generateJobNotification(message,title,intent);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
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


    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        removeFirebaseOrigianlNotificaitons();
        try {
            String timestamp = "",order_number="";
            Bundle bundle = intent.getExtras();
            //Data retrieved from notification payload send
            String title = bundle.getString("title");
            String message = bundle.getString("message");
            if(bundle.getString("timestamp")!=null) {
                timestamp = bundle.getString("timestamp");
            }
            String type = bundle.getString("type");
            if(bundle.getString("order_number")!=null) {
                order_number = bundle.getString("order_number");
            }
            String res_status = bundle.getString("res_status");



            if (type == null) {
                // Nothing we can do, unexpected event type

            } else if (type.equals(Constants.NOTIFICATION_TYPE_ACCEPTED)) {
                ApplicationContext.getInstance().playNotificationSound();
                showNewOrderNotification(message == null ? "" : message, timestamp);
                //broadcastNewOrder(message == null ? "" : message, order_number == null ? "" : order_number);
                if ( ! TextUtils.isEmpty(order_number)) {
                    //printOrderDetails(order_number);
                }

            } else if (type.equals(Constants.NOTIFICATION_CHARITY_STATUS)) {
                Intent charityIntent = new Intent(CHARITY_STATUS_INTENT);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(charityIntent);

            } else if (type.equalsIgnoreCase("restautant_open_status")) {
                if (res_status == null) {
                    // Nothing we can do

                } else if (res_status.equalsIgnoreCase("open")) {
                    // timestamp AND order_number will be null
                    if(userPreferences!=null) {
                        persistIsOpen(userPreferences, true);
                    }
                    broadcastOpenClose(res_status);
                    showOpenCloseNotification(message == null ? "" : message, true);

                } else if (res_status.equalsIgnoreCase("closed")) {
                    // timestamp AND order_number will be null
                    if(userPreferences!=null) {
                        persistIsOpen(userPreferences, false);
                    }
                    broadcastOpenClose(res_status);
                    showOpenCloseNotification(message == null ? "" : message, false);
                }

            } else if (type.equals(Constants.NOTIFICATION_TYPE_CANCELED)) {

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }
    private void removeFirebaseOrigianlNotificaitons() {

        //check notificationManager is available
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null )
            return;

        //check api level for getActiveNotifications()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //if your Build version is less than android 6.0
            //we can remove all notifications instead.
            //notificationManager.cancelAll();
            return;
        }

        //check there are notifications
        StatusBarNotification[] activeNotifications =
                notificationManager.getActiveNotifications();
        if (activeNotifications == null)
            return;

        //remove all notification created by library(super.handleIntent(intent))
        for (StatusBarNotification tmp : activeNotifications) {

            String tag = tmp.getTag();
            int id = tmp.getId();

            //trace the library source code, follow the rule to remove it.
            if (tag != null && tag.contains("FCM-Notification"))
                notificationManager.cancel(tag, id);
        }
    }

}
