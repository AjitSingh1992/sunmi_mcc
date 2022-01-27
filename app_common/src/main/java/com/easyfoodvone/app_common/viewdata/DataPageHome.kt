package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataPageHome(
        val dataHeader: DataIncludeHeader,
        val dataSideMenu: DataIncludeSideMenu,
        val dataOrdersHeader: DataIncludeOrdersHeader,
        val logoUrl: String,
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>,
        val showRestaurantHeader: ObservableField<Boolean>,
        val newOrderToDisplayCount: ObservableField<Int>) {

    interface OutputEvents {
    }

    interface InputEvents {
        fun toggleSideDrawer()
    }
}