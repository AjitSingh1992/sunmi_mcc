package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataIncludeSideMenu(
    val deliverySettingsEnabled: ObservableField<Boolean>,
    val isRestaurantOpen: ObservableField<Boolean>,
    val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickClose()
        fun onClickOpenToggle()
        fun onClickItem(item: SideMenuItem)
    }

    enum class SideMenuItem {
        SERVE_STYLE,
        ORDERS,
        MENU,
        DELIVERY_SETTINGS,
        RESTAURANT_TIMINGS,
        ORDERS_REPORT,
        REVENUE_REPORT,
        OFFERS,
        REVIEW_N_RATINGS,
        CHARITY,
        PROFILE,
        CHANGE_PASSWORD,
        LOGOUT
    }
}