package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataPageDonateSuccess(val outputEvents: OutputEvents,
                            val isCancellationPage: ObservableField<Boolean>) {

    interface OutputEvents {
        fun onClickOk()
    }

    // Helper to assign initial values
    constructor(outputEvents: OutputEvents) : this(
            outputEvents,
            ObservableField(false))
}