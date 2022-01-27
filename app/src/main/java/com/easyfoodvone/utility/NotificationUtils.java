package com.easyfoodvone.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.easyfoodvone.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NotificationUtils {
    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(
            @NonNull String title,
            @NonNull String message,
            @Nullable String timeStamp,
            @NonNull Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    public void showNotificationMessage(
            @NonNull final String title,
            @NonNull final String message,
            @Nullable final String timeStamp,
            @NonNull Intent intent,
            @Nullable String imageUrl) {

        // Check for empty push message
        if (TextUtils.isEmpty(message)) {
            return;
        }

        // notification icon
        final int icon = R.drawable.logo_notification;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (TextUtils.isEmpty(imageUrl)) {
            showSmallNotification(icon, title, message, timeStamp, resultPendingIntent, null);

        } else if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
            Bitmap bitmap = getBitmapFromURL(imageUrl);
            if (bitmap != null) {
                showBigNotification(bitmap, icon, title, message, timeStamp, resultPendingIntent, null);
            } else {
                showSmallNotification(icon, title, message, timeStamp, resultPendingIntent, null);
            }
        }
    }

    private void showSmallNotification(
            int icon,
            @NonNull String title,
            @NonNull String message,
            @Nullable String timeStamp,
            @NonNull PendingIntent resultPendingIntent,
            @Nullable Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);
        String id = mContext.getString(R.string.app_name); // default_channel_id

        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, id);

            Notification notification = mBuilder
                    .setSmallIcon(icon)
                    .setTicker(title)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    //.setSound(alarmSound)
                    .setWhen(getTimeMilliSec(timeStamp, System.currentTimeMillis()))
                    .setSmallIcon(R.drawable.logo_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();

            notificationManager.notify(Constants.NOTIFICATION_ID, notification);

        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, id);

            Notification notification = mBuilder
                    .setSmallIcon(icon)
                    .setTicker(title)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    //.setSound(alarmSound)
                    .setWhen(getTimeMilliSec(timeStamp, System.currentTimeMillis()))
                    .setSmallIcon(R.drawable.logo_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();

            notificationManager.notify(Constants.NOTIFICATION_ID, notification);
        }
    }

    private void showBigNotification(
            @NonNull Bitmap bitmap,
            int icon,
            @NonNull String title,
            @NonNull String message,
            @Nullable String timeStamp,
            @NonNull PendingIntent resultPendingIntent,
            @Nullable Uri alarmSound) {

        String id = mContext.getString(R.string.app_name); // default_channel_id
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, id);
            Notification notification;
            notification = mBuilder.setSmallIcon(icon).setTicker(title)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
//                    .setSound(alarmSound)
                    .setStyle(bigPictureStyle)
                    .setWhen(getTimeMilliSec(timeStamp, System.currentTimeMillis()))
                    .setSmallIcon(R.drawable.logo_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();
            notificationManager.notify(Constants.NOTIFICATION_ID_BIG_IMAGE, notification);

        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, id);
            Notification notification;
            notification = mBuilder.setSmallIcon(icon).setTicker(title)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
//                    .setSound(alarmSound)
                    .setStyle(bigPictureStyle)
                    .setWhen(getTimeMilliSec(timeStamp, System.currentTimeMillis()))
                    .setSmallIcon(R.drawable.logo_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();
            notificationManager.notify(Constants.NOTIFICATION_ID_BIG_IMAGE, notification);
        }
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private static long getTimeMilliSec(@Nullable String timeStamp, long defaultVal) {
        if (timeStamp == null) {
            return defaultVal;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return defaultVal;
        }
    }
}