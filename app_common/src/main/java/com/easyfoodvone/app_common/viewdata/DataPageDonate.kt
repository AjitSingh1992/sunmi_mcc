package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataPageDonate(val outputEvents: OutputEvents,
                     val numberMeals: ObservableField<String>) {

    interface OutputEvents {
        fun onClickDonate()
    }

    // Helper to assign initial values
    constructor(outputEvents: OutputEvents) : this(
            outputEvents,
            ObservableField(""))
}