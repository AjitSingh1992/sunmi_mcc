package com.easyfoodvone.ingenicoo;

import static com.itextpdf.text.factories.RomanNumberFactory.getString;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyfoodvone.OrdersActivity;
import com.easyfoodvone.R;
import com.easyfoodvone.utility.ApplicationContext;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.NotificationUtils;
import com.ingenico.pushyclient.MainActivity;

import me.pushy.sdk.Pushy;

public class PushReceiver extends BroadcastReceiver {
    public final static String TEST_NOTIFICATION_ACTION = "order_reciver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra("event").equals(TEST_NOTIFICATION_ACTION)) {
            Intent intent2 = new Intent(Constants.NOTIFICATION_TYPE_ACCEPTED);
            intent2.putExtra("message", "new order");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);

            notificationRing(context);
        } else if (intent.getAction().equals("order_event")) {
            notificationRing(context);
        } else if (intent.getAction().equals("order_notification")) {
            notificationRing(context);
        }/*
        // Attempt to extract the "title" property from the data payload, or fallback to app shortcut label
        String notificationTitle = intent.getStringExtra("title") != null ? intent.getStringExtra("title") : context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();

        // Attempt to extract the "message" property from the data payload: {"message":"Hello World!"}
        String notificationText = intent.getStringExtra("message") != null ? intent.getStringExtra("message") : "Test notification";

        // Prepare a notification with vibration, sound and lights
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));

        // Automatically configure a Notification Channel for devices running Android O+
        Pushy.setNotificationChannel(builder, context);

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // Build the notification and display it
        //
        // Use a random notification ID so multiple
        // notifications don't overwrite each other
        notificationManager.notify((int)(Math.random() * 100000), builder.build());*/
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void notificationRing(Context context){
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        Boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

        ApplicationContext.getInstance().playNotificationSound();

        Toast.makeText(context, "notification recieved", Toast.LENGTH_SHORT).show();

        if (isInBackground) {
            showNewOrderNotification(context, "new order", "");
        } else {
            Intent intent = new Intent(context, OrdersActivity.class);
            intent.putExtra("event", "order");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNewOrderNotification(Context context, @NonNull String message, @Nullable String timestamp) {
        final Intent resultIntent = new Intent(context, OrdersActivity.class);
        resultIntent.putExtra(Constants.NOTIFICATION_TYPE_ACCEPTED, Constants.NOTIFICATION_TYPE_ACCEPTED);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        resultIntent.putExtra("message", message);
        showNotificationMessage(context, "New Orders!", message, timestamp, resultIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNotificationMessage(Context context, @NonNull String title, @NonNull String message, @Nullable String timeStamp, @NonNull Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // notificationUtils.showNotificationMessage(title, message, timeStamp, intent);

        generateJobNotification(context, message, title, intent);
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.playNotificationSound();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void generateJobNotification(Context context, String message, String title, Intent intent) {
        String CHANNEL_ID = "easyFood_01";// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        boolean useNewMethod = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("event", "order");

        final PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        Bitmap bitmap = null;
        if (useNewMethod)
            bitmap = ((BitmapDrawable) context.getDrawable(R.drawable.ic_launcher)).getBitmap();
        else
            bitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();


        //  Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder;
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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

        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(bitmap)
                .setContentTitle(getString(R.string.app_name) + " " + title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)

                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setSound(null);
        mNotificationManager.notify(4, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }


}
