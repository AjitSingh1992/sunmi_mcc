/*Created by Omnisttechhub Solution*/
package com.easyfoodvone.utility;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.material.snackbar.Snackbar;

import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Omnisttechhub Solutiongit
 */

public class Helper {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final String API_DELIVERY_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * for showing the messages in the bottom
     */
    public static void showSnackBar(View view1, String message) {
        try {
            Snackbar snackbar = Snackbar.make(view1, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * for showing the messages in the bottom
     */
    public static void showSnackBar(TextView view1, String message) {
        try {
            Snackbar snackbar = Snackbar.make(view1, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = activity.findViewById(android.R.id.content);
            if (in != null) {
                in.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Throwable e) {
        }
    }

    // TODO check Locale.UK is acceptable for all easy clients?
    public static @Nullable Date parseDeliveryDate(@Nullable String inputDate) {
        // e.g. "2019-09-19 08:34:35"
        SimpleDateFormat inputFormat = new SimpleDateFormat(API_DELIVERY_DATE_TIME_FORMAT);
        try {
            return inputFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO check Locale.UK is acceptable for all easy clients?
    public static String formatDate(String input) {
        @Nullable Date inputDate = parseDeliveryDate(input);
        if (inputDate == null) {
            return "";
        }

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy HH:mm ");
        return outputFormat.format(inputDate);
    }

    // TODO check Locale.UK is acceptable for all easy clients?
    public static @NonNull String formatDateForAPIWithExtraTime(@NonNull Date inputDate) {
        SimpleDateFormat apiFormat = new SimpleDateFormat(API_DELIVERY_DATE_TIME_FORMAT);
        return apiFormat.format(inputDate);
    }

    /**
     * This method is to return number with zero or not zero i.e. to make a number in two digit
     *
     * @param number specifies the number that needs to be foramtted
     */
    public static String addZero(int number) {
        if (number > 9)
            return "" + number;
        else
            return "0" + number;
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null
        ) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    public static String capitalize(String line) {

        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }
}

