package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataIncludeHeader(
        val pageTitle: ObservableField<String>,
        val restaurantAddress: ObservableField<String>,
        val restaurantName: ObservableField<String>,
        val restaurantNumber: ObservableField<String>,
        val showBurgerMenu: ObservableField<Boolean>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickBack()
        fun onClickBurger()
    }

    constructor(
            pageTitle: String,
            restaurantAddress: String,
            restaurantName: String,
            restaurantNumber: String,
            showBurgerMenu: Boolean,
            outputEvents: OutputEvents) : this(
        ObservableField(pageTitle),
        ObservableField(restaurantAddress),
        ObservableField(restaurantName),
        ObservableField(restaurantNumber),
        ObservableField(showBurgerMenu),
        outputEvents)
}