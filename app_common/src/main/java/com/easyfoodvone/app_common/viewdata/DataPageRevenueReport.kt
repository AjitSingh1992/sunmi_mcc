package com.easyfoodvone.app_common.viewdata

import androidx.databinding.ObservableField

class DataPageRevenueReport(
        val isPinCheckSuccessful: ObservableField<Boolean>,
        val isDataLoaded: ObservableField<Boolean>,
        val totalOrders: ObservableField<String>,
        val totalProductSold: ObservableField<String>,
        val totalTaxApplied: ObservableField<String>,
        val totalDiscountApplied: ObservableField<String>,
        val totalRevenueCollected: ObservableField<String>,
        val grossProfit: ObservableField<String>,
        val startDate: ObservableField<String>,
        val endDate: ObservableField<String>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onClickAll()
        fun onClickToday()
        fun onClickYesterday()
        fun onClickFindBetweenDates()
        fun onClickStartDate()
        fun onClickEndDate()
    }
}