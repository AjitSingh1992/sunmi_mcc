package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataDialogRestaurantTime(
        val isOpen: ObservableField<Boolean>,
        val openingTimeFrom: ObservableField<String>,
        val openingTimeTo: ObservableField<String>,
        val collectionTimeFrom: ObservableField<String>,
        val collectionTimeTo: ObservableField<String>,
        val deliveryTimeFrom: ObservableField<String>,
        val deliveryTimeTo: ObservableField<String>,
        val dayName: ObservableField<String>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickIsOpen()
        fun onClickTime(dataField: ObservableField<String>)
        fun onClickUpdate()
        fun onClickCancel()
    }
}