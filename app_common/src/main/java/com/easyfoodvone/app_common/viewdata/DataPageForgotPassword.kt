package com.easyfoodvone.app_common.viewdata

import android.widget.TextView
import com.easyfoodvone.app_common.separation.ObservableField

class DataPageForgotPassword(
        val dataHeader: DataIncludeHeader,
        val outputEvents: OutputEvents,
        val inputEmailAddress: ObservableField<String>,
        val emailError: ObservableField<String>) {

    interface OutputEvents {
        fun onClickReset()
        fun onClickCancel()
    }

    // Helper to assign initial values
    constructor(dataHeader: DataIncludeHeader, outputEvents: OutputEvents) : this(
            dataHeader,
            outputEvents,
            ObservableField(""),
            ObservableField(""))

    fun keepOldTextIfNowEmpty(textView: TextView, newValue: String): CharSequence {
        if (newValue.isEmpty()) {
            return textView.text
        } else {
            return newValue
        }
    }
}
