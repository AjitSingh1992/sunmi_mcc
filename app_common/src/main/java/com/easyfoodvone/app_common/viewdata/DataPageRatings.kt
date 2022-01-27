package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataPageRatings(val outputEvents: OutputEvents,
                      val averageRating: ObservableField<String>,
                      val averageNoRating: ObservableField<String>) {

    interface OutputEvents {
        fun onClickSearch()
    }

    // Helper to assign initial values
    constructor(outputEvents: OutputEvents) : this(
        outputEvents,
        ObservableField(""),
        ObservableField("")
    )
}