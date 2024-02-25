package com.easyfoodvone.utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyfoodvone.OrdersActivity;
import com.easyfoodvone.models.LoginResponse;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.concurrent.Executors;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class UserPreferences {

    private static final UserPreferences userPreferences = new UserPreferences();
    public static UserPreferences get() {
        return userPreferences;
    }

    private static final String FILE_USER_PREFERENCES = "preferense";
    private static final String KEY_LOGIN_RESPONSE = "login_response";
    private static final String KEY_IS_LOGGED_IN = "LoginCheck";
    private static final String KEY_FIREBASE_TOKEN = "firebase_token";
    private static final String PUSHY_TOKEN = "pushy_token";
    private static final String DEVICE_TYPE = "device_type";
    private static final String KEY_RESTAURANT_LOGO_BITMAP = "restaurant_logo";

    private UserPreferences() { }

    public void setLoggedIn(Context context, @NonNull LoginResponse.Data loginResponse) {
        SharedPreferences.Editor edit = getSharedPref(context).edit();
        edit.putBoolean(KEY_IS_LOGGED_IN, true);
        edit.putString(KEY_LOGIN_RESPONSE, serialiseLoginResponse(loginResponse));
        edit.commit();
    }

    public boolean isLoggedIn(Context context) {
        return getSharedPref(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedOut(Context context) {
        // Clear everything except for the firebase token (we add it back at the end)
        String firebaseToken = getFirebaseToken(context);

        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.clear();
        editor.putString(KEY_FIREBASE_TOKEN, firebaseToken);
        editor.commit();

        if (Helper.getDeviceName().contains("Sunmi")) {
            UserPreferences.get().setDEVICE_TYPE(context, "0");
        } else if (Helper.getDeviceName().contains("DX8000")) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(context);
                    UserPreferences.get().setPushyToken(context, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });

            UserPreferences.get().setDEVICE_TYPE(context, "2");
        } else if (Helper.getDeviceName().contains("A920Pro") || Helper.getDeviceName().contains("Lephone")) {
            UserPreferences.get().setDEVICE_TYPE(context, "3");
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(context);
                    UserPreferences.get().setPushyToken(context, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });

        } else if (Helper.getDeviceName().contains("Qualcomm Saturn1000F2")) {
            UserPreferences.get().setDEVICE_TYPE(context, "4");

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(context);
                    UserPreferences.get().setPushyToken(context, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });

        } else {
            UserPreferences.get().setDEVICE_TYPE(context, "4");

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    String deviceToken = Pushy.register(context);
                    UserPreferences.get().setPushyToken(context, deviceToken);
                } catch (PushyException e) {
                    throw new RuntimeException(e);
                }

            });
        }


    }

    @Nullable
    public LoginResponse.Data getLoggedInResponse(Context context) {
        @Nullable String jsonText = getSharedPref(context).getString(KEY_LOGIN_RESPONSE, null);
        if (jsonText != null) {
            Gson gson = new Gson();
            return gson.fromJson(jsonText, LoginResponse.Data.class);  //EDIT: gso to gson
        } else {
            return null;
        }
    }

    public void setLoggedInResponse(Context context, @NonNull LoginResponse.Data data) {
        if ( ! isLoggedIn(context)) {
            return;
        }

        SharedPreferences.Editor edit = getSharedPref(context).edit();
        edit.putString(KEY_LOGIN_RESPONSE, serialiseLoginResponse(data));
        edit.commit();
    }

    @Nullable
    public String getFirebaseToken(Context context) {
        return getSharedPref(context).getString(KEY_FIREBASE_TOKEN, null);
    }

    public void setFirebaseToken(Context context, @Nullable String token) {
        SharedPreferences.Editor edit = getSharedPref(context).edit();
        edit.putString(KEY_FIREBASE_TOKEN, token);
        edit.commit();
    }
    @Nullable
    public String getPushyToken(Context context) {
        return getSharedPref(context).getString(PUSHY_TOKEN, null);
    }

    public void setPushyToken(Context context, @Nullable String token) {
        SharedPreferences.Editor edit = getSharedPref(context).edit();
        edit.putString(PUSHY_TOKEN, token);
        edit.commit();
    }

    @Nullable
    public String getDEVICE_TYPE(Context context) {
        return getSharedPref(context).getString(DEVICE_TYPE, null);
    }

    public void setDEVICE_TYPE(Context context, @Nullable String token) {
        SharedPreferences.Editor edit = getSharedPref(context).edit();
        edit.putString(DEVICE_TYPE, token);
        edit.commit();
    }

    public void setRestaurantLogoBitmap(Context context, byte[] byteArray) {
        SharedPreferences.Editor edit = getSharedPref(context).edit();
        edit.putString(KEY_RESTAURANT_LOGO_BITMAP, Arrays.toString(byteArray));
        edit.apply();
    }

    @Nullable
    public byte[] getRestaurantLogoBitmap(Context context) {
        SharedPreferences pref = getSharedPref(context);
        String stringArray = pref.getString(KEY_RESTAURANT_LOGO_BITMAP, null);

        if (stringArray != null) {
            String[] split = stringArray.substring(1, stringArray.length() - 1).split(", ");
            byte[] array = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = Byte.parseByte(split[i]);
            }

            return array;
        }
        return null;
    }






    private SharedPreferences getSharedPref(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences(FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return pref;
    }

    @NonNull
    private String serialiseLoginResponse(@NonNull LoginResponse.Data data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
