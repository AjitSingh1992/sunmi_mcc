package com.easyfoodvone.app_common.viewdata

import androidx.databinding.ObservableField

class DataDialogAcceptDeliveryTime(
        val deliveryDateTime: ObservableField<String>,
        val minsAboveAverage: ObservableField<Int>,
        val averageDeliveryTime: ObservableField<Int>,
        val buttonsDataIsLoading: ObservableField<Boolean>,
        val buttonsDataIsAvailable: ObservableField<Boolean>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickAccept()
        fun onClickCancel()
        fun onClickAdd()
        fun onClickSubtract()
    }
}