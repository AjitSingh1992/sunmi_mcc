package com.easyfoodvone.app_common.viewdata

import android.widget.TextView
import com.easyfoodvone.app_common.separation.ObservableField

class DataPageContactSupport(val dataHeader: DataIncludeHeader,
                             val outputEvents: OutputEvents,
                             val inputEmailAddress: ObservableField<String>,
                             val inputMessage: ObservableField<String>,
                             val emailError: ObservableField<String>,
                             val messageError: ObservableField<String>) {

    interface OutputEvents {
        fun onClickSend()
        fun onClickCancel()
    }

    // Helper to assign initial values
    constructor(dataHeader: DataIncludeHeader, outputEvents: OutputEvents) : this(
            dataHeader,
            outputEvents,
            ObservableField(""),
            ObservableField(""),
            ObservableField(""),
            ObservableField(""))

    fun keepOldTextIfNowEmpty(textView: TextView, newValue: String): CharSequence {
        return if (newValue.isEmpty()) {
            textView.text
        } else {
            newValue
        }
    }
}
