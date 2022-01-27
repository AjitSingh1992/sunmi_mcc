package com.easyfoodvone

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.Log

class ConfigurationSwitcher {

    enum class UIChange {
        IGNORE,
        RESET_TO_PHONE,
        RESET_TO_TABLET
    }

    enum class UICurrent {
        PHONE,
        TABLET
    }

    private val TAG = ConfigurationSwitcher::class.java.simpleName

    /**
    screenOrientation is locked to portrait or landscape programmatically, in case we need exceptions for Clover devices
    We don't want the activity to restart on minor changes to avoid activity-level dialogs being dismissed or current page changing?

    Note that any change events not listed under configChanges will recreate OrdersActivity (fragments will reset to top level)
    1. If this is the only "restart" change and the smallestScreenSize hasn't changed enough to swap between layouts, don't change it
     * smallestScreenSize - Alternate layout needs to be loaded. This will change if the app adjusts split screen, for example
    2. If the activity doesn't restart we can keep fragment data, just reloading the layout
     * density|fontScale|layoutDirection|locale - layout needs reloading to pick up new textsize/dimens/strings/resources
     * uiMode - We might want night mode styles in the future
    3. These might want to recreate the Activity, as the screen size changed. Don't actually restart to avoid infinite loop when orientation lock is changed.
     * orientation|screenSize|screenLayout - No different anything if the phone is rotated or split screen is used or overall size becomes small/medium/large
    4. These shouldn't recreate the Activity, or even fragment as they're not really changing anything substantial enough
     * keyboardHidden - Layout will just adjust its size for the temporary keyboard
     * keyboard|touchscreen|navigation|mcc|mnc - We definitely don't care
     * colorMode - Probably don't care

    We programmatically recreate fragments as needed on resource changes, rather than letting it happen automatically
    1. We need to recreate fragments, it's not possible to change fragment view once inflated (fragment onconfigchange no use)
    2. Views need to be recreated to offer manual toggle between Tablet and Phone bindings (auto can't handle exceptions for weird Clover devices)
    */
    fun checkConfigurationChange(oldConfig: Configuration, newConfig: Configuration): UIChange {
        val diffBits = newConfig.diff(oldConfig)

        val restartIfLayoutChangedBits: Int =
                ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE
        val restartMandatoryBits: Int =
                ActivityInfo.CONFIG_DENSITY or
                ActivityInfo.CONFIG_FONT_SCALE or
                ActivityInfo.CONFIG_LAYOUT_DIRECTION or
                ActivityInfo.CONFIG_LOCALE or
                ActivityInfo.CONFIG_UI_MODE
        val minorSizeAdjustBits: Int =
                ActivityInfo.CONFIG_ORIENTATION or
                ActivityInfo.CONFIG_SCREEN_SIZE or
                ActivityInfo.CONFIG_SCREEN_LAYOUT
        val ignoreBits: Int =
                ActivityInfo.CONFIG_KEYBOARD_HIDDEN or
                ActivityInfo.CONFIG_KEYBOARD or
                ActivityInfo.CONFIG_TOUCHSCREEN or
                ActivityInfo.CONFIG_NAVIGATION or
                ActivityInfo.CONFIG_MCC or
                ActivityInfo.CONFIG_MNC or
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ActivityInfo.CONFIG_COLOR_MODE else 0)

        val restartIfLayoutChanged: Boolean = (diffBits and restartIfLayoutChangedBits) != 0;
        val restartMandatory: Boolean = (diffBits and restartMandatoryBits) != 0;
        val minorSizeAdjust: Boolean = (diffBits and minorSizeAdjustBits) != 0;
        val ignore: Boolean = (diffBits and ignoreBits) != 0;

        Log.d(TAG, "Handle configuration change" +
                " smallestScreenSize=" + (diffBits and ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE) +
                " density=" + (diffBits and ActivityInfo.CONFIG_DENSITY) +
                " fontScale=" + (diffBits and ActivityInfo.CONFIG_FONT_SCALE) +
                " layoutDirection=" + (diffBits and ActivityInfo.CONFIG_LAYOUT_DIRECTION) +
                " locale=" + (diffBits and ActivityInfo.CONFIG_LOCALE) +
                " uiMode=" + (diffBits and ActivityInfo.CONFIG_UI_MODE) +
                " bitsRestartIfLayoutChanged=" + (diffBits and restartIfLayoutChangedBits) +
                " bitsRestartMandatory=" + (diffBits and restartMandatoryBits) +
                " bitsMinorSizeAdjust=" + (diffBits and minorSizeAdjustBits) +
                " bitsIgnore=" + (diffBits and ignoreBits));

        val oldUI = getUICurrent(oldConfig)
        val newUI = getUICurrent(newConfig)

        if (! ignore && ! minorSizeAdjust && ! restartIfLayoutChanged && ! restartMandatory) {
            // Unexpected flag
            return UIChange.IGNORE

        } else if (restartMandatory || (restartIfLayoutChanged && oldUI != newUI)) {
            Log.d(TAG, "Handle configuration change confirmed")

            // Something resource related changed (density etc.), reload definitely needed
            return when (getUICurrent(newConfig)) {
                UICurrent.PHONE -> UIChange.RESET_TO_PHONE
                UICurrent.TABLET -> UIChange.RESET_TO_TABLET
            }

        } else {
            // (restartIfLayoutChanged but DP didn't change enough) or (minorSizeAdjust) or (total ignore)
            return UIChange.IGNORE
        }
    }

    fun getUICurrent(config: Configuration): UICurrent {
        return if (config.smallestScreenWidthDp < 500) {
            UICurrent.PHONE
        } else {
            UICurrent.TABLET
        }
    }
}