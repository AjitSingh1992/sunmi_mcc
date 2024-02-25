package com.easyfoodvone.pax;


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
import android.os.Build;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.itextpdf.text.factories.RomanNumberFactory.getString;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_DATA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFICATION_CLICK;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFICATION_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFY_DATA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MEIDA;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_CONTENT;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_DATA;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_NID;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_TITLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.easyfoodvone.OrdersActivity;
import com.easyfoodvone.R;
import com.easyfoodvone.utility.ApplicationContext;
import com.easyfoodvone.utility.Constants;
import com.easyfoodvone.utility.NotificationUtils;

/**
 * Created by fojut on 2019/5/20.
 */
public class PushMessageReceiver extends BroadcastReceiver {

    private static final Logger logger = LoggerFactory.getLogger(PushMessageReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {

         if (intent.getAction().equals("order_event")) {
            notificationRing(context);
        } else if (intent.getAction().equals("order_notification")) {
            notificationRing(context);
        }


        if (ACTION_NOTIFY_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) {
            logger.info("### NOTIFY_DATA_MESSAGE_RECEIVED ###");
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            logger.info("### notification title={}, content={} ###", title, content);
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
            logger.info("### data json={} ###", dataJson);
            Toast.makeText(context, "  data=" + dataJson, Toast.LENGTH_SHORT).show();
        } else if (ACTION_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) {
            logger.info("### NOTIFY_DATA_MESSAGE_RECEIVED ###");
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
            logger.info("### data json={} ###", dataJson);
            Toast.makeText(context, "  data=" + dataJson, Toast.LENGTH_SHORT).show();
        } else if (ACTION_NOTIFICATION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            logger.info("### NOTIFICATION_MESSAGE_RECEIVED ###");
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            logger.info("### notification title={}, content={} ###", title, content);
        } else if (ACTION_NOTIFICATION_CLICK.equals(intent.getAction())) {
            logger.info("### NOTIFICATION_CLICK ###");
            int nid = intent.getIntExtra(EXTRA_MESSAGE_NID, 0);
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
            logger.info("### notification nid={}, title={}, content={}, dataJson={} ###", nid, title, content, dataJson);
        } else if (ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //You can decide when to show this media notification, immediately or later.
            logger.info("### ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED ###");
            String mediaJson = intent.getStringExtra(EXTRA_MEIDA);
            logger.info("### media json={} ###", mediaJson);
            Toast.makeText(context, "  mediaJson=" + mediaJson, Toast.LENGTH_SHORT).show();
        }
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