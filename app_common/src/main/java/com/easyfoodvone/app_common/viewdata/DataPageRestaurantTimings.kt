package com.easyfoodvone.app_common.viewdata

import androidx.databinding.ObservableArrayList
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.AllDaysRestaurantTiming

class DataPageRestaurantTimings(
    val outputEvents: OutputEvents,
    val inputEvents: ObservableField<InputEvents?>,
    val restaurantTimings: ObservableArrayList<AllDaysRestaurantTiming.Data>,
    val showingPopupAdd: ObservableField<DataDialogRestaurantTime?>,
    val showingPopupEdit: ObservableField<DataDialogRestaurantTime?>) {

    interface OutputEvents {
        fun onClickAdd(timings: AllDaysRestaurantTiming.Data)
        fun onClickEdit(timings: AllDaysRestaurantTiming.Data.TimingData)
        fun onClickDelete(position: Int, timings: AllDaysRestaurantTiming.Data.TimingData)
    }

    interface InputEvents {
        fun showPopupDeleteConfirm(onYes: Runnable)
    }
}