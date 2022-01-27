package com.easyfoodvone.utility

import android.os.SystemClock

class SplashScreenTimer(val splashVisibleForMillis: Long) {

    private var elapsedRealtimeMillis: Long? = null

    fun beginBeforeAnyAppInitialisation() {
        elapsedRealtimeMillis = SystemClock.elapsedRealtime()
    }

    fun endIfSplashRemainingBlockThread() {
        val millisSinceBegin = SystemClock.elapsedRealtime() - elapsedRealtimeMillis!!

        if (millisSinceBegin < splashVisibleForMillis) {
            val remainingMillis = splashVisibleForMillis - millisSinceBegin;

            Thread.sleep(remainingMillis)
        }
    }
}