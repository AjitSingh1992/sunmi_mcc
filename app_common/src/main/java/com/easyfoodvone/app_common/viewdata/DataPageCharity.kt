package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataPageCharity(val outputEvents: OutputEvents,
                      val targetMeals: ObservableField<String>,
                      val donatedMeals: ObservableField<String>) {

    interface OutputEvents {
        fun onClickDonate()
    }

    // Helper to assign initial values
    constructor(outputEvents: OutputEvents) : this(
            outputEvents,
            ObservableField(""),
            ObservableField(""))
}