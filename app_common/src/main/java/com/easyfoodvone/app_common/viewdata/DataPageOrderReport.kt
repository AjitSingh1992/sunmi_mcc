package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.OrderReportResponse

class DataPageOrderReport(
        val isPinCheckSuccessful: ObservableField<Boolean>,
        val isDataLoaded: ObservableField<Boolean>,
        val startDate: ObservableField<String>,
        val endDate: ObservableField<String>,
        val totalOrders: ObservableField<String>,
        val totalRevenue: ObservableField<String>,
        val totalItems: ObservableField<String>,
        val totalDiscount: ObservableField<String>,
        val acceptedCount: ObservableField<String>,
        val acceptedPercent: ObservableField<String>,
        val acceptedAmount: ObservableField<String>,
        val declinedCount: ObservableField<String>,
        val declinedPercent: ObservableField<String>,
        val declinedAmount: ObservableField<String>,
        val totaltaxes: ObservableField<String>,
        val restaurantwallet: ObservableField<String>,
        val ordersList: ObservableField<List<OrderReportResponse.OrdersList>?>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickAll()
        fun onClickYesterday()
        fun onClickToday()
        fun onClickStartDate()
        fun onClickEndDate()
        fun onClickSearchBetweenDates()
        fun onClickEmailReport()
        fun onClickPrintReport()
    }
}