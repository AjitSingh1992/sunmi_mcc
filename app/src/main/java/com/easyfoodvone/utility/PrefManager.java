package com.easyfoodvone.utility;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {

    private static PrefManager sInstance;
    private final SharedPreferences sharedPreferences;


    private PrefManager(Context context) {
        super();
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        sharedPreferences.edit().apply();
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PrefManager(context);
        }
        return sInstance;
    }


    public void deletePreference(String key) {
        if (sharedPreferences.contains(key)) {
            sharedPreferences.edit().remove(key).apply();
        }
    }

    public void deleteAllPreference() {
        try {
            sharedPreferences.edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePreference(String key, Object value) {
        deletePreference(key);
        if (value instanceof Boolean) {
            sharedPreferences.edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Integer) {
            sharedPreferences.edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof Float) {
            sharedPreferences.edit().putFloat(key, (Float) value).apply();
        } else if (value instanceof Long) {
            sharedPreferences.edit().putLong(key, (Long) value).apply();
        } else if (value instanceof String) {
            sharedPreferences.edit().putString(key, (String) value).apply();
        } else if (value instanceof Enum) {
            sharedPreferences.edit().putString(key, value.toString()).apply();
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }

    }

    public <T> T getPreference(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    public <T> T getPreference(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public boolean isPreferenceExists(String key) {
        return sharedPreferences.contains(key);
    }


}
