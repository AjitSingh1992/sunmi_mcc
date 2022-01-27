package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataPageRestaurantProfile(val outputEvents: OutputEvents,
                                val postcode: ObservableField<String>,
                                val restname: ObservableField<String>,
                                val serve_style: ObservableField<String>,
                                val about: ObservableField<String>,
                                val web: ObservableField<String>,
                                val phone: ObservableField<String>,
                                val landline: ObservableField<String>,
                                val address: ObservableField<String>,
                                val email: ObservableField<String>
) {

    interface OutputEvents {
        fun onClickSave()
    }

    constructor(outputEvents: OutputEvents) : this(
        outputEvents,
        ObservableField(""),
        ObservableField(""),
        ObservableField(""),
        ObservableField(""),
        ObservableField(""),
        ObservableField(""),
        ObservableField(""),
        ObservableField(""),
        ObservableField(""))
}