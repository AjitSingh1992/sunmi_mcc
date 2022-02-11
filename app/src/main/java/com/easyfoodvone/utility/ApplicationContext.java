package com.easyfoodvone.utility;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.easyfoodvone.R;
import com.easyfoodvone.utility.printerutil.AidlUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.newrelic.agent.android.NewRelic;

public class ApplicationContext extends Application {
    private static ApplicationContext applicationContext;
    private static Context context;
    private MediaPlayer player;
    private Handler handler;

    public static synchronized ApplicationContext getInstance() {
        return applicationContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        SplashScreenTimer timer = new SplashScreenTimer(3000);
        timer.beginBeforeAnyAppInitialisation();

        super.onCreate();

        workaroundKitKatTLSConnectionIssues();

        //MjQ4ODA5NXxNT0JJTEV8QVBQTElDQVRJT058MTY3MTU3MDQ
        NewRelic.withApplicationToken(
                "eu01xx8e8c3e4790f9795fc7133941ac935ff9a204"
        ).start(this.getApplicationContext());

        applicationContext = this;
        context = getApplicationContext();
        handler = new Handler();
        AidlUtil.getInstance().connectPrinterService(this);

        timer.endIfSplashRemainingBlockThread();
    }
    public static Context getAppContext() {
        return context;
    }
    public void playNotificationSound() {
        try {
            if (player == null) {
                handler.removeCallbacks(stopPlayer);
                Uri alarmSound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.mobile_order_notification_sound);
                player = MediaPlayer.create(this, alarmSound);
                player.setLooping(true);
                player.start();
                handler.postDelayed(stopPlayer, (1000 * 180));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopNotificationSound() {
        try {
            boolean playing = player != null && player.isLooping() && player.isPlaying();
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Runnable stopPlayer = new Runnable() {
        @Override
        public void run() {
            stopNotificationSound();
        }
    };

    private void workaroundKitKatTLSConnectionIssues() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            // You will get a connection error on KitKat (something to do with TLS 1.2)
            // Unless we install this Google Play Provider
            try {
                ProviderInstaller.installIfNeeded(this);

            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();

                // Prompt the user to install/update/enable Google Play services.
                try {
                    Intent repairPrompt = e.getIntent();
                    startActivity(repairPrompt);
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();

                // Give the user bad news
                Toast.makeText(
                        ApplicationContext.this,
                        "Sorry, Google Play Services are required for connectivity on Android 4.4 or below",
                        Toast.LENGTH_LONG)
                    .show();
            }
        }
    }
}