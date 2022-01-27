package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.OrdersListResponse

class DataRowOrderOverview(
        val currentAcceptedStatus: ObservableField<String>,
        val orderDateTablet: ObservableField<String>,
        val orderDatePhone: ObservableField<String>,
        val orderTimeTablet: ObservableField<String>,
        val orderTimePhone: ObservableField<String>,
        val deliveryDateTablet: ObservableField<String>,
        val deliveryDatePhone: ObservableField<String>,
        val deliveryTimeTablet: ObservableField<String>,
        val deliveryTimePhone: ObservableField<String>,
        val deliveryTypeTablet: ObservableField<String>,
        val deliveryTypePhone: ObservableField<String>,
        val orderID: ObservableField<String>,
        val customerName: ObservableField<String>,
        val previousOrder: ObservableField<String>,
        val orderAmount: ObservableField<String>,
        val paymentType: ObservableField<String>,
        val customerAddress: ObservableField<String>,
        val status: ObservableField<OrderStatus>,
        val outputEvents: OutputEvents,
        val inputEvents: ObservableField<InputEvents?>) {

    interface OutputEvents {
        fun bind(clickedOrder: OrdersListResponse.Orders)
        fun onClickNewOrderAccept()
        fun onClickNewOrderReject()
        fun onClickViewDetail()
        fun onClickEditAcceptedStatusBegin()
        fun onClickEditAcceptedStatusEnd(clickedStatus: DataPageOrderList.AcceptedOrderRequestStatus)
        fun stopNotificationSound()
    }

    interface InputEvents {
        fun showEditAcceptedStatusChooser(options: List<Pair<String, DataPageOrderList.AcceptedOrderRequestStatus>>)
    }

    enum class OrderStatus {
        NEW,
        ACCEPTED,
        REJECTED
    }
}