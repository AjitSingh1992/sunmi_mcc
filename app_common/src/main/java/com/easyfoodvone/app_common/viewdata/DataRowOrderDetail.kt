package com.easyfoodvone.app_common.viewdata

import android.os.Handler
import androidx.databinding.Observable
import androidx.databinding.ObservableList
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.OrdersListResponse

class DataRowOrderDetail(
        val loading: ObservableField<Boolean>,
        val isExpanded: ObservableField<Boolean>,
        val itemRows: ObservableList<ItemRow>,
        val gbpSubTotal: ObservableField<String>,
        val gbpDeliveryCharges: ObservableField<String>,
        val gbpDiscount: ObservableField<String>,
        val deliveryPIN: ObservableField<String>,
        val customerDeliveryPINTitle: ObservableField<String>,
        val gbpTotal: ObservableField<String>,
        val notes: ObservableField<String>,
        val deliveryTableNumber: ObservableField<String>,
        val deliveryRequestedDateTime: ObservableField<String>,
        val deliveryCustomerName: ObservableField<String>,
        val deliveryCustomerPhone: ObservableField<String>,
        val deliveryCustomerAddress: ObservableField<String>,
        val barcodeCodabarFormat: ObservableField<String>,
        val outputEvents: OutputEvents) {

    val loadingAfterDelay = ObservableField(false)
    val handler = Handler()
    val assignLoadingAfterDelay = Runnable {
        loadingAfterDelay.set(loading.get())
    }
    init {
        // No need to ever remove this listener as we own both the loading and loadingAfterDelay objects
        loading.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                handler.removeCallbacks(assignLoadingAfterDelay)

                if (loading.get() == true) {
                    // Start loading after a delay so that the row layout doesn't expand immediately.
                    // If it expanded immediately, the "scroll overview to top of page" behaviour wouldn't work.
                    handler.postDelayed(assignLoadingAfterDelay, 1500)
                } else {
                    // Hide the spinner immediately so the loaded content doesn't overlap the spinner
                    loadingAfterDelay.set(loading.get())
                }
            }
        })
    }

    class ItemRow(
            val indentLevel: Int,
            val quantity: String,
            val itemName: String,
            val eachPrice: String,
            val totalPrice: String)

    interface OutputEvents {
        fun bind(clickedOrder: OrdersListResponse.Orders)
        fun toggleVisible()
        fun onClickPrint()
    }
}