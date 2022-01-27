package com.easyfoodvone.app_common.viewdata

class DataDialogRestaurantMenuDisableItemTimeChooser(val outputEvents: OutputEvents) {
    interface OutputEvents {
        fun onClickRemainingDay()
        fun onClickPermanent()
        fun onClickCancel()
    }
}