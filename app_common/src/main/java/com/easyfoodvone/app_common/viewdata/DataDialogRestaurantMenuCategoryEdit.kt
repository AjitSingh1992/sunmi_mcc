package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField

class DataDialogRestaurantMenuCategoryEdit(
        val categoryName: ObservableField<String>,
        val categoryNameError: ObservableField<String>,
        val categoryActive: ObservableField<Boolean>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickToggle()
        fun onClickUpdate()
        fun onClickCancel()
    }
}